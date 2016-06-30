package com.yuanluesoft.cms.sitemanage.actions.sitetemplatetheme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.forms.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.cms.templatemanage.actions.templatetheme.TemplateThemeAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SiteTemplateThemeAction extends TemplateThemeAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(id==0) {
			return null;
		}
		SiteTemplateTheme siteTemplateThemeForm = (SiteTemplateTheme)form;
		SiteTemplateThemeService siteTemplateThemeService = (SiteTemplateThemeService)getService("siteTemplateThemeService");
		return siteTemplateThemeService.getSiteTemplateTheme(id, siteTemplateThemeForm.getPageSiteId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		SiteService siteService = (SiteService)getService("siteService");
		if(!siteService.checkPopedom(themeForm.getPageSiteId(), "manager", sessionInfo)) { //检查用户是否当前站点的管理员
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.actions.templatetheme.TemplateThemeAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme theme = (com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme)record;
		if(theme!=null) {
			SiteService siteService = (SiteService)getService("siteService");
			if(themeForm.getPageSiteId()!=theme.getSiteId() && !siteService.checkPopedom(theme.getSiteId(), "manager", sessionInfo)) { //检查用户是否站点的管理员
				themeForm.setSubForm("Read");
			}
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(theme!=null && ListUtils.findObjectByProperty(theme.getUsages(), "siteId", new Long(themeForm.getPageSiteId()))==null) { //模板不是在当前站点临时启用的
			form.getFormActions().removeFormAction("取消临时启用");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		setSiteName(form);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		setSiteName(form);
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		themeForm.setPageSiteId(themeForm.getSiteId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateReloadURL(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String generateReloadURL(ActionForm form, HttpServletRequest request) {
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		String url = super.generateReloadURL(form, request);
		return url==null ? null : url + "&pageSiteId=" + themeForm.getPageSiteId();
	}

	/**
	 * 设置站点名称
	 * @param form
	 * @throws Exception
	 */
	private void setSiteName(ActionForm form) throws Exception {
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		SiteService siteService = (SiteService)getService("siteService");
		if(themeForm.getSiteName()==null || themeForm.getSiteName().isEmpty()) {
			themeForm.setSiteName(siteService.getDirectoryName(themeForm.getSiteId()));
		}
		themeForm.setPageSiteName(themeForm.getPageSiteId()==themeForm.getSiteId() ? themeForm.getSiteName() : siteService.getDirectoryName(themeForm.getPageSiteId()));
	}
}