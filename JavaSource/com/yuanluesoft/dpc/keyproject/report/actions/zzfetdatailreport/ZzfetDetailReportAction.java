package com.yuanluesoft.dpc.keyproject.report.actions.zzfetdatailreport;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.report.forms.ZzfetDetailReport;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ZzfetDetailReportAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ZzfetDetailReport report = (ZzfetDetailReport)form;
		try {
			if(getRecordControlService().getAccessLevel(report.getProjectId(), KeyProject.class.getName(), sessionInfo)<RecordControlService.ACCESS_LEVEL_READONLY) {
				throw new PrivilegeException();
			}
		} 
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		ZzfetDetailReport report = (ZzfetDetailReport)form;
		Date date = DateTimeUtils.date();
		report.setYear(DateTimeUtils.getYear(date));
		report.setMonth(DateTimeUtils.getMonth(date) + 1);
		report.setReportType("前期工作台帐");
	}
}