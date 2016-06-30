package com.yuanluesoft.bidding.project.report.actions.agentsalesreport;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.project.report.forms.admin.AgentSalesReport;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AgentSalesReportAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains("application_visitor")) {
			throw new PrivilegeException();
		}
		BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
		try {
			List cities = biddingProjectParameterService.listReportVisitableCities(sessionInfo);
			if(cities==null || cities.isEmpty()) {
				throw new PrivilegeException();
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		AgentSalesReport report = (AgentSalesReport)form;
		Date date = DateTimeUtils.set(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, 1);
		report.setBeginDate(DateTimeUtils.add(date, Calendar.MONTH, -1));
		report.setEndDate(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1));
	}
}