package com.yuanluesoft.bidding.enterprise.services.spring;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEKeyLog;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEmployee;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.services.EmployeeService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.security.service.exception.SecurityException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanlue
 *
 */
public class EmployeeServiceImpl extends BusinessServiceImpl implements EmployeeService {
	private CryptService cryptService; //加/解密服务
	private int tryCount; //一个企业允许注册的试用用户数量
	private int tryDays; //试用天数
	private boolean ekeySupport; //是否支持ekey

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
	    BiddingEmployee employee = (BiddingEmployee)super.load(recordClass, id);
	    if(employee==null) {
	    	return null;
	    }
    	if(employee.getPassword()==null || employee.getPassword().equals("")) {
    		employee.setPassword(encryptPassword(employee.getId(), employee.getLoginName()));
    	}
    	employee.setPassword("{" + employee.getPassword() + "}");
    	return employee;
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		BiddingEmployee employee = (BiddingEmployee)record;
		//用户名不区分大小写
		employee.setLoginName(employee.getLoginName().toLowerCase()); //转换为小写
		employee.setPassword(encryptPassword(employee.getId(), employee.getPassword()));
		super.save(record);
		employee.setPassword("{" + employee.getPassword() + "}");
		writeEKeyAction(employee, true);
		return employee;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		BiddingEmployee employee = (BiddingEmployee)record;
		//用户名不区分大小写
		employee.setLoginName(employee.getLoginName().toLowerCase()); //转换为小写
		employee.setPassword(encryptPassword(employee.getId(), employee.getPassword()));
		writeEKeyAction(employee, false);
		super.update(record);
		employee.setPassword("{" + employee.getPassword() + "}");
		return employee;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		BiddingEmployee employee = (BiddingEmployee)record;
		if(employee.getEkeyId()!=null && !employee.getEkeyId().equals("")) {
			writeEKeyAction(employee, "因用户删除收回");
		}
		super.delete(record);
	}

	/**
	 * 记录ekey操作
	 * @param employee
	 * @param isNew
	 * @throws ServiceException
	 */
	private void writeEKeyAction(BiddingEmployee employee, boolean isNew) throws ServiceException {
		if(!ekeySupport) {
			return;
		}
		BiddingEmployee oldEmployee = null;
		if(!isNew) {
			oldEmployee = (BiddingEmployee)getDatabaseService().findRecordByHql("from BiddingEmployee BiddingEmployee where BiddingEmployee.id=" + employee.getId());
		}
		if(oldEmployee==null || (oldEmployee.getEkeyId()==null || oldEmployee.getEkeyId().equals(""))) { //新纪录,或者原来没有写过key
			if(employee.getEkeyId()!=null && !employee.getEkeyId().equals("")) {
				BiddingEKeyLog log = getLastEKeyAction(employee.getEkeyNO());
				writeEKeyAction(employee, (log==null || log.getActionType().indexOf("收回")!=-1 ? "新发" : "变更使用人"));
				return;
			}
		}
		else if(employee.getEkeyId()==null || employee.getEkeyId().equals("")) { //没有ekeyid
			if(oldEmployee.getEkeyId()!=null && !oldEmployee.getEkeyId().equals("")) { //原来有,收回
				employee.setEkeyNO(oldEmployee.getEkeyNO());
				writeEKeyAction(employee, "收回");
				employee.setEkeyNO(null);
				return;
			}
		}
		else if(employee.getEkeyNO().equals(oldEmployee.getEkeyNO())) { //编号没有变
			//检查用户类型是否变更
			if(employee.getIsPermanent()!=oldEmployee.getIsPermanent()) {
				writeEKeyAction(employee, employee.getIsPermanent()=='1' ? "用户转换为永久性用户" : "用户转换为试用用户");
				return;
			}
			else if(employee.getIsPermanent()!='1' && !employee.getTryEndDate().equals(oldEmployee.getTryEndDate())) { //检查试用时间是否变更 
				writeEKeyAction(employee, "变更试用时间");
				return;
			}
		}
		else if(employee.getEkeyId().equals(oldEmployee.getEkeyId())) { ////编号改变,ekeyid不变
			writeEKeyAction(employee, "修改KEY编号");
			return;
		}
		else {
			writeEKeyAction(employee, "换KEY");
			return;
		}
		//更新最后一次的ekey操作日志
		if(!isNew) {
			updateEKeyAction(employee);
		}
	}
	
	/**
	 * 记录EKey操作
	 * @param employee
	 * @param actionType
	 * @throws ServiceException
	 */
	public void writeEKeyAction(BiddingEmployee employee, String actionType) throws ServiceException {
		if(!ekeySupport) {
			return;
		}
		BiddingEKeyLog ekeyLog = new BiddingEKeyLog();
		try {
			PropertyUtils.copyProperties(ekeyLog, employee);
		} 
		catch (Exception e) {
			
		}
		ekeyLog.setId(UUIDLongGenerator.generateId());
		ekeyLog.setActionTime(DateTimeUtils.now());
		ekeyLog.setTransactor(employee.getLastTransactor());
		ekeyLog.setTransactorId(employee.getLastTransactorId());
		ekeyLog.setActionType(actionType);
		//设置企业资质
		ekeyLog.setEnterpriseCert(ListUtils.join(getDatabaseService().findRecordsByHql("select BiddingEnterpriseCert.type from BiddingEnterpriseCert BiddingEnterpriseCert where BiddingEnterpriseCert.enterpriseId=" + employee.getEnterpriseId()), ",", false));
		getDatabaseService().saveRecord(ekeyLog);
	}
	
	/**
	 * 更新最后一次的ekey操作记录
	 * @param employee
	 * @throws ServiceException
	 */
	private void updateEKeyAction(BiddingEmployee employee) throws ServiceException {
		//获取最后的操作记录
		String hql = "from BiddingEKeyLog BiddingEKeyLog" +
					 " where BiddingEKeyLog.employeeId=" +  employee.getId() +
					 " order by BiddingEKeyLog.actionTime DESC";
		BiddingEKeyLog ekeyLog = (BiddingEKeyLog)getDatabaseService().findRecordByHql(hql);
		if(ekeyLog==null) {
			return;
		}
		long id = ekeyLog.getId();
		try {
			PropertyUtils.copyProperties(ekeyLog, employee);
		} 
		catch (Exception e) {
			
		}
		ekeyLog.setId(id);
		//更新操作人
		String transactor = ekeyLog.getTransactor();
		if(transactor==null || transactor.equals("")) {
			transactor = employee.getLastTransactor();
		}
		else if(!transactor.equals(employee.getLastTransactor()) && !transactor.endsWith("," + employee.getLastTransactor())) {
			transactor += "," + employee.getLastTransactor();
		}
		ekeyLog.setTransactor(transactor);
		getDatabaseService().updateRecord(ekeyLog);
	}
	
	/**
	 * 获取ekey最后一次的操作
	 * @param ekeyNo
	 * @throws ServiceException
	 */
	private BiddingEKeyLog getLastEKeyAction(String ekeyNO) throws ServiceException {
		return (BiddingEKeyLog)getDatabaseService().findRecordByHql("from BiddingEKeyLog BiddingEKeyLog where BiddingEKeyLog.ekeyNO='" + JdbcUtils.resetQuot(ekeyNO) + "' order by BiddingEKeyLog.actionTime DESC");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EmployeeService#isTryOver(long)
	 */
	public boolean isTryOver(long enterpriseId) throws ServiceException {
		String hql = "select count(BiddingEmployee.id)" +
				     " from BiddingEmployee BiddingEmployee" +
				     " where BiddingEmployee.enterpriseId=" + enterpriseId +
				     " and BiddingEmployee.isPermanent!='1'";
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count!=null && count.intValue()>=tryCount;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EmployeeService#addEmplyee(com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise, java.lang.String, java.lang.String)
	 */
	public void addEmplyee(BiddingEnterprise enterprise, String loginName, String password) throws ServiceException {
		BiddingEmployee employee = new BiddingEmployee();
		employee.setId(UUIDLongGenerator.generateId()); //ID
		employee.setEnterpriseId(enterprise.getId()); //企业ID
		employee.setEnterpriseName(enterprise.getName()); //企业名称
		employee.setName(enterprise.getName()); //用户姓名
		employee.setLoginName(loginName); //登录用户名,在试用期限到达前或未生效时不允许重复
		employee.setPassword(password); //登录密码
		employee.setHalt('0'); //是否停用
		employee.setIsPermanent('1'); //是否永久性用户
		employee.setTryDays(tryDays); //试用有效期(天)
		employee.setTryEndDate(null); //试用截止时间,在完成注册以后设置
		employee.setEkeyId(null); //EKeyID,10位随机数,由控件产生并写入key中
		employee.setEkeyNO(null); //EKey编号,ekey外壳上的序号
		employee.setLastTransactor(null); //最后经办人
		employee.setLastTransactorId(0); //最后经办人ID
		employee.setLastTransactTime(null); //最后办理时间
		employee.setContractNo(null); //合同编号
		employee.setTel(null); //联系电话
		employee.setDeposit(0); //已收押金
		employee.setDepositBill(null); //押金收据号码
		employee.setDrawTime(null); //领取软件
		employee.setDamage(null); //损坏内容
		employee.setDamageMoney(0); //已收赔偿金额
		employee.setSaleMoney(0); //已收销售款
		employee.setSaleBill(null); //发票号码
		employee.setReceiptNo(null); //收据号码
		employee.setEnterpriseTransactor(null); //企业经办人
		employee.setRemark(null); //备注
		save(employee);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		BiddingEmployee employee = (BiddingEmployee)getDatabaseService().findRecordByHql("from BiddingEmployee BiddingEmployee where BiddingEmployee.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'"); //按登录用户名查找用户
		if(employee==null) {
			return false;
		}
		if(validateOldPassword) {
			if(employee.getPassword()==null || employee.getPassword().equals("")) {
			    if(!oldPassword.equals(employee.getLoginName())) {
			        throw(new WrongPasswordException()); //密码错误
			    }
			}
			else if(!encryptPassword(employee.getId(), oldPassword).equals(employee.getPassword())) {
				throw(new WrongPasswordException()); //密码错误
			}
		}
		//设置新密码
		employee.setPassword(encryptPassword(employee.getId(), newPassword));
		getDatabaseService().updateRecord(employee);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		BiddingEmployee employee = (BiddingEmployee)load(BiddingEmployee.class, memberId);
		if(employee==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, employee);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PersonService.PERSON_TYPE_OTHER);
		member.setOriginalRecord(employee);
		return member;
	}

	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
		if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
	    try {
	        return cryptService.encrypt(password, "" + userId, false);
	    } 
	    catch (SecurityException e) {
	        throw new ServiceException();
	    }
	}

	/**
	 * 口令解密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String decryptPassword(long userId, String password) throws ServiceException {
		try {
			return cryptService.decrypt(password, "" + userId, false);
		}
		catch (SecurityException e) {
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			BiddingSessionInfo sessionInfo = new BiddingSessionInfo();
			BiddingEmployee employee = (BiddingEmployee)getDatabaseService().findRecordByHql("from BiddingEmployee BiddingEmployee where BiddingEmployee.loginName='" + JdbcUtils.resetQuot(loginName) + "'"); //按登录用户名查找用户
			if(employee==null) { //不是企业用户
				return null;
			}
			//设置用户信息
			sessionInfo.setLoginName(employee.getLoginName());
			sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER);
			String password = employee.getPassword();
			if(password==null || password.equals("")) {
				password = employee.getLoginName();
			}
			else {//口令解密
				password = decryptPassword(employee.getId(), password);
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(employee.getName());
			sessionInfo.setUserId(employee.getId());
			String orgIds = "0," + employee.getEnterpriseId();
			sessionInfo.setUnitId(employee.getEnterpriseId());
			sessionInfo.setUnitName(employee.getEnterpriseName());
			sessionInfo.setDepartmentId(employee.getEnterpriseId());
			sessionInfo.setDepartmentName(employee.getEnterpriseName());
			//设置部门信息
			sessionInfo.setDepartmentIds(orgIds);
			//获取企业资质
			List certs = getDatabaseService().findRecordsByHql("select BiddingEnterpriseCert.type from BiddingEnterpriseCert BiddingEnterpriseCert where BiddingEnterpriseCert.enterpriseId=" + employee.getEnterpriseId());
			sessionInfo.setCerts(ListUtils.join(certs, ",", false));
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select BiddingEmployee.id from BiddingEmployee BiddingEmployee where BiddingEmployee.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		loginName = loginName.toLowerCase();
    	//用户校验
    	BiddingEmployee employee = (BiddingEmployee)getDatabaseService().findRecordByHql("from BiddingEmployee BiddingEmployee where BiddingEmployee.loginName='" + JdbcUtils.resetQuot(loginName) + "'"); //按登录用户名查找用户
    	if(employee==null) {
    		return null;
    	}
    	if(employee.getHalt()=='1' || (employee.getIsPermanent()!='1' && DateTimeUtils.now().after(employee.getTryEndDate()))) {
    		throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT); //帐号停用
    	}
    	//检查keyid,企业id,用户id是否正确
    	if(ekeySupport &&
    	   (employee.getEkeyId()==null ||
    	    !employee.getEkeyId().equals(request.getParameter("keyId")) ||
    	    employee.getEnterpriseId()!=Long.parseLong(request.getParameter("companyId")) ||
    	    employee.getId()!=Long.parseLong(request.getParameter("userId")))) {
    		throw new LoginException(MemberService.LOGIN_INVALID_USERNAME_OR_PASSWORD);
    	}
    	String correctPassword = loginName; //正确的密码
		//密码校验
	    if(employee.getPassword()!=null && !employee.getPassword().equals("")) { //注:密码为空时,要求用户输入的密码必须和用户名相同
	    	correctPassword =  cryptService.decrypt(employee.getPassword(), "" + employee.getId(), false);
	    }
		return new MemberLogin(employee.getLoginName(), employee.getId(), PersonService.PERSON_TYPE_OTHER, correctPassword, !passwordMatcher.matching(correctPassword, password));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EmployeeService#exportEKeyReport(java.sql.Date, java.sql.Date, javax.servlet.http.HttpServletResponse)
	 */
	public void exportEKeyReport(Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("软件销售报表.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "bidding/enterprise/template/ekeyreport.template.xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			
			//获取数据
			String where = null;
			if(beginDate!=null) {
				where = "BiddingEKeyLog.actionTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")";
			}
			if(endDate!=null) {
				where = (where==null ? "" : where + " and ") + "BiddingEKeyLog.actionTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")";
			}
			final int TOP_ROWS = 3; //从第4行开始输出数据

			//******** 输出销售报表,非借用的 ********
			String hql = "from BiddingEKeyLog BiddingEKeyLog" +
						 " where BiddingEKeyLog.actionType='新发'" +
						 " and BiddingEKeyLog.isPermanent='1'" +
						 (where==null ? "" : " and " + where) +
						 " order by BiddingEKeyLog.actionTime";
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			int rowIndex = TOP_ROWS + 1;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录,日期	企业名称	企业性质	证书编号	购买合同编号	购买日期	经办人姓名	联系电话	销售金额	收据号码	发票号码	领取软件	备注
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					BiddingEKeyLog ekeyLog = (BiddingEKeyLog)iterator.next();
					Object[] values = {new Date(ekeyLog.getActionTime().getTime()), //日期
									   ekeyLog.getEnterpriseName(), //企业名称
									   ekeyLog.getEnterpriseCert(), //企业性质
									   ekeyLog.getEkeyNO(), //证书编号
									   ekeyLog.getContractNo(), //购买合同编号
									   new Date(ekeyLog.getActionTime().getTime()), //购买日期
									   ekeyLog.getEnterpriseTransactor(), //经办人姓名
									   ekeyLog.getTel(), //联系电话
									   new Float(ekeyLog.getSaleMoney()), //销售金额
									   ekeyLog.getReceiptNo(), //收据号码
									   ekeyLog.getSaleBill(), //发票号码
									   (ekeyLog.getDrawTime()!=null ? "已领取" : "未领取"), //领取软件
									   ekeyLog.getRemark()}; //备注
					ExcelUtils.writeExcelRow(ws, values, rowIndex++, TOP_ROWS);
				}
				if(data.size()<100) {
					break;
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
			
			//******** 输出借用报表 ********
			hql = "from BiddingEKeyLog BiddingEKeyLog" +
				  " where BiddingEKeyLog.actionType='新发'" +
				  " and BiddingEKeyLog.isPermanent!='1'" +
				  (where==null ? "" : " and " + where) +
				  " order by BiddingEKeyLog.actionTime";
			ws = wwb.getSheet(1);
			rowIndex = TOP_ROWS + 1;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录,日期	企业名称	企业性质	证书编号	合同编号	经办人姓名	联系电话	已收押金	押金收据号码	收据号码	领取软件	借用开始日	借用到期日	备注
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					BiddingEKeyLog ekeyLog = (BiddingEKeyLog)iterator.next();
					Object[] values = {new Date(ekeyLog.getActionTime().getTime()), //日期
									   ekeyLog.getEnterpriseName(), //企业名称
									   ekeyLog.getEnterpriseCert(), //企业性质
									   ekeyLog.getEkeyNO(), //证书编号
									   ekeyLog.getContractNo(), //合同编号
									   ekeyLog.getEnterpriseTransactor(), //经办人姓名
									   ekeyLog.getTel(), //联系电话
									   new Float(ekeyLog.getDeposit()), //已收押金
									   ekeyLog.getDepositBill(), //押金收据号码
									   ekeyLog.getReceiptNo(), //收据号码
									   (ekeyLog.getDrawTime()!=null ? "已领取" : "未领取"), //领取软件
									   new Date(ekeyLog.getActionTime().getTime()), //借用开始日
									   ekeyLog.getTryEndDate(), //借用到期日
									   ekeyLog.getRemark()}; //备注
					ExcelUtils.writeExcelRow(ws, values, rowIndex++, TOP_ROWS);
				}
				if(data.size()<100) {
					break;
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
			
			//******** 输出回收报表 ********
			hql = "from BiddingEKeyLog BiddingEKeyLog" +
				  " where BiddingEKeyLog.actionType='收回'" +
				  (where==null ? "" : " and " + where) +
				  " order by BiddingEKeyLog.actionTime";
			ws = wwb.getSheet(2);
			rowIndex = TOP_ROWS + 1;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录,日期	企业名称	证书编号	合同编号	经办人姓名	联系电话	办理内容	借用日期	损坏内容	已收赔偿金额	已收销售款	发票号码	备注
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					BiddingEKeyLog ekeyLog = (BiddingEKeyLog)iterator.next();
					Object[] values = {new Date(ekeyLog.getActionTime().getTime()), //日期
									   ekeyLog.getEnterpriseName(), //企业名称
									   ekeyLog.getEkeyNO(), //证书编号
									   ekeyLog.getContractNo(), //合同编号
									   ekeyLog.getEnterpriseTransactor(), //经办人姓名
									   ekeyLog.getTel(), //联系电话
									   "回收", //办理内容
									   new Date(ekeyLog.getDrawTime().getTime()), //借用日期
									   ekeyLog.getDamage(), //损坏内容
									   new Float(ekeyLog.getDamageMoney()), //已收赔偿金额
									   new Float(ekeyLog.getSaleMoney()), //已收销售款
									   ekeyLog.getSaleBill(), //发票号码
									   ekeyLog.getRemark()}; //备注
					ExcelUtils.writeExcelRow(ws, values, rowIndex++, TOP_ROWS);
				}
				if(data.size()<100) {
					break;
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
	
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
	}
	
	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	/**
	 * @return the tryCount
	 */
	public int getTryCount() {
		return tryCount;
	}

	/**
	 * @param tryCount the tryCount to set
	 */
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	/**
	 * @return the tryDays
	 */
	public int getTryDays() {
		return tryDays;
	}

	/**
	 * @param tryDays the tryDays to set
	 */
	public void setTryDays(int tryDays) {
		this.tryDays = tryDays;
	}

	/**
	 * @return the ekeySupport
	 */
	public boolean isEkeySupport() {
		return ekeySupport;
	}

	/**
	 * @param ekeySupport the ekeySupport to set
	 */
	public void setEkeySupport(boolean ekeySupport) {
		this.ekeySupport = ekeySupport;
	}
}