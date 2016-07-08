package com.yuanluesoft.educ.teach.service.spring;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.educ.teach.pojo.Teach;
import com.yuanluesoft.educ.teach.service.TeachService;
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

public class TeachServiceImpl extends BusinessServiceImpl implements
		TeachService {
	private PersonService personService ;
	private BusinessService businessService;
	private CryptService cryptService; //加/解密服务
	private OrgService orgService; //组织机构服务
	private String mainOrgId;
	private SessionService sessionService;
	private PageService pageService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		String directoryName = "";
		Org department = null;
		
		directoryName = "教师";
		department = (Org)orgService.createDirectory(-1,Long.valueOf(mainOrgId).longValue(), directoryName, "unitDepartment", null, 0, null);
		Teach teach= (Teach)record;
		teach.setPassword("123456");
		//注册用户
		personService.addEmployee(record.getId(), teach.getName(), teach.getLoginId(), teach.getPassword(), teach.getSex(), null, null, null, null, null, department.getId()+"", record.getId(), teach.getName());
		teach.setLoginId(teach.getLoginId().toLowerCase()); //转换为小写
		teach.setPassword(encryptPersonPassword(teach.getId(), teach.getLoginId(), teach.getPassword())); //加密口令
		
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return super.save(record);
	}
	
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		
		Teach teachOld = (Teach)load(Teach.class, record.getId());
		Teach teachNew = (Teach)record;
		//用户信息变更
		if(!teachOld.getName().equals(teachNew.getName()) || !teachOld.getLoginId().equals(teachNew.getLoginId()) || !teachOld.getPassword().equals(teachNew.getPassword())){
			Person person = (Person)load(Person.class, record.getId());
			if(person!=null){
				person.setName(teachNew.getName());
				person.setLoginName(teachNew.getLoginId().toLowerCase()); //转换为小写
				String oldLoginName = (String)getDatabaseService().findRecordByHql("select Person.loginName from Person Person where Person.id=" + person.getId());
				person.setPassword(encryptPersonPassword(person.getId(), person.getLoginName(), teachNew.getPassword())); //加密口令
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
		teachNew.setLoginId(teachNew.getLoginId().toLowerCase()); //转换为小写
		
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
