package com.yuanluesoft.educ.student.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.educ.student.service.StudentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author zyh
 *
 */
public class StudentServiceImpl extends PublicServiceImpl implements StudentService {
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private PersonService personService ;
	private BusinessService businessService;
	private CryptService cryptService; //加/解密服务
	private OrgService orgService; //组织机构服务
	private String mainOrgId;
	private SessionService sessionService;
	private PageService pageService;
	
	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public String getMainOrgId() {
		return mainOrgId;
	}

	public void setMainOrgId(String mainOrgId) {
		this.mainOrgId = mainOrgId;
	}

	public OrgService getOrgService() {
		return orgService;
	}

	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public CryptService getCryptService() {
		return cryptService;
	}

	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		//注册学生使之成为系统用户，组织目录为“学生”
		Stude stude = (Stude) record;
		if(stude.getIsValid()=='1'){
			Org department = null;
			String directoryName = "学生";
			department = (Org)orgService.createDirectory(-1, Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);                   
			
			//注册用户
			personService.addEmployee(record.getId(), stude.getName(), stude.getLoginId(), stude.getPassword(), stude.getSex(), null, null, null, null, null, department.getId()+"", record.getId(), stude.getName());
			
		}
		stude.setLoginId(stude.getLoginId().toLowerCase()); //转换为小写
		stude.setPassword(encryptPersonPassword(stude.getId(), stude.getLoginId(), stude.getPassword())); //加密口令
		
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.save(record);
	}
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		Stude stude=(Stude)record;
		//更新变更描述
		if(stude.getIsAlter()=='1' && stude.getIsValid()=='0'){
			Stude originalStude = (Stude)load(Stude.class, stude.getAlterStudentId());
			if(originalStude!=null) {
				try {
					List descriptions = compareStude(stude, originalStude);
					if(descriptions==null || descriptions.isEmpty()) {
						stude.setAlterDescription(null);
					}
					else {
						String description = null;
						int index = 1;
						for(Iterator iterator = descriptions.iterator(); iterator.hasNext();) {
							description = (description==null ? "" : description + "\r\n") + (index++) + "、" + iterator.next();
						}
						stude.setAlterDescription(description);
					}
				}
				catch (Exception e) {
					Logger.exception(e);
					throw new ServiceException(e.getMessage());
				}
			}
		}
		
		//用户在系统的信息变更
		Stude studeOld = (Stude)load(Stude.class, record.getId());
		if(!studeOld.getName().equals(stude.getName()) || !studeOld.getLoginId().equals(stude.getLoginId()) || !studeOld.getPassword().equals(stude.getPassword())){
			Person person = (Person)load(Person.class, record.getId());
			if(person!=null){
				if(!studeOld.getName().equals(stude.getName())
				person.setName(stude.getName());
				person.setLoginName(stude.getLoginId().toLowerCase()); //转换为小写
				String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
				person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), stude.getPassword())); //加密口令
				businessService.update(person);
				try {
					if(!oldLoginName.equals(person.getLoginName())) { //登录用户名已修改
						sessionService.removeSessionInfo(oldLoginName); //删除原用户名的session info
					}
					else {
						sessionService.removeSessionInfo(person.getLoginName()); //删除当前用户的session info
					}
				}
				catch (SessionException e) {
					Logger.exception(e);
				}
			}
		}
		stude.setLoginId(stude.getLoginId().toLowerCase()); //转换为小写
		stude.setPassword(encryptPersonPassword(stude.getId(), stude.getLoginId(), stude.getPassword())); //加密口令
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		// TODO 自动生成方法存根
		//删除用户的同时也要删除留在系统的用户注册信息
		Person person = (Person)personService.getPerson(record.getId());
		if(person!=null){
			personService.delete(person);
		}
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		super.delete(record);
	}
	
	private List compareStude(Stude stude, Stude originalStude) throws Exception {
		String[] properties = {"name", "姓名",
							   "sex", "性别",
							   "idcardNumber", "身份证号",
							   "nation", "民族",
							   "studentId", "学号",
							   "department", "系部",
							   "speciality", "专业",
							   "stuclass", "班级",
							   "grade", "年级",
							   "idcardAddress", "身份证地址",
							   "houseAddress", "家庭地址",
							   "phone", "联系电话",
							   "isOurStudent", "是否我们学院学生",
							   "remark", "备注"};
		List descriptions = comparePojo(stude, originalStude, properties);
		return descriptions;
	}

	/**
	 * 比较对象
	 * @param dest
	 * @param orig
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private List comparePojo(Object dest, Object orig, String[] properties) throws Exception {
		List differences = new ArrayList();
		for(int i=0; i<properties.length; i+=2) {
			Object newValue = PropertyUtils.getProperty(dest, properties[i]);
			Object originalValue = PropertyUtils.getProperty(orig, properties[i]);
			if(((newValue==null || "".equals(newValue)) && (originalValue==null || "".equals(originalValue))) || (newValue!=null && newValue.equals(originalValue))) {
				continue;
			}
			//添加变更记录
			if(newValue==null || "".equals(newValue)) {
				differences.add("删除：" + properties[i+1]+ "，原值为“" + StringUtils.format(originalValue, null, "") + "”");
			}
			else if(originalValue==null || "".equals(originalValue)) {
				differences.add("添加：" + properties[i+1] + "，值为“" + StringUtils.format(newValue, null, "") + "”");
			}
			else {
				differences.add("修改：" + properties[i+1] + "，从“" + StringUtils.format(originalValue, null, "") + "”改为“" + StringUtils.format(newValue, null, "") + "”");
			}
		}
		return differences;
	}

	public void completeAlter(Stude studeAlter, SessionInfo sessionInfo) throws ServiceException {
		// TODO 自动生成方法存根
		try {
			//获取企业记录
			Stude stude = (Stude)load(Stude.class, studeAlter.getAlterStudentId());
			if(stude==null) {
				studeAlter.setIsValid('C');
				return;
			}
			//更新主记录
			String workflowInstanceId = stude.getWorkflowInstanceId(); //工作流实例ID
			Timestamp created = stude.getCreated();

			PropertyUtils.copyProperties(stude, studeAlter);

			stude.setId(stude.getAlterStudentId()); //ID
			stude.setCreated(created); //保持原来的登记日期
			stude.setAlterStudentId(0);
			stude.setIsValid('1');
			stude.setIsAlter('0');
			stude.setAlterDescription(null);
			stude.setWorkflowInstanceId(workflowInstanceId); //工作流实例ID
			getDatabaseService().updateRecord(stude);
			
			studeAlter.setIsValid('C');
			//重新生成和学生关联的静态页面
			pageService.rebuildStaticPageForModifiedObject(stude, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		} catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	public void completeRegist(Stude stude, SessionInfo sessionInfo) throws ServiceException {
		// TODO 自动生成方法存根
		stude.setIsValid('1');
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(stude, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}

	public Stude createAlter(long studeId, SessionInfo sessionInfo) throws ServiceException {
		// TODO 自动生成方法存根
		String workflowInstanceId = null;
		try {
			//获取工作流入口列表
			List workflowEntries = workflowExploitService.listWorkflowEntries("educ/student", null, sessionInfo);
			if(workflowEntries==null || workflowEntries.isEmpty()) {
				throw new ServiceException("no privilege");
			}
			
	    	//获取学生记录
			Stude stude = (Stude)load(Stude.class, studeId);
			//复制学生信息
			Stude studeAlter = new Stude();
			PropertyUtils.copyProperties(studeAlter, stude);
			studeAlter.setId(UUIDLongGenerator.generateId()); //ID
			studeAlter.setAlterStudentId(studeId); //变更前的ID
			studeAlter.setIsValid('0');
			studeAlter.setIsAlter('1'); //设置为变更记录
			studeAlter.setOpinions(null);
			//获取流程
			WorkflowEntry workflowEntry = (WorkflowEntry)ListUtils.findObjectByProperty(workflowEntries, "workflowName", "学生变更");
			//创建工作流实例
			workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId(), false, studeAlter, null, sessionInfo);

	    	studeAlter.setWorkflowInstanceId(workflowInstanceId);
	    	getDatabaseService().saveRecord(studeAlter); //工作流实例ID
			return studeAlter;
		}
		catch(Exception e) {
			Logger.exception(e);
			try {
				//删除流程实例
				workflowExploitService.removeWorkflowInstance(workflowInstanceId, null, sessionInfo);
			}
			catch(Exception we) {
				
			}
			throw new ServiceException(e.getMessage());
		}
	}

	public boolean isStudentValid(long studeId) {
		// TODO 自动生成方法存根
		String hql = "select Stude.isValid from Stude Stude where Stude.id=" + studeId;
		Character isValid = (Character)getDatabaseService().findRecordByHql(hql);
		return isValid!=null && isValid.charValue()=='1';
	}
	
	/**
	 * 加密用户口令
	 * @param person
	 * @throws ServiceException
	 */
	private String encryptPersonPassword(long personId, String personLoginName, String password) throws ServiceException {
		if(password==null || password.equals("")) { //口令未设置,则以用户登录名为口令
		    password = personLoginName;
		}
		else if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
		//加密口令
		return encryptPassword(personId, password);
	}
	
	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
		return cryptService.encrypt(password, "" + userId, false);
		
	}

