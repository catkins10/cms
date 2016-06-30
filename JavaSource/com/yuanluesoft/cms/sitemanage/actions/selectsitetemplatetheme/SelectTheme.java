package com.yuanluesoft.cms.sitemanage.actions.selectsitetemplatetheme;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.viewselectdialog.OpenSelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.ViewSelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SelectTheme extends OpenSelectDialog {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.viewselectdialog.OpenSelectDialog#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ViewSelectDialog viewDialog = (ViewSelectDialog)dialog;
		viewDialog.setApplicationName("cms/sitemanage");
		viewDialog.setViewName("siteTemplateTheme");
		super.initDialog(dialog, sessionInfo, request);
		SiteService siteService = (SiteService)getService("siteService");
		long pageSiteId = RequestUtils.getParameterLongValue(request, "pageSiteId");
		//检查用户是否主题所在站点的管理员
		if(siteService.checkPopedom(pageSiteId, "manager", sessionInfo)) {
			//添加“新建主题”按钮
			String script = "DialogUtils.openDialog('" + request.getContextPath() + "/cms/sitemanage/siteTemplateTheme.shtml" +
							"?act=create&amp;siteId=" + pageSiteId + "'," +
							"550, 300)";
			dialog.getFormActions().addFormAction(0, "新建主题", script, false);
			//添加“修改主题”按钮
			script = "if(getCurrentData())DialogUtils.openDialog('" + request.getContextPath() + "/cms/sitemanage/siteTemplateTheme.shtml?act=edit&amp;pageSiteId=" + pageSiteId + "&amp;id=' + getDataFieldValue(getCurrentData(), 'id'), 550, 300)";
			dialog.getFormActions().addFormAction(1, "编辑主题", script, false);
		}
	}
}