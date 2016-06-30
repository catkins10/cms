package com.yuanluesoft.bidding.enterprise.actions.jobholder.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction;
import com.yuanluesoft.bidding.enterprise.forms.admin.Jobholder;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class JobholderAction extends EnterpriseAction  {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Jobholder jobholderForm = (Jobholder)form;
		jobholderForm.getJobholder().setSex('M');
	}
}