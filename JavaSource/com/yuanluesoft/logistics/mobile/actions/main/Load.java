package com.yuanluesoft.logistics.mobile.actions.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.logistics.mobile.pages.LogisticsMobilePageService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogisticsMobilePageService pageService = (LogisticsMobilePageService)getService("logisticsMobilePageService");
        pageService.writePage("logistics/mobile", "main", request, response, false);
        return null;
    }
}