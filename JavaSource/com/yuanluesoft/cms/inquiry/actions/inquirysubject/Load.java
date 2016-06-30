package com.yuanluesoft.cms.inquiry.actions.inquirysubject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
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
    	InquiryService inquiryService = (InquiryService)getService("inquiryService");
    	InquirySubject inquirySubject = (InquirySubject)inquiryService.load(InquirySubject.class, RequestUtils.getParameterLongValue(request, "id"));
    	request.setAttribute(PageService.PAGE_ATTRIBUTE_RECORD, inquirySubject);
    	try {
			getSessionInfo(request, response);
		}
		catch(Exception se) {
			if(inquirySubject==null || inquirySubject.getIsAnonymous()!='1') { //不是匿名
	    		return redirectToLogin(this, mapping, form, request, response, se, false);
	    	}
		}
		PageService pageService = (PageService)getService("inquirySubjectPageService");
    	pageService.writePage("cms/inquiry", inquirySubject.getIsQuestionnaire()=='1' ? "questionnaire" : "inquirySubject", request, response, false);
    	return null;
    }
}