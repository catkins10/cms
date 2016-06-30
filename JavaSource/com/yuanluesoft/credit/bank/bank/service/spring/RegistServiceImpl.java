package com.yuanluesoft.credit.bank.bank.service.spring;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.credit.bank.bank.pojo.Bank;
import com.yuanluesoft.credit.enterprise.pojo.Enterprise;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseIn;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseOut;
import com.yuanluesoft.credit.serviceorg.org.pojo.ServiceOrg;
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
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 银行注册服务
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

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		String directoryName = "";
		Org department = null;
		if (record instanceof Bank) {//银行信息
			directoryName = "银行";  
			department = (Org)orgService.createDirectory(-1, Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);                   
			Bank bank = (Bank) record;
			//注册用户
			personService.addEmployee(record.getId(), bank.getName(), bank.getLoginName(), bank.getPassword(), 'M', null, null, null, null, null, department.getId()+"", bank.getCreatorId(), bank.getCreator());
			bank.setLoginName(bank.getLoginName().toLowerCase()); //转换为小写
			bank.setPassword(encryptPersonPassword(bank.getId(), bank.getLoginName(), bank.getPassword())); //加密口令
		}else if(record instanceof ServiceOrg){//服务机构信息
			directoryName = "服务机构";
			department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
			ServiceOrg serviceOrg= (ServiceOrg)record;
			//注册用户
			personService.addEmployee(record.getId(), serviceOrg.getName(), serviceOrg.getLoginName(), serviceOrg.getPassword(), 'M', null, null, null, null, null, department.getId()+"", serviceOrg.getCreatorId(), serviceOrg.getCreator());
			serviceOrg.setLoginName(serviceOrg.getLoginName().toLowerCase()); //转换为小写
			serviceOrg.setPassword(encryptPersonPassword(serviceOrg.getId(), serviceOrg.getLoginName(), serviceOrg.getPassword())); //加密口令
		}else if(record instanceof Enterprise){
			directoryName = "企业";
			department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
			Enterprise enterprise= (Enterprise)record;
			//注册用户
			personService.addEmployee(record.getId(), enterprise.getName(), enterprise.getLoginName(), enterprise.getPassword(), 'M', null, null, null, null, null, department.getId()+"", enterprise.getCreatorId(), enterprise.getCreator());
			enterprise.setLoginName(enterprise.getLoginName().toLowerCase()); //转换为小写
			enterprise.setPassword(encryptPersonPassword(enterprise.getId(), enterprise.getLoginName(), enterprise.getPassword())); //加密口令
		}else if(record instanceof EnterpriseIn){
			directoryName = "企业";
			department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
			EnterpriseIn enterprise= (EnterpriseIn)record;
			//注册用户
			personService.addEmployee(record.getId(), enterprise.getName(), enterprise.getLoginName(), enterprise.getPassword(), 'M', null, null, null, null, null, department.getId()+"", enterprise.getCreatorId(), enterprise.getCreator());
			enterprise.setLoginName(enterprise.getLoginName().toLowerCase()); //转换为小写
			enterprise.setPassword(encryptPersonPassword(enterprise.getId(), enterprise.getLoginName(), enterprise.getPassword())); //加密口令
		}
		else if(record instanceof EnterpriseOut){
			directoryName = "企业";
			department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
			EnterpriseOut enterprise= (EnterpriseOut)record;
			//注册用户
			personService.addEmployee(record.getId(), enterprise.getName(), enterprise.getLoginName(), enterprise.getPassword(), 'M', null, null, null, null, null, department.getId()+"", enterprise.getCreatorId(), enterprise.getCreator());
			enterprise.setLoginName(enterprise.getLoginName().toLowerCase()); //转换为小写
			enterprise.setPassword(encryptPersonPassword(enterprise.getId(), enterprise.getLoginName(), enterprise.getPassword())); //加密口令
		}
		
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.save(record);
	}
	
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if (record instanceof Bank) {//银行信息
			Bank bankOld = (Bank)load(Bank.class, record.getId());
			Bank bankNew = (Bank)record;
			//用户信息变更
			if(!bankOld.getName().equals(bankNew.getName()) || !bankOld.getLoginName().equals(bankNew.getLoginName()) || !bankOld.getPassword().equals(bankNew.getPassword())){
				Person person = (Person)load(Person.class, record.getId());
				if(person!=null){
					person.setName(bankNew.getName());
					person.setLoginName(bankNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), bankNew.getPassword())); //加密口令
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
			bankNew.setLoginName(bankNew.getLoginName().toLowerCase()); //转换为小写
			bankNew.setPassword(encryptPersonPassword(bankNew.getId(), bankNew.getLoginName(), bankNew.getPassword())); //加密口令
		}else if(record instanceof ServiceOrg){//服务机构信息
			ServiceOrg serviceOrgOld = (ServiceOrg)load(ServiceOrg.class, record.getId());
			ServiceOrg serviceOrgNew = (ServiceOrg)record;
			//用户信息变更
			if(!serviceOrgOld.getName().equals(serviceOrgNew.getName()) || !serviceOrgOld.getLoginName().equals(serviceOrgNew.getLoginName()) || !serviceOrgOld.getPassword().equals(serviceOrgNew.getPassword())){
				Person person = (Person)load(Person.class, record.getId());
				if(person!=null){
					person.setName(serviceOrgNew.getName());
					person.setLoginName(serviceOrgNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), serviceOrgNew.getPassword())); //加密口令
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
			serviceOrgNew.setLoginName(serviceOrgNew.getLoginName().toLowerCase()); //转换为小写
			serviceOrgNew.setPassword(encryptPersonPassword(serviceOrgNew.getId(), serviceOrgNew.getLoginName(), serviceOrgNew.getPassword())); //加密口令
		}else if(record instanceof Enterprise){//个体企业信息
			Enterprise enterpriseOld = (Enterprise)load(Enterprise.class, record.getId());
			Enterprise enterpriseNew = (Enterprise)record;
			//用户信息变更
			if(!enterpriseOld.getName().equals(enterpriseNew.getName()) || !enterpriseOld.getLoginName().equals(enterpriseNew.getLoginName()) || !enterpriseOld.getPassword().equals(enterpriseNew.getPassword())){
				Person person = (Person)load(Person.class, record.getId());
				if(person!=null){
					person.setName(enterpriseNew.getName());
					person.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), enterpriseNew.getPassword())); //加密口令
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
			enterpriseNew.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
			enterpriseNew.setPassword(encryptPersonPassword(enterpriseNew.getId(), enterpriseNew.getLoginName(), enterpriseNew.getPassword())); //加密口令
		}
		else if(record instanceof EnterpriseIn){//内资企业信息
			EnterpriseIn enterpriseOld = (EnterpriseIn)load(EnterpriseIn.class, record.getId());
			EnterpriseIn enterpriseNew = (EnterpriseIn)record;
			//用户信息变更
			if(!enterpriseOld.getName().equals(enterpriseNew.getName()) || !enterpriseOld.getLoginName().equals(enterpriseNew.getLoginName()) || !enterpriseOld.getPassword().equals(enterpriseNew.getPassword())){
				Person person = (Person)load(Person.class, record.getId());
				if(person!=null){
					person.setName(enterpriseNew.getName());
					person.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), enterpriseNew.getPassword())); //加密口令
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
			enterpriseNew.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
			enterpriseNew.setPassword(encryptPersonPassword(enterpriseNew.getId(), enterpriseNew.getLoginName(), enterpriseNew.getPassword())); //加密口令
		}
		else if(record instanceof EnterpriseOut){//外资企业信息
			EnterpriseOut enterpriseOld = (EnterpriseOut)load(EnterpriseOut.class, record.getId());
			EnterpriseOut enterpriseNew = (EnterpriseOut)record;
			//用户信息变更
			if(!enterpriseOld.getName().equals(enterpriseNew.getName()) || !enterpriseOld.getLoginName().equals(enterpriseNew.getLoginName()) || !enterpriseOld.getPassword().equals(enterpriseNew.getPassword())){
				Person person = (Person)load(Person.class, record.getId());
				if(person!=null){
					person.setName(enterpriseNew.getName());
					person.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
					String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
					person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), enterpriseNew.getPassword())); //加密口令
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
			enterpriseNew.setLoginName(enterpriseNew.getLoginName().toLowerCase()); //转换为小写
			enterpriseNew.setPassword(encryptPersonPassword(enterpriseNew.getId(), enterpriseNew.getLoginName(), enterpriseNew.getPassword())); //加密口令
		}
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.update(record);
	}
	
	public void delete(Record record) throws ServiceException {
		// TODO 自动生成方法存根
		Person person = (Person)personService.getPerson(record.getId());
		if(person!=null){
			personService.delete(person);
		}
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		super.delete(record);
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
	
	
	
	
}