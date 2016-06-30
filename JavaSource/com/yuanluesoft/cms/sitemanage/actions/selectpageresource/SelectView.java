package com.yuanluesoft.cms.sitemanage.actions.selectpageresource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SelectView extends SelectDialogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenDialogAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		com.yuanluesoft.cms.sitemanage.forms.SelectView dialogForm = (com.yuanluesoft.cms.sitemanage.forms.SelectView)dialog;
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
    	dialogForm.setTree(pageDefineService.createViewTree(dialogForm.getCurrentApplicationName(), dialogForm.getCurrentPageName(), dialogForm.isRssChannelSupportOnly(), dialogForm.isTotalSupportOnly(), dialogForm.isNavigatorSupportOnly(), dialogForm.isConatinsEmbedViewOnly(), dialogForm.isSiteReferenceSupportOnly(), sessionInfo));
    	dialogForm.setTitle("选择记录列表"); //设置对话框名称
    	dialogForm.setSelectNodeTypes("view");
	}
}