package com.yuanluesoft.im.webim.actions.chat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("webimChatPageService");
    	if("true".equals(request.getParameter("customer"))) { //客服对话:网友页面
    		pageService.writePage("im/cs", "chatOfCustomer", request, response, false);
    	}
    	else {
    		pageService.writePage("im/webim", ("true".equals(request.getParameter("customerService")) ? "chatOfSpecialist" : "chat"), request, response, false);
        }
    	return null;
    }
}