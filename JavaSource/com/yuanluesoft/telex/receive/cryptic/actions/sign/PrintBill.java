package com.yuanluesoft.telex.receive.cryptic.actions.sign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSign;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;
import com.yuanluesoft.telex.receive.cryptic.forms.Sign;

/**
 * 
 * @author linchuan
 *
 */
public class PrintBill extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		//获取电报列表
        Sign signForm = (Sign)form;
        ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
        List telegrams = receiveTelegramService.listTelegrams(signForm.getSelectedTelegramSignIds());
        //设置签收人信息
        if(telegrams!=null && !telegrams.isEmpty()) {
            TelegramSignPerson signPerson;
        	TelegramSign telegramSign = (TelegramSign)telegrams.get(0);
        	if(telegramSign.getReturnTime()==null) { //未清退
        		signForm.setSignTelegrams(telegrams);
        		signPerson = receiveTelegramService.getSignPerson(telegramSign.getSignPersonId());
        	}
        	else { //已清退
        		signForm.setReturnTelegrams(telegrams);
        		signPerson = receiveTelegramService.getSignPerson(telegramSign.getReturnPersonId());
        	}
        	signForm.setPrintTime(DateTimeUtils.now()); //打印时间
        	//设置用户信息
        	signForm.setSignPersonName(signPerson.getPersonName());
        	String orgName = signPerson.getPersonOrgFullName();
        	signForm.setSignPersonOrgFullName(orgName.substring(orgName.lastIndexOf('/') + 1));
        	signForm.setAgent(signPerson.isAgent()); //是否代理人
        }
    }
}