package com.yuanluesoft.enterprise.exam.question.actions.questionimport.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.question.forms.admin.QuestionImport;
import com.yuanluesoft.enterprise.exam.question.service.QuestionImportService;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class QuestionImportAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_editor")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		}
		else {
			QuestionImportService questionImportService = (QuestionImportService)getService("questionImportService");
			QuestionImport importDataForm = (QuestionImport)form;
			try {
				return questionImportService.importData(importDataForm.getId(), importDataForm.getSource(), importDataForm.getDescription(), importDataForm.getPostIds(), importDataForm.getPosts(), importDataForm.getKnowledgeIds(), importDataForm.getKnowledges(), importDataForm.getItemIds(), importDataForm.getItems(), importDataForm.getRemark(), sessionInfo);
			}
			catch(ServiceException se) {
				if(se.getMessage()!=null) {
					importDataForm.setError(se.getMessage());
					throw new ValidateException();
				}
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		QuestionImport importDataForm = (QuestionImport)form;
		if(importDataForm.isDeleteData()) {
			QuestionService questionService = (QuestionService)getService("questionService");
			questionService.deleteImportedData(importDataForm.getId());
		}
	}
}