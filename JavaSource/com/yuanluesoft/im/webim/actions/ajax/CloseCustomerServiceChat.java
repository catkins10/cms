package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CloseCustomerServiceChat extends AjaxAction {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		com.yuanluesoft.im.model.message.CloseCustomerServiceChat closeCustomerServiceChat = new com.yuanluesoft.im.model.message.CloseCustomerServiceChat();
		closeCustomerServiceChat.setCustomerServiceChatId(RequestUtils.getParameterLongValue(request, "customerServiceChatId"));
		return closeCustomerServiceChat;
	}
}