package com.yuanluesoft.cms.advice.actions.advicefeedback.admin;


import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.advice.actions.advicetopic.admin.AdviceTopicAction;
import com.yuanluesoft.cms.advice.pojo.AdviceFeedback;
import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author lmiky
 *
 */
public class AdviceFeedbackAction extends AdviceTopicAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		AdviceTopic adviceTopic = (AdviceTopic)mainRecord;
		return adviceTopic.getFeedbacks()==null || adviceTopic.getFeedbacks().isEmpty() ? null : (AdviceFeedback)adviceTopic.getFeedbacks().iterator().next();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		AdviceFeedback adviceFeedback = (AdviceFeedback)component;
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct()) && (component instanceof com.yuanluesoft.cms.inquiry.pojo.Inquiry)) {
			adviceFeedback.setCreated(DateTimeUtils.now());
			adviceFeedback.setCreator(sessionInfo.getUserName());
			adviceFeedback.setCreatorId(sessionInfo.getUserId());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}