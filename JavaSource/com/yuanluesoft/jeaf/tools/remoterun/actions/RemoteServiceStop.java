package com.yuanluesoft.jeaf.tools.remoterun.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.tools.forms.Tools;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class RemoteServiceStop extends org.apache.struts.action.Action {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String serviceName = request.getParameter("service");
    	Runtime.getRuntime().exec("net stop \"" + serviceName + "\"");
        Tools formTools = (Tools)form;
        formTools.setActionResult("远程服务已关闭！");
        return mapping.findForward("result");
    }
}