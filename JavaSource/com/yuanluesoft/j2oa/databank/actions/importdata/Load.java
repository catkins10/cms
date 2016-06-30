package com.yuanluesoft.j2oa.databank.actions.importdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 * 
 */
public class Load extends BaseAction {

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
		//检查用户的根目录的管理权限
		DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
		if(!databankDirectoryService.checkPopedom(0, "manager", sessionInfo)) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		com.yuanluesoft.j2oa.databank.forms.Import importForm = (com.yuanluesoft.j2oa.databank.forms.Import)form;
		importForm.getFormActions().addFormAction(-1, "关闭", "window.close()", true);
		importForm.getFormActions().addFormAction(-1, "导入", "FormUtils.doAction('doImport')", false);
		if(importForm.getDisplayMode()==null || importForm.getDisplayMode().isEmpty()) {
			importForm.setDisplayMode("window");
		}
		importForm.setFormTitle("目录导入");
		return mapping.findForward("load");
	}
}