package com.yuanluesoft.cms.sitemanage.actions.selectsite;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.forms.Select;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;

/**
 * 
 * @author linchuan
 *
 */
public class SelectSiteAction extends TreeDialogAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (SiteService)getService("siteService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getExtendPropertyNames(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getExtendPropertyNames(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		//匿名用户,删除禁用的目录以及被重定向的目录
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			return "halt,redirectUrl";
		}
		return super.getExtendPropertyNames(dialogForm, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#resetChildNodes(java.util.List, com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetChildNodes(List childNodes, TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.resetChildNodes(childNodes, dialogForm, request, sessionInfo);
		//匿名用户,删除禁用的目录以及被重定向的目录
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			for(Iterator iterator = childNodes.iterator(); iterator.hasNext();) {
				TreeNode node = (TreeNode)iterator.next();
				String value;
				if("1".equals(node.getExtendPropertyValue("halt"))) { //已经停用
					iterator.remove();
				}
				else if((value=node.getExtendPropertyValue("redirectUrl"))!=null && !value.isEmpty()) { //指定了跳转地址
					iterator.remove();
				}
				else {
					node.setExtendPropertyValue("halt", null);
					node.setExtendPropertyValue("redirectUrl", null);
				}
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		String directoryTypes = dialogForm.getSelectNodeTypes();
		if(directoryTypes==null || sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名用户
			return "site".equals(directoryTypes) ? directoryTypes : "column";
		}
		if(directoryTypes.indexOf("site")==-1 && (directoryTypes.indexOf("column")!=-1 || directoryTypes.indexOf("viewReference")!=-1)) {
			directoryTypes += ",site"; //追加site,以便获取下级站点下的栏目
		}
		return directoryTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "站点/栏目选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		Select siteSelectForm = (Select)dialogForm;
		return "listChildSites.shtml" + (siteSelectForm.isAnonymousAlways() ? "?anonymousAlways=true" : "");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getPopedomFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getPopedomFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		Select siteSelectForm = (Select)dialogForm;
		if(siteSelectForm.getPopedomFilters()!=null && !siteSelectForm.getPopedomFilters().equals("")) {
			return siteSelectForm.getPopedomFilters();
		}
		return super.getPopedomFilters(dialogForm, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#createTree(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Tree tree = super.createTree(dialogForm, request, sessionInfo);
		Select siteSelectForm = (Select)dialogForm;
		if(siteSelectForm.isDisplayRecentUsed()) { //创建“历史记录”节点，添加用户最后使用过的10个栏目
			SiteService siteService = (SiteService)getService("siteService");
			TreeNode recentUseNode  = siteService.createRecentUsenTreeNode(getExtendPropertyNames(dialogForm, request, sessionInfo), sessionInfo, 5);
			if(recentUseNode!=null) {
				tree.getRootNode().getChildNodes().add(0, recentUseNode);
			}
		}
		return tree;
	}
}