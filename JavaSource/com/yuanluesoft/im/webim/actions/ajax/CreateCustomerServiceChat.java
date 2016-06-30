package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 创建客服对话
 * @author linchuan
 *
 */
public class CreateCustomerServiceChat extends AjaxAction {
    
    public CreateCustomerServiceChat() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		com.yuanluesoft.im.model.message.CreateCustomerServiceChat createCustomerServiceChat = new com.yuanluesoft.im.model.message.CreateCustomerServiceChat();
		createCustomerServiceChat.setSpecialistId(RequestUtils.getParameterLongValue(request, "specialistId"));
		createCustomerServiceChat.setSiteId(RequestUtils.getParameterLongValue(request, "siteId"));
		return createCustomerServiceChat;
	}
}