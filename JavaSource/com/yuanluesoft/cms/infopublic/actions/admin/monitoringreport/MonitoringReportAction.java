package com.yuanluesoft.cms.infopublic.actions.admin.monitoringreport;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.infopublic.forms.admin.MonitoringReport;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class MonitoringReportAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		MonitoringReport reportForm = (MonitoringReport)form;
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		if(!publicDirectoryService.checkPopedom(reportForm.getDirectoryId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		if("get".equalsIgnoreCase(request.getMethod())) {
			MonitoringReport reportForm = (MonitoringReport)form;
			//设置统计时间
			Date date = DateTimeUtils.set(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, 1);
			reportForm.setBeginDate(DateTimeUtils.add(date, Calendar.MONTH, -1));
			reportForm.setEndDate(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		MonitoringReport reportForm = (MonitoringReport)form;
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
		com.yuanluesoft.cms.infopublic.model.MonitoringReport report = publicInfoService.writeMonitoringReport(reportForm.getDirectoryId(), reportForm.getBeginDate(), reportForm.getEndDate());
		if(report!=null) {
			PropertyUtils.copyProperties(reportForm, report);
		}
	}
}