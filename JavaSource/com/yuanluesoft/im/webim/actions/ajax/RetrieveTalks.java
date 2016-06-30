package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.TalkDetailRequest;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class RetrieveTalks extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		TalkDetailRequest detailRequest = new TalkDetailRequest();
		detailRequest.setChatId(RequestUtils.getParameterLongValue(request, "chatId")); //对话ID
		detailRequest.setCustomerServiceChatId(RequestUtils.getParameterLongValue(request, "customerServiceChatId")); //客服对话ID
		detailRequest.setBeginTime(RequestUtils.getParameterLongValue(request, "beginTime")); //开始时间
		return detailRequest;
	}
}