package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.AcceptSerialNumberConfig;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService;
import com.yuanluesoft.cms.onlineservice.interactive.forms.OnlineServiceInteractiveForm;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.resource.ProgrammingParticipant;


/**
 * 
 * @author linchuan
 *
 */
public class AcceptAction extends com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept.admin.AcceptAction {

	public AcceptAction() {
		super();
		alwaysAutoSend = true; //自动发送
		externalAction = true;
	}

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
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Accept acceptForm = (Accept)form;
		form.getTabs().removeTab("guide");
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			if(acceptForm.getStep()==null || acceptForm.getStep().isEmpty() || "new".equals(acceptForm.getStep())) { //填写基本信息
				acceptForm.setStep("new");
				form.getTabs().removeTab("material");
				form.getFormActions().removeFormAction("提交");
			}
			else {
				form.getFormActions().removeFormAction("下一步");
				form.getTabs().setTabSelected("material");
			}
			form.getTabs().removeTab("workflowLog"); //不显示流程记录
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#resetParticipants(java.util.List, boolean, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List resetParticipants(List participants, boolean anyoneParticipate, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(anyoneParticipate) { //任何人都可办理,把当前事项的经办人作为流程办理人
			participants = new ArrayList();
			participants.add(new ProgrammingParticipant("transactor", "网上办事经办"));
		}
		return participants;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		final Accept acceptForm = (Accept)form;
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Member member = memberServiceList.getMember(sessionInfo.getUserId());
		acceptForm.setMemberId(sessionInfo.getUserId());
		acceptForm.setMember(member.getCompany());
		acceptForm.setCreator(sessionInfo.getUserName()); //申报人
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
		
		if(acceptForm.getCreatorCertificateName()==null) {
			acceptForm.setCreatorCertificateName(member.getIdentityCardName()==null || member.getIdentityCardName().isEmpty() ? "营业执照" : member.getIdentityCardName()); //证件名称
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
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			OnlineServiceInteractive interactive = (OnlineServiceInteractive)record;
			interactive.setCreatorId(sessionInfo.getUserId()); //申报人ID
			interactive.setCreator(sessionInfo.getUserName()); //申报人
			interactive.setCreated(DateTimeUtils.now()); //申报时间
			interactive.setLoginName(sessionInfo.getLoginName()); //登录用户名
			//获取办理事项名称
			OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
			interactive.setItemName(onlineServiceItemService.getOnlineServiceItem(interactive.getItemId()).getName());
			interactive.setCreatorIP(request.getRemoteHost());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		OnlineServiceInteractiveForm interactiveForm = (OnlineServiceInteractiveForm)form;
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		if(interactiveForm.getItemId()==0 && interactiveForm.getId()>0) {
			OnlineServiceAcceptService onlineServiceAcceptService = (OnlineServiceAcceptService)getService("onlineServiceAcceptService");
			try {
				interactiveForm.setItemId(((OnlineServiceAccept)onlineServiceAcceptService.load(OnlineServiceAccept.class, interactiveForm.getId())).getItemId());
			}
			catch (ServiceException e) {
				Logger.exception(e);
			}
		}
		return new Link(Environment.getWebApplicationSafeUrl() +  "/cms/onlineservice/accept/login.shtml?itemId=" + interactiveForm.getItemId() + (siteId>0 ? "&siteId=" + siteId : "") + "&templateName=onlineservice", "utf-8");
	}
}