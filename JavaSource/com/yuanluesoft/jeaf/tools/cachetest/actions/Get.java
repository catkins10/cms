package com.yuanluesoft.jeaf.tools.cachetest.actions;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 从cache读取
 * @author linchuan
 *
 */
public class Get extends CacheTestAction {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Set keys = listValues();
    	if(keys==null || keys.isEmpty()) {
    		return null;
    	}
    	for(Iterator iterator = keys.iterator(); iterator.hasNext();) {
    		response.getWriter().write(iterator.next() + "<br>");
    	}
        return null;
    }
}