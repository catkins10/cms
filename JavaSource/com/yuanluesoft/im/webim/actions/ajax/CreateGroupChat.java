package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 创建讨论组
 * @author linchuan
 *
 */
public class CreateGroupChat extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		com.yuanluesoft.im.model.message.CreateGroupChat createGroupChat = new com.yuanluesoft.im.model.message.CreateGroupChat();
		createGroupChat.setFromChatId(RequestUtils.getParameterLongValue(request, "fromChatId"));
		createGroupChat.setChatPersonIds(RequestUtils.getParameterStringValue(request, "chatPersonIds"));
		return createGroupChat;
	}
}