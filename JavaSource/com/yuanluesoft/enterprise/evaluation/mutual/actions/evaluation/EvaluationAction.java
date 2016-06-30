package com.yuanluesoft.enterprise.evaluation.mutual.actions.evaluation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.evaluation.mutual.forms.Evaluation;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluation;
import com.yuanluesoft.enterprise.evaluation.mutual.service.MutualEvaluationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		MutualEvaluation mutualEvaluation = (MutualEvaluation)record;
		if(mutualEvaluation.getCreatorId()==sessionInfo.getUserId()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Evaluation evaluationForm = (Evaluation)form;
		MutualEvaluationService mutualEvaluationService = (MutualEvaluationService)getService("mutualEvaluationService");
		MutualEvaluation mutualEvaluation = mutualEvaluationService.createMutualEvaluation(sessionInfo);
		if(mutualEvaluation!=null) {
			PropertyUtils.copyProperties(evaluationForm, mutualEvaluation);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Evaluation evaluationForm = (Evaluation)form;
		MutualEvaluationService mutualEvaluationService = (MutualEvaluationService)getService("mutualEvaluationService");
		return mutualEvaluationService.submitMutualEvaluation(evaluationForm.getId(), OPEN_MODE_CREATE.equals(openMode), request, sessionInfo);
	}
}