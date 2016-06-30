package com.yuanluesoft.appraise.actions.task.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.appraise.actions.AppraiseFormAction;
import com.yuanluesoft.appraise.forms.admin.Task;
import com.yuanluesoft.appraise.pojo.AppraiseTask;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class TaskAction extends AppraiseFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		AppraiseTask task = (AppraiseTask)record;
		Task taskForm = (Task)form;
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		form.getTabs().addTab(-1, "options", "评议选项", "options.jsp", false);
		if((task!=null && task.getIsSpecial()==0) || (task==null && taskForm.getIsSpecial()==0)) {
			FieldUtils.getFormField(form.getFormDefine(), "appraiserType", request).setParameter("itemsText", "基础库评议员|0\\0管理服务对象|1");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Task taskForm = (Task)form;
		taskForm.setMonths(taskForm.getAppraiseMonths().substring(1, taskForm.getAppraiseMonths().length()-1).split(","));
		taskForm.setAllAppraiserJobs(taskForm.getAppraiserJobs()==null || taskForm.getAppraiserJobs().isEmpty());
		if(!taskForm.isAllAppraiserJobs()) {
			taskForm.setSelectedAppraiserJobs(taskForm.getAppraiserJobs().split(","));
		}
		taskForm.setAllRecipientJobs(taskForm.getRecipientJobs()==null || taskForm.getRecipientJobs().isEmpty());
		if(!taskForm.isAllRecipientJobs()) {
			taskForm.setSelectedRecipientJobs(taskForm.getRecipientJobs().split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Task taskForm = (Task)formToValidate;
		if(taskForm.getDelegateAttend()!=0 && (taskForm.getDelegateInviteSms()==null || taskForm.getDelegateInviteSms().isEmpty())) {
			throw new ValidateException("评议代表邀请短信格式不能为空");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Task taskForm = (Task)form;
		AppraiseTask task = (AppraiseTask)record;
		task.setLastModified(DateTimeUtils.now());
		task.setLastModifier(sessionInfo.getUserName());
		task.setLastModifierId(sessionInfo.getUserId());
		task.setAppraiseMonths("," + ListUtils.join(taskForm.getMonths(), ",", false) + ",");
		task.setAppraiserJobs(taskForm.isAllAppraiserJobs() ? null : ListUtils.join(taskForm.getSelectedAppraiserJobs(), ",", false));
		task.setRecipientJobs(taskForm.isAllRecipientJobs() ? null : ListUtils.join(taskForm.getSelectedRecipientJobs(), ",", false));
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}