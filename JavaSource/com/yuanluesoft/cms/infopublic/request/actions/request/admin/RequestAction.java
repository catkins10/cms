/**
 * 
 */
package com.yuanluesoft.cms.infopublic.request.actions.request.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yuanluesoft.cms.infopublic.request.forms.admin.Request;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 *
 * @author LinChuan
 *
 */
public class RequestAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runRequest";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			SiteService siteService = (SiteService)getService("siteService");
			Request requestForm = (Request)form;
			if(requestForm.getSiteName()==null || requestForm.getSiteName().isEmpty()) {
				try {
					if(siteService.getFirstManagedSite(sessionInfo)!=null || siteService.getFirstEditabledSite(sessionInfo)!=null) {
						return RecordControlService.ACCESS_LEVEL_EDITABLE;
					}
				}
				catch (ServiceException e) {
					
				}
			}
			else if(siteService.checkPopedom(requestForm.getSiteId(), "manager,editor", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("requestRegist.jsp");
		}
		PublicRequest publicRequest = (PublicRequest)record;
		if(publicRequest!=null && publicRequest.getNotify()!=null && !publicRequest.getNotify().isEmpty()) {
			Request requestForm = (Request)form;
			requestForm.getTabs().addTab(1, "notify", "告知书", "notify.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Request requestForm = (Request)form;
		if(requestForm.getCreated()==null) {
			requestForm.setCreated(DateTimeUtils.now());
		}
		requestForm.setIsPublic('1'); //默认设置为允许公开
		//生成编号
		PublicService publicService = (PublicService)getService("publicService");
		if(requestForm.getSn()==null) {
			requestForm.setSn(publicService.getSN());
		}
		//性别
		requestForm.setCreatorSex('M');
		//证件名称
		if(requestForm.getCreatorCertificateName()==null) {
			requestForm.setCreatorCertificateName("身份证");
		}
		//申请人类型
		if(requestForm.getProposerType()<'1') {
			requestForm.setProposerType('1');
		}
		//登记人
		requestForm.setRegistrant(sessionInfo.getUserName());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Request requestForm = (Request)form;
		if(requestForm.getMedium()!=null) {
			requestForm.setMediums(requestForm.getMedium().split(","));
		}
		if(requestForm.getReceiveMode()!=null) {
			requestForm.setReceiveModes(requestForm.getReceiveMode().split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generatePojo(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PublicRequest publicRequest = (PublicRequest)super.generateRecord(form, openMode, request, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			Request requestForm = (Request)form;
			publicRequest.setMedium(requestForm.getMediums()==null ? null : StringUtils.join(requestForm.getMediums(), ","));
			publicRequest.setReceiveMode(requestForm.getReceiveModes()==null ? null : StringUtils.join(requestForm.getReceiveModes(), ","));
			publicRequest.setRegistrantId(sessionInfo.getUserId()); //登记人ID
			publicRequest.setRegistrant(sessionInfo.getUserName()); //登记人
		}
		return publicRequest;
	}
}