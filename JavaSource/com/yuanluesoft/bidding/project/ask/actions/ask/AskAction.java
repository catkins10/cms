package com.yuanluesoft.bidding.project.ask.actions.ask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.project.ask.forms.admin.Ask;
import com.yuanluesoft.bidding.project.ask.pojo.BiddingProjectAsk;
import com.yuanluesoft.bidding.project.ask.service.BiddingProjectAskService;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author linchuan
 *
 */
public class AskAction extends com.yuanluesoft.bidding.project.ask.actions.ask.admin.AskAction {

	public AskAction() {
		super();
		sessionInfoClass = BiddingSessionInfo.class;
		alwaysAutoSend = true; //外部页面，总是自动发送
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.ask.actions.ask.admin.AskAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return new Link(Environment.getWebApplicationSafeUrl() +  "/bidding/enterprise/login.shtml" + (siteId>0 ? "?siteId=" + siteId : ""), "utf-8"); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		BiddingProjectAskService biddingProjectAskService = (BiddingProjectAskService)getService("biddingProjectAskService");
		if(!biddingProjectAskService.isNeedReply()) { //不需要应答
			return null;
		}
		//获取工作流人口列表
		List workflowEntries = getWorkflowExploitService().listWorkflowEntries(workflowForm.getFormDefine().getApplicationName(), null, sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) {
			throw new Exception("Approval workflows are not exists.");
		}
		//使用第一个流程入口
		WorkflowEntry workflowEntry = (WorkflowEntry)workflowEntries.get(0);
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Ask askForm = (Ask)form;
		//askForm.setEnterpriseName(sessionInfo.getDepartmentName()); //提问企业
		//获取工程名称
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		askForm.setProjectName(biddingProjectService.getProject(askForm.getProjectId()).getProjectName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			BiddingProjectAsk ask = (BiddingProjectAsk)record;
			//ask.setAskPersonId(sessionInfo.getUserId()); //提问用户ID
			//ask.setAskPersonName(sessionInfo.getUserName()); //提问用户
			//ask.setEnterpriseId(sessionInfo.getDepartmentId()); //提问企业ID
			//ask.setEnterpriseName(sessionInfo.getDepartmentName()); //提问企业
			//项目名称
			BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
			ask.setProjectName(biddingProjectService.getProject(ask.getProjectId()).getProjectName());
			//提问时间
			ask.setAskTime(DateTimeUtils.now());
			//转换提问发起点为中文
			Field field = FieldUtils.getRecordField(BiddingProject.class.getName(), ask.getAskFrom(), request);
			if(field!=null) {
				ask.setAskFrom(field.getTitle());
			}
			BiddingProjectAskService biddingProjectAskService = (BiddingProjectAskService)getService("biddingProjectAskService");
			if(!biddingProjectAskService.isNeedReply()) { //不需要应答
				ask.setIsPublic('1'); //直接设置为公开
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.ask.actions.ask.admin.AskAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("Create");
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().removeTab("workflowLog"); //不显示流程记录
	}
}