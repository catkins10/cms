package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.webim.service.WebimService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public abstract class AjaxAction extends BaseAction {
	
	/**
	 * 生成消息
	 * @param form
	 * @param request
	 * @return
	 */
	protected abstract Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	WebimService webimService = (WebimService)getService("webimService");
    	SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
    	Message message = generateMessage(form, request);
    	long customerServiceChatId = RequestUtils.getParameterLongValue(request, "customerServiceChatId");
    	long userId = 0;
    	if(customerServiceChatId>0) {
    		userId = customerServiceChatId;
    	}
    	else if(sessionInfo!=null) {
    		userId = sessionInfo.getUserId();
    	}
    	else if(!anonymousEnable) {
    		userId = -1;
    	}
    	webimService.processMessage(userId, message, request, response);
    	return null;
    }
}