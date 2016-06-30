package com.yuanluesoft.cms.interview.collect.actions.collectprologue.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.interview.collect.forms.admin.CollectPrologue;
import com.yuanluesoft.cms.interview.collect.pojo.InterviewCollectPrologue;
import com.yuanluesoft.cms.interview.collect.service.InterviewCollectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CollectPrologueAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE.equals(getOpenMode(form, request))) { //新建
			CollectPrologue collectPrologueForm = (CollectPrologue)form;
			InterviewCollectService interviewCollectService = (InterviewCollectService)getService("interviewCollectService");
			return interviewCollectService.getCollectPrologue(collectPrologueForm.getSiteId());
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			InterviewCollectPrologue prologue = (InterviewCollectPrologue)record;
			prologue.setCreated(DateTimeUtils.now());
			prologue.setCreator(sessionInfo.getUserName());
			prologue.setCreatorId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}