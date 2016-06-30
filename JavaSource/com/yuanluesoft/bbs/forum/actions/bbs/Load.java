package com.yuanluesoft.bbs.forum.actions.bbs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bbs.base.actions.BbsBaseAction;
import com.yuanluesoft.bbs.forum.forms.Bbs;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends BbsBaseAction {
	
	public Load() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		Bbs bbsForm = (Bbs)form;
		bbsForm.setAnonymousEnable(anonymousEnable);
		ForumService forumService = (ForumService)getService("forumService");
		//检查用户的访问权限
		List popedoms = forumService.listDirectoryPopedoms(bbsForm.getId(), sessionInfo);
		if(popedoms==null || popedoms.isEmpty()) {
			popedoms = forumService.listChildDirectoryPopedoms(bbsForm.getId(), sessionInfo); //检查是否有下级目录的权限
			if(popedoms==null || popedoms.isEmpty()) {
				return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
			}
		}
		bbsForm.setBbs(forumService.getBbs(bbsForm.getId(), sessionInfo));
		if(bbsForm.getRequestUrl()==null) {
			bbsForm.setRequestUrl(RequestUtils.getRequestURL(request, true));
			bbsForm.setLoginUrl(bbsForm.getRequestUrl());
		}
		return mapping.findForward("load");
	}
}