package com.yuanluesoft.cms.inquiry.actions.inquirydescription;

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
		try {
			getSessionInfo(request, response);
		}
		catch(Exception se) {
			InquiryService inquiryService = (InquiryService)getService("inquiryService");
	    	if(!inquiryService.isAnonymous(RequestUtils.getParameterLongValue(request, "id"))) { //不是匿名
	    		return redirectToLogin(this, mapping, form, request, response, se, false);
	    	}
		}
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("cms/inquiry", "inquiryDescription", request, response, false);
    	return null;
    }
}