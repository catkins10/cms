package com.yuanluesoft.j2oa.attendance.actions.attendancereport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.attendance.forms.AttendanceReport;
import com.yuanluesoft.j2oa.attendance.service.AttendanceService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends AttendanceReportAction {
    
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
		AttendanceService attendanceService = (AttendanceService)getService("attendanceService");
		AttendanceReport report = (AttendanceReport)form;
        attendanceService.writeReport(report.getOrgId(), report.getBeginDate(), report.getEndDate(), response);
	}
}