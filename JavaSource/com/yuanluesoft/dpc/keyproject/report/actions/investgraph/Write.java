package com.yuanluesoft.dpc.keyproject.report.actions.investgraph;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.keyproject.report.forms.InvestGraph;
import com.yuanluesoft.dpc.keyproject.report.service.NpdpcKeyProjectReportService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends InvestGraphAction {
    
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
		NpdpcKeyProjectReportService npdpcKeyProjectReportService = (NpdpcKeyProjectReportService)getService("npdpcKeyProjectReportService");
		InvestGraph report = (InvestGraph)form;
		npdpcKeyProjectReportService.writeInvestGraph(report.getYear(), report.getGraphBy(), report.getGraphType(), report.getWidth(), report.getHeight(), response);
	}
}