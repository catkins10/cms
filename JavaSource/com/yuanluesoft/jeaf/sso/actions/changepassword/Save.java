package com.yuanluesoft.jeaf.sso.actions.changepassword;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sso.service.SsoSessionService;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends ChangePasswordAction {
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	anonymousAlways = request.getSession().getAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME)!=null;
    	return executeSubmitAction(mapping, form, request, response, false, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#generateScriptAfterSubmit()
	 */
	protected String generateScriptAfterSubmit() {
		return "var callback = DialogUtils.getDialogArguments();" +
			   "if(callback) {" +
			   "	callback.call(null);" +
			   "}";
	}
}