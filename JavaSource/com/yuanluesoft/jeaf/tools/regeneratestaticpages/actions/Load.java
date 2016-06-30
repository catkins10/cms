package com.yuanluesoft.jeaf.tools.regeneratestaticpages.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.regeneratestaticpages.forms.RegenerateStaticPages;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends RegenerateStaticPagesAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		//检查用户有没有用户管理的权限
		try {
			checkPrivilege(sessionInfo);
		}
		catch(PrivilegeException pe) {
			return redirectToLogin(this, mapping, form, request, response, pe, false);
		}
		RegenerateStaticPages regenerateStaticPagesForm = (RegenerateStaticPages)form;
		regenerateStaticPagesForm.setFormTitle("重新生成静态页面");
		regenerateStaticPagesForm.getFormActions().addFormAction(-1, "提交", "FormUtils.submitForm();", true);
		return mapping.findForward("load");
    }
}