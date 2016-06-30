package com.yuanluesoft.dpc.investmentproject.actions.projectreport.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.investmentproject.forms.admin.ProjectReport;
import com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends ProjectReportAction {
    
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
		InvestmentProjectService investmentProjectService = (InvestmentProjectService)getService("investmentProjectService");
		ProjectReport report = (ProjectReport)form;
		investmentProjectService.writeProjectReport(report.getBeginDate(), report.getEndDate(), report.getArea(), response);
	}
}