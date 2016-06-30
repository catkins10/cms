package com.yuanluesoft.telex.receive.cryptic.actions.sign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;
import com.yuanluesoft.telex.receive.cryptic.forms.Sign;

/**
 * 
 * @author linchuan
 *
 */
public class ListTodoTelegrams extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.cryptic.actions.sign.SignAction#loadFormResource(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void loadFormResource(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.loadFormResource(form, acl, sessionInfo, request);
		form.setFormTitle("签收/清退");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
        Sign signForm = (Sign)form;
        ReceiveTelegramService receiveTelegramService = (ReceiveTelegramService)getService("receiveTelegramService");
        //获取当前签收人
        TelegramSignPerson signPerson = getCurrentSignPerson(request);
    	//设置用户信息
    	signForm.setSignPersonName(signPerson.getPersonName());
    	signForm.setSignPersonOrgFullName(signPerson.getPersonOrgFullName());
    	signForm.setAgent(signPerson.isAgent()); //是否代理人
        //获取待签收和待清退的电报列表
        signForm.setSignTelegrams(receiveTelegramService.listToSignTelegrams(signPerson, (signForm.getTelegramId()>0 ? signForm.getTelegramId() + "" : null)));
        signForm.setReturnTelegrams(receiveTelegramService.listToReturnTelegrams(signPerson, (signForm.getTelegramId()>0 ? signForm.getTelegramId() + "" : null)));
    }
}