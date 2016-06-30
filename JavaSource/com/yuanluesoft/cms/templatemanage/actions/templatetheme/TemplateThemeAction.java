package com.yuanluesoft.cms.templatemanage.actions.templatetheme;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.templatemanage.forms.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateThemeAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
		//检查主题是否被使用
		TemplateThemeService templateThemeService = (TemplateThemeService)getBusinessService(form.getFormDefine());
		try {
			if(!templateThemeService.isThemeUsed(form.getId())) {
				return;
			}
		} 
		catch (ServiceException e) {
			
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		TemplateTheme themeForm = (TemplateTheme)form;
		themeForm.setLastModified(DateTimeUtils.now());
		themeForm.setLastModifier(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme theme = (com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme)record;
		if(theme==null) {
			form.getFormActions().removeFormAction("取消临时启用");
		}
		else if(theme.isDefaultTheme()) {
			form.getFormActions().removeFormAction("设为默认主题");
			form.getFormActions().removeFormAction("临时启用");
			form.getFormActions().removeFormAction("取消临时启用");
		}
		else if(theme.isTemporaryOpening()) {
			form.getFormActions().removeFormAction("临时启用");
		}
		else {
			form.getFormActions().removeFormAction("取消临时启用");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme theme = (com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme)record;
		theme.setLastModified(DateTimeUtils.now());
		theme.setLastModifier(sessionInfo.getUserName());
		theme.setLastModifierId(sessionInfo.getUserId());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}