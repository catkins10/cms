package com.yuanluesoft.chd.evaluation.data.actions.prerequisitesdata;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.chd.evaluation.data.forms.PrerequisitesData;
import com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PrerequisitesDataAction extends FormAction {

	public PrerequisitesDataAction() {
		super();
		externalAction = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		PrerequisitesData prerequisitesDataForm = (PrerequisitesData)form;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		if(evaluationDirectoryService.checkPopedom(prerequisitesDataForm.getPlantId(), "all", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PrerequisitesData prerequisitesDataForm = (PrerequisitesData)form;
		if(prerequisitesDataForm.getDeclareYear()==0) {
			prerequisitesDataForm.setDeclareYear(DateTimeUtils.getYear(DateTimeUtils.date()));
		}
		EvaluationDataService evaluationDataService = (EvaluationDataService)getService("chdEvaluationDataService");
		prerequisitesDataForm.setPrerequisitesDataList(evaluationDataService.loadPrerequisitesDataList(prerequisitesDataForm.getPlantId(), prerequisitesDataForm.getDeclareYear()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PrerequisitesData prerequisitesDataForm = (PrerequisitesData)form;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		if(!evaluationDirectoryService.checkPopedom(prerequisitesDataForm.getPlantId(), "manager,leader,transactor", sessionInfo)) {
			prerequisitesDataForm.setSubForm(SUBFORM_READ);
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EvaluationDataService evaluationDataService = (EvaluationDataService)getService("chdEvaluationDataService");
		PrerequisitesData prerequisitesDataForm = (PrerequisitesData)form;
		evaluationDataService.savePrerequisitesDataList(prerequisitesDataForm.getPlantId(), prerequisitesDataForm.getDeclareYear(), request, sessionInfo);
		return null;
	}
}