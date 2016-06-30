package com.yuanluesoft.job.talent.actions.printreports.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.job.talent.pages.TalentReportPageService;

/**
 * 
 * @author chuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	TalentReportPageService pageService = (TalentReportPageService)getService("jobTalentReportPageService");
    	pageService.writePage("job/talent", "true".equals(request.getParameter("printNotify")) ? "notifyPrint" : "reportPrint", request, response, false);
    	return null;
    }
}