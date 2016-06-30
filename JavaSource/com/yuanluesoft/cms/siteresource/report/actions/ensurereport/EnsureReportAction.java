package com.yuanluesoft.cms.siteresource.report.actions.ensurereport;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.report.forms.EnsureReport;
import com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService;
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
public class EnsureReportAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		EnsureReport reportForm = (EnsureReport)form;
		SiteService siteService = (SiteService)getService("siteService");
		try {
			if(reportForm.getSiteName()==null) {
				if(siteService.getFirstManagedSite(sessionInfo)!=null) {
					return;
				}
			}
			else {
				if(siteService.checkPopedom(reportForm.getSiteId(), "manager", sessionInfo)) {
					return;
				}
			}
		}
		catch (ServiceException e) {
			
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		if("get".equalsIgnoreCase(request.getMethod())) {
			EnsureReport reportForm = (EnsureReport)form;
			//设置统计时间
			Date date = DateTimeUtils.set(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, 1);
			date = DateTimeUtils.set(date, Calendar.MONTH, DateTimeUtils.getMonth(date)/3*3);
			reportForm.setBeginDate(DateTimeUtils.add(date, Calendar.MONTH, -3));
			reportForm.setEndDate(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1));
			
			//设置站点名称
			SiteService siteService = (SiteService)getService("siteService");
			WebSite site = siteService.getFirstManagedSite(sessionInfo);
			reportForm.setSiteId(site.getId());
			reportForm.setSiteName(site.getDirectoryName());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		EnsureReport reportForm = (EnsureReport)form;
		SiteResourceStatService siteResourceStatService = (SiteResourceStatService)getService("siteResourceStatService");
		com.yuanluesoft.cms.siteresource.report.model.ensurereport.EnsureReport report = siteResourceStatService.writeEnsureReport(reportForm.getSiteId(), reportForm.getBeginDate(), reportForm.getEndDate());
		reportForm.setQuarter(DateTimeUtils.getMonth(reportForm.getBeginDate()) / 3 + 1);
		reportForm.setMonths(report.getMonths());
		reportForm.setUnitCategories(report.getUnitCategories());
	}
}