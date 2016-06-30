package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 根据用户ID,创建一个对话
 * @author linchuan
 *
 */
public class CreateChat extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		com.yuanluesoft.im.model.message.CreateChat createChat = new com.yuanluesoft.im.model.message.CreateChat();
		createChat.setChatPersonId(RequestUtils.getParameterLongValue(request, "chatPersonId"));
		return createChat;
	}
}