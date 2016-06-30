package com.yuanluesoft.logistics.mobile.actions.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class Authentication extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.logistics.mobile.forms.Authentication authenticationForm = (com.yuanluesoft.logistics.mobile.forms.Authentication)form;
    	if(authenticationForm.getMobileNumber()==null || authenticationForm.getMobileNumber().isEmpty()) {
    		authenticationForm.setError("随车联系人手机号码不能为空");
    		return mapping.getInputForward();
    	}
    	if(authenticationForm.getPlateNumber()==null || authenticationForm.getPlateNumber().isEmpty()) {
    		authenticationForm.setError("车牌号不能为空");
    		return mapping.getInputForward();
    	}
    	if(authenticationForm.getLicenceNumber()==null || authenticationForm.getLicenceNumber().isEmpty()) {
    		authenticationForm.setError("行车证号不能为空");
    		return mapping.getInputForward();
    	}
    	LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
    	LogisticsVehicle vehicle = logisticsVehicleService.loadVehicle(authenticationForm.getPlateNumber()); //按车牌号码获取车辆
    	if(vehicle==null || !authenticationForm.getMobileNumber().equals(vehicle.getLinkmanTel()) || !authenticationForm.getLicenceNumber().equals(vehicle.getLicenceNumber())) {
    		authenticationForm.setError("信息不正确");
    		return mapping.getInputForward();
    	}
        response.sendRedirect("main.shtml?authentication=true&mobileNumber=" + Encoder.getInstance().desEncode(request.getParameter("mobileNumber"), "05071813", "utf-8", null));
    	return null;
    }
}