/*	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		// TODO 自动生成方法存根
		Stude stude = (Stude)getDatabaseService().findRecordByHql("from Stude Stude where Stude.loginId='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'"); //按登录用户名查找用户
		if(stude==null) {
			return false;
		}
		if(validateOldPassword) {
			if(stude.getPassword()==null || stude.getPassword().equals("")) {
			    if(!oldPassword.equals(stude.getLoginId())) {
			        throw(new WrongPasswordException()); //密码错误
			    }
			}
			else if(!encryptPassword(stude.getId(), oldPassword).equals(stude.getPassword())) {
				throw(new WrongPasswordException()); //密码错误
			}
		}
		//设置新密码
		stude.setPassword(encryptPassword(stude.getId(), newPassword));
		getDatabaseService().updateRecord(stude);
		return true;
	}

	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		// TODO 自动生成方法存根
		try {
			SessionInfo sessionInfo = new SessionInfo();
			Stude stude = (Stude)getDatabaseService().findRecordByHql("from Stude Stude where Stude.loginId='" + JdbcUtils.resetQuot(loginName) + "'"); //按登录用户名查找用户
			if(stude==null) { //不是企业用户
				return null;
			}
			//设置用户信息
			sessionInfo.setLoginName(stude.getLoginId());
			sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER);
			String password = stude.getPassword();
			if(password==null || password.equals("")) {
				password = stude.getLoginId();
			}
			else {//口令解密
				password = decryptPassword(stude.getId(), password);
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(stude.getName());
			sessionInfo.setUserId(stude.getId());
			sessionInfo.setDepartmentName(stude.getDepartment());
			
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	public Member getMember(long memberId) throws ServiceException {
		// TODO 自动生成方法存根
		Stude stude = (Stude)load(Stude.class, memberId);
		if(stude==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, stude);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PersonService.PERSON_TYPE_OTHER);
		member.setOriginalRecord(stude);
		return member;
	}

	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		// TODO 自动生成方法存根
		//		检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select Stude.id from Stude Stude where Stude.loginId='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		// TODO 自动生成方法存根
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		loginName = loginName.toLowerCase();
    	//用户校验
    	Stude stude = (Stude)getDatabaseService().findRecordByHql("from Stude Stude where Stude.loginId='" + JdbcUtils.resetQuot(loginName) + "'"); //按登录用户名查找用户
    	if(stude==null) {
    		return null;
    	}
    	//检查用户id是否正确
    	if(stude.getId()!=Long.parseLong(request.getParameter("userId"))) {
    		throw new LoginException(MemberService.LOGIN_INVALID_USERNAME_OR_PASSWORD);
    	}
    	String correctPassword = loginName; //正确的密码
		//密码校验
	    if(stude.getPassword()!=null && !stude.getPassword().equals("")) { //注:密码为空时,要求用户输入的密码必须和用户名相同
	    	correctPassword =  cryptService.decrypt(stude.getPassword(), "" + stude.getId(), false);
	    }
		return new MemberLogin(stude.getLoginId(), stude.getId(), PersonService.PERSON_TYPE_OTHER, correctPassword, !passwordMatcher.matching(correctPassword, password));
	}*/
	
//	/**
//	 * 口令加密
//	 * @param userId
//	 * @param password
//	 * @return
//	 * @throws ServiceException
//	 */
//	private String encryptPassword(long userId, String password) throws ServiceException {
//		if(password.startsWith("{") && password.endsWith("}")) { //口令解密
//			return password.substring(1, password.length() - 1);
//		}
//	    try {
//	        return cryptService.encrypt(password, "" + userId, false);
//	    } 
//	    catch (SecurityException e) {
//	        throw new ServiceException();
//	    }
//	}
//
//	/**
//	 * 口令解密
//	 * @param userId
//	 * @param password
//	 * @return
//	 * @throws ServiceException
//	 */
//	private String decryptPassword(long userId, String password) throws ServiceException {
//		try {
//			return cryptService.decrypt(password, "" + userId, false);
//		}
//		catch (SecurityException e) {
//			throw new ServiceException();
//		}
//	}
}