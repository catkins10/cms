package com.yuanluesoft.dpc.keyproject.report.actions.zzfetyearreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.keyproject.report.forms.ZzfetYearReport;
import com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends ZzfetYearReportAction {
    
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
		ZzfetKeyProjectReportService zzfetKeyProjectReportService = (ZzfetKeyProjectReportService)getService("zzfetKeyProjectReportService");
		ZzfetYearReport report = (ZzfetYearReport)form;
		zzfetKeyProjectReportService.writeYearReport(report.getYear(), report.getMonth(), report.getKeyMonth(), report.getDevelopmentArea(), null, response, sessionInfo);
	}
}