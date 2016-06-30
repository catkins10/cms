package com.yuanluesoft.land.declare.actions.admin.excelimport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.land.declare.forms.ExcelImport;
import com.yuanluesoft.land.declare.service.LandDeclareService;

/**
 * 
 * @author kanshiwei
 *
 */
public class ImportDataAction extends FormAction {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_importData")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response,SessionInfo sessionInfo) throws Exception {
		ExcelImport importDataForm = (ExcelImport)form;
		LandDeclareService landDeclareService = (LandDeclareService)getService("landDeclareService");
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		//获取上传的文件
		List attachments = attachmentService.list("land/declare", "data", importDataForm.getId(), false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ValidateException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		try {
			landDeclareService.importData(attachment.getFilePath());
		}
		catch (ServiceException se) {
			if(se.getMessage()!=null) {
				Logger.exception(se);
				throw new ValidateException(se.getMessage());
			}
			throw se;
		}
		return null;
	}
}
