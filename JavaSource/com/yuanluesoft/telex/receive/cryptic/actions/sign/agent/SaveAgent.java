package com.yuanluesoft.telex.receive.cryptic.actions.sign.agent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignAgent;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;
import com.yuanluesoft.telex.receive.cryptic.actions.sign.SignAction;
import com.yuanluesoft.telex.receive.cryptic.forms.Sign;

/**
 * 
 * @author linchuan
 *
 */
public class SaveAgent extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSubmitAction(mapping, form, request, response, false, null, "listTodoTelegrams");
    }
    
        /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		Sign signForm = (Sign)form;
        ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
        //保存代理人信息
        TelegramSignAgent signAgent = receiveTelegramService.saveSignAgent(signForm.getAgentName(), signForm.getAgentOrgId(), signForm.getAgentOrgName(), signForm.getAgentCertificateName(), signForm.getAgentCertificateCode(), signForm.getAgentSex(), signForm.getTemplate(), sessionInfo);
        //设置为当前签收人
        setCurrentSignPerson(request, receiveTelegramService.getSignPerson(signAgent.getId()));
    }
}