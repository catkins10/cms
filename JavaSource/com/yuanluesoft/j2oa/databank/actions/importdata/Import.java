package com.yuanluesoft.j2oa.databank.actions.importdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
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
public class Import extends BaseAction {

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
    	if(importForm.getSystemDirectory()!=null && !importForm.getSystemDirectory().equals("") && importForm.getDatabankDirectoryName()!=null && !importForm.getDatabankDirectoryName().equals("")) {
	    	//开始导入
    		DatabankDataService databankDataService = (DatabankDataService)getService("databankDataService");
    		databankDataService.importSystemDirectory(importForm.getSystemDirectory(), importForm.getDatabankDirectoryId(), sessionInfo.getUserId(), sessionInfo.getUserName());
    	}
    	importForm.setActionResult("导入成功！");
    	importForm.getFormActions().addFormAction(-1, "关闭", "window.close()", true);
    	return mapping.findForward("result");
	}
}