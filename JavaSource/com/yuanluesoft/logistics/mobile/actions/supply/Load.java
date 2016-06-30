package com.yuanluesoft.logistics.mobile.actions.supply;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.Encoder;
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
    	LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
    	String mobileNumber = Encoder.getInstance().desDecode(request.getParameter("mobileNumber"), "05071813", "utf-8", null);
    	LogisticsVehicle vehicle = logisticsVehicleService.loadVehicleByLinkmanTel(mobileNumber); //按电话号码获取车辆
    	if(vehicle==null) {
    		response.setCharacterEncoding("utf-8");
    		response.getWriter().write("<html><body>车辆未登记或者帐号已经停用</body></html>");
    		return null;
    	}
    	LogisticsMobilePageService pageService = (LogisticsMobilePageService)getService("logisticsMobilePageService");
        pageService.writePage("logistics/mobile", "supply", request, response, false);
        return null;
    }
}