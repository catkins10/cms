/*
 * Created on 2007-7-7
 *
 */
package com.yuanluesoft.cms.templatemanage.actions.template;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.cms.templatemanage.forms.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 *
 * @author linchuan
 *
 */
public class TemplateAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#isLockByPerson(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#lock(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void lock(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//获取站点权限
		Template templateForm = (Template)form;
		long siteId;
		if(record==null) { //新记录
			siteId = templateForm.getSiteId();
		}
		else {
			siteId = ((com.yuanluesoft.cms.templatemanage.pojo.Template)record).getSiteId();
		}
		SiteService siteService = (SiteService)getService("siteService");
		//获取用户对站点的权限
		if(!siteService.checkPopedom(siteId, "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		TemplateService templateService = (TemplateService)getBusinessService(form.getFormDefine());
		Template templateForm = (Template)form;
		templateForm.setIsSelected('0');
		TemplateTheme theme = templateForm.getThemeId()==0 ? null : getTemplateTheme(templateForm.getThemeId());
		templateForm.setThemePageWidth(theme==null ? 0 : theme.getPageWidth());
		templateForm.setThemeType(theme==null ? TemplateThemeService.THEME_TYPE_COMPUTER : theme.getType());
		//设置模板HTML
		try {
			HTMLDocument templateDocument = templateService.getInitializeTemplateHTMLDocument(templateForm.getThemeId(), templateForm.getSiteId(), true, request);
			if(templateDocument!=null) {
				HTMLParser htmlParser = (HTMLParser)getService("htmlParser");
				templateForm.setPageHTML(htmlParser.getDocumentHTML(templateDocument, "utf-8"));
			}
		}
		catch(Exception e) {
			
		}
		//设置站点页面配置
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
		SitePage sitePage = pageDefineService.getSitePage(templateForm.getApplicationName(), templateForm.getPageName());
		templateForm.setSearchPage(sitePage.isSearchPage());
		templateForm.setSearchResultsName(sitePage.getSearchResults());
		templateForm.setFormTitle("模板配置 - " + sitePage.getTitle());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(record==null) { //新建模板、且在模板上传时,记录为空
			return;
		}
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Template templateForm = (Template)form;
		TemplateService templateService = (TemplateService)getBusinessService(form.getFormDefine());
		//设置模板HTML
		HTMLDocument templateDocument = templateService.getTemplateHTMLDocument(templateForm.getId(), -1, true, request);
		if(templateDocument!=null) {
			HTMLParser htmlParser = (HTMLParser)getService("htmlParser");
			templateForm.setPageHTML(htmlParser.getDocumentHTML(templateDocument, "utf-8"));
		}
		//设置站点页面配置
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
		SitePage sitePage = pageDefineService.getSitePage(templateForm.getApplicationName(), templateForm.getPageName());
		templateForm.setSearchPage(sitePage.isSearchPage());
		templateForm.setSearchResultsName(sitePage.getSearchResults());
		templateForm.setFormTitle(templateForm.getTemplateName() + " - 模板");
		//设置页面宽度
		TemplateTheme theme = templateForm.getThemeId()==0 ? null : getTemplateTheme(templateForm.getThemeId());
		templateForm.setThemePageWidth(theme==null ? 0 : theme.getPageWidth());
		templateForm.setThemeType(theme==null ? TemplateThemeService.THEME_TYPE_COMPUTER : theme.getType());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Template templateForm = (Template)form;
		com.yuanluesoft.cms.templatemanage.pojo.Template template = (com.yuanluesoft.cms.templatemanage.pojo.Template)record;
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
    	//重置页面HTML字段
		Field pageHtmlField = form.getFormDefine().getField("pageHTML");
		pageDefineService.retrievePageHtmlField(pageHtmlField, template==null ? templateForm.getApplicationName() : template.getApplicationName(), template==null ? templateForm.getPageName() : template.getPageName(), sessionInfo);
		templateForm.setInternalForm(true);
	}

	/**
	 * 获取主题
	 * @param themeId
	 * @return
	 * @throws ServiceException
	 */
	private TemplateTheme getTemplateTheme(long themeId) throws ServiceException {
		SiteTemplateThemeService themeService = (SiteTemplateThemeService)getService("siteTemplateThemeService");
		return (TemplateTheme)themeService.load(TemplateTheme.class, themeId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Template templateForm = (Template)form;
		TemplateService templateService = (TemplateService)getBusinessService(form.getFormDefine());
		
		//保存数据库记录
		if(record!=null){
			com.yuanluesoft.cms.templatemanage.pojo.Template template = (com.yuanluesoft.cms.templatemanage.pojo.Template)record;
			template.setLastModified(DateTimeUtils.now()); //最后修改时间
			template.setLastModifierId(sessionInfo.getUserId()); //最后修改人ID
			template.setLastModifier(sessionInfo.getUserName()); //最后修改人姓名
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		
		//执行模板HTML页面操作
		if("upload".equals(templateForm.getTemplateAction())) { //上传模板
			templateService.uploadTemplate(templateForm.getId());
		}
		else if("restore".equals(templateForm.getTemplateAction())) { //恢复到上一个模板
			templateService.restoreTemplateHTML(templateForm.getId());
		}
		else if("copy".equals(templateForm.getTemplateAction())) { //模板复制
			templateService.copyTemplate(templateForm.getId(), templateForm.getCopiedTemplateId());
		}
		else if("loadNormal".equals(templateForm.getTemplateAction())) { //加载预置模板
			templateService.loadNormalTemplate(templateForm.getApplicationName(), templateForm.getPageName(), templateForm.getId());
		}
		else { //其他
			templateService.saveTemplateHTML(templateForm.getId(), templateForm.getPageHTML(), request); //保存模板HTML
		}
		//设置为默认模板
		if("setAsDefault".equals(templateForm.getTemplateAction())) {
			templateService.setDefaultTemplate(templateForm.getId());
		}
		return record;
	}
}