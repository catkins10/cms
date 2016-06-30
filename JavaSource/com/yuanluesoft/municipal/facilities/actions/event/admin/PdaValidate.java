package com.yuanluesoft.municipal.facilities.actions.event.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.municipal.facilities.forms.admin.FacilitiesEvent;

/**
 * 已废弃
 * @author linchuan
 *
 */
public class PdaValidate extends EventAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	alwaysAutoSend = true; //自动发送
        return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#resetParticipants(java.util.List, boolean, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List resetParticipants(List participants, boolean anyoneParticipate, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		FacilitiesEvent eventForm = (FacilitiesEvent)workflowForm;
		participants = new ArrayList();
		participants.add(new Person(eventForm.getPdaValidateUserId() + "", eventForm.getPdaValidateUserName()));
		return participants;
	}
}