package com.yuanluesoft.jeaf.dialog.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 选择对话框操作
 * @author linchuan
 *
 */
public class SelectDialogAction extends BaseAction {
	
	public SelectDialogAction() {
		super();
		anonymousEnable = true;
	}

	/**
	 * 初始化对话框
	 * @param dialog
	 * @param request
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		//设置对话框按钮列表
		dialog.getFormActions().addFormAction(-1, "确定", "doOk()", true);
		dialog.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog()", false);
	}
	
	/**
	 * 执行打开对话框操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeOpenDialogAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SelectDialog dialog = (SelectDialog)form;
		anonymousAlways = dialog.isAnonymousAlways(); //是否强制匿名
		//会话检查
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		loadFormDefine(dialog, request);
		try {
			initDialog(dialog, sessionInfo, request);
		}
		catch(PrivilegeException pe) {
			return redirectToLogin(this, mapping, form, request, response, pe, false);
		}
 		return mapping.findForward("load");
    }
}