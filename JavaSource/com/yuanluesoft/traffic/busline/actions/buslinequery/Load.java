package com.yuanluesoft.traffic.busline.actions.buslinequery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.traffic.busline.pojo.BusLine;
import com.yuanluesoft.traffic.busline.service.BusLineService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String pageName = "station";
    	if("按线路名称".equals(request.getParameter("queryMode"))) {
        	//获取公交线路
    		BusLineService busLineService = (BusLineService)getService("busLineService");
    		BusLine busLine = busLineService.getBusLineByName(request.getParameter("key"));
    		pageName = busLine==null ? "busLineNotExists" : "busLine";
    		request.setAttribute("record", busLine);
    	}
        PageService pageService = (PageService)getService("busLinePageService");
        pageService.writePage("traffic/busline", pageName, request, response, false);
       	return null;
    }
}