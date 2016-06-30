package com.yuanluesoft.jeaf.usermanage.actions.personalindex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pages.UserPageService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = null;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		if(!sessionInfo.isInternalUser()) { //不是内部用户
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
    	//检查用户对EAI的访问权限
    	EAIClient eaiClient = (EAIClient)getService("eaiClient");
    	if(!eaiClient.isEAIVisitor(sessionInfo.getLoginName())) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	//加载页面
    	UserPageService pageService = (UserPageService)getService("userPageService");
    	pageService.writePage("jeaf/usermanage", "personalIndex", request, response, false);
		return null;
    }
}