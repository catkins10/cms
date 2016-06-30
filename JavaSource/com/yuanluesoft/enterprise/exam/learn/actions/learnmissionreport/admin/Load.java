package com.yuanluesoft.enterprise.exam.learn.actions.learnmissionreport.admin;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.exam.learn.forms.admin.LearnMissionReport;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMissionAccomplish;
import com.yuanluesoft.enterprise.exam.learn.service.LearnService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends DialogFormAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_mission")) {
			return;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		LearnMissionReport learnMissionReport = (LearnMissionReport)form;
		LearnService learnService = (LearnService)getService("learnService");
		//获取需要参加学习的人员
		List notLearnPersons = getRecordControlService().listVisitorPersons(learnMissionReport.getMissionId(), LearnMission.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY);
		learnMissionReport.setNotLearnPersons(notLearnPersons);
		//获取学习完成情况
		List learned = learnService.listLearnMissionAccomplishs(learnMissionReport.getMissionId());
		learnMissionReport.setLearned(learned);
		//从需要参加考试的人员中剔除已经考过的用户
		for(Iterator iterator = learned==null ? null : learned.iterator(); iterator!=null && iterator.hasNext();) {
			LearnMissionAccomplish accomplish = (LearnMissionAccomplish)iterator.next();
			ListUtils.removeObjectByProperty(notLearnPersons, "id", new Long(accomplish.getPersonId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }
}