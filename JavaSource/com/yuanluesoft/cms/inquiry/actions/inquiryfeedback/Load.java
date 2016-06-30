package com.yuanluesoft.cms.inquiry.actions.inquiryfeedback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String inquiryIds = request.getParameter("inquiryIds");
    	InquiryService inquiryService = (InquiryService)getService("inquiryService");
    	try {
			getSessionInfo(request, response);
		}
		catch(Exception se) {
			boolean isAnonymous = inquiryIds!=null ? inquiryService.isAnonymous(inquiryIds) : inquiryService.isAnonymous(RequestUtils.getParameterLongValue(request, "id"));
			if(!isAnonymous) {
				return redirectToLogin(this, mapping, form, request, response, se, false);
			}
		}
		request.setAttribute("record", inquiryIds!=null ? inquiryService.getInquiryFeedback(inquiryIds) : inquiryService.getInquiryFeedback(RequestUtils.getParameterLongValue(request, "id")));
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("cms/inquiry", "inquiryFeedback", request, response, false);
    	return null;
    }
}