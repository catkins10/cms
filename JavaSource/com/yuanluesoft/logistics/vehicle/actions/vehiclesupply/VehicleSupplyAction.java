package com.yuanluesoft.logistics.vehicle.actions.vehiclesupply;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.logistics.usermanage.model.LogisticsSessionInfo;
import com.yuanluesoft.logistics.vehicle.forms.VehicleSupply;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleSupply;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleSupplyAction extends com.yuanluesoft.logistics.vehicle.actions.vehiclesupply.admin.VehicleSupplyAction {

	public VehicleSupplyAction() {
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
			if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名用户
				throw new PrivilegeException();
			}
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		LogisticsVehicleSupply vehicleSupply = (LogisticsVehicleSupply)record;
		if(vehicleSupply.getUserId()==sessionInfo.getUserId()) { //是发布人自己
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY; //发布以后只读
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置使用的CMS页面名称
		VehicleSupply vehicleSupplyForm = (VehicleSupply)form;
		if(accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE) {
			vehicleSupplyForm.setSubForm("vehicleSupply");
		}
		else if(SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			vehicleSupplyForm.setSubForm("vehicleSupplyInfoAnonymous"); //匿名页面
		}
		else {
			vehicleSupplyForm.setSubForm("vehicleSupplyInfo");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicleSupply vehicleSupply = (LogisticsVehicleSupply)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			vehicleSupply.setUserId(sessionInfo.getUserId()); //联盟用户ID
		}
		vehicleSupply.setIssue(1); //自动设为发布状态
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}