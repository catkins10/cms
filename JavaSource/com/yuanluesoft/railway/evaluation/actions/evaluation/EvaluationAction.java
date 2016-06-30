package com.yuanluesoft.railway.evaluation.actions.evaluation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.railway.evaluation.forms.Evaluation;
import com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService;

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
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_evaluate")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		Evaluation evaluationForm = (Evaluation)form;
		if(evaluationForm.getPersonId()==0 || evaluationForm.getPersonId()==sessionInfo.getUserId()) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		RailwayEvaluationService railwayEvaluationService = (RailwayEvaluationService)getService("railwayEvaluationService");
		Evaluation evaluationForm = (Evaluation)form;
		PropertyUtils.copyProperties(evaluationForm, railwayEvaluationService.getPersonalRailwayEvaluation(evaluationForm.getPersonId()==0 ? sessionInfo.getUserId() : evaluationForm.getPersonId(), evaluationForm.getYear(), evaluationForm.getMonth()));
	}
}