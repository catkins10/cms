package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.ChatDetailRequest;
import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 根据聊天ID,加载对话
 * @author linchuan
 *
 */
public class LoadChat extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		ChatDetailRequest detailRequest = new ChatDetailRequest();
		detailRequest.setChatId(RequestUtils.getParameterLongValue(request, "chatId"));
		return detailRequest;
	}
}