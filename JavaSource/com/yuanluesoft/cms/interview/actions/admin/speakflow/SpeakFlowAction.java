package com.yuanluesoft.cms.interview.actions.admin.speakflow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.interview.forms.admin.SpeakFlow;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeakFlow;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SpeakFlowAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SpeakFlow speakFlowForm = (SpeakFlow)form;
		if(OPEN_MODE_CREATE.equals(speakFlowForm.getAct())) {
			InterviewService interviewService = (InterviewService)getService("interviewService");
			InterviewSpeakFlow speakFlow = interviewService.loadSpeakFlow(speakFlowForm.getSiteId());
			if(speakFlow!=null) {
				speakFlowForm.setAct(OPEN_MODE_EDIT);
				speakFlowForm.setId(speakFlow.getId());
			}
		}
		return super.load(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//获取角色列表
		InterviewService interviewService = (InterviewService)getService("interviewService");
		SpeakFlow speakFlowForm = (SpeakFlow)form;
		InterviewSpeakFlow speakFlow = (InterviewSpeakFlow)record;
		List roles = interviewService.listInterviewRoles(OPEN_MODE_CREATE.equals(openMode) ? speakFlowForm.getSiteId() : speakFlow.getSiteId());
		speakFlowForm.setRoles("主持人,嘉宾" + (roles==null || roles.isEmpty() ? "" : "," + ListUtils.join(roles, "role", ",", false)));
	}
}
