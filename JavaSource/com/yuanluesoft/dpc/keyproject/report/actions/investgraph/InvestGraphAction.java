package com.yuanluesoft.dpc.keyproject.report.actions.investgraph;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.report.forms.InvestGraph;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InvestGraphAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains("application_manager") && !acl.contains("application_visitor")) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		InvestGraph report = (InvestGraph)form;
		Date date = DateTimeUtils.date();
		report.setYear(DateTimeUtils.getYear(date));
		report.setGraphBy("industry");
		report.setGraphByTitle("按行业");
		report.setGraphType("总体投资分析");
	}
}