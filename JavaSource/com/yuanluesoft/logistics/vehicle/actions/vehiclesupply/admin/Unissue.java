package com.yuanluesoft.logistics.vehicle.actions.vehiclesupply.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleSupply;

/**
 * 
 * @author linchuan
 *
 */
public class Unissue extends VehicleSupplyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "撤销成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.actions.vehiclesupply.admin.VehicleSupplyAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicleSupply vehicleSupply = (LogisticsVehicleSupply)record;
		vehicleSupply.setIssue(0);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}