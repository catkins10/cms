package com.yuanluesoft.cms.sitemanage.actions.accesstotalchart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.processor.TotalsProcessor;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TotalsProcessor totalsProcessor = (TotalsProcessor)getService("totalsProcessor");
        totalsProcessor.writeAccessTotalChart(request, response);
    	return null;
    }
}