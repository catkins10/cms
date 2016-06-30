package com.yuanluesoft.microblog.actions.receivedmessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Load extends org.apache.struts.action.Action {
    
    // Global Forwards
    public static final String GLOBAL_FORWARD_result = "result"; 

    // Local Forwards
    public static final String FORWARD_load = "load"; 

    
    public Load() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: Write method body
        throw new UnsupportedOperationException("Method is not implemented");
    }

}