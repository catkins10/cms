package com.yuanluesoft.traffic.busline.actions.admin.busline;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.traffic.busline.forms.admin.BusLine;
import com.yuanluesoft.traffic.busline.service.BusLineService;

/**
 * 
 * @author lmiky
 *
 */
public class BusLineAction extends FormAction {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.dao.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) { //总是允许管理员编辑
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			if(acl.contains("manageUnit_editor")) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		if(acl.contains("manageUnit_editor")) {
			com.yuanluesoft.traffic.busline.pojo.BusLine busLine = (com.yuanluesoft.traffic.busline.pojo.BusLine)record;
			try {
				if(getOrgService().getPersonalUnitOrSchool(busLine.getModifyPersonId()).getId()==sessionInfo.getUnitId()) { //本单位的其他管理员可以修改
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			catch (ServiceException e) {
				
			}
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		//form.getTabs().addTab(-1, "stations", "途经站点", "stations.jsp", false);
		form.getTabs().addTab(-1, "changes", "线路变更通知", "changes.jsp", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		BusLine busLineForm = (BusLine)form;
		busLineForm.setTicketPrice("1元"); //票价设为1元
		busLineForm.setLastModified(DateTimeUtils.now()); //最后更新时间
		busLineForm.setModifyPerson(sessionInfo.getUserName()); //最后更新用户
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		BusLine busLineForm = (BusLine)form;
		busLineForm.setUplinkStationNames(ListUtils.join(busLineForm.getUplinkStations(), "name", ", ", false)); //上行站点列表
		busLineForm.setDownlinkStationNames(ListUtils.join(busLineForm.getDownlinkStations(), "name", ", ", false)); //下行站点列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.traffic.busline.pojo.BusLine busLine = (com.yuanluesoft.traffic.busline.pojo.BusLine)record;
		BusLine busLineForm = (BusLine)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			busLine.setModifyPersonId(sessionInfo.getUserId()); //最后更新用户ID
			busLine.setModifyPerson(sessionInfo.getUserName()); //最后更新用户
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存站点
		BusLineService busLineService = (BusLineService)getService("busLineService");
		busLineService.updateBusLineStations(busLine, busLineForm.getDownlinkStationNames(), busLineForm.getUplinkStationNames());
		return busLine;
		
	}
}