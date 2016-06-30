package com.yuanluesoft.bbs.usermanage.actions.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bbs.base.actions.BbsBaseAction;
import com.yuanluesoft.bbs.usermanage.forms.BbsUser;
import com.yuanluesoft.bbs.usermanage.service.BbsUserService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BbsBaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
        }
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		BbsUser bbsUserForm = (BbsUser)form;
		if(sessionInfo.getUserId()==bbsUserForm.getId()) {
			response.sendRedirect("personalPanel.shtml?target=bbsUser");
			return null;
		}
		//获取用户信息
		BbsUserService bbsUserService = (BbsUserService)getService("bbsUserService");
		com.yuanluesoft.bbs.usermanage.model.BbsUser bbsUser = bbsUserService.getBbsUserModel(bbsUserForm.getId());
		PropertyUtils.copyProperties(bbsUserForm, bbsUser);
		return mapping.findForward("load");
    }
}
