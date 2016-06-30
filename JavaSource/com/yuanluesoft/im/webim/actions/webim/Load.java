package com.yuanluesoft.im.webim.actions.webim;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	boolean cs = "true".equals(request.getParameter("customerService"));
    	if(!cs) { //不是客服页面
	    	SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
	    	if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //未登录
	    		//打开登录页面
	    		String html = "<html>" +
	    					  "<head>" +
	    					  "	<script charset=\"utf-8\" language=\"JavaScript\" src=\"" + request.getContextPath() + "/im/webim/js/webim.js\" type=\"text/javascript\"></script>" +
	    					  "</head>" +
	    					  "<body onload=\"new WebimLoader().login();\">" +
	    					  "</body>" +
	    					  "</html>";
	    		response.getWriter().print(html);
	    		return null;
	    	}
    	}
    	PageService pageService = (PageService)getService("webimPageService");
    	if(cs) {
    		pageService.writePage("im/cs", "webcs", request, response, false);
    	}
    	else {
    		pageService.writePage("im/webim", "webim", request, response, false);
    	}
    	return null;
    }
}