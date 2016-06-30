package com.yuanluesoft.j2oa.meeting.actions.meeting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.messagecenter.service.MessageService;

/*
 * 
 * @author LinChuan
*
 */
public class Delete extends MeetingAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeDeleteAction(mapping, form, request, response, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#delete(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public void delete(ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		super.delete(form, request, response, actionResult);
		((MessageService)getService("messageService")).removeMessages(((com.yuanluesoft.jeaf.form.ActionForm)form).getId());
	}
}