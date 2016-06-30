package com.yuanluesoft.cms.sitemanage.actions.selectpageresource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SelectPage extends SelectDialogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenDialogAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		com.yuanluesoft.cms.sitemanage.forms.SelectPage dialogForm = (com.yuanluesoft.cms.sitemanage.forms.SelectPage)dialog;
		boolean columnSupportOnly = false;
		if(dialogForm.isSitePageOnly()) {
			SiteService siteService = (SiteService)getService("siteService");
			WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(dialogForm.getSiteId());
			columnSupportOnly = !(webDirectory instanceof WebSite);
		}
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
    	dialogForm.setTree(pageDefineService.createPageTree(dialogForm.getCurrentApplicationName(), dialogForm.isSitePageOnly(), columnSupportOnly, dialogForm.isInternalPageOnly(), dialogForm.getUserId()!=0, dialogForm.isAdvertPutSupportOnly(), sessionInfo));
    	dialogForm.setTitle("选择页面"); //设置对话框名称
    	dialogForm.setSelectNodeTypes("page");
  }
}