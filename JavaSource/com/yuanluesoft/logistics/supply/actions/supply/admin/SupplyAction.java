package com.yuanluesoft.logistics.supply.actions.supply.admin;

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
import com.yuanluesoft.logistics.supply.forms.admin.Supply;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupply;
import com.yuanluesoft.logistics.supply.service.LogisticsSupplyService;

/**
 * 
 * @author linchuan
 *
 */
public class SupplyAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_superintend")) { //管理员或者负责人
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //总是编辑状态
		}
		if(acl.contains("manageUnit_regist")) { //登记人
			if(OPEN_MODE_CREATE.equals(openMode)) { //新建
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			LogisticsSupply supply = (LogisticsSupply)record;
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
		Supply supplyForm = (Supply)form;
		supplyForm.setCreator(sessionInfo.getUserName());
		supplyForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Supply supplyForm = (Supply)form;
		supplyForm.setDepartureAreaIds(ListUtils.join(supplyForm.getDepartures(), "departureId", ",", false)); //出发地点ID列表
		supplyForm.setDepartureAreas(ListUtils.join(supplyForm.getDepartures(), "departure", ",", false)); //出发地点列表
		supplyForm.setDestinationAreaIds(ListUtils.join(supplyForm.getDestinations(), "destinationId", ",", false)); //目的地点ID列表
		supplyForm.setDestinationAreas(ListUtils.join(supplyForm.getDestinations(), "destination", ",", false)); //目的地点列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		LogisticsSupply supply = (LogisticsSupply)record;
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
		LogisticsSupply supply = (LogisticsSupply)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(supply.getUserId()==0) {
				supply.setUserId(sessionInfo.getUserId()); //联盟用户ID
			}
			supply.setCreatorId(sessionInfo.getUserId()); //登记人ID
			supply.setCreator(sessionInfo.getUserName()); //登记人
			supply.setCreated(DateTimeUtils.now()); //登记时间
			supply.setCreatorIP(request.getRemoteAddr()); //登记人IP
		}
		supply.setLastModified(DateTimeUtils.now()); //最后修改时间
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Supply supplyForm = (Supply)form;
		LogisticsSupplyService logisticsSupplyService = (LogisticsSupplyService)getService("logisticsSupplyService");
		logisticsSupplyService.updateLogisticsSupply(supplyForm.getId(), OPEN_MODE_CREATE.equals(openMode), supplyForm.getDepartureAreaIds(), supplyForm.getDepartureAreas(), supplyForm.getDestinationAreaIds(), supplyForm.getDestinationAreas());
		return record;
	}
}