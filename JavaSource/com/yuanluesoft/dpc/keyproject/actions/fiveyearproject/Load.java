package com.yuanluesoft.dpc.keyproject.actions.fiveyearproject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //直接跳转到重点项目界面
    	response.sendRedirect(request.getContextPath() + "/dpc/keyproject/project.shtml?fiveYearPlan=1&" + request.getQueryString());
    	return null;
    }
}