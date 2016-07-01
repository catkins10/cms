package com.yuanluesoft.educ.student.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.educ.student.forms.StudentForm;
import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction.WorkflowActionParticipantCallback;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

public class StudentAction extends PublicServiceAction {

	private WorkflowExploitService workflowExploitService; //工作流利用服务
	
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	public StudentAction() {
		super();
		anonymousEnable = true;
	}
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		StudentForm studentForm = (StudentForm)form;
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		Stude studeFind= (Stude)databaseService.findRecordByHql("from com.yuanluesoft.educ.student.pojo.Stude Stude where Stude.idcardNumber = '"+studentForm.getIdcardNumber()+"'");
		if(studeFind!=null){
			studentForm.setError("您已提交过了！");
			throw new ValidateException();
		}
		
		AttachmentService attachmentService=(AttachmentService)getService("attachmentService");
		Stude stude=(Stude)record;
		try {
			List attachments=attachmentService.list("educ/student", "images", studentForm.getId(), false, 1, request);
			if(attachments!=null && !attachments.isEmpty()){
				Attachment attachment=(Attachment)attachments.get(0);
				stude.setImageName(attachment.getName());
			}else{
				stude.setImageName(null);
			}
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取工作流人口列表
		List workflowEntries = workflowExploitService.listWorkflowEntries(workflowForm.getFormDefine().getApplicationName(), null, sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) {
			throw new Exception("Approval workflows are not exists.");
		}
		//获取流程
		WorkflowEntry workflowEntry = (WorkflowEntry)ListUtils.findObjectByProperty(workflowEntries, "workflowName", "学生注册");
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}
	
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode,
				request, sessionInfo);
		StudentForm studentForm = (StudentForm)form;
		studentForm.setSubForm("student"); //提交页面
		
		Stude stude = (Stude)record;
		
		boolean alterEnabled = false; //是否允许变更
		if(stude!=null && stude.getIsValid()=='1' && stude.getIsAlter()!='1') {
			//检查流程配置,判断是否允许变更
			List workflowEntries = workflowExploitService.listWorkflowEntries("educ/student", null, sessionInfo);
			if(workflowEntries!=null && !workflowEntries.isEmpty()) { //有流程入口
				alterEnabled = ListUtils.findObjectByProperty(workflowEntries, "workflowName", "学生变更")!=null;
			}
		}
		//删除"学生变更"按钮
		if(!alterEnabled) {
			studentForm.getFormActions().removeFormAction("学生变更");
		}
	}
	
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		// TODO 自动生成方法存根
		super.validateBusiness(validateService, form, openMode, record, sessionInfo,
				request);
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		StudentForm studentForm = (StudentForm)form;
		Stude stude=(Stude)record;
		long personId = stude.getId();
		
		//检查用户是否被使用
		if(memberServiceList.isLoginNameInUse(stude.getIdcardNumber(),personId)) {
			studentForm.setError("账号已经被使用");
			throw new ValidateException();
		}
	}
	
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		StudentForm studentForm = (StudentForm)form;
		if(studentForm.getPassword()!=null &&
				   !"".equals(studentForm.getPassword()) &&
				   (!studentForm.getPassword().startsWith("{") ||
				    !studentForm.getPassword().endsWith("}"))) {
			studentForm.setPassword("{" + studentForm.getPassword() + "}");
				}
	}
}
