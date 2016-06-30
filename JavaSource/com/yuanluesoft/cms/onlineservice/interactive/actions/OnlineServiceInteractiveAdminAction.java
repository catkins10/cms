package com.yuanluesoft.cms.onlineservice.interactive.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.interactive.forms.OnlineServiceInteractiveForm;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemTransactor;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public abstract class OnlineServiceInteractiveAdminAction extends PublicServiceAdminAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		OnlineServiceInteractiveForm interactiveForm = (OnlineServiceInteractiveForm)form;
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		//获取办理事项名称
		OnlineServiceItem serviceItem = onlineServiceItemService.getOnlineServiceItem(interactiveForm.getItemId());
		interactiveForm.setServiceItem(serviceItem);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		OnlineServiceInteractiveForm interactiveForm = (OnlineServiceInteractiveForm)workflowForm;
		
		List participants = null;
		if("transactor".equals(programmingParticipantId)) { //经办
			participants = onlineServiceItemService.listServiceItemTransactors(interactiveForm.getItemId());
		}
		else if("manager".equals(programmingParticipantId)) { //管理员
			participants = onlineServiceItemService.listServiceItemManagers(interactiveForm.getItemId());
		}
		if(participants==null || participants.isEmpty()) {
			return null;
		}
		
		//转换为办理人
		for(int i=0; i<participants.size(); i++) {
			Object participant = participants.get(i);
			if(participant instanceof OnlineServiceItemTransactor) {
				OnlineServiceItemTransactor transactor = (OnlineServiceItemTransactor)participant;
				participants.set(i, new Element("" + transactor.getUserId(), transactor.getUserName()));
			}
			else {
				DirectoryPopedom popedom  = (DirectoryPopedom)participant;
				participants.set(i, new Element("" + popedom.getUserId(), popedom.getUserName()));
			}
		}
		return participants;
	}
}