package com.yuanluesoft.cms.inquiry.actions.inquiryfeedback.admin;


import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin.InquirySubjectAction;
import com.yuanluesoft.cms.inquiry.pojo.InquiryFeedback;
import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
/**
 * 
 * @author lmiky
 *
 */
public class InquiryFeedbackAction extends InquirySubjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		InquirySubject inquirySubject = (InquirySubject)mainRecord;
		return inquirySubject.getFeedbacks()==null || inquirySubject.getFeedbacks().isEmpty() ? null : (InquiryFeedback)inquirySubject.getFeedbacks().iterator().next();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		InquiryFeedback inquiryFeedback = (InquiryFeedback)component;
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct()) && (component instanceof com.yuanluesoft.cms.inquiry.pojo.Inquiry)) {
			inquiryFeedback.setCreated(DateTimeUtils.now());
			inquiryFeedback.setCreator(sessionInfo.getUserName());
			inquiryFeedback.setCreatorId(sessionInfo.getUserId());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}