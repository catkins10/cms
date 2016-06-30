package com.yuanluesoft.telex.receive.plain.actions.receiveplaintelegram;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.base.service.TelexService;
import com.yuanluesoft.telex.receive.base.pojo.ReceiveTelegram;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;

/**
 * 
 * @author linchuan
 *
 */
public class PrintTransactionSheet extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//会话检查
    	SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        //权限控制
        List acl = getAccessControlService().getAcl("telex/receive/plain", sessionInfo);
		if(!acl.contains("application_manager") && !acl.contains("manageUnit_sign") && !acl.contains("manageUnit_create")) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		com.yuanluesoft.telex.base.forms.PrintTransactionSheet printForm = (com.yuanluesoft.telex.base.forms.PrintTransactionSheet)form;
        //获取电报
		ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
		ReceiveTelegram telegram = receiveTelegramService.getReceiveTelegram(printForm.getTelegramId());
		//生成办理单
		TelexService telexService = (TelexService)getService("telexService");
		printForm.setSheet(telexService.generateTransactionSheet(telegram, printForm.getSheetId()));
    	return mapping.findForward("load");
    }
}