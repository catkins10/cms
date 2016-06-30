package com.yuanluesoft.cms.smssubscription.actions.unsubscribe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.smssubscription.actions.subscribe.SubscribeAction;
import com.yuanluesoft.cms.smssubscription.forms.Subscribe;
import com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class UnsubscribeAction extends SubscribeAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Subscribe subscribeForm = (Subscribe)form;
		SmsSubscriptionService smsSubscriptionService = (SmsSubscriptionService)getService("smsSubscriptionService");
		//订阅
		return smsSubscriptionService.unsubscribe(subscribeForm.getSubscriberNumber(), subscribeForm.getContentName(), subscribeForm.getSubscribeParameter(), subscribeForm.getSiteId());
	}
}