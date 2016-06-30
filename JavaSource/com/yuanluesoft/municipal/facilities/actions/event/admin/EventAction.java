package com.yuanluesoft.municipal.facilities.actions.event.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.municipal.facilities.forms.admin.FacilitiesEvent;
import com.yuanluesoft.municipal.facilities.pojo.PdaUser;
import com.yuanluesoft.municipal.facilities.service.FacilitiesService;

/**
 * 
 * @author linchuan
 *
 */
public class EventAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runEvent";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		FacilitiesEvent facilitiesEvent = (FacilitiesEvent)form;
		facilitiesEvent.setIsReceipt('1');
		facilitiesEvent.setCreated(DateTimeUtils.now());
		facilitiesEvent.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(1, "photo", "照片对比", "eventPhoto.jsp", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent facilitiesEvent = (com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent)record;
			facilitiesEvent.setCreated(DateTimeUtils.now());
			facilitiesEvent.setCreator(sessionInfo.getUserName());
			facilitiesEvent.setCreatorId(sessionInfo.getUserId());
			if(facilitiesEvent.getEventNumber()==null || facilitiesEvent.getEventNumber().equals("")) {
				FacilitiesService facilitiesService = (FacilitiesService)getService("facilitiesService");
				facilitiesEvent.setEventNumber(facilitiesService.generateEventNumber(facilitiesEvent, false));
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		FacilitiesService facilitiesService = (FacilitiesService)getService("facilitiesService");
		//获取PDA使用者列表
		List pdaUsers = facilitiesService.listPdaUsers(sessionInfo);
		if(pdaUsers==null || pdaUsers.isEmpty()) {
			return null;
		}
		//转换为办理人
		for(int i=0; i<pdaUsers.size(); i++) {
			PdaUser pdaUser = (PdaUser)pdaUsers.get(i);
			//转换为Person模型
			Person personParticipant = new Person();
			personParticipant.setId("" + pdaUser.getId());
			String path = Environment.getContextPath() + "/" + workflowForm.getFormDefine().getApplicationName() + "/";
			personParticipant.setName(pdaUser.getName() + "<span> (<a><img src=\"" + path + pdaUser.getLoginImage() + "\"></a> <a><img src=\"" + path + pdaUser.getGPSLoginImage() + "\"></a> <a href=\"javascript:locatePdaUser('" + pdaUser.getCode() + "')\">定位</a> <a href=\"javascript:trackPdaUser('" + pdaUser.getCode() + "')\">跟踪</a> <a href=\"javascript:showPdaUserHistory('" + pdaUser.getCode() + "')\">历史轨迹</a>)</span>");
			pdaUsers.set(i, personParticipant);
		}
		return pdaUsers;
	}
}