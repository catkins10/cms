package com.yuanluesoft.cms.onlineservice.faq.actions.faq.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.faq.forms.admin.Faq;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaqSubjection;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author yuanluesoft
 *
 */
public class FaqAction extends WorkflowAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName(com.yuanluesoft.jeaf.workflow.form.WorkflowForm)
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runFaqApproval";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#retrieveWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		Faq faqForm = (Faq)workflowForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		String workflowId = onlineServiceDirectoryService.getWorkflowId(faqForm.getDirectoryId(), "faq");
		//获取工作流人口
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(workflowId, null, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			throw new Exception("Approval workflows are not exists.");
		}
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Faq faqForm = (Faq)workflowForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		return onlineServiceDirectoryService.listDirectoryVisitors(faqForm.getDirectoryId(), programmingParticipantId, true, false, 0);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Faq faqForm = (Faq)workflowForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		return onlineServiceDirectoryService.checkPopedom(faqForm.getDirectoryId(), programmingParticipantId, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		Faq faqForm = (Faq)form;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		//获取用户的资源发布权限,只判断第一个目录
		long directoryId;
		if(faqForm.getDirectoryName()!=null) {
			directoryId = faqForm.getDirectoryId();
		}
		else {
			if(OPEN_MODE_CREATE.equals(openMode)) {
				directoryId = faqForm.getDirectoryId();
			}
			else {
				directoryId = ((OnlineServiceFaqSubjection)faq.getSubjections().iterator().next()).getDirectoryId();
			}
		}
		//获取用户对目录的权限
		List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(directoryId, sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		if(popedoms.contains("manager")) {
			acl.add("manager");
		}
		if(popedoms.contains("transactor")) {
			acl.add("transactor");
		}
		if(faq!=null && (popedoms.contains("manager") || faq.getIssuePersonId()==sessionInfo.getUserId())) { //允许管理员和发布人撤销发布/重新发布
			if(faq.getStatus()==OnlineServiceFaqService.FAQ_STATUS_ISSUE) {
				acl.add("unissue");
			}
			else if(faq.getStatus()==OnlineServiceFaqService.FAQ_STATUS_UNISSUE || faq.getStatus()==OnlineServiceFaqService.FAQ_STATUS_NOPASS) {
				acl.add("reissue");
			}
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Faq faqForm = (Faq)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			if(faqForm.getDirectoryName()==null || faqForm.getDirectoryName().isEmpty()) { //把投诉或者咨询设为常见问题
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			if(acl.contains("manager") || acl.contains("transactor")) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		if(acl.contains("unissue") || acl.contains("reissue")) { //撤销发布或重发布
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(record!=null) {
			OnlineServiceFaq faq = (OnlineServiceFaq)record;
			if(faq.getStatus()>=OnlineServiceFaqService.FAQ_STATUS_DELETED) { //逻辑删除
				if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
				throw new PrivilegeException();
			}
			if(faq.getIssuePersonId()>0) { //已经发布过
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		if(faq.getStatus()>=OnlineServiceFaqService.FAQ_STATUS_DELETED) { //逻辑删除
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return;
			}
			throw new PrivilegeException();
		}
		if(acl.contains("manager")) { //管理员
			return;
		}
		if(faq.getWorkflowInstanceId()==null || faq.getWorkflowInstanceId().isEmpty()) {
			throw new PrivilegeException(); //导入的数据,不允许删除
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(acl.contains("reissue")) {
			form.setSubForm(form.getSubForm().replace("Read", "Edit"));
		}
		else if(acl.contains("unissue")) {
			form.setSubForm(form.getSubForm().replace("Edit", "Read"));
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().removeTab("workflowLog");
		}
		if(record!=null) {
			OnlineServiceFaq faq = (OnlineServiceFaq)record;
			if(faq.getStatus()>=OnlineServiceFaqService.FAQ_STATUS_DELETED) { //逻辑删除
				form.getFormActions().removeFormAction("删除");
				form.getFormActions().removeFormAction("保存");
				form.setSubForm(form.getSubForm().replace("Edit", "Read"));
			}
			else {
				form.getFormActions().removeFormAction("永久删除");
				form.getFormActions().removeFormAction("撤销删除");
			}
		}
		form.getTabs().addTab(1, "relationFaqs", "相关问题", "relationFaqs.jsp", false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		if(faq.getStatus()!=OnlineServiceFaqService.FAQ_STATUS_ISSUE) { //未发布
			if(workflowInstanceCompleted) { //流程结束
				faq.setStatus(OnlineServiceFaqService.FAQ_STATUS_NOPASS); //办结/未通过审核
			}
			else if(isReverse) { //回退
				faq.setStatus(OnlineServiceFaqService.FAQ_STATUS_UNDO); //回退/取回
			}
			else { //流转中
				faq.setStatus(OnlineServiceFaqService.FAQ_STATUS_TODO); //回退/取回
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoReverse(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoReverse(workflowForm, record, request, response, sessionInfo);
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		faq.setStatus(OnlineServiceFaqService.FAQ_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoUndo(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoUndo(workflowForm, record, request, response, sessionInfo);
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		faq.setStatus(OnlineServiceFaqService.FAQ_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Faq faqForm = (Faq)form;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		
		boolean first = true;
		String otherNames = null;
		String otherIds = null;
		for(Iterator iterator = faqForm.getSubjections().iterator(); iterator.hasNext();) {
			OnlineServiceFaqSubjection subjection = (OnlineServiceFaqSubjection)iterator.next();
			try {
				String directoryName = onlineServiceDirectoryService.getDirectoryFullName(subjection.getDirectoryId(), "/", "mainDirectory");
				if(first) {
					first = false;
					//设置所在目录名称
					faqForm.setDirectoryName(directoryName);
					faqForm.setDirectoryId(subjection.getDirectoryId());
				}
				else {
					otherIds = (otherIds==null ? "" : otherIds + ",") + subjection.getDirectoryId();
					otherNames = (otherNames==null ? "" : otherNames + ",") + directoryName;
				}
			}
			catch(Exception e) {
				
			}
		}
		//设置所在的其他目录名称
		faqForm.setOtherDirectoryIds(otherIds);
		faqForm.setOtherDirectoryNames(otherNames);
		
		//设置隶属事项ID、名称
		faqForm.setItemIds(ListUtils.join(faqForm.getSubjectionItems(), "itemId", ",", false));
		faqForm.setItemNames(ListUtils.join(faqForm.getSubjectionItems(), "itemName", ",", false));
		
		//设置关联问题列表
		faqForm.setRelationFaqs(onlineServiceFaqService.listRelationFaqs(faqForm.getId(), false));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Faq faqForm = (Faq)form;
		if(faqForm.getSourceRecordId()>0) { //把投诉或者咨询设为常见问题
			if(faqForm.getQuestion()==null) {
				try {
					String hql = "from " + faqForm.getSourceRecordClass() + " " + faqForm.getSourceRecordClass() + 
								 " where " + faqForm.getSourceRecordClass() + ".id=" + faqForm.getSourceRecordId();
					PublicService publicService = (PublicService)((DatabaseService)getService("databaseService")).findRecordByHql(hql, ListUtils.generateList("opinions"));
					faqForm.setQuestion(publicService.getSubject());
					//获取必填的意见字段
					List opinionFields = FieldUtils.listRecordFields(publicService.getClass().getName(), "opinion", null, null, null, false, false, false, false, 1);
					Field requiredOpinionField = (Field)ListUtils.findObjectByProperty(opinionFields, "required", Boolean.TRUE);
					//获取办理意见
					Opinion opinion = (Opinion)ListUtils.findObjectByProperty(publicService.getOpinions(), "opinionType", requiredOpinionField.getName());
					faqForm.setAnswer(StringUtils.generateHtmlContent(opinion.getOpinion()));
				}
				catch(Exception e) {
					
				}
			}
		}
		else if("get".equalsIgnoreCase(request.getMethod())) {
			OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
			faqForm.setDirectoryName(onlineServiceDirectoryService.getDirectoryFullName(faqForm.getDirectoryId(), "/", "mainDirectory")); //设置所在目录
		}
		faqForm.setCreated(DateTimeUtils.now());
		faqForm.setCreator(sessionInfo.getUserName()); //创建人
		faqForm.setCreated(DateTimeUtils.now()); //创建时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		Faq faqForm = (Faq)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(faq.getCreated()==null) {
				faq.setCreated(DateTimeUtils.now());
			}
			faq.setCreatorId(sessionInfo.getUserId()); //创建人ID
			faq.setCreator(sessionInfo.getUserName()); //创建人
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		//保存所属目录
		if(faqForm.getDirectoryName()!=null) {
			String directoryIds = faqForm.getOtherDirectoryIds();
			directoryIds = faqForm.getDirectoryId() + (directoryIds==null || directoryIds.equals("") ? "" : "," + directoryIds);
			onlineServiceFaqService.updateFaqSubjectios(faq, OPEN_MODE_CREATE.equals(openMode), directoryIds, faqForm.getItemIds(), faqForm.getItemNames());
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		if("true".equals(request.getParameter("physical"))) { //物理删除
			super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		}
		else { //逻辑删除
			OnlineServiceFaq faq = (OnlineServiceFaq)record;
			faq.setStatus((char)(OnlineServiceFaqService.FAQ_STATUS_DELETED + (faq.getStatus() - '0')));
			onlineServiceFaqService.update(faq);
		}
	}
}