package com.yuanluesoft.bidding.enterprise.actions.enterprisecertsurvey.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction;
import com.yuanluesoft.bidding.enterprise.forms.admin.EnterpriseCertSurvey;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCertSurvey;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class EnterpriseCertSurveyAction extends EnterpriseAction  {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		EnterpriseCertSurvey surveyForm = (EnterpriseCertSurvey)form;
		if(surveyForm.getSurvey().getId()==0) {
			return null;
		}
		return getBusinessService(null).load(BiddingEnterpriseCertSurvey.class, surveyForm.getSurvey().getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		EnterpriseCertSurvey surveyForm = (EnterpriseCertSurvey)form;
		surveyForm.getSurvey().setSurveyYear(DateTimeUtils.getYear(DateTimeUtils.date()));
	}
}