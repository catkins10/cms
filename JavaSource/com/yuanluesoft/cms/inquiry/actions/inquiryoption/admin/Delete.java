package com.yuanluesoft.cms.inquiry.actions.inquiryoption.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.forms.admin.InquiryOption;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends InquiryOptionAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InquiryOption inquiryOptionForm = (InquiryOption)form;
		if(inquiryOptionForm.getIsQuestionnaire()=='1') {
			return executeDeleteComponentAction(mapping, form, "option", null, "refreshInquiry", request, response);
		}
		else {
			return executeDeleteComponentAction(mapping, form, "option", "options", "refreshInquirySubject", request, response);
		}
	}
}
