package com.yuanluesoft.jeaf.usermanage.actions.templateconfigure;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.dialog.actions.viewselectdialog.OpenSelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.ViewSelectDialog;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 选择模板
 * @author linchuan
 *
 */
public class SelectTemplate extends OpenSelectDialog {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.viewselectdialog.OpenSelectDialog#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ViewSelectDialog viewDialog = (ViewSelectDialog)dialog;
		String pageName = request.getParameter("pageName");
		
		//获取页面定义
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
		SitePage sitePage = pageDefineService.getSitePage(viewDialog.getApplicationName(), pageName);
		
		//设置视图名称
		if(sitePage.getTemplateView()!=null) {
			viewDialog.setApplicationName(sitePage.getTemplateView().getViewApplication());
			viewDialog.setViewName(sitePage.getTemplateView().getViewName());
		}
		else {
			viewDialog.setApplicationName("jeaf/usermanage");
			viewDialog.setViewName("admin/userPageTemplate");
		}
		super.initDialog(dialog, sessionInfo, request);
		
		View view = viewDialog.getViewPackage().getView();
		//添加“新建模板”按钮
		String script = "PageUtils.newrecord('" + viewDialog.getApplicationName() + "', '" + view.getForm() + "', 'mode=fullscreen', 'applicationName=" + viewDialog.getApplicationName() + "&amp;pageName=" + pageName + "&amp;userId=" + request.getParameter("userId") + "');DialogUtils.closeDialog()";
		dialog.getFormActions().addFormAction(0, "新建模板", script, false);
		
		//调整“确定”按钮名称为“修改模板”
		FormAction formAction = (FormAction)ListUtils.findObjectByProperty(dialog.getFormActions(), "title", "确定");
		formAction.setTitle("修改模板");
	
		dialog.setCloseDialogOnOK(false); //“OK”后不关闭对话框
		dialog.setScript("PageUtils.editrecord('" + viewDialog.getApplicationName() + "', '" + view.getForm() + "', '{id}', 'mode=fullscreen')");
		
		//设置对话框标题
		viewDialog.setTitle("模板配置 - " + sitePage.getTitle());
	}
}