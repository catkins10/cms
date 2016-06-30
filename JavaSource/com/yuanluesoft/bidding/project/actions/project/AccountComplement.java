package com.yuanluesoft.bidding.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class AccountComplement extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.bidding.project.forms.AccountComplement complementForm = (com.yuanluesoft.bidding.project.forms.AccountComplement)form;
    	if(new Long(complementForm.getProjectId()).equals(request.getSession().getAttribute("complementProjectId"))) {
    		BiddingService biddingService = (BiddingService)getService("biddingService");
    		biddingService.accountComplement(complementForm.getProjectId(), complementForm.getSignUpId(), complementForm.getEnterpriseName(), complementForm.getEnterpriseBank(), complementForm.getEnterpriseAccount());
    	}
    	return null;
    }
}