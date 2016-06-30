package com.yuanluesoft.cms.evaluation.actions.evaluationtopic.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.evaluation.forms.admin.EvaluationTopic;
import com.yuanluesoft.cms.evaluation.model.total.EvaluationTotal;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationTopicAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)record;
		if(topic==null || topic.getIssue()!='1') {
			acl.add("issue"); //增加发布全新
		}
		else {
			acl.add("unissue"); //增加撤销发布全新
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(acl.contains("manageUnit_transact")) { //经办
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			try {
				return getRecordControlService().getAccessLevel(record.getId(), record.getClass().getName(), sessionInfo);
			}
			catch (ServiceException e) {
				Logger.exception(e);
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		EvaluationTopic topicForm = (EvaluationTopic)form;
		topicForm.setCreated(DateTimeUtils.now());
		topicForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		EvaluationTopic topicForm = (EvaluationTopic)form;
		//设置被测评人和测评人
		topicForm.setTargetPersons(getRecordControlService().getVisitors(topicForm.getId(), com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD));
		topicForm.setEvaluatePersons(getRecordControlService().getVisitors(topicForm.getId(), com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		if(topicForm.getTabs().getTab("total")!=null) { //需要统计
			EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
			topicForm.setTotals(evaluationService.evaluationTotal(topicForm.getId()));
			if(topicForm.getTotals()!=null) {
				List totalsSortByOrg = new ArrayList(topicForm.getTotals());
				Collections.sort(totalsSortByOrg, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						return ((EvaluationTotal)arg0).getTargetPersonOrg().compareTo(((EvaluationTotal)arg1).getTargetPersonOrg());
					}
				});
				topicForm.setTotalsSortByOrg(totalsSortByOrg);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		EvaluationTopic topicForm = (EvaluationTopic)form;
		com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)record;
		if(topic!=null && topic.getIssue()=='1') { //已经发布
			topicForm.setSubForm(topicForm.getSubForm().replaceAll("Edit", "Read")); //显示只读子表单
		}
		//设置TAB页签
		topicForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		topicForm.getTabs().addTab(-1, "items", "测评项目", "evaluationItems.jsp", false);
		topicForm.getTabs().addTab(-1, "options", "选择项", "evaluationOptions.jsp", false);
		if(!OPEN_MODE_CREATE.equals(openMode) && accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
			topicForm.getTabs().addTab(-1, "total", "汇总(按得分)", "evaluationTotal.jsp", false);
			topicForm.getTabs().addTab(-1, "totalByOrg", "汇总(按所在部门)", "evaluationTotalByOrg.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EvaluationTopic topicForm = (EvaluationTopic)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)record;
			topic.setCreated(DateTimeUtils.now());
			topic.setCreator(sessionInfo.getUserName());
			topic.setCreatorId(sessionInfo.getUserId());
			//给创建者授予编辑权限
			getRecordControlService().appendVisitor(topic.getId(), topic.getClass().getName(), sessionInfo.getUserId(), RecordControlService.ACCESS_LEVEL_EDITABLE);
		}
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存被测评人和测评人
		if(topicForm.getTargetPersons().getVisitorIds()!=null) {
			getRecordControlService().updateVisitors(topicForm.getId(), com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class.getName(), topicForm.getTargetPersons(), RecordControlService.ACCESS_LEVEL_PREREAD);
			getRecordControlService().updateVisitors(topicForm.getId(), com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class.getName(), topicForm.getEvaluatePersons(), RecordControlService.ACCESS_LEVEL_READONLY);
		}
		return record;
	}
}