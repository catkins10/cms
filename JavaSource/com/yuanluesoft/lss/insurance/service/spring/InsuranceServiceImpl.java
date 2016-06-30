package com.yuanluesoft.lss.insurance.service.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.lss.insurance.model.InsuranceSessionInfo;
import com.yuanluesoft.lss.insurance.pojo.InsuranceAccount;
import com.yuanluesoft.lss.insurance.pojo.InsuranceParameter;
import com.yuanluesoft.lss.insurance.pojo.InsuranceUser;
import com.yuanluesoft.lss.insurance.service.InsuranceService;

/**
 * 
 * @author linchuan
 *
 */
public class InsuranceServiceImpl extends BusinessServiceImpl implements InsuranceService {
	private AttachmentService attachmentService; //附件服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.lss.insurance.service.InsuranceService#importData(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void importData(long importId, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("lss/insurance", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connection = DriverManager.getConnection("jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=" + attachment.getFilePath(), null, null); 
			statement = connection.createStatement();
			
			//导入用户
			try {
				resultSet = statement.executeQuery("select name as identityCardNumber," +
												   " pwd as password" +
												   " from users");
				importUsers(resultSet);
			}
			catch(SQLException se) {
				
			}
			finally {
				try { 
					resultSet.close(); 
				}
				catch(Exception e) {
					
				}
			}
			
			//导入记账利率、缴纳比例
			try {
				resultSet = statement.executeQuery("select NF as year," +
												   " JZLL as rate," +
												   " JZBL as accountRatio," +
												   " GRBL as payRatio" +
												   " from dmk");
				importParameters(resultSet);
			}
			catch(SQLException se) {
				
			}
			finally {
				try { 
					resultSet.close(); 
				}
				catch(Exception e) {
					
				}
			}
			
