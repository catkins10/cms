package com.yuanluesoft.jeaf.sms.actions.admin.smsunitconfig.smsunitbusiness;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.actions.admin.smsunitconfig.SmsUnitConfigAction;
import com.yuanluesoft.jeaf.sms.forms.admin.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.pojo.SmsBusiness;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitConfig;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SmsUnitBusinessAction extends SmsUnitConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			return super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		}
		catch(PrivilegeException pe) {
			if(record!=null && getOrgService().checkPopedom(((SmsUnitConfig)record).getUnitId(), "manager", sessionInfo)) { //单位管理员
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw pe;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeleteComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeleteComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
		if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && !acl.contains("manageUnit_unitManage")) {
			throw new PrivilegeException();
		}
		SmsService smsService = (SmsService)getService("smsService");
		com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness smsUnitBusiness = (com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)component;
		//检查是否有短信发送记录,如果有不允许删除
		try {
			if(smsService.isSmsSent(smsUnitBusiness.getUnitConfig().getUnitId(), smsUnitBusiness.getBusinessName())) {
				throw new PrivilegeException();
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, char, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		form.setSubForm(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_unitManage") ? "Edit" : "Visit");
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		SmsService smsService = (SmsService)getService("smsService");
		SmsUnitBusiness unitBusinessForm = (SmsUnitBusiness)form;
		//获取短信业务
		SmsBusiness smsBusiness = (SmsBusiness)smsService.load(SmsBusiness.class, unitBusinessForm.getUnitBusiness().getBusinessId());
		unitBusinessForm.getUnitBusiness().setBusinessName(smsBusiness.getName());
		unitBusinessForm.getUnitBusiness().setPostfix(smsBusiness.getPostfix());
		//初始化计费设置
		unitBusinessForm.getUnitBusiness().setChargeMode(smsBusiness.getChargeMode());
		unitBusinessForm.getUnitBusiness().setPrice(smsBusiness.getPrice());
		unitBusinessForm.getUnitBusiness().setDiscount(smsBusiness.getDiscount());
		//检查是否需要配置权限
		com.yuanluesoft.jeaf.sms.model.SmsBusiness smsBusinessDefine = smsService.getSmsBusiness(smsBusiness.getName());
		unitBusinessForm.setSendPopedomConfig(smsBusinessDefine.isSendPopedomConfig());
		unitBusinessForm.setReceivePopedomConfig(smsBusinessDefine.isReceivePopedomConfig());
		unitBusinessForm.getUnitBusiness().setLastModified(DateTimeUtils.now()); //最后修改时间
		unitBusinessForm.getUnitBusiness().setLastModifier(sessionInfo.getUserName()); //最后修改人
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
		RecordControlService recordControlService = getRecordControlService();
		SmsUnitBusiness unitBusinessForm = (SmsUnitBusiness)form;
		unitBusinessForm.setSmsSendEditors(recordControlService.getVisitors(component.getId(), component.getClass().getName(), SmsService.SMS_SEND_EDITOR)); //短信发送编辑
		unitBusinessForm.setSmsSendAuditors(recordControlService.getVisitors(component.getId(), component.getClass().getName(), SmsService.SMS_SEND_AUDITOR)); //短信发送审核
		unitBusinessForm.setSmsReceiveAccepters(recordControlService.getVisitors(component.getId(), component.getClass().getName(), SmsService.SMS_RECEIVE_ACCEPTER)); //短信接收受理
		unitBusinessForm.setSmsReceiveAuditors(recordControlService.getVisitors(component.getId(), component.getClass().getName(), SmsService.SMS_RECEIVE_AUDITOR)); //短信接收审核
		//检查是否需要配置权限
		SmsService smsService = (SmsService)getService("smsService");
		com.yuanluesoft.jeaf.sms.model.SmsBusiness smsBusinessDefine = smsService.getSmsBusiness(unitBusinessForm.getUnitBusiness().getBusinessName());
		unitBusinessForm.setSendPopedomConfig(smsBusinessDefine.isSendPopedomConfig());
		unitBusinessForm.setReceivePopedomConfig(smsBusinessDefine.isReceivePopedomConfig());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness smsUnitBusiness = (com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)component;
		smsUnitBusiness.setLastModified(DateTimeUtils.now()); //最后修改时间
		smsUnitBusiness.setLastModifier(sessionInfo.getUserName()); //最后修改人
		smsUnitBusiness.setLastModifierId(sessionInfo.getUserId()); //最后修改人ID
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
		RecordControlService recordControlService = getRecordControlService();
		SmsUnitBusiness unitBusinessForm = (SmsUnitBusiness)form;
		if(unitBusinessForm.getSmsSendEditors().getVisitorNames()!=null) {
			recordControlService.updateVisitors(component.getId(), component.getClass().getName(), unitBusinessForm.getSmsSendEditors(), SmsService.SMS_SEND_EDITOR); //短信发送编辑
			recordControlService.updateVisitors(component.getId(), component.getClass().getName(), unitBusinessForm.getSmsSendAuditors(), SmsService.SMS_SEND_AUDITOR); //短信发送审核
			recordControlService.updateVisitors(component.getId(), component.getClass().getName(), unitBusinessForm.getSmsReceiveAccepters(), SmsService.SMS_RECEIVE_ACCEPTER); //短信接收受理
			recordControlService.updateVisitors(component.getId(), component.getClass().getName(), unitBusinessForm.getSmsReceiveAuditors(), SmsService.SMS_RECEIVE_AUDITOR); //短信接收审核
		}
	}
}