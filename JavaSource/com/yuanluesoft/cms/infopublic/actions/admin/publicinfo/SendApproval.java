package com.yuanluesoft.cms.infopublic.actions.admin.publicinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class SendApproval extends PublicInfoAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, "发送完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#run(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void run(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		super.run(mapping, form, request, response, actionResult);
		com.yuanluesoft.cms.infopublic.forms.admin.PublicInfo publicInfoForm = (com.yuanluesoft.cms.infopublic.forms.admin.PublicInfo)form;
		if("发送完成".equals(publicInfoForm.getActionResult())) {
			addCreateNextAction(publicInfoForm);
		}
	}
}