package com.yuanluesoft.cms.evaluation.actions.evaluation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.evaluation.forms.Evaluation;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationAction extends com.yuanluesoft.cms.evaluation.actions.evaluation.admin.EvaluationAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.action.BaseAction#getSessionInfo(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, HttpServletResponse response) throws SessionException, SystemUnregistException {
    	try {
			anonymousAlways = (getEvaluationTopic(request).getAnonymous()=='1');
		} 
		catch (Exception e) {
	
		}
    	return super.getSessionInfo(request, response);
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
		Evaluation evaluationForm = (Evaluation)form;
		return evaluationService.loadEvaluationMark(evaluationForm.getTopicId(), evaluationForm.getTargetPersonId(), request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Evaluation evaluationForm = (Evaluation)form;
		EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
		return evaluationService.submitEvaluation(evaluationForm.getTopicId(), evaluationForm.getTargetPersonId(), request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = getEvaluationTopic(request);
			if(topic.getIssue()=='1' && //已经发布的
			   (topic.getAnonymous()=='1' || //匿名测评
			    getRecordControlService().getAccessLevel(topic.getId(), topic.getClass().getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY)) { //或者是测评人
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch(Exception e) {
			
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Evaluation evaluationForm = (Evaluation)form;
		writeEvaluationForm(form, sessionInfo, request);
		evaluationForm.setEvaluateTime(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		writeEvaluationForm(form, sessionInfo, request);
	}
}