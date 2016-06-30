package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.pojo.UserLoginTemplate;
import com.yuanluesoft.jeaf.usermanage.pojo.UserPageTemplate;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class UserPageTemplateServiceImpl extends TemplateServiceImpl implements UserPageTemplateService {
	private PersonService personService; //用户服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof UserLoginTemplate) {
			UserLoginTemplate userLoginTemplate = (UserLoginTemplate)record;
			if(userLoginTemplate.getHostNames()!=null && userLoginTemplate.getHostNames().startsWith(",")) {
				userLoginTemplate.setHostNames(userLoginTemplate.getHostNames().substring(1, userLoginTemplate.getHostNames().length() - 1));
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof UserLoginTemplate) {
			resetUserLoginTemplate((UserLoginTemplate)record);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof UserLoginTemplate) {
			resetUserLoginTemplate((UserLoginTemplate)record);
		}
		return super.update(record);
	}
	
	/**
	 * 重设登录模版
	 * @param userLoginTemplate
	 */
	private void resetUserLoginTemplate(UserLoginTemplate userLoginTemplate) {
		if(userLoginTemplate.getHostNames()==null) {
			return;
		}
		String hostNames = StringUtils.trim(userLoginTemplate.getHostNames());
		userLoginTemplate.setHostNames(hostNames.isEmpty() ? null : "," + hostNames + ",");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		if(template instanceof UserLoginTemplate) {
			UserLoginTemplate userLoginTemplate = (UserLoginTemplate)template;
			//获取相同域名的模板列表
			String hql = "from UserLoginTemplate UserLoginTemplate" +
						 " where UserLoginTemplate.hostNames" + (userLoginTemplate.getHostNames()==null || userLoginTemplate.getHostNames().isEmpty() ? " is null" : "='" + JdbcUtils.resetQuot(userLoginTemplate.getHostNames()) + "'");
			return getDatabaseService().findRecordsByHql(hql);
		}
		UserPageTemplate userPageTemplate = (UserPageTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from UserPageTemplate UserPageTemplate" +
					 " where UserPageTemplate.applicationName='" + JdbcUtils.resetQuot(template.getApplicationName()) + "'" +
					 " and UserPageTemplate.pageName='" + JdbcUtils.resetQuot(template.getPageName()) + "'" +
					 " and UserPageTemplate.userId='" + userPageTemplate.getUserId() + "'" +
					 " and UserPageTemplate.applicationNames" + (userPageTemplate.getApplicationNames()==null || userPageTemplate.getApplicationNames().isEmpty() ? " is null" : "='" + JdbcUtils.resetQuot(userPageTemplate.getApplicationNames()) + "'");
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		if(template==null) {
			return null;
		}
		if(template instanceof UserLoginTemplate) { //用户登录模版
			UserLoginTemplate userLoginTemplate = (UserLoginTemplate)template;
			if(userLoginTemplate.getHostNames()==null || userLoginTemplate.getHostNames().isEmpty()) {
				return null;
			}
			String hql = "from UserLoginTemplate UserLoginTemplate" +
						 " where UserLoginTemplate.hostNames is null" +
						 " order by UserLoginTemplate.isSelected DESC";
			return (UserLoginTemplate)getDatabaseService().findRecordByHql(hql);
		}
		UserPageTemplate userPageTemplate = (UserPageTemplate)template;
		if(userPageTemplate.getUserId()==0) {
			return null;
		}
		long departmentId;
		long personId = 0;
		Person person = personService.getPerson(userPageTemplate.getUserId());
		if(person==null) { //当前模板不是个人的
			departmentId = userPageTemplate.getUserId();
		}
		else { //当前模板是个人的
			personId = person.getId();
			departmentId = ((PersonSubjection)person.getSubjections().iterator().next()).getOrgId();
		}
		return getTemplate(personId, departmentId, template.getApplicationName(), template.getPageName(), false, true, null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService#getTemplateHTMLDocument(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String applicationName, String pageName, SessionInfo sessionInfo, HttpServletRequest request) throws ServiceException {
		if(pageName.equals("internalLogin")) { //登录页面
			return getLoginTemplateHTMLDocument(request);
		}
		HTMLDocument templateDocument = null;
		String cacheKey = applicationName + "/" +
						  pageName + "/" +
						  ("application".equals(pageName) ? request.getParameter("applicationName") + "/" : "") + //应用页面
						  (sessionInfo==null || sessionInfo.getLoginName().equals(SessionService.ANONYMOUS) ? 0 : sessionInfo.getUserId());
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(cacheKey);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument!=null) {
			return (HTMLDocument)templateDocument.cloneNode(true);
		}
		//获取模板
		long personId = 0;
		long departmentId = 0;
		if(sessionInfo!=null && !sessionInfo.getLoginName().equals(SessionService.ANONYMOUS)) {
			personId = sessionInfo.getUserId();
			departmentId = sessionInfo.getDepartmentId();
		}
		UserPageTemplate template = null;
		if("application".equals(pageName)) {
			template = getTemplate(personId, departmentId, applicationName, pageName, true, false, request);
		}
		if(template==null) {
			template = getTemplate(personId, departmentId, applicationName, pageName, false, false, request);
		}
		if(template!=null) {
			templateDocument = getTemplateHTMLDocument(template.getId(), 0, false, request);
		}
		else {
			NormalTemplate normalTemplate = getNormalTemplateHTMLDocument(applicationName, pageName, TemplateThemeService.THEME_TYPE_COMPUTER); //获取预设模板
			if(normalTemplate!=null) {
				templateDocument = normalTemplate.getHtmlDocument();
			}
		}
		if(templateDocument==null) {
			return null;
		}
		//写入缓存
		try {
			getCache().put(cacheKey, templateDocument);
		}
		catch (CacheException e) {
			
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/**
	 * 获取登录模版
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getLoginTemplateHTMLDocument(HttpServletRequest request) throws ServiceException {
		final String hostName = request.getServerName().toLowerCase();
		String cacheKey = "login/" + hostName;
		HTMLDocument templateDocument = null;
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(cacheKey);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument!=null) {
			return (HTMLDocument)templateDocument.cloneNode(true);
		}
		String hql = "from UserLoginTemplate UserLoginTemplate" +
					 " where UserLoginTemplate.hostNames like '%," + JdbcUtils.resetQuot(hostName) + ",%'";
		for(int index = hostName.indexOf('.'); index!=-1; index = hostName.indexOf('.', index + 1)) {
			hql += " or UserLoginTemplate.hostNames like '%," + JdbcUtils.resetQuot(hostName.substring(index + 1)) + ",%'";
		}
		hql += " or UserLoginTemplate.hostNames is null" +
			   " order by UserLoginTemplate.isSelected DESC";
		List templates = getDatabaseService().findRecordsByHql(hql);
		UserLoginTemplate template = null;
		UserLoginTemplate noneHostTemplate = null; //没有指定域名的模版
		int level = hostName.length();
		for(Iterator iterator = templates==null ? null : templates.iterator(); level>0 && iterator!=null && iterator.hasNext();) {
			UserLoginTemplate loginTemplate = (UserLoginTemplate)iterator.next();
			if(loginTemplate.getHostNames()==null) {
				noneHostTemplate = noneHostTemplate==null ? loginTemplate : noneHostTemplate;
				continue;
			}
			for(int index=0; index!=-1 && index<level; index=hostName.indexOf('.', index + 1)) {
				if(loginTemplate.getHostNames().indexOf("," + (index==0 ? hostName : hostName.substring(index + 1)) + ",")!=-1) {
					template = loginTemplate;
					level = index;
					break;
				}
			}
		}
		template = template==null ? noneHostTemplate : template;
		if(template!=null) {
			templateDocument = getTemplateHTMLDocument(template.getId(), 0, false, request);
		}
		else {
			NormalTemplate normalTemplate = getNormalTemplateHTMLDocument("jeaf/usermanage", "internalLogin", TemplateThemeService.THEME_TYPE_COMPUTER); //获取预设模板
			if(normalTemplate!=null) {
				templateDocument = normalTemplate.getHtmlDocument();
			}
		}
		if(templateDocument==null) {
			return null;
		}
		//写入缓存
		try {
			getCache().put(cacheKey, templateDocument);
		}
		catch (CacheException e) {
			
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}

	/**
	 * 获取模板
	 * @param personId
	 * @param departmentId
	 * @param applicationName
	 * @param pageName
	 * @param applicationPageFilter 当pageName=="application"时,是否按应用名称过滤页面
	 * @param parentTemplate
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected UserPageTemplate getTemplate(long personId, long departmentId, String applicationName, String pageName, boolean applicationPageFilter, boolean parentTemplate, HttpServletRequest request) throws ServiceException {
		String hqlFilter = (!applicationPageFilter ? " and UserPageTemplate.applicationNames is null" : " and concat(';', concat(UserPageTemplate.applicationNames, ';')) like '%;" + JdbcUtils.resetQuot(request.getParameter("applicationName")) + ";%'");
		if(!parentTemplate && personId>0) { //不是获取父模板,且用户ID大于0
			//查找用户自己的模板
			String hql = "from UserPageTemplate UserPageTemplate" +
						 " where UserPageTemplate.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
						 " and UserPageTemplate.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
						 " and UserPageTemplate.userId=" + personId +
						 hqlFilter +
						 " order by UserPageTemplate.isSelected DESC";
			UserPageTemplate template = (UserPageTemplate)getDatabaseService().findRecordByHql(hql);
			if(template!=null) { //有自己的模板
				return template;
			}
		}
		boolean retrieveDepartmentTemplate = personId>0 || !parentTemplate; //用户ID大于0或者获取自己的模板，获取用户所在部门的模板
		//获取上级机构的模板
		String hql = "select UserPageTemplate" +
					 " from UserPageTemplate UserPageTemplate, OrgSubjection OrgSubjection" +
					 " where UserPageTemplate.userId=OrgSubjection.parentDirectoryId" +
					 " and UserPageTemplate.applicationName='" + JdbcUtils.resetQuot(applicationName) + "'" +
					 " and UserPageTemplate.pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
					 " and OrgSubjection.directoryId=" + departmentId +
					 (retrieveDepartmentTemplate ? "" : " and OrgSubjection.parentDirectoryId!=" + departmentId) +
					 hqlFilter +
				     " order by OrgSubjection.id, UserPageTemplate.isSelected DESC";
		return (UserPageTemplate)getDatabaseService().findRecordByHql(hql);
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}