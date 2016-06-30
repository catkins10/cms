package com.yuanluesoft.jeaf.lock.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.lock.service.LockService;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Unlock extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LockService lockService = (LockService)getService("lockService");
    	lockService.unlock(request.getParameter("lockTarget"), Long.parseLong(request.getParameter("personId")));
    	response.getWriter().write("<html><body onload='window.close()'></body></html>");
        return null;
    }
}