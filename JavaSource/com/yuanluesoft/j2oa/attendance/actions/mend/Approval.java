package com.yuanluesoft.j2oa.attendance.actions.mend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.attendance.pojo.AttendanceMend;
import com.yuanluesoft.j2oa.attendance.service.AttendanceService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Approval extends MendAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, false, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterApproval(java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void afterApproval(String approvalResult, WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		super.afterApproval(approvalResult, workflowForm, request, record, openMode, sessionInfo);
		if("同意".equals(approvalResult)) {
			AttendanceMend mend = (AttendanceMend)record;
			AttendanceService attendanceService = (AttendanceService)getService("attendanceService");
			attendanceService.approvalMend(mend);
		}
	}
}