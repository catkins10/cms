/*
 * Created on 2005-11-8
 *
 */
package com.yuanluesoft.j2oa.receival.actions.receival;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.document.pojo.DocumentOption;
import com.yuanluesoft.j2oa.document.service.DocumentService;
import com.yuanluesoft.j2oa.receival.forms.Receival;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalTemplateConfig;
import com.yuanluesoft.j2oa.receival.service.ReceivalService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
 * 
 */
public class ReceivalAction extends WorkflowAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "run";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Receival formReceival = (Receival)form;
		formReceival.setRegistPerson(sessionInfo.getUserName()); //登记人
		formReceival.setRegistDepartment(sessionInfo.getDepartmentName()); //登记部门
		formReceival.setReceivalDate(DateTimeUtils.date()); //收文时间
		
		//获取选择项
		DocumentService documentService = (DocumentService)getService("documentService");
		DocumentOption option = documentService.getDocumentOption();
		if(option.getPriority()!=null) { ///紧急程度
			formReceival.setPriority(option.getPriority().split(",")[0]);
		}
		if(option.getSecureLevel()!=null) { //秘密等级
			formReceival.setSecureLevel(option.getSecureLevel().split(",")[0]);
		}
		if(option.getSecureTerm()!=null) { //保密期限
			formReceival.setSecureTerm(option.getSecureTerm().split(",")[0]);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Receival formReceival = (Receival)form;
		//设置内部分发人员
		RecordVisitorList visitors = getRecordControlService().getVisitors(formReceival.getId(), com.yuanluesoft.j2oa.receival.pojo.Receival.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
		    formReceival.setInterSendVisitors(visitors);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.j2oa.receival.pojo.Receival pojoReceival = (com.yuanluesoft.j2oa.receival.pojo.Receival)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			pojoReceival.setRegistPerson(sessionInfo.getUserName()); //登记人
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Receival formReceival = (Receival)form;
		if(request.getParameterValues("interSendVisitors.visitorIds")!=null) { //提交的内容中必须包含内部分发
			getRecordControlService().updateVisitors(pojoReceival.getId(), com.yuanluesoft.j2oa.receival.pojo.Receival.class.getName(), formReceival.getInterSendVisitors(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//生成办理单
		if(record!=null) {
			Receival formReceival = (Receival)form;
			DocumentService documentService = (DocumentService)getService("documentService");
			ReceivalService receivalService = (ReceivalService)getService("receivalService");
			ReceivalTemplateConfig templateConfig = receivalService.getReceivalTemplateConfig();
			String handling = templateConfig==null ? null : documentService.generateHandling(templateConfig.getHandlingHtmlTemplate(), record);
			if(handling!=null && !handling.isEmpty()) {
				formReceival.setHandling(handling);
				formReceival.getTabs().addTab(1, "handling", "办理单", "handling.jsp", true);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
}
