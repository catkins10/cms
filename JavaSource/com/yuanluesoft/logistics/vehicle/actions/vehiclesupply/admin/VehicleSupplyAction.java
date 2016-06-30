package com.yuanluesoft.logistics.vehicle.actions.vehiclesupply.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.logistics.vehicle.forms.admin.VehicleSupply;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleSupply;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleSupplyAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_superintend")) { //管理员或者负责人
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //总是编辑状态
		}
		if(acl.contains("manageUnit_regist")) { //登记人
			if(OPEN_MODE_CREATE.equals(openMode)) { //新建
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			LogisticsVehicleSupply supply = (LogisticsVehicleSupply)record;
			return (supply.getIssue()==0 && supply.getCreatorId()==sessionInfo.getUserId() ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY); //新建
		}
		if(!OPEN_MODE_CREATE.equals(openMode) && acl.contains(AccessControlService.ACL_APPLICATION_VISITOR)) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		VehicleSupply vehicleSupplyForm = (VehicleSupply)form;
		vehicleSupplyForm.setCreator(sessionInfo.getUserName());
		vehicleSupplyForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		VehicleSupply vehicleSupplyForm = (VehicleSupply)form;
		vehicleSupplyForm.setFreeVehicleNumbers(ListUtils.join(vehicleSupplyForm.getFreeVehicles(), "plateNumber", ",", false)); //载货车辆车牌列表
		vehicleSupplyForm.setDepartureAreaIds(ListUtils.join(vehicleSupplyForm.getDepartures(), "departureId", ",", false)); //出发地点ID列表
		vehicleSupplyForm.setDepartureAreas(ListUtils.join(vehicleSupplyForm.getDepartures(), "departure", ",", false)); //出发地点列表
		vehicleSupplyForm.setDestinationAreaIds(ListUtils.join(vehicleSupplyForm.getDestinations(), "destinationId", ",", false)); //目的地点ID列表
		vehicleSupplyForm.setDestinationAreas(ListUtils.join(vehicleSupplyForm.getDestinations(), "destination", ",", false)); //目的地点列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicleSupply supply = (LogisticsVehicleSupply)record;
		if(supply!=null && supply.getIssue()==1) { //已发布
			form.setSubForm(SUBFORM_READ); //只读
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		boolean issue = false;
		boolean unissue = false;
		if(supply!=null && supply.getIssue()==1) { //已发布
			//检查用户是否管理员或者负责人,如果是,则允许撤销发布
			unissue = acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_superintend");
		}
		else { //未发布
			//检查用户是否登记人、管理员或者负责人,如果是,则允许发布
			issue = supply==null || sessionInfo.getUserId()==supply.getCreatorId() || acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_superintend");
		}
		if(!issue) {
			form.getFormActions().removeFormAction("发布");
		}
		if(!unissue) {
			form.getFormActions().removeFormAction("撤销发布");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicleSupply vehicleSupply = (LogisticsVehicleSupply)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(vehicleSupply.getUserId()==0) {
				vehicleSupply.setUserId(sessionInfo.getUserId()); //联盟用户ID
			}
			vehicleSupply.setCreatorId(sessionInfo.getUserId()); //登记人ID
			vehicleSupply.setCreator(sessionInfo.getUserName()); //登记人
			vehicleSupply.setCreated(DateTimeUtils.now()); //登记时间
			vehicleSupply.setCreatorIP(request.getRemoteAddr()); //登记人IP
		}
		vehicleSupply.setLastModified(DateTimeUtils.now()); //最后修改时间
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		VehicleSupply vehicleSupplyForm = (VehicleSupply)form;
		LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
		logisticsVehicleService.updateLogisticsVehicleSupply(vehicleSupplyForm.getId(), OPEN_MODE_CREATE.equals(openMode), vehicleSupplyForm.getFreeVehicleNumbers(), vehicleSupplyForm.getDepartureAreaIds(), vehicleSupplyForm.getDepartureAreas(), vehicleSupplyForm.getDestinationAreaIds(), vehicleSupplyForm.getDestinationAreas());
		return record;
	}
}