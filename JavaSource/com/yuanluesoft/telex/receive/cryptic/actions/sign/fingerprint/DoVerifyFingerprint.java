package com.yuanluesoft.telex.receive.cryptic.actions.sign.fingerprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.fingerprint.service.FingerprintService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;
import com.yuanluesoft.telex.receive.cryptic.actions.sign.SignAction;
import com.yuanluesoft.telex.receive.cryptic.forms.Sign;

/**
 * 
 * @author linchuan
 *
 */
public class DoVerifyFingerprint extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSubmitAction(mapping, form, request, response, false, null, "listTodoTelegrams");
    	if(forward!=null && "listTodoTelegrams".equals(forward.getName()) && "true".equals(request.getAttribute("failed"))) {
			Sign signForm = (Sign)form;
			signForm.getFormActions().addFormAction(0, "代签收/清退", "location='agent.shtml?telegramId=" + signForm.getTelegramId() + "'", false);
			signForm.getFormActions().addFormAction(1, "重新采集指纹", "history.back()", false);
    		return mapping.findForward("failed");
    	}
    	return forward;
    }
    	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
	    //指纹比对
        FingerprintService fingerprintService = (FingerprintService)getService("fingerprintService");
        long signPersonId = fingerprintService.verifyFingerprint(request.getParameter("fingerprintFeatureData"), request.getRemoteHost(), 0);
        if(signPersonId<=0) { //指纹验证没有通过
        	 request.setAttribute("failed", "true");
        	 return;
        }
        ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
        //认证通过
        try {
        	setCurrentSignPerson(request, receiveTelegramService.getSignPerson(signPersonId));
        }
        catch(Exception e) {
        	//fingerprintService.removeFingerprint(signPersonId); //删除指纹记录
        	request.setAttribute("failed", "true");
        	return;
        }
    }
}