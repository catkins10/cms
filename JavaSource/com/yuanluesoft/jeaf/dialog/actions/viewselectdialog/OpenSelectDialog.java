/*
 * Created on 2005-1-10
 *
 */
package com.yuanluesoft.jeaf.dialog.actions.viewselectdialog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.ViewSelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 *
 * @author LinChuan
 *
 */
public class OpenSelectDialog extends SelectDialogAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenDialogAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		ViewSelectDialog viewDialog = (ViewSelectDialog)dialog;
		//获取选择对话框定义
		View view = ((ViewDefineService)getService("viewDefineService")).getView(viewDialog.getApplicationName(), viewDialog.getViewName(), sessionInfo);
		if(dialog.getTitle()==null) {
			dialog.setTitle(view.getTitle());
		}
		if(viewDialog.isPaging()) {
			viewDialog.getFormActions().addFormAction(0, "第一页", "loadSelectViewData('firstPage')", false);
			viewDialog.getFormActions().addFormAction(1, "上一页", "loadSelectViewData('previousPage')", false);
			viewDialog.getFormActions().addFormAction(2, "下一页", "loadSelectViewData('nextPage')", false);
			viewDialog.getFormActions().addFormAction(3, "最后一页", "loadSelectViewData('lastPage')", false);
		}
		if(viewDialog.isShowPrintBuddon()) {
			viewDialog.getFormActions().addFormAction(0, "打印", "window.open('" + request.getContextPath() + "/jeaf/application/applicationView.shtml?" + request.getQueryString() + "&viewPackage.currentViewAction=printAsExcel&viewPackage.categories=' + StringUtils.utf8Encode(document.getElementsByName('viewPackage.categories')[0].value))", false);
		}
		viewDialog.getViewPackage().setView(view);
	}
}