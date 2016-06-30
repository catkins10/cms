package com.yuanluesoft.jeaf.gps.actions.map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.gps.forms.Map;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map mapForm = (Map)form;
        externalAction = !mapForm.isInternal();
        try {
        	getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        GpsService gpsService = (GpsService)getService("gpsService");
        mapForm.setSubForm(gpsService.getMapProviderName() + "/map.jsp");
        if(mapForm.isSelectCity() && !mapForm.isIpLocationDisable()) { //城市选择,并且不禁止按IP定位
        	//按IP获取所在城市
        	Location location = gpsService.getLocationByIP(request.getRemoteAddr(), false, true);
        	if(location!=null && location.getCoordinate()!=null) {
        		mapForm.setLatitude(location.getCoordinate().getLatitude());
        		mapForm.setLongitude(location.getCoordinate().getLongitude());
        	}
        }
    	return mapping.findForward("load");
    }
}