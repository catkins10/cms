package com.yuanluesoft.educ.student.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.educ.student.forms.admin.StudentForm;
import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

public class StudentAction extends PublicServiceAdminAction {

	public StudentAction() {
		super();
		anonymousEnable = true;
	}

	public String getWorkflowActionName(WorkflowForm workflowForm) {
		// TODO 自动生成方法存根
		return "runStudent";
	}

	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			Stude stude = (Stude)record;
			if(stude.getWorkflowInstanceId()==null || stude.getWorkflowInstanceId().isEmpty()) {
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
		WorkflowEntry workflowEntry = (WorkflowEntry)ListUtils.findObjectByProperty(workflowEntries, "workflowName", "学生注册");
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		StudentForm studentForm = (StudentForm)form;
		Stude stude = (Stude)record;
		
		boolean alterEnabled = false; //是否允许变更
		if(stude!=null && stude.getIsValid()=='1' && stude.getIsAlter()!='1') {
			//检查流程配置,判断是否允许变更
			List workflowEntries = getWorkflowExploitService().listWorkflowEntries("educ/student", null, sessionInfo);
			if(workflowEntries!=null && !workflowEntries.isEmpty()) { //有流程入口
				alterEnabled = ListUtils.findObjectByProperty(workflowEntries, "workflowName", "学生变更")!=null;
			}
		}
		//删除"学生变更"按钮
		if(!alterEnabled) {
			studentForm.getFormActions().removeFormAction("学生变更");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#setFormTitle(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		if(record==null || !(record instanceof Stude)) {
			super.setFormTitle(form, record, request, sessionInfo);
		}
		else {
			Stude stude = (Stude)record;
			form.setFormTitle(stude.getName() + " - 学生" + (stude.getIsAlter()=='1' ? "变更" : ""));
		}
	}	
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根

		StudentForm studentForm = (StudentForm)form;
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		Stude studeFind= (Stude)databaseService.findRecordByHql("from com.yuanluesoft.educ.student.pojo.Stude Stude where Stude.isValid='1' and Stude.idcardNumber = '"+studentForm.getIdcardNumber()+"'");
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
		
		//后台需设置默认密码
		String password=stude.getPassword();
		if(password==null || "".equals(password)){
			stude.setPassword("123456");
		}
		
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
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
