package com.yuanluesoft.bidding.project.report.actions.documentsalesreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.report.actions.agentsalesreport.AgentSalesReportAction;
import com.yuanluesoft.bidding.project.report.forms.admin.AgentSalesReport;
import com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author lmiky
 *
 */
public class Write extends AgentSalesReportAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSubmitAction(mapping, form, request, response, false, null, "success"); //指定一个forward
    	if(forward==null || !"success".equals(forward.getName())) {
    		return forward;
    	}
    	return null;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BiddingProjectReportService biddingProjectReportService = (BiddingProjectReportService)getService("biddingProjectReportService");
        AgentSalesReport report = (AgentSalesReport)form;
        String paymentBanks = ListUtils.join(report.getPaymentBanks(), ",", false);
        biddingProjectReportService.writeDocumentSalesReport(report.getBeginDate(), report.getEndDate(), paymentBanks==null || paymentBanks.isEmpty() ? null : paymentBanks, report.getCities(), false, response);
	}
}