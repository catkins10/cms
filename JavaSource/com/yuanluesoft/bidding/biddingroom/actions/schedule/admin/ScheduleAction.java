package com.yuanluesoft.bidding.biddingroom.actions.schedule.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.biddingroom.forms.admin.Schedule;
import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoom;
import com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ScheduleAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager") || acl.contains("manageUnit_create")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Schedule scheduleForm = (Schedule)form;
		//设置城市名称
		BiddingRoomService biddingRoomService = (BiddingRoomService)getService("biddingRoomService");
		List rooms = biddingRoomService.listRooms("开标", null);
		if(rooms!=null && !rooms.isEmpty()) {
			scheduleForm.setCity(((BiddingRoom)rooms.get(0)).getCity());
		}
		//设置为开标
		scheduleForm.setRoomType("开标");
	}
}