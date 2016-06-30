package com.yuanluesoft.cms.evaluation.actions.evaluationtopic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.evaluation.forms.EvaluationTopic;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends FormAction {
  
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	EvaluationTopic topicForm = (EvaluationTopic)form;
    	EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
    	com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)evaluationService.load(com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class, topicForm.getId());
    	anonymousAlways = (topic.getAnonymous()=='1');
    	return executeLoadAction(mapping, form, request, response);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)record;
			if(topic.getIssue()=='1') { //已经发布的
				if(topic.getAnonymous()=='1') { //匿名
					return RecordControlService.ACCESS_LEVEL_READONLY;
				}
				//实名
				try {
					return getRecordControlService().getAccessLevel(record.getId(), record.getClass().getName(), sessionInfo);
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
			}
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		//设置待测评用户和已测评用户
		EvaluationTopic topicForm = (EvaluationTopic)form;
		com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)record;
		EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
		topicForm.setToEvaluateTargetPersons(evaluationService.listToEvaluateTargetPersons(topic, request, sessionInfo));
		topicForm.setEvaluatedTargetPersons(evaluationService.listEvaluatedTargetPersons(topic, request, sessionInfo));
	}
}