package com.yuanluesoft.cms.advice.actions.advicetopic.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.advice.forms.admin.AdviceTopic;
import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AdviceTopicAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		AdviceTopic adviceTopicForm = (AdviceTopic)form;
		adviceTopicForm.setCreated(DateTimeUtils.now());
		adviceTopicForm.setCreator(sessionInfo.getUserName());
		if(adviceTopicForm.getIssue()==0) {
			adviceTopicForm.setIssue('1');
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.cms.advice.pojo.AdviceTopic adviceTopic = (com.yuanluesoft.cms.advice.pojo.AdviceTopic)record;
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(adviceTopic!=null) {
			if(adviceTopic.getEndDate()!=null && !adviceTopic.getEndDate().before(DateTimeUtils.now())) { //征集未结束
				form.getFormActions().removeFormAction("结果反馈");
			}
			if(adviceTopic.getFeedbacks()!=null && !adviceTopic.getFeedbacks().isEmpty()) { //有结果反馈
				form.getTabs().addTab(-1, "feedback", "结果反馈", "feedback.jsp", false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.advice.pojo.AdviceTopic adviceTopic = (com.yuanluesoft.cms.advice.pojo.AdviceTopic)record;
			if(adviceTopic.getCreated()==null) {
				adviceTopic.setCreated(DateTimeUtils.now());
			}
			adviceTopic.setCreator(sessionInfo.getUserName());
			adviceTopic.setCreatorId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}