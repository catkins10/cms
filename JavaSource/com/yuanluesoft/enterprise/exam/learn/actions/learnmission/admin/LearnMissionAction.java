package com.yuanluesoft.enterprise.exam.learn.actions.learnmission.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.learn.forms.admin.LearnMission;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class LearnMissionAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_mission")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		LearnMission learnMissionForm = (LearnMission)form;
		learnMissionForm.setCreated(DateTimeUtils.now());
		learnMissionForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		LearnMission learnMissionForm = (LearnMission)form;
		//设置参加学习人员
		learnMissionForm.setLearners(getRecordControlService().getVisitors(learnMissionForm.getId(), com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission learnMission = (com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission)record;
		LearnMission learnMissionForm = (LearnMission)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			learnMission.setCreated(DateTimeUtils.now());
			learnMission.setCreatorId(sessionInfo.getUserId());
			learnMission.setCreator(sessionInfo.getUserName());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存参加学习人员
		getRecordControlService().updateVisitors(learnMission.getId(), com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission.class.getName(), learnMissionForm.getLearners(), RecordControlService.ACCESS_LEVEL_READONLY);
		return record;
	}
}