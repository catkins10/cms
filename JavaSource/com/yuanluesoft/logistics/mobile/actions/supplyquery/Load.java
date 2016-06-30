package com.yuanluesoft.logistics.mobile.actions.supplyquery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.logistics.mobile.forms.SupplyQuery;
import com.yuanluesoft.logistics.mobile.pages.LogisticsMobilePageService;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SupplyQuery supplyQuery = (SupplyQuery)form;
    	LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
    	String mobileNumber = Encoder.getInstance().desDecode(supplyQuery.getMobileNumber(), "05071813", "utf-8", null);
    	LogisticsVehicle vehicle = logisticsVehicleService.updateVehicleStatus(mobileNumber, "true".equals(RequestUtils.getParameterStringValue(request, "empty"))); //按电话号码获取车辆
    	if(vehicle==null) {
    		response.setCharacterEncoding("utf-8");
    		response.getWriter().write("<html><body>车辆未登记或者帐号已经停用</body></html>");
    		return null;
    	}
    	if(supplyQuery.getPlaceName()==null || supplyQuery.getPlaceName().isEmpty()) { //地址为空
    		try {
    			GpsService gpsService = (GpsService)getService("gpsService");
	    		Location location = gpsService.getLocation(vehicle.getLinkmanTel(), true);
	    		if(location!=null && location.getPlaceName()!=null) {
	    			supplyQuery.setPlaceName(location.getPlaceName());
	    		}
	    	}
    		catch(Exception e) {
    			Logger.exception(e);
    		}
    	}
    	LogisticsMobilePageService pageService = (LogisticsMobilePageService)getService("logisticsMobilePageService");
        pageService.writePage("logistics/mobile", "supplyQuery", request, response, false);
        return null;
    }
}