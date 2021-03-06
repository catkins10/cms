package com.yuanluesoft.logistics.vehicle.actions.vehiclesupply.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends VehicleSupplyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	anonymousEnable = RequestUtils.getParameterLongValue(request, "id")!=0;
    	return executeLoadAction(mapping, form, request, response);
    }
}