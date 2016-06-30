package com.yuanluesoft.telex.receive.cryptic.actions.receivecryptictelegram;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.actions.receivetelegram.ReceiveTelegramAction;

/**
 * 
 * @author linchuan
 *
 */
public class ReceiveCrypticTelegramAction extends ReceiveTelegramAction {

	public ReceiveCrypticTelegramAction() {
		super();
		isCryptic = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.actions.receivetelegram.ReceiveTelegramAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram telegramForm = (com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram)form;
		telegramForm.setNeedReturn('1');
	}
}