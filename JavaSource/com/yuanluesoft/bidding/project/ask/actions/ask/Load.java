package com.yuanluesoft.bidding.project.ask.actions.ask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.ask.forms.admin.Ask;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends AskAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Ask askForm = (Ask)form;
    	if(OPEN_MODE_CREATE.equals(askForm.getAct()) || askForm.getAct()==null || askForm.getAct().isEmpty()) {
    		anonymousAlways = true;
    	}
    	return executeLoadAction(mapping, form, request, response);
    }
}