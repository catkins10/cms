package com.yuanluesoft.job.apply.actions.apply.interview;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.job.apply.actions.apply.ApplyAction;
import com.yuanluesoft.job.apply.pojo.JobApply;
import com.yuanluesoft.job.apply.service.JobApplyService;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewAction extends ApplyAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
		JobApply apply = (JobApply)mainRecord;
		if(apply.getStatus()<3) {
			apply.setStatus(3);
			((JobApplyService)getService("jobApplyService")).update(apply);
		}
	}
}