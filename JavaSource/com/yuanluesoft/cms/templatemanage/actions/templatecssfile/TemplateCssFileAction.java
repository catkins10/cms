package com.yuanluesoft.cms.templatemanage.actions.templatecssfile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.templatemanage.forms.TemplateCssFile;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * CSS文件编辑
 * @author linchuan
 *
 */
public class TemplateCssFileAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			TemplateCssFile templateCssFileForm = (TemplateCssFile)form;
			char accessLevel = getRecordControlService().getRegistedRecordAccessLevel(templateCssFileForm.getId(), request.getSession());
			if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
				return;
			}
		}
		catch (ServiceException e) {
			
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		TemplateCssFile templateCssFileForm = (TemplateCssFile)form;
		//获取CSS内容
        AttachmentService attachmentService = (AttachmentService)getService("templateAttachmentService");
        String cssFile = attachmentService.getSavePath("cms/templatemanage", "css", templateCssFileForm.getId(), false) + templateCssFileForm.getFileName();
        templateCssFileForm.setCssContent(FileUtils.readStringFromFile(cssFile, "utf-8"));
 	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		TemplateCssFile templateCssFileForm = (TemplateCssFile)form;
		//保存CSS内容
        AttachmentService attachmentService = (AttachmentService)getService("templateAttachmentService");
        String cssFile = attachmentService.getSavePath("cms/templatemanage", "css", templateCssFileForm.getId(), false) + templateCssFileForm.getFileName();
        FileUtils.saveStringToFile(cssFile, templateCssFileForm.getCssContent(), "utf-8", true);
	}
}