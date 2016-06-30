package com.yuanluesoft.cms.inquiry.actions.inquiry.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.forms.admin.Inquiry;

/**
 * 
 * @author linchuan
 *
 */
public class Refresh extends InquiryAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Inquiry inquiryForm = (Inquiry)form;
		if(OPEN_MODE_CREATE_COMPONENT.equals(inquiryForm.getAct())) {
			return executeLoadComponentAction(mapping, form, "inquiry", request, response);
		}
		else {
			return executeSaveComponentAction(mapping, form, "inquiry", "inquiries", "subjectId", "refreshInquirySubject", true, request, response);
		}
	}
}