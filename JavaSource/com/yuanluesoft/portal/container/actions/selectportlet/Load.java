package com.yuanluesoft.portal.container.actions.selectportlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.container.forms.SelectPortlet;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends SelectDialogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenDialogAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		SelectPortlet dialogForm = (SelectPortlet)dialog;
		PortletDefinitionService portletDefinitionService = (PortletDefinitionService)getService("portletDefinitionService");
    	dialogForm.setTree(portletDefinitionService.createPortletTree(sessionInfo));
    	dialogForm.setTitle("选择PORTLET"); //设置对话框名称
    	dialogForm.setSelectNodeTypes("portlet");
	}
}