package com.yuanluesoft.cms.sitemanage.actions.headline;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.forms.Headline;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.CookieUtils;

/**
 * 
 * @author linchuan
 *
 */
public class HeadlineAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Headline headlineForm = (Headline)form;
		SiteService siteService = (SiteService)getService("siteService");
		String managedDirectoryIds = null;
		String[] directoryIds = headlineForm.getDirectoryIds().split(",");
		for(int i=0; i<directoryIds.length; i++) {
			//用户要有管理权限
			if(siteService.checkPopedom(Long.parseLong(directoryIds[i]), "manager", sessionInfo)) {
				managedDirectoryIds = (managedDirectoryIds==null ? "" : managedDirectoryIds + ",") + directoryIds[i];
			}
		}
		if(managedDirectoryIds==null) {
			throw new PrivilegeException();
		}
		headlineForm.setDirectoryIds(managedDirectoryIds);
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Headline headlineForm = (Headline)form;
		SiteService siteService = (SiteService)getService("siteService");
		return siteService.getHeadline(Long.parseLong(headlineForm.getDirectoryIds().split(",")[0]));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
    	//保存头版头条
		Headline headlineForm = (Headline)form;
		SiteService siteService = (SiteService)getService("siteService");
		for(int i=0; i<headlineForm.getSelectedDirectoryIds().length; i++) {
			siteService.setHeadline(Long.parseLong(headlineForm.getSelectedDirectoryIds()[i]), headlineForm.getHeadlineName(), headlineForm.getHeadlineURL(), headlineForm.getSummarize());
		}
    	return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		writeForm(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		writeForm(form, request, response);
	}
	
	/**
	 * 填充页面
	 * @param form
	 * @throws Exception
	 */
	private void writeForm(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Headline headlineForm = (Headline)form;
		headlineForm.setSelectedDirectoryIds(headlineForm.getDirectoryIds().split(","));
		//从cookie获取头版头条名称、概述以及链接地址
		String headlineName = CookieUtils.getCookie(request, "headlineName");
		if(headlineName!=null && !headlineName.isEmpty()) {
			headlineForm.setHeadlineName(URLDecoder.decode(headlineName, "utf-8"));
		}
		String summarize = CookieUtils.getCookie(request, "summarize");
		if(summarize!=null && !summarize.isEmpty()) {
			headlineForm.setSummarize(URLDecoder.decode(summarize, "utf-8"));
		}
		String headlineURL = CookieUtils.getCookie(request, "headlineURL");
		if(headlineURL!=null && !headlineURL.isEmpty()) {
			headlineForm.setHeadlineURL(URLDecoder.decode(headlineURL, "utf-8"));
		}
		CookieUtils.removeCookie(response, "headlineName", "/", null);
		CookieUtils.removeCookie(response, "summarize", "/", null);
		CookieUtils.removeCookie(response, "headlineURL", "/", null);
	}
}