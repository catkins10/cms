package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.AcceptSerialNumberConfig;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAdminAction;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class AcceptAction extends OnlineServiceInteractiveAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runAccept";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//form.getTabs().addTab(1, "guide", "办事指南", null, false);
		form.getTabs().addTab(1, "material", "材料申报", null, false);
		form.getTabs().getTab("basic").setJspFile(null);
		
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		final Accept acceptForm = (Accept)form;
		acceptForm.setAgentorId(sessionInfo.getUserId());
		acceptForm.setAgentor(sessionInfo.getUserName()); //申报人
		acceptForm.setCreated(DateTimeUtils.now()); //申报时间
		if(acceptForm.getIsPublic()==0) {
			acceptForm.setIsPublic('1'); //设置为允许公开
		}
		//获取办理事项名称
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		OnlineServiceItem serviceItem = onlineServiceItemService.getOnlineServiceItem(acceptForm.getItemId());
		if(serviceItem!=null){
			acceptForm.setServiceItem(serviceItem);
			acceptForm.setItemName(serviceItem.getName());
		}
		//生成编号
		if(acceptForm.getSn()==null&&acceptForm.getItemName()!=null) {
			

			DatabaseService databaseService = (DatabaseService)getService("databaseService");
			AcceptSerialNumberConfig acceptSerialNumberConfig=(AcceptSerialNumberConfig) databaseService.findRecordByHql("from AcceptSerialNumberConfig  AcceptSerialNumberConfig");
		    if(acceptSerialNumberConfig!=null&&acceptSerialNumberConfig.getContent()!=null&&acceptSerialNumberConfig.getContent().length()!=0){
		    	NumerationCallback numerationCallback = new NumerationCallback() {
					public Object getFieldValue(String fieldName, int fieldLength) {
						if("办事事项".equals(fieldName)) {
							return acceptForm.getItemName();
						}else {
							return null;
						}
					}
				};
				NumerationService numerationService=(NumerationService)getService("numerationService");
				String serialNumber=numerationService.generateNumeration("办事受理", "受理编号", acceptSerialNumberConfig.getContent(), false, numerationCallback);
				serialNumber.trim();
				acceptForm.setSn(serialNumber);
		    }else{
		    	PublicService publicService = (PublicService)getService("publicService");
		    	acceptForm.setSn(publicService.getSN());
		    }
			
		}
		//设置受理件数
		acceptForm.setCaseNumber(1);
		//设置手机号码等
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Member member = memberServiceList.getMember(sessionInfo.getUserId());
		if(acceptForm.getCreatorCertificateName()==null) {
			acceptForm.setCreatorCertificateName(member.getIdentityCardName()==null || member.getIdentityCardName().isEmpty() ? "身份证" : member.getIdentityCardName()); //证件名称
		}
		if(acceptForm.getCreatorIdentityCard()==null) {
			acceptForm.setCreatorIdentityCard(member.getIdentityCard()); //证件号码
		}
		if(acceptForm.getCreatorTel()==null) {
			acceptForm.setCreatorMobile(member.getMobile()); //手机
		}
		if(acceptForm.getCreatorTel()==null) {
			acceptForm.setCreatorTel(member.getTel()); //联系电话
		}
		if(acceptForm.getCreatorMail()==null) {
			acceptForm.setCreatorMail(member.getMailAddress()); //邮箱
		}
		if(acceptForm.getCreatorUnit()==null) {
			acceptForm.setCreatorUnit(member.getCompany()); //所在单位
		}
		if(acceptForm.getCreatorFax()==null) {
			acceptForm.setCreatorFax(member.getFax()); //传真
		}
		if(acceptForm.getCreatorAddress()==null) {
			acceptForm.setCreatorAddress(member.getAddress()); //地址
		}
		if(acceptForm.getCreatorPostalcode()==null) {
			acceptForm.setCreatorPostalcode(member.getPostalcode()); //邮编
		}
	}
	
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		final OnlineServiceInteractive interactive = (OnlineServiceInteractive)record;
		OnlineServiceAccept accept = (OnlineServiceAccept)record;
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Member member = memberServiceList.getMember(accept.getMemberId());
		if(OPEN_MODE_CREATE.equals(openMode)) {
			accept.setCreatorId(sessionInfo.getUserId());
			accept.setCreator(sessionInfo.getUserName());
			interactive.setCreated(DateTimeUtils.now()); //申报时间
			interactive.setLoginName(sessionInfo.getLoginName()); //登录用户名
		}
		//获取办理事项名称
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		interactive.setItemName(onlineServiceItemService.getOnlineServiceItem(interactive.getItemId()).getName());
		interactive.setCreatorIP(request.getRemoteHost());
//		生成编号
		if(interactive.getSn()==null || interactive.getSn().equals("")) {
			DatabaseService databaseService = (DatabaseService)getService("databaseService");
			AcceptSerialNumberConfig acceptSerialNumberConfig=(AcceptSerialNumberConfig) databaseService.findRecordByHql("from AcceptSerialNumberConfig  AcceptSerialNumberConfig");
		    if(acceptSerialNumberConfig!=null&&acceptSerialNumberConfig.getContent()!=null&&acceptSerialNumberConfig.getContent().length()!=0){
		    	NumerationCallback numerationCallback = new NumerationCallback() {
					public Object getFieldValue(String fieldName, int fieldLength) {
						if("办事事项".equals(fieldName)) {
							return interactive.getItemName();
						}else {
							return null;
						}
					}
				};
				NumerationService numerationService=(NumerationService)getService("numerationService");
				String serialNumber=numerationService.generateNumeration("办事受理", "受理编号", acceptSerialNumberConfig.getContent(), false, numerationCallback);
				serialNumber.trim();
				interactive.setSn(serialNumber);
		    }else{
		    	PublicService publicService = (PublicService)getService("publicService");
		    	interactive.setSn(publicService.getSN());
		    }
			
		}
		
//		设置手机号码等
		
		if(accept.getCreatorCertificateName()==null) {
			accept.setCreatorCertificateName(member.getIdentityCardName()==null || member.getIdentityCardName().isEmpty() ? "营业执照" : member.getIdentityCardName()); //证件名称
		}
		if(accept.getCreatorIdentityCard()==null) {
			accept.setCreatorIdentityCard(member.getIdentityCard()); //证件号码
		}
		if(accept.getCreatorTel()==null) {
			accept.setCreatorMobile(member.getMobile()); //手机
		}
		if(accept.getCreatorTel()==null) {
			accept.setCreatorTel(member.getTel()); //联系电话
		}
		if(accept.getCreatorMail()==null) {
			accept.setCreatorMail(member.getMailAddress()); //邮箱
		}
		if(accept.getCreatorUnit()==null) {
			accept.setCreatorUnit(member.getCompany()); //所在单位
		}
		if(interactive.getCreatorFax()==null) {
			interactive.setCreatorFax(member.getFax()); //传真
		}
		if(accept.getCreatorAddress()==null) {
			accept.setCreatorAddress(member.getAddress()); //地址
		}
		if(accept.getCreatorPostalcode()==null) {
			accept.setCreatorPostalcode(member.getPostalcode()); //邮编
		}
		
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		OnlineServiceAccept accept = (OnlineServiceAccept)record;
		if(workflowInstanceWillComplete && accept!=null && accept.getCaseAccept()=='1') {
			accept.setCaseCompleteTime(DateTimeUtils.now());
		}
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}