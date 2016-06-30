package com.yuanluesoft.cms.onlineservice.interactive.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.interactive.forms.OnlineServiceInteractiveForm;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public abstract class OnlineServiceInteractiveAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		OnlineServiceInteractiveForm interactiveForm = (OnlineServiceInteractiveForm)form;
		if(interactiveForm.getItemId()>0) {
			//获取办理事项名称
			OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
			OnlineServiceItem serviceItem = onlineServiceItemService.getOnlineServiceItem(interactiveForm.getItemId());
			if(serviceItem!=null) {
				interactiveForm.setServiceItem(serviceItem);
				interactiveForm.setItemName(serviceItem.getName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			OnlineServiceInteractive interactive = (OnlineServiceInteractive)record;
			//获取办理事项名称
			OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
			interactive.setItemName(onlineServiceItemService.getOnlineServiceItem(interactive.getItemId()).getName());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}