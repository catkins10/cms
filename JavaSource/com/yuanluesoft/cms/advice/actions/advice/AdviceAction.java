package com.yuanluesoft.cms.advice.actions.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advice.pojo.Advice;
import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.cms.advice.service.AdviceService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AdviceAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Advice advice = (Advice)record;
		if(advice.getTopic()==null) {
			AdviceService adviceService = (AdviceService)getService("adviceService");
			advice.setTopic((AdviceTopic)adviceService.load(AdviceTopic.class, advice.getTopicId()));
		}
		advice.setSiteId(advice.getTopic().getSiteId());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}