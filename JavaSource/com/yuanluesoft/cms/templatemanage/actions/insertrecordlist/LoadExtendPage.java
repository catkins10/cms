package com.yuanluesoft.cms.templatemanage.actions.insertrecordlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class LoadExtendPage extends BaseAction {

	public LoadExtendPage() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = getSessionInfo(request, response);
		com.yuanluesoft.jeaf.form.ActionForm extendPage = (com.yuanluesoft.jeaf.form.ActionForm)form;
		//加表单定义
		loadFormDefine(extendPage, request);
		//初始化表单
		initForm(extendPage, sessionInfo, request);
		if(extendPage.getDisplayMode()==null || extendPage.getDisplayMode().isEmpty()) {
			extendPage.setDisplayMode("dialog");
		}
		return mapping.findForward("load");
	}
	
	/**
	 * 初始化
	 * @param extendPage
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	protected void initForm(com.yuanluesoft.jeaf.form.ActionForm extendPage, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		
	}
}