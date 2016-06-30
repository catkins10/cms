package com.yuanluesoft.bidding.enterprise.actions.ekeyreport.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.enterprise.forms.admin.EKeyReport;
import com.yuanluesoft.bidding.enterprise.services.EmployeeService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends EKeyReportAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeLoadAction(mapping, form, request, response);
    	if(forward!=null && "load".equals(forward.getName())) {
    		return null;
    	}
        return forward;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		EKeyReport reportForm = (EKeyReport)form;
        EmployeeService employeeService = (EmployeeService)getService("employeeService");
        employeeService.exportEKeyReport(reportForm.getBeginDate(), reportForm.getEndDate(), response);
	}
}