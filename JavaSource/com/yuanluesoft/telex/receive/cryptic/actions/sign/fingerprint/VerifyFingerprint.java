package com.yuanluesoft.telex.receive.cryptic.actions.sign.fingerprint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.cryptic.actions.sign.SignAction;

/**
 * 
 * @author linchuan
 *
 */
public class VerifyFingerprint extends SignAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#loadFormResource(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void loadFormResource(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.loadFormResource(form, acl, sessionInfo, request);
		form.setFormTitle("指纹认证");
	}
}