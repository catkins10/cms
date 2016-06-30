package com.yuanluesoft.cms.monitor.actions.unitconfig.monitorsql;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.monitor.actions.unitconfig.UnitConfigAction;
import com.yuanluesoft.cms.monitor.forms.MonitorSql;
import com.yuanluesoft.cms.monitor.service.MonitorService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorSqlAction extends UnitConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		MonitorSql monitorSqlForm = (MonitorSql)form;
		//设置SQL样本
		if(monitorSqlForm.getMonitorSql().getCaptureContentClass()!=null &&
		   !monitorSqlForm.getMonitorSql().getCaptureContentClass().isEmpty() &&
		   (monitorSqlForm.getMonitorSql().getCaptureSql()==null || monitorSqlForm.getMonitorSql().getCaptureSql().isEmpty())) {
			MonitorService monitorService = (MonitorService)getService("monitorService");
			monitorSqlForm.getMonitorSql().setCaptureSql(monitorService.getSampleSql(monitorSqlForm.getMonitorSql().getCaptureContentClass()));
		}
	}
}