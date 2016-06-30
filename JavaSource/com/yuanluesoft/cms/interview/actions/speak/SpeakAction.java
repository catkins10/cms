package com.yuanluesoft.cms.interview.actions.speak;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeak;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 访谈发言
 * @author linchuan
 *
 */
public class SpeakAction extends FormAction {

	public SpeakAction() {
		super();
		anonymousEnable = true;
		sessionInfoClass = InterviewSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() + "/cms/interview/interviewLiveLogin.shtml?id=" + request.getParameter("subjectId"), "utf-8");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			return record;
		}
		InterviewService interviewService = (InterviewService)getService("interviewService");
		interviewService.submitSpeak((InterviewSpeak)record, request, (InterviewSessionInfo)sessionInfo);
		return record;
	}
}