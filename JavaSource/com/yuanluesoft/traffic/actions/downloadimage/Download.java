package com.yuanluesoft.traffic.actions.downloadimage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.traffic.service.TrafficImageService;

/**
 * 
 * @author linchuan
 *
 */
public class Download extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TrafficImageService trafficImageService = (TrafficImageService)getService("trafficImageService");
        trafficImageService.downloadImage(request, response);
    	return null;
    }
}