package com.yuanluesoft.logistics.vehicle.actions.vehicle.admin;

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
import com.yuanluesoft.logistics.vehicle.forms.admin.Vehicle;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) ||  acl.contains("manageUnit_superintend") || acl.contains("manageUnit_regist")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
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
		Vehicle vehicleForm = (Vehicle)form;
		vehicleForm.setCreator(sessionInfo.getUserName());
		vehicleForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsVehicle vehicle = (LogisticsVehicle)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(vehicle.getUserId()==0) {
				vehicle.setUserId(sessionInfo.getUserId()); //联盟用户ID
			}
			vehicle.setCreatorId(sessionInfo.getUserId()); //登记人ID
			vehicle.setCreator(sessionInfo.getUserName()); //登记人
			vehicle.setCreated(DateTimeUtils.now()); //登记时间
			vehicle.setCreatorIP(request.getRemoteAddr()); //登记人IP
		}
		vehicle.setLastModified(DateTimeUtils.now()); //最后修改时间
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}