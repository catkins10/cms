/**
 * 
 */
package com.yuanluesoft.jeaf.usermanage.actions.selectorg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.usermanage.forms.Select;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgRoot;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 *
 * @author LinChuan
 *
 */
public class SelectOrgAction extends TreeDialogAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (OrgService)getService("orgService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getPopedomFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getPopedomFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		Select selectForm = (Select)dialogForm;
		if(selectForm.isManagedOnly()) { //仅有管理权限的
			return "manager";
		}
		return super.getPopedomFilters(dialogForm, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getExtendPropertyNames(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getExtendPropertyNames(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "fullName";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		if("area".equals(dialogForm.getSelectNodeTypes())) {
			return "地区选择";
		}
		else if("unit".equals(dialogForm.getSelectNodeTypes())) {
			return "单位选择";
		}
		else if("unitDepartment".equals(dialogForm.getSelectNodeTypes()) || "schoolDepartment".equals(dialogForm.getSelectNodeTypes())) {
			return "部门选择";
		}
		else if("category".equals(dialogForm.getSelectNodeTypes())) {
			return "分类选择";
		}
		else {
			return "组织机构选择";
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listChildOrgs.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#createTree(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Select selectForm = (Select)dialogForm;
		String parentNodeId = request.getParameter("parentNodeId");
		if(parentNodeId!=null && !parentNodeId.equals("")) { //有指定上级目录
			return super.createTree(dialogForm, request, null); //显示全部
		}
		Tree tree = super.createTree(dialogForm, request, sessionInfo);
		if(!selectForm.isHideRoot() && //需要单独显示根目录树
		   !tree.getRootNode().getNodeId().equals("0") && //当前树根不是根组织
		   (sessionInfo.getUserType()==PersonService.PERSON_TYPE_TEACHER || //允许教师查看整个目录
		   sessionInfo.getUserType()==PersonService.PERSON_TYPE_EMPLOYEE)) { //允许普通用户查看整个目录
			OrgService orgService = (OrgService)getService("orgService");
			selectForm.setRoot((OrgRoot)orgService.getDirectory(0));
		}
		return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#listChildNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listChildNodes(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		return super.listChildNodes(dialogForm, request, sessionInfo); //显示全部时,不传入会话
	}
}