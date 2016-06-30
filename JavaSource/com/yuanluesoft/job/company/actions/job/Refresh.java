package com.yuanluesoft.job.company.actions.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.service.JobCompanyService;

/**
 * 
 * @author linchuan
 *
 */
public class Refresh extends JobAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.job.company.actions.job.JobAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Job job = (Job)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		jobCompanyService.refreshJob(job);
		return job;
	}
}