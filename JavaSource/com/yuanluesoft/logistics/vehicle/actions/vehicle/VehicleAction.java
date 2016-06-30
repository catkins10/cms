package com.yuanluesoft.logistics.vehicle.actions.vehicle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.logistics.usermanage.model.LogisticsSessionInfo;
import com.yuanluesoft.logistics.vehicle.forms.Vehicle;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleAction extends com.yuanluesoft.logistics.vehicle.actions.vehicle.admin.VehicleAction {

	public VehicleAction() {
		super();
		externalAction = true;
		anonymousEnable = true;
		sessionInfoClass = LogisticsSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			Vehicle vehicleForm = (Vehicle)form;
			if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName()) && !"unregist".equals(vehicleForm.getActionResult())) { //匿名用户
				throw new PrivilegeException();
			}
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		LogisticsVehicle vehicle = (LogisticsVehicle)record;
		if(vehicle.getUserId()==sessionInfo.getUserId()) { //是发布人自己
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY; //发布以后只读
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Vehicle vehicleForm = (Vehicle)form;
		if(request.getMethod().equalsIgnoreCase("get") && vehicleForm.getPlateNumber()!=null && !vehicleForm.getPlateNumber().equals("")) { //车牌号不为空
			LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
			LogisticsVehicle vehicle = logisticsVehicleService.loadVehicle(vehicleForm.getPlateNumber());
			if(vehicle==null) {
				vehicleForm.setActionResult("unregist");
			}
			return vehicle;
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置使用的页面名称
		Vehicle vehicleForm = (Vehicle)form;
		if("unregist".equals(vehicleForm.getActionResult())) {
			vehicleForm.setSubForm("vehicleUnregist");
		}
		else if(accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE) {
			vehicleForm.setSubForm(OPEN_MODE_CREATE.equals(openMode) ? "vehicle" : "vehicleModify");
		}
		else if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			vehicleForm.setSubForm("vehicleInfoAnonymous"); //匿名页面
		}
		else {
			vehicleForm.setSubForm("vehicleInfo");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicle vehicle = (LogisticsVehicle)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			vehicle.setUserId(sessionInfo.getUserId()); //联盟用户ID
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}