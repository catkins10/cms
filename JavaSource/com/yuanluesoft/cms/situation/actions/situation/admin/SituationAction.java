package com.yuanluesoft.cms.situation.actions.situation.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.situation.forms.admin.Situation;
import com.yuanluesoft.cms.situation.service.SituationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class SituationAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runSituation";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(getOrgService().checkPopedom(sessionInfo.getUnitId(), "situationTransactor", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		else {
			com.yuanluesoft.cms.situation.pojo.Situation situation = (com.yuanluesoft.cms.situation.pojo.Situation)record;
			if(getOrgService().checkPopedom(situation.getUnitId(), "situationTransactor", sessionInfo)) { //检查用户是否受理单位的民情办理人
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			if(situation.getCoordinateUnitId()!=0 && getOrgService().checkPopedom(situation.getCoordinateUnitId(), "situationTransactor", sessionInfo)) { //检查用户是否办理单位的民情办理人
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Situation situationForm = (Situation)form;
		if(situationForm.getCreated()==null) {
			situationForm.setCreated(DateTimeUtils.now());
		}
		situationForm.setUnitId(sessionInfo.getUnitId()); //受理部门ID
		situationForm.setUnitName(sessionInfo.getUnitName()); //受理部门
		situationForm.setIsPublic('1'); //默认设置为允许公开
		//生成编号
		PublicService publicService = (PublicService)getService("publicService");
		if(situationForm.getSn()==null) {
			situationForm.setSn(publicService.getSN());
		}
		//证件名称
		if(situationForm.getCreatorCertificateName()==null) {
			situationForm.setCreatorCertificateName("身份证");
		}
		if(situationForm.getReceiver()==null) {
			situationForm.setReceiver(sessionInfo.getUserName());
			situationForm.setReceiveTime(DateTimeUtils.now());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Situation situationForm = (Situation)form;
		situationForm.setVillage(situationForm.getUnitName().endsWith("村") || situationForm.getUnitName().endsWith("社区"));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("situationTransactor".equals(programmingParticipantId)) {
			com.yuanluesoft.cms.situation.pojo.Situation situation = (com.yuanluesoft.cms.situation.pojo.Situation)record;
			SituationService situationService = (SituationService)getService("situationService");
			return ListUtils.generateList(situationService.getSituationTransactorParticipant(situation.getCoordinateUnitId()==0 ? situation.getUnitId() : situation.getCoordinateUnitId(), situation.getCoordinateUnitId()==0 ? situation.getUnitName() : situation.getCoordinateUnitName()));
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.cms.situation.pojo.Situation situation = (com.yuanluesoft.cms.situation.pojo.Situation)record;
		if(ListUtils.findObjectByProperty(form.getFormActions(), "title", "上报")!=null) {
			//获取上级受理单位
			SituationService situationService = (SituationService)getService("situationService");
			Org higherUnit = situationService.getHigherSituationUnit(situation.getCoordinateUnitId()==0 ? situation.getUnitId() : situation.getCoordinateUnitId());
			if(higherUnit==null) {
				form.getFormActions().removeFormAction("上报");
			}
		}
		if(accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE || situation==null || situation.getFeedbackTime()==null) {
			form.getFormActions().removeFormAction("回应函补录");
		}
		if(situation!=null && situation.getCoordinates()!=null && !situation.getCoordinates().isEmpty()) {
			form.getTabs().addTab(1, "coordinates", "上报或协调其它单位", "coordinates.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.situation.pojo.Situation situation = (com.yuanluesoft.cms.situation.pojo.Situation)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			situation.setUnitId(sessionInfo.getUnitId()); //受理部门ID
			situation.setUnitName(sessionInfo.getUnitName()); //受理部门
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}