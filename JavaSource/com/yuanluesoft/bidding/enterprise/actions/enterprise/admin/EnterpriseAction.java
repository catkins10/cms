package com.yuanluesoft.bidding.enterprise.actions.enterprise.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.forms.admin.Enterprise;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author yuanlue
 *
 */
public class EnterpriseAction extends WorkflowAction {

	public EnterpriseAction() {
		super();
		anonymousEnable = true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runRegistApproval";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			BiddingEnterprise enterprise = (BiddingEnterprise)record;
			if(enterprise.getWorkflowInstanceId()==null || enterprise.getWorkflowInstanceId().isEmpty()) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取工作流人口列表
		List workflowEntries = getWorkflowExploitService().listWorkflowEntries(workflowForm.getFormDefine().getApplicationName(), null, sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) {
			throw new Exception("Approval workflows are not exists.");
		}
		//获取流程
		WorkflowEntry workflowEntry = (WorkflowEntry)ListUtils.findObjectByProperty(workflowEntries, "workflowName", "企业注册");
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Enterprise enterpriseForm = (Enterprise)form;
		enterpriseForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			BiddingEnterprise enterprise = (BiddingEnterprise)record;
			enterprise.setCreated(DateTimeUtils.now());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Enterprise enterpriseForm = (Enterprise)form;
		BiddingEnterprise enterprise = (BiddingEnterprise)record;
		
		boolean alterEnabled = false; //是否允许变更
		boolean nullifyEnabled = false; //是否允许注销
		boolean registEmployeeEnabled = false;
		if(enterprise!=null && enterprise.getIsValid()=='1' && enterprise.getIsAlter()!='1' && enterprise.getIsNullify()!='1') {
			//是否允许注册用户
			registEmployeeEnabled = acl.contains("manageUnit_registEmployee");
			//检查流程配置,判断是否允许变更和注销
			List workflowEntries = getWorkflowExploitService().listWorkflowEntries("bidding/enterprise", null, sessionInfo);
			if(workflowEntries!=null && !workflowEntries.isEmpty()) { //有流程入口
				alterEnabled = ListUtils.findObjectByProperty(workflowEntries, "workflowName", "企业变更")!=null;
				nullifyEnabled = ListUtils.findObjectByProperty(workflowEntries, "workflowName", "企业注销")!=null;
			}
		}
		//删除"企业变更"和"企业注销"按钮
		if(!alterEnabled) {
			enterpriseForm.getFormActions().removeFormAction("企业变更");
		}
		if(!nullifyEnabled) {
			enterpriseForm.getFormActions().removeFormAction("企业注销");
		}
		//设置TAB列表
		enterpriseForm.getTabs().addTab(1, "certs", "企业资质", "enterpriseCerts" + (accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE ? "Read" : "Edit") + ".jsp", false);
		enterpriseForm.getTabs().addTab(2, "jobholders", "企业人员", "jobholders" + (accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE ? "Read" : "Edit") + ".jsp", false);
		if(registEmployeeEnabled) {
			enterpriseForm.getTabs().addTab(3, "employees", "企业用户注册", "employeesEdit.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#setFormTitle(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		if(record==null || !(record instanceof BiddingEnterprise)) {
			super.setFormTitle(form, record, request, sessionInfo);
		}
		else {
			BiddingEnterprise enterprise = (BiddingEnterprise)record;
			form.setFormTitle(enterprise.getName() + " - 企业" + (enterprise.getIsAlter()=='1' ? "变更" : (enterprise.getIsNullify()=='1' ? "注销" : "")));
		}
	}
}