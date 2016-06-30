package com.yuanluesoft.cms.templatemanage.actions.customcssfile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.templatemanage.forms.CustomCssFile;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class CustomCssFileAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		CustomCssFile customForm = (CustomCssFile)form;
		SiteService siteService = (SiteService)getService("siteService");
		if(siteService.checkPopedom(customForm.getSiteId(), "manager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		CustomCssFile customForm = (CustomCssFile)form;
		if(id==0 && customForm.getCssUrl()!=null) {
	        //加载CSS文件
	        TemplateService templateService = (TemplateService)getService("templateService");
	        return templateService.loadCssFile(customForm.getCssUrl());
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		CustomCssFile customForm = (CustomCssFile)form;
		TemplateService templateService = (TemplateService)getService("templateService");
		customForm.setFromCssFile(customForm.getCssUrl());
		customForm.setCssText(templateService.loadCssText(customForm.getCssUrl()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		CustomCssFile customForm = (CustomCssFile)form;
		TemplateService templateService = (TemplateService)getService("templateService");
		customForm.setCssText(templateService.loadCssText(customForm.getCssUrl()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		CustomCssFile customForm = (CustomCssFile)form;
        TemplateService templateService = (TemplateService)getService("templateService");
        //保存CSS
        templateService.saveCssFile(customForm.getId(), customForm.getCssName(), customForm.getCssUrl(), customForm.getFromCssFile(), customForm.getSiteId(), customForm.getCssText());
     	return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		CustomCssFile customForm = (CustomCssFile)form;
        TemplateService templateService = (TemplateService)getService("templateService");
        templateService.deleteCssFile(customForm.getId());
	}

	/**
	 * 关闭对话框并更新源窗口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void refreshDialogOpener(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String html = "<html>" +
					  " <head>" +
					  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
					  "  <script>" +
					  "  function refreshDialogOpener() {" +
					  "    DialogUtils.getDialogOpener().setTimeout('location.reload()', 1);" +
					  "    DialogUtils.closeDialog();" +
					  "  }" +
					  "  </script>" +
					  " </head>" +
					  " <body onload=\"refreshDialogOpener()\"></body>" +
					  "</html>";
		response.getWriter().write(html);
	}
}