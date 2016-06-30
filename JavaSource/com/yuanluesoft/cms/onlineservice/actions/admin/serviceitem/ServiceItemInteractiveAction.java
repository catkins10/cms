package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemInteractive;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceMainDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceOpinion;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemInteractiveAction extends ServiceItemAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char acceccLevel = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		if(acceccLevel>RecordControlService.ACCESS_LEVEL_READONLY && component!=null) {
			OnlineServiceInteractive interactive = (OnlineServiceInteractive)component;
			if(interactive.getWorkflowInstanceId()!=null && !interactive.getWorkflowInstanceId().isEmpty()) { //网上用户提交的
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return acceccLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		ServiceItemInteractive interactiveForm = (ServiceItemInteractive)form;
		interactiveForm.getInteractiveRecord().setPublicBody('1');
		interactiveForm.getInteractiveRecord().setPublicWorkflow('1');
		interactiveForm.getInteractiveRecord().setCreated(DateTimeUtils.now());
		interactiveForm.getOpinion().setPersonName(sessionInfo.getUserName());
		interactiveForm.getOpinion().setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
		ServiceItemInteractive interactiveForm = (ServiceItemInteractive)form;
		//获取必填的办理意见类型,默认"部门办理"意见
		Field requiredOpinionField = getRequiredOpinionField(interactiveForm);
		if(requiredOpinionField!=null) {
			OnlineServiceInteractive interactive = (OnlineServiceInteractive)component;
			PublicServiceOpinion opinion = (PublicServiceOpinion)ListUtils.findObjectByProperty(interactive.getOpinions(), "opinionType", requiredOpinionField.getName());
			if(opinion!=null) {
				interactiveForm.setOpinion(opinion);
			}
		}
	}
	
	/**
	 * 获取必填字段
	 * @return
	 */
	private Field getRequiredOpinionField(ServiceItemInteractive interactiveForm) {
		List opinionFields = FieldUtils.listRecordFields(interactiveForm.getInteractiveRecord().getClass().getName(), "opinion", null, null, null, true, false, false, false, 1);
		return (Field)ListUtils.findObjectByProperty(opinionFields, "required", new Boolean(true));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		OnlineServiceItem serviceItem = (OnlineServiceItem)mainRecord;
		OnlineServiceInteractive interactive = (OnlineServiceInteractive)component;
		interactive.setItemName(serviceItem.getName());
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			interactive.setCreatorCertificateName("身份证");
			interactive.setPublicPass('1');
			//设置隶属站点
			OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
			OnlineServiceMainDirectory mainDirectory = onlineServiceDirectoryService.getMainDirectory(((OnlineServiceItemSubjection)serviceItem.getSubjections().iterator().next()).getDirectoryId());
			interactive.setSiteId(mainDirectory.getSiteId());
		}
		if(interactive.getCreated()==null || interactive.getCreated().after(DateTimeUtils.now())) {
			interactive.setCreated(DateTimeUtils.now());
		}
		//设置编号
		if(interactive.getSn()==null || interactive.getSn().isEmpty()) {
			String date = DateTimeUtils.formatTimestamp(interactive.getCreated(), "yyyyMMdd");
			if(date.equals(DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyyMMdd"))) { //今天
				PublicService publicService = (PublicService)getService("publicService");
				interactive.setSn(publicService.getSN());
			}
			else { //其它时间
				String hql = "select Max(PublicService.sn)" +
							 " from PublicService PublicService" +
							 " where PublicService.sn like '" + date + "%'";
				DatabaseService databaseService = (DatabaseService)getService("databaseService");
				String sn = (String)databaseService.findRecordByHql(hql);
				int sequence = 1;
				if(sn!=null && !sn.isEmpty()) {
					try {
						sequence = Integer.parseInt(sn.substring(date.length())) + 1;
					}
					catch(Exception e) {
						
					}
				}
				interactive.setSn(date + StringUtils.formatNumber(sequence, 5, true));
			}
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
		//添加权限记录
		RecordControlService recordControlService = (RecordControlService)getService("recordControlService");
		recordControlService.appendVisitor(interactive.getId(), interactive.getClass().getName(), 0, RecordControlService.ACCESS_LEVEL_READONLY);
		
		//保存办理意见
		ServiceItemInteractive interactiveForm = (ServiceItemInteractive)form;
		if(interactiveForm.getOpinion().getCreated()==null || interactiveForm.getOpinion().getCreated().after(DateTimeUtils.now())) {
			interactiveForm.getOpinion().setCreated(DateTimeUtils.now());
		}
		Field requiredOpinionField = getRequiredOpinionField(interactiveForm);
		if(requiredOpinionField!=null && interactiveForm.getOpinion().getOpinion()!=null && !interactiveForm.getOpinion().getOpinion().isEmpty()) {
			PublicServiceOpinion opinion = (PublicServiceOpinion)ListUtils.findObjectByProperty(interactive.getOpinions(), "id", new Long(interactiveForm.getOpinion().getId()));
			OpinionService opinionService = (OpinionService)getService("opinionService");
			if(opinion==null) {
				opinionService.saveOpinion(interactive.getClass().getName(), 0, interactive.getId(), interactiveForm.getOpinion().getOpinion(), requiredOpinionField.getName(), 0, interactiveForm.getOpinion().getPersonName(), 0, null, null, null, null, interactiveForm.getOpinion().getCreated());
			}
			else {
				opinionService.saveOpinion(interactive.getClass().getName(), opinion.getId(), interactive.getId(), interactiveForm.getOpinion().getOpinion(), requiredOpinionField.getName(), opinion.getPersonId(), interactiveForm.getOpinion().getPersonName(), opinion.getAgentId(), opinion.getAgentName(), opinion.getActivityId(), opinion.getActivityName(), opinion.getWorkItemId(), interactiveForm.getOpinion().getCreated());
			}
		}
	}
}