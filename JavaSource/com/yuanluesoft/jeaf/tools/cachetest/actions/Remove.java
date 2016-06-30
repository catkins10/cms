package com.yuanluesoft.jeaf.tools.cachetest.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Remove extends CacheTestAction {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(request.getParameter("all")!=null) {
    		removeAllValue();
    	}
    	else {
    		removeValue();
    	}
        return null;
    }
}