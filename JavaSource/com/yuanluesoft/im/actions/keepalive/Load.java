package com.yuanluesoft.im.actions.keepalive;

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
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
        	getSessionInfo(request, response);
        }
        catch(SessionException se) {
    		String html = "<html>" +
    					  " <body onload=\"parent.setTimeout('onSessionFailed()', 1);\"></body>" +
    					  "</html>";
    		response.getWriter().write(html);
    		return null;
        }
        return null;
    }
}