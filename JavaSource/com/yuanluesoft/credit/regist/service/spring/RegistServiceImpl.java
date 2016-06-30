package com.yuanluesoft.credit.regist.service.spring;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.credit.regist.pojo.admin.CreditUser;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 注册服务
 * @author zyh
 *
 */
public class RegistServiceImpl extends BusinessServiceImpl {
	private PersonService personService ;
	private BusinessService businessService;
	private CryptService cryptService; //加/解密服务
	private OrgService orgService; //组织机构服务
	private String mainOrgId ;
	private SessionService sessionService;
	private PageService pageService;
	private UserSecurityService userSecurityService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		String directoryName = "";
		Org department = null;
		directoryName = "企业会员用户";  
		department = (Org)orgService.createDirectory(-1, Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);                   
		CreditUser user = (CreditUser) record;
		user.setPassword(encryptPersonPassword(user.getId(), user.getLoginName(), user.getPassword())); //加密口令
		if(user.getStatus()==1){
			//注册用户
			personService.addEmployee(record.getId(), user.getName(), user.getLoginName(), user.getPassword(), 'M', null, null, null, null, null, department.getId()+"", user.getApproverId(), user.getApprover());
			user.setLoginName(user.getLoginName().toLowerCase()); //转换为小写
		}
		//pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.save(record);
	}
	
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		CreditUser userOld = (CreditUser)load(CreditUser.class, record.getId());
		CreditUser userNew = (CreditUser)record;
			Person person = (Person)load(Person.class, record.getId());
			//审核
			if(userNew.getStatus()==1 && person==null){
				String directoryName = "";
				Org department = null;
				directoryName = "企业会员用户";  
				department = (Org)orgService.createDirectory(-1, Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);                   
				
//				注册用户
				personService.addEmployee(record.getId(), userNew.getName(), userNew.getLoginName(), userNew.getPassword(), 'M', null, null, null, null, null, department.getId()+"", userNew.getApproverId(), userNew.getApprover());
			//重新启用用户
			}else if(userNew.getStatus()==1 && person!=null && userSecurityService.isHalt(person.getId())){
//				设置启用
				userSecurityService.setHalt(person.getId(), false);
				businessService.update(person);
			}else if(userNew.getStatus()==2 && person!=null){
				//设置停用
				userSecurityService.setHalt(person.getId(), true);
				businessService.update(person);
			}
			
			if(!userOld.getName().equals(userNew.getName()) || !userOld.getLoginName().equals(userNew.getLoginName()) || !userOld.getPassword().equals(userNew.getPassword())){
//				用户信息变更
				if(person!=null){
					person.setName(userNew.getName());
					person.setLoginName(userNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(userNew.getId(), userNew.getLoginName(), userNew.getPassword())); //加密口令
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
			userNew.setLoginName(userNew.getLoginName().toLowerCase()); //转换为小写
			userNew.setPassword(encryptPersonPassword(userNew.getId(), userNew.getLoginName(), userNew.getPassword())); //加密口令
		//pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.update(record);
	}
	
	public void delete(Record record) throws ServiceException {
		// TODO 自动生成方法存根
		Person person = (Person)personService.getPerson(record.getId());
		if(person!=null){
			personService.delete(person);
		}
		//pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		super.delete(record);
	}

	/**
	 * 加密用户口令
	 * @param person
	 * @throws ServiceException
	 */
	public String encryptPersonPassword(long personId, String personLoginName, String password) throws ServiceException {
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
	
	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}



	public CryptService getCryptService() {
		return cryptService;
	}



	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
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



	public BusinessService getBusinessService() {
		return businessService;
	}



	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}



	public SessionService getSessionService() {
		return sessionService;
	}



	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}



	public PageService getPageService() {
		return pageService;
	}



	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}



	public UserSecurityService getUserSecurityService() {
		return userSecurityService;
	}



	public void setUserSecurityService(UserSecurityService userSecurityService) {
		this.userSecurityService = userSecurityService;
	}
	
	
	
	
}