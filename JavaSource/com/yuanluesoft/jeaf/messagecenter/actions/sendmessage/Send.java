package com.yuanluesoft.jeaf.messagecenter.actions.sendmessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.messagecenter.forms.SendMessage;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Send extends SendMessageAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSubmitAction(mapping, form, request, response, true, "发送完成", null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SendMessage sendMessageForm = (SendMessage)form;
		//发送消息
		MessageService messageService = (MessageService)getService("messageService");
		String[] ids = sendMessageForm.getReceivePersonIds().split(",");
		for(int i=0; i<ids.length; i++) {
			messageService.sendMessageToPerson(Long.parseLong(ids[i]), sendMessageForm.getContent(), sessionInfo.getUserId(), sessionInfo.getUserName(), sendMessageForm.getPriority(), UUIDLongGenerator.generateId(), sendMessageForm.getWebLink(), sendMessageForm.getSendTime(), sendMessageForm.getBindSendMode(), null, 0, null);
		}
	}
}