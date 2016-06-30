package com.yuanluesoft.im.webim.actions.ajax;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.TalkSubmit;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ChatTalk extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		String content = "";
		BufferedReader reader = request.getReader();
		for(String line = reader.readLine(); line!=null; line = reader.readLine()) {
			content += line;
		}
		TalkSubmit talkSubmit = new TalkSubmit();
		talkSubmit.setChatId(RequestUtils.getParameterLongValue(request, "chatId")); //对话ID
		talkSubmit.setCustomerServiceChatId(RequestUtils.getParameterLongValue(request, "customerServiceChatId")); //客服对话ID
		talkSubmit.setContent(content); //内容
		return talkSubmit;
	}
}