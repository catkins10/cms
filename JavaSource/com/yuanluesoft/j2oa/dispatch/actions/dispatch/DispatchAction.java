/*
 * Created on 2005-9-9
 *
 */
package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.dispatch.forms.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchBody;
import com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService;
import com.yuanluesoft.j2oa.document.pojo.DocumentOption;
import com.yuanluesoft.j2oa.document.service.DocumentService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 *
 * @author linchuan
 *
 */
public class DispatchAction extends	WorkflowAction {

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
		Dispatch formDispatch = (Dispatch)form;
		formDispatch.setDraftPerson(sessionInfo.getUserName()); //起草人
		formDispatch.setDraftDepartment(sessionInfo.getDepartmentName()); //起草部门
		formDispatch.setDraftDate(new Timestamp(System.currentTimeMillis())); //起草时间
		if(formDispatch.getPublicType()==null) {
			formDispatch.setPublicType("主动公开");
		}
		
		//获取选择项
		DocumentService documentService = (DocumentService)getService("documentService");
		DocumentOption option = documentService.getDocumentOption();
		if(option.getPriority()!=null) { ///紧急程度
			formDispatch.setPriority(option.getPriority().split(",")[0]);
		}
		if(option.getSecureLevel()!=null) { //秘密等级
			formDispatch.setSecureLevel(option.getSecureLevel().split(",")[0]);
		}
		if(option.getSecureTerm()!=null) { //保密期限
			formDispatch.setSecureTerm(option.getSecureTerm().split(",")[0]);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Dispatch formDispatch = (Dispatch)form;
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch pojoDispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
		//设置正文
		if(pojoDispatch.getBodies()!=null && !pojoDispatch.getBodies().isEmpty()) {
		    DispatchBody dispatchBody = (DispatchBody)pojoDispatch.getBodies().iterator().next();
			formDispatch.setHtmlBody(dispatchBody.getHtmlBody());
		}
		//设置内部分发人员
		RecordVisitorList visitors = getRecordControlService().getVisitors(formDispatch.getId(), com.yuanluesoft.j2oa.dispatch.pojo.Dispatch.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
		    formDispatch.setInterSendVisitors(visitors);
		}
		//设置印发时间,用于生成正式文件
		if(formDispatch.getDistributeDate()==null &&
		   (ListUtils.findObjectByProperty(formDispatch.getFormActions(), "title", "生成正式文件")!=null ||
		    ListUtils.findObjectByProperty(formDispatch.getFormActions(), "title", "分发")!=null)) {
			formDispatch.setDistributeDate(DateTimeUtils.date());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generatePojo(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record generateRecord(ActionForm form, String openMode,HttpServletRequest request, SessionInfo sessionInfo)	throws Exception {
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch pojoDispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)super.generateRecord(form, openMode, request, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			pojoDispatch.setDraftPerson(sessionInfo.getUserName()); //起草人
			pojoDispatch.setDraftDate(new Timestamp(System.currentTimeMillis())); //起草时间
			pojoDispatch.setMarkYear(Calendar.getInstance().get(Calendar.YEAR)); //文件字年度
		}
		return pojoDispatch;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.String, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch dispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
		if(!"不公开".equals(dispatch.getPublicType())) {
			dispatch.setPublicReason(null);
		}
		else if(dispatch.getPublicReason()==null || dispatch.getPublicReason().isEmpty()) { //不公开,要求输入理由
			Dispatch formDispatch = (Dispatch)form;
			formDispatch.setError("不公开理由不能为空");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		com.yuanluesoft.j2oa.dispatch.pojo.Dispatch pojoDispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
		Dispatch formDispatch = (Dispatch)form;
		if(request.getParameterValues("interSendVisitors.visitorIds")!=null) { //提交的内容中包含内部分发
			getRecordControlService().updateVisitors(pojoDispatch.getId(), com.yuanluesoft.j2oa.dispatch.pojo.Dispatch.class.getName(), formDispatch.getInterSendVisitors(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Dispatch formDispatch = (Dispatch)form;
		//设置TAB列表
		formDispatch.getTabs().addTab(1, "body", "正文", "dispatchBody" + (accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE ? "Read" : "Edit") + ".jsp", false);
		FormAction formAction = (FormAction)ListUtils.findObjectByProperty(formDispatch.getFormActions(), "title", "分发并发送");
		if(formAction!=null) {
			formAction.setTitle("分发");
		}
		//生成办理单
		if(record!=null) {
			DocumentService documentService = (DocumentService)getService("documentService");
			DispatchTemplateService dispatchTemplateService = (DispatchTemplateService)getService("dispatchTemplateService");
			com.yuanluesoft.j2oa.dispatch.pojo.Dispatch dispatch = (com.yuanluesoft.j2oa.dispatch.pojo.Dispatch)record;
			//获取模板
			String handling = dispatchTemplateService.getHandlingTemplate(dispatch.getDocType(), dispatch.getDocMark());
			//生成办理单
			handling = documentService.generateHandling(handling, dispatch);
			if(handling!=null && !handling.isEmpty()) {
				formDispatch.setHandling(handling);
				formDispatch.getTabs().addTab(1, "handling", "办理单", "handling.jsp", true);
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