			//导入个人账户
			try {
				resultSet = statement.executeQuery("select SHBZM as identityCardNumber," +  //身份证号码
												   " XM as name," + //姓名
												   " NF as year," + //年度
												   " BNGRZH as accountCapital," + //本年度个人账户本金
												   " BNGRJN as payCapital," + //其中个人缴纳本金
												   " NDLJYS as monthNumber," + //本年底累计缴费月数
												   " NDGRZH as yearTotal," + //年底账户本息累计
												   " NDGRJN as yearPayTotal," + //其中个人缴纳本息
												   " NJFJS as cardinalNumber" + //年缴费基数（含补缴）
												   " from grzhk");
				importAccounts(resultSet);
			}
			catch(SQLException se) {
				
			}
			finally {
				try { 
					resultSet.close(); 
				}
				catch(Exception e) {
					
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try { 
				statement.close(); 
			}
			catch(Exception e) {
				
			}
			try {
				connection.close(); 
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入用户
	 * @param resultSet
	 * @throws ServiceException
	 */
	private void importUsers(ResultSet resultSet) throws ServiceException, SQLException {
		while(resultSet.next()) {
			InsuranceUser user = new InsuranceUser();
			JdbcUtils.copyFields(user, resultSet);
			user.setIdentityCardNumber(user.getIdentityCardNumber().toUpperCase()); //转换为大写
			//检查用户是否已经存在
			String hql = "from InsuranceUser InsuranceUser where InsuranceUser.identityCardNumber='" + JdbcUtils.resetQuot(user.getIdentityCardNumber()) + "'";
			InsuranceUser existsUser = (InsuranceUser)getDatabaseService().findRecordByHql(hql);
			if(existsUser==null) {
				user.setId(UUIDLongGenerator.generateId());
				getDatabaseService().saveRecord(user);
			}
		}
	}
	
	/**
	 * 导入记账利率、缴纳比例
	 * @param resultSet
	 * @throws ServiceException
	 */
	private void importParameters(ResultSet resultSet) throws ServiceException, SQLException {
		while(resultSet.next()) {
			InsuranceParameter parameter = new InsuranceParameter();
			JdbcUtils.copyFields(parameter, resultSet);
			//检查用户是否已经存在
			String hql = "from InsuranceParameter InsuranceParameter where InsuranceParameter.year=" + parameter.getYear();
			InsuranceParameter existsParameter = (InsuranceParameter)getDatabaseService().findRecordByHql(hql);
			if(existsParameter==null) {
				parameter.setId(UUIDLongGenerator.generateId());
				getDatabaseService().saveRecord(parameter);
			}
			else {
				parameter.setId(existsParameter.getId());
				getDatabaseService().updateRecord(parameter);
			}
		}
	}
	
	/**
	 * 导入个人账户
	 * @param resultSet
	 * @throws ServiceException
	 */
	private void importAccounts(ResultSet resultSet) throws ServiceException, SQLException {
		while(resultSet.next()) {
			InsuranceAccount account = new InsuranceAccount();
			JdbcUtils.copyFields(account, resultSet);
			account.setIdentityCardNumber(account.getIdentityCardNumber().toUpperCase()); //转换为大写
			//检查用户是否已经存在
			String hql = "from InsuranceAccount InsuranceAccount" +
						 " where InsuranceAccount.identityCardNumber='" + JdbcUtils.resetQuot(account.getIdentityCardNumber()) + "'" +
						 " and InsuranceAccount.year=" + account.getYear();
			InsuranceAccount existsAccount = (InsuranceAccount)getDatabaseService().findRecordByHql(hql);
			if(existsAccount==null) {
				account.setId(UUIDLongGenerator.generateId());
				getDatabaseService().saveRecord(account);
			}
			else {
				account.setId(existsAccount.getId());
				getDatabaseService().updateRecord(account);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.lss.insurance.service.InsuranceService#registUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	public InsuranceUser registUser(String identityCardNumber, String name, String password) throws ServiceException {
		//检查用户是否已经注册
		identityCardNumber = identityCardNumber.toUpperCase(); //转换为大写
		String hql = "from InsuranceUser InsuranceUser where InsuranceUser.identityCardNumber='" + JdbcUtils.resetQuot(identityCardNumber) + "'";
		InsuranceUser user = (InsuranceUser)getDatabaseService().findRecordByHql(hql);
		if(user!=null && user.getPassword()!=null && !user.getPassword().isEmpty()) { //用户已经注册过,且密码不为空
			throw new ServiceException("您的身份证号码已经被注册，不允许重复注册。");
		}
		//检查身份证号码和姓名是否匹配
		hql = "select InsuranceAccount.id" +
			  " from InsuranceAccount InsuranceAccount" +
			  " where InsuranceAccount.identityCardNumber='" + JdbcUtils.resetQuot(identityCardNumber) + "'" +
			  " and InsuranceAccount.name='" + JdbcUtils.resetQuot(name) + "'";
		if(getDatabaseService().findRecordByHql(hql)==null) {
			throw new ServiceException("您的身份证号码和姓名不匹配或者您的身份证号码未在本系统中登记。");
		}
		if(user!=null) {
			user.setPassword(password);
			getDatabaseService().updateRecord(user);
		}
		else {
			user = new InsuranceUser();
			user.setId(UUIDLongGenerator.generateId());
			user.setIdentityCardNumber(identityCardNumber);
			user.setPassword(password);
			getDatabaseService().saveRecord(user);
		}
		return user;
	}
	
	/**
	 * 按身份证号码获取用户
	 * @param identityCardNumber
	 * @return
	 */
	private InsuranceUser getInsuranceUserByIdentityCardNumber(String identityCardNumber) {
		String hql = "from InsuranceUser InsuranceUser where InsuranceUser.identityCardNumber='" + JdbcUtils.resetQuot(identityCardNumber) + "'";
		return (InsuranceUser)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		InsuranceUser user = getInsuranceUserByIdentityCardNumber(loginName);
		if(user==null) {
			return false;
		}
		if(validateOldPassword && !oldPassword.equals(user.getPassword())) {
			throw new WrongPasswordException();
		}
		user.setPassword(newPassword);
		getDatabaseService().updateRecord(user);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		InsuranceUser user = getInsuranceUserByIdentityCardNumber(loginName);
		if(user==null) {
			return null;
		}
		InsuranceSessionInfo sessionInfo = new InsuranceSessionInfo();
		//设置用户信息
		sessionInfo.setLoginName(user.getIdentityCardNumber());
		sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER + 1);
		sessionInfo.setPassword(user.getPassword());
		sessionInfo.setUserName(user.getIdentityCardNumber());
		sessionInfo.setUserId(user.getId());
		sessionInfo.setDepartmentId(0);
		sessionInfo.setDepartmentIds("0");
		return sessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		InsuranceUser user = (InsuranceUser)load(InsuranceUser.class, memberId);
		if(user==null) {
			return null;
		}
		Member member = new Member();
		member.setIdentityCard("身份证");
		member.setIdentityCard(user.getIdentityCardNumber());
		member.setMemberType(PersonService.PERSON_TYPE_OTHER);
		return member;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		String hql = "select InsuranceUser.id" +
					 " from InsuranceUser InsuranceUser" +
					 " where InsuranceUser.identityCardNumber='" + JdbcUtils.resetQuot(loginName) + "'";
		Number id = (Number)getDatabaseService().findRecordByHql(hql);
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
		loginName = loginName.toUpperCase();
		String hql = "from InsuranceUser InsuranceUser where InsuranceUser.identityCardNumber='" + JdbcUtils.resetQuot(loginName) + "'";
		InsuranceUser user = (InsuranceUser)getDatabaseService().findRecordByHql(hql);
		if(user==null) {
    		return null;
    	}
    	return new MemberLogin(user.getIdentityCardNumber(), user.getId(), PersonService.PERSON_TYPE_OTHER, user.getPassword(), !passwordMatcher.matching(user.getPassword(), password));
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}