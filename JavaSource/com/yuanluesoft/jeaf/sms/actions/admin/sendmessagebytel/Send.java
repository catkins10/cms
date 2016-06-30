package com.yuanluesoft.jeaf.sms.actions.admin.sendmessagebytel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.forms.admin.SendMessageByTel;
import com.yuanluesoft.jeaf.sms.service.SmsService;

/**
 * 
 * @author linchuan
 *
 */
public class Send extends SendMessageByTelAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSubmitAction(mapping, form, request, response, true, "发送完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SmsService smsService = (SmsService)getService("smsService");
		SendMessageByTel sendForm = (SendMessageByTel)form;
		smsService.sendShortMessage(sessionInfo.getUserId(), sessionInfo.getUserName(), sessionInfo.getUnitId(), "系统消息", null, sendForm.getReceiverNumbers(), sendForm.getMessage(), sendForm.getSendTime(), -1, false, null, true);
	}
}