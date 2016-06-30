package com.yuanluesoft.cms.inquiry.actions.inquiryresult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.forms.Inquiry;
import com.yuanluesoft.cms.inquiry.forms.admin.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String inquiryIds = request.getParameter("inquiryIds");
    	InquiryService inquiryService = (InquiryService)getService("inquiryService");
    	try {
			getSessionInfo(request, response);
		}
		catch(SessionException se) {
			boolean isAnonymous = inquiryIds!=null ? inquiryService.isAnonymous(inquiryIds) : inquiryService.isAnonymous(RequestUtils.getParameterLongValue(request, "id"));
			if(!isAnonymous) {
				return redirectToLogin(this, mapping, form, request, response, se, false);
			}
		}
		if(inquiryService.isHideResult(inquiryIds)) {
			return mapping.findForward("inquiryNotEnd");
		}
		if(form instanceof Inquiry){
			Inquiry inquiryForm = (Inquiry)form;
			boolean questionnaire = inquiryIds!=null ? inquiryService.hasQuestionnaire(inquiryIds) : inquiryService.isQuestionnaire(RequestUtils.getParameterLongValue(request, "id"));
			inquiryForm.setSubForm(questionnaire ? "questionnaireResult" : "inquiryResult"); //判断是否有问卷
			
		}
		else if(form instanceof InquirySubject){//前台展现多种统计图
			InquirySubject inquirySubjectForm=(InquirySubject)form;
			com.yuanluesoft.cms.inquiry.pojo.InquirySubject inquirySubject=null;
			if(inquiryIds!=null && !inquiryIds.isEmpty()){
				inquirySubject=(com.yuanluesoft.cms.inquiry.pojo.InquirySubject) inquiryService.retrieveInquiryResults(inquiryIds).get(0);
			}else{
				long inquirySubjectId=RequestUtils.getParameterLongValue(request, "id");
				inquirySubject=(com.yuanluesoft.cms.inquiry.pojo.InquirySubject)inquiryService.load(com.yuanluesoft.cms.inquiry.pojo.InquirySubject.class,inquirySubjectId );
			}
			inquiryService.retrieveInquiryResults(inquirySubject);
			inquirySubjectForm.setInquiries(inquirySubject.getInquiries());
			inquirySubjectForm.setSubject(inquirySubject.getSubject());
			boolean questionnaire = inquiryIds!=null ? inquiryService.hasQuestionnaire(inquiryIds) : inquiryService.isQuestionnaire(RequestUtils.getParameterLongValue(request, "id"));
			inquirySubjectForm.setIsQuestionnaire(questionnaire?'1':'0');
			inquirySubjectForm.setSubForm(questionnaire ? "questionnaireResult" : "inquiryResult"); //判断是否有问卷
		}
	    return mapping.findForward("load");
    }
}