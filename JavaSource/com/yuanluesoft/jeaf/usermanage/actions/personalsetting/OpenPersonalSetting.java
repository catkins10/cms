package com.yuanluesoft.jeaf.usermanage.actions.personalsetting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class OpenPersonalSetting extends BaseAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) { //校验登录URL是否正确
    		redirectToSecureLink(request, response);
    		return null;
    	}
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
    	return mapping.findForward("load");
    }
}
