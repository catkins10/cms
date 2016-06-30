package com.yuanluesoft.job.company.actions.selectparameter;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.job.company.forms.SelectParameter;
import com.yuanluesoft.job.company.service.JobParameterService;

/**
 * 
 * @author linchuan
 *
 */
public class SelectParameterAction extends TreeDialogAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return getParameterType((SelectParameter)dialogForm) + "选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (JobParameterService)getService("jobParameterService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listChildParameters.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		if("edit".equals(dialog.getAct()) && getAcl("job/company", sessionInfo).contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			SelectParameter selectParameter = (SelectParameter)dialog;
			String script = "var nodeType=window.tree.getSelectedNodeType();" +
							"var items='';";
			if("1".equals(selectParameter.getParentNodeId())) {
				script +=	"if(nodeType=='industryRoot' || nodeType=='industryCategory') {" +
							"  items='行业分类|industryCategory\\0行业|industry';" +
							"}" +
							"else if(nodeType=='industry') {" +
							"  items='岗位|post';" +
							"}";
			}
			else {
				script +=	"if(nodeType=='specialtyRoot' || nodeType=='specialtyCategory') {" +
							"  items='专业分类|specialtyCategory\\0专业|specialty';" +
							"}";
			}
			script +=		"PopupMenu.popupMenu(items, function(menuItemId, menuItemTitle) {" +
							"  DialogUtils.openDialog('" + request.getContextPath() + "/job/company/admin/jobParameter.shtml?parentDirectoryId=' + window.tree.getSelectedNodeId() + '&directoryType=' + menuItemId, 500, 300);" +
							"}, this, 120, 'right');";
			dialog.getFormActions().addFormAction(-1, "新建▼", script, true);
			script = "if(Number(window.tree.getSelectedNodeId())>10)DialogUtils.openDialog('" + request.getContextPath() + "/job/company/admin/jobParameter.shtml?id=' + window.tree.getSelectedNodeId(), 430, 300);";
			dialog.getFormActions().addFormAction(-1, "编辑", script, false);
			dialog.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog()", false);
		}
	}
	
	/**
	 * 获取参数类型
	 * @param selectParameterDialog
	 * @return
	 */
	private String getParameterType(SelectParameter selectParameterDialog) {
		if("1".equals(selectParameterDialog.getParentNodeId())) {
			return "行业";
		}
		else if("2".equals(selectParameterDialog.getParentNodeId())) {
			return "专业";
		}
		else if("3".equals(selectParameterDialog.getParentNodeId())) {
			return "岗位";
		}
		return null; 
	}
}