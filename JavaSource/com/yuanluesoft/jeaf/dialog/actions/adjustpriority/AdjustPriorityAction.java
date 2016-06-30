package com.yuanluesoft.jeaf.dialog.actions.adjustpriority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.adjustpriority.service.PriorityService;
import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.AdjustPriority;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustPriorityAction extends SelectDialogAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!request.getMethod().toLowerCase().equals("post")) { //不是提交
			return executeOpenDialogAction(mapping, form, request, response);
		}
		try {
			adjust(form, request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, true);
		}
		catch(PrivilegeException pe) {
			return redirectToLogin(this, mapping, form, request, response, pe, false);
		}
		response.getWriter().write("<html><script language=\"JavaScript\" charset=\"utf-8\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/common.js\"></script><script>DialogUtils.closeDialog();</script></html>");
		return null;
	}
	
	/**
	 * 获取视图所在应用名称
	 * @param viewForm
	 * @return
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return adjustPriorityForm.getApplicationName();
	}
	
	/**
	 * 获取视图名称
	 * @param viewForm
	 * @return
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return adjustPriorityForm.getViewName();
	}
	
	/**
	 * 重置视图
	 * @param adjustPriorityForm
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		AdjustPriority adjustPriorityForm = (AdjustPriority)dialog;
		//检查调整权限
		List acl = getAccessControlService().getAcl(adjustPriorityForm.getApplicationName(), sessionInfo);
		checkAdjustPrivilege(adjustPriorityForm, request, acl, sessionInfo);
		//设置标题
		if(adjustPriorityForm.getTitle()==null) {
			adjustPriorityForm.setTitle("调整优先级");
		}
		//获取视图定义
		View view = getView(adjustPriorityForm, request, sessionInfo);
		view.setPageRows(1000);
		//调用视图服务填充视图包
		adjustPriorityForm.getViewPackage().setViewMode(View.VIEW_DISPLAY_MODE_MULTI_SELECT);
		((ViewService)getService("viewService")).retrieveViewPackage(adjustPriorityForm.getViewPackage(), view, 0, false, false, false, request, sessionInfo);
    	adjustPriorityForm.getViewPackage().setRowNum(1);
 		//获取高优先级的记录列表
 		adjustPriorityForm.setHighPriorityRecords(getPriorityService().listHighPriorityRecords(view, request, sessionInfo));
	}

	/**
	 * 调整优先级
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void adjust(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = getSessionInfo(request, response);
		AdjustPriority adjustPriorityForm = (AdjustPriority)form;
		//检查调整权限
		List acl = getAccessControlService().getAcl(adjustPriorityForm.getApplicationName(), sessionInfo);
		checkAdjustPrivilege(adjustPriorityForm, request, acl, sessionInfo);
		//获取视图定义
		View view = getView(adjustPriorityForm, request, sessionInfo);
 		//调整优先级
 		getPriorityService().adjustPriority(adjustPriorityForm.getSelectedRecordIds(), view, request, sessionInfo);
	}
	
	/**
	 * 获取视图
	 * @param adjustPriorityForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	private View getView(AdjustPriority adjustPriorityForm, HttpServletRequest request, SessionInfo sessionInfo)  throws Exception {
		ViewDefineService viewDefineService = getViewDefineService();
		View view = viewDefineService.getView(getViewApplicationName(adjustPriorityForm), getViewName(adjustPriorityForm), sessionInfo);
		resetView(adjustPriorityForm, view, request, sessionInfo);
		return view;
	}
	
	/**
	 * 检查用户的调整权限
	 * @param form
	 * @param request
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkAdjustPrivilege(AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains(AccessControlService.ACL_APPLICATION_VISITOR)) {
			throw new PrivilegeException();
		}
	}
	
	/**
	 * 获取视图定义服务
	 * @return
	 * @throws SystemUnregistException
	 */
	protected ViewDefineService getViewDefineService() throws SystemUnregistException {
		return (ViewDefineService)getService("viewDefineService");
	}
	
	/**
	 * 获取优先级服务
	 * @return
	 * @throws SystemUnregistException
	 */
	protected PriorityService getPriorityService() throws SystemUnregistException {
		return (PriorityService)getService("priorityService");
	}
}