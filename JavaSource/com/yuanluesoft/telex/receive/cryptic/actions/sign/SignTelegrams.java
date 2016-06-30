package com.yuanluesoft.telex.receive.cryptic.actions.sign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;
import com.yuanluesoft.telex.receive.cryptic.forms.Sign;

/**
 * 
 * @author linchuan
 *
 */
public class SignTelegrams extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSubmitAction(mapping, form, request, response, false, null, "complete");
    	if(forward!=null && "complete".equals(forward.getName()) && "true".equals(request.getAttribute("reverifyFingerprint"))) {
    		return null;
    	}
    	return forward;
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.cryptic.actions.sign.SignAction#loadFormResource(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void loadFormResource(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.loadFormResource(form, acl, sessionInfo, request);
		Sign signForm = (Sign)form;
		form.setFormTitle("完成签收");
		form.getFormActions().addFormAction(0, "打印办理单", "window.open('printBill.shtml?selectedTelegramSignIds=" + signForm.getSelectedTelegramSignIds()  + "')", false);
		form.getFormActions().addFormAction(1, "继续签收/清退", "location='listTodoTelegrams.shtml?telegramId=" + signForm.getTelegramId() + "'", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
        Sign signForm = (Sign)form;
        ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
        TelegramSignPerson signPerson = getCurrentSignPerson(request);
        if(signPerson==null) {
        	response.sendRedirect("verifyFingerprint.shtml?telegramId=" + signForm.getTelegramId());
        	request.setAttribute("reverifyFingerprint", "true");
        	return;
        }
    	//设置用户信息
    	signForm.setSignPersonName(signPerson.getPersonName());
    	signForm.setSignPersonOrgFullName(signPerson.getPersonOrgFullName());
    	signForm.setAgent(signPerson.isAgent()); //是否代理人
        List signed = receiveTelegramService.signTelegrams(signPerson, signForm.getSelectedTelegramSignIds(), DateTimeUtils.now(), sessionInfo);
        //设置签收的电报列表
        signForm.setSignTelegrams(signed);
        signForm.setSelectedTelegramSignIds(ListUtils.join(signed, "id", ",", false));
    }
}