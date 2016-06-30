package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.SystemMessageFeedback;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class FeedbackSystemMessage extends AjaxAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		SystemMessageFeedback feedback = new SystemMessageFeedback();
		feedback.setSystemMessageId(RequestUtils.getParameterLongValue(request, "systemMessageId"));
		return feedback;
	}
}