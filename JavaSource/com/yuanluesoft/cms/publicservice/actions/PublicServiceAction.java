package com.yuanluesoft.cms.publicservice.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceForm;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public abstract class PublicServiceAction extends FormAction {
	
	public PublicServiceAction() {
		super();
		anonymousAlways = true; //总是匿名
		unallowableHtmlTags = new String[] {"script"}; //禁用的html标记
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		return new ArrayList();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PublicServiceForm publicServiceForm = (PublicServiceForm)form;
		publicServiceForm.setCreated(DateTimeUtils.now());
		publicServiceForm.setIsPublic('1'); //默认设置为允许公开
		//生成编号
		if(publicServiceForm.getSn()==null && !RequestUtils.getRequestInfo(request).isClientRequest()) { //编号为空,且不是客户端请求
			PublicService publicService = (PublicService)getService("publicService");
			publicServiceForm.setSn(publicService.getSN());
		}
		//性别
		if(publicServiceForm.getCreatorSex()==0) {
			publicServiceForm.setCreatorSex('M');
		}
		//证件名称
		if(publicServiceForm.getCreatorCertificateName()==null) {
			publicServiceForm.setCreatorCertificateName("身份证");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(record==null) {
			return;
		}
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getOpenMode(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getOpenMode(ActionForm form, HttpServletRequest request) {
		if(request.getParameter("approvalQuerySN")!=null) {
			return OPEN_MODE_OPEN; //自动设置为open方式
		}
		return super.getOpenMode(form, request);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		PublicServiceForm publicServiceForm = (PublicServiceForm)form;
		PublicService publicService = (PublicService)getService("publicService");
		com.yuanluesoft.cms.publicservice.pojo.PublicService pojoPublicService;
		if(request.getParameter("approvalQuerySN")!=null) {
			//按approvalQuerySN和approvalQueryPassword获取POJO
			pojoPublicService = publicService.loadPublicService(publicServiceForm.getFormDefine().getRecordClassName(), request.getParameter("approvalQuerySN"), request.getParameter("approvalQueryPassword"));
			if(pojoPublicService!=null) {
				if(pojoPublicService.getIsDeleted()==1) {
					return null;
				}
				publicQueryResult(pojoPublicService);
			}
			return pojoPublicService;
		}
		//按ID获取
		pojoPublicService = (com.yuanluesoft.cms.publicservice.pojo.PublicService)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(pojoPublicService==null || pojoPublicService.getIsDeleted()==1) {
			return null;
		}
		if(request.getAttribute("submit")!=null) { //提交操作
			return pojoPublicService;
		}
		
		//检查客户端ID是否匹配
		String clientDeviceId = request.getParameter("client.deviceId");
		if(clientDeviceId!=null && !clientDeviceId.isEmpty() && clientDeviceId.equals(pojoPublicService.getClientDeviceId())) {
			publicQueryResult(pojoPublicService);
			return pojoPublicService;
		}
		
		//检查密码
		String password = request.getParameter("approvalQueryPassword");
		if(password==null) {
			password = publicServiceForm.getQueryPassword();
		}
		if(password!=null) {
			if(!password.equals(pojoPublicService.getQueryPassword())) { //密码输入错误
				publicServiceForm.setError("密码输入错误");
				return pojoPublicService;
			}
			else {
				publicQueryResult(pojoPublicService);
				return pojoPublicService;
			}
		}
		if(pojoPublicService.getPublicPass()!='1') { //未被允许公开
			throw new PrivilegeException();
		}
		pojoPublicService.setQueryPassword(null);
		return pojoPublicService;
	}
	
	/**
	 * 公开查询结果
	 * @param pojoPublicService
	 * @throws Exception
	 */
	protected void publicQueryResult(com.yuanluesoft.cms.publicservice.pojo.PublicService pojoPublicService) throws Exception {
		pojoPublicService.setPublicPass('1');
		pojoPublicService.setPublicBody('1');
		pojoPublicService.setPublicWorkflow('1');
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		forceValidateCode = true; //强制验证码校验
		if(RequestUtils.getRequestInfo(request).isClientRequest()) { //客户端请求
			forceValidateCode = false;
		}
		else {
			BusinessService businessService = getBusinessService(((ActionForm)formToValidate).getFormDefine());
			if(businessService instanceof PublicService) {
				forceValidateCode = ((PublicService)businessService).isForceValidateCode();
			}
		}
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			com.yuanluesoft.cms.publicservice.pojo.PublicService publicServicePojo = (com.yuanluesoft.cms.publicservice.pojo.PublicService)record;
			//记录IP
			publicServicePojo.setCreatorIP(RequestUtils.getRemoteAddress(request) + ":" + RequestUtils.getRemotePort(request));
			//记录时间
			publicServicePojo.setCreated(DateTimeUtils.now());
			if(RequestUtils.getRequestInfo(request).isClientRequest() && "htmleditor".equals(FieldUtils.getFormField(form.getFormDefine(), "content", request).getInputMode())) { //客户端,且输入方式是html编辑器
				publicServicePojo.setContent(StringUtils.generateHtmlContent(publicServicePojo.getContent()));
			}
			publicServicePojo.setClientDeviceId(request.getParameter("client.deviceId"));
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/**
	 * 执行提交操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param reload
	 * @param message
	 * @param forwardName
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSubmitAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("submit", "1");
    	return executeSaveAction(mapping, form, request, response, false, null, null, "result");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//允许新建,不允许修改
		if(OPEN_MODE_CREATE.equals(openMode) || (record!=null && ((com.yuanluesoft.cms.publicservice.pojo.PublicService)record).getCreated()==null)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException(); //不允许删除
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getWaterMark(com.yuanluesoft.jeaf.attachmentmanage.model.AttachmentConfig, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		//PublicServiceForm publicServiceForm = (PublicServiceForm)form;
		//return ((SiteService)getService("siteService")).getWaterMark(publicServiceForm.getSiteId());
		return null;
	}
}