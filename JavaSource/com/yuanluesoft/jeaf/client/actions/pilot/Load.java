package com.yuanluesoft.jeaf.client.actions.pilot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.client.pages.ClientPilotPageService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ClientPilotPageService clientPilotPageService = (ClientPilotPageService)getService("clientPilotPageService");
    	clientPilotPageService.writePage("jeaf/client", "pilot", request, response, false);
    	return null;
    }
}