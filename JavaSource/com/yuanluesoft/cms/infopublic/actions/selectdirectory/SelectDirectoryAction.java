package com.yuanluesoft.cms.infopublic.actions.selectdirectory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.forms.SelectDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
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
public class SelectDirectoryAction extends TreeDialogAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (PublicDirectoryService)getService("publicDirectoryService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		String directoryTypes = dialogForm.getSelectNodeTypes();
		if(directoryTypes==null || directoryTypes.equals("") || directoryTypes.equals("all")) {
    		directoryTypes = "info,main,category,article";
    	}
		else {
			if(directoryTypes.indexOf("info")!=-1) {
				if(directoryTypes.indexOf("main")==-1) {
					directoryTypes += ",main";
				}
				if(directoryTypes.indexOf("category")==-1) {
					directoryTypes += ",category";
				}
	    	}
			if(directoryTypes.indexOf("article")!=-1 && directoryTypes.indexOf("main")==-1) {
	    		directoryTypes += ",main";
	    	}
			if(directoryTypes.indexOf("category")==-1 && directoryTypes.indexOf("main")==-1) {
	   			directoryTypes += ",main";
	    	}
		}
    	boolean anonymous = sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName());
    	if(anonymous) {
    		directoryTypes = directoryTypes.replaceAll("main,", "").replaceAll(",main", "").replaceAll("article,", "").replaceAll(",article", ""); //匿名用户,不允许浏览其他主目录
    	}
		return directoryTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "信息公开目录选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		SelectDirectory selectForm = (SelectDirectory)dialogForm;
		return "listChildDirectories.shtml?anonymousAlways=" + selectForm.isAnonymousAlways() + "&countPublicInfo=" + selectForm.isCountPublicInfo() + "&displayDirectoryCode=" + selectForm.isDisplayDirectoryCode();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#createTree(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		SelectDirectory selectForm = (SelectDirectory)dialogForm;
		Tree tree = super.createTree(dialogForm, request, sessionInfo);
		if(selectForm.isCountPublicInfo()) { //获取信息数量
			List nodes = new ArrayList();
			nodes.add(tree.getRootNode());
			countPublicInfo(nodes);
			countPublicInfo(tree.getRootNode().getChildNodes());
    	}
		if(selectForm.isDisplayDirectoryCode()) { //显示目录代码
			displayDirectoryCode(tree.getRootNode().getChildNodes());
		}
		if(selectForm.isDisplayRecentUsed()) { //创建“历史记录”节点，添加用户最后使用过的5个目录
			PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
			TreeNode recentUseNode  = publicDirectoryService.createRecentUsenTreeNode(getExtendPropertyNames(dialogForm, request, sessionInfo), sessionInfo, 5);
			if(recentUseNode!=null) {
				tree.getRootNode().getChildNodes().add(0, recentUseNode);
			}
		}
		return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#listChildNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listChildNodes(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		SelectDirectory selectForm = (SelectDirectory)dialogForm;
		List treeNodes = super.listChildNodes(dialogForm, request, sessionInfo);
		if(selectForm.isCountPublicInfo()) { //获取信息数量
			countPublicInfo(treeNodes);
		}
		if(selectForm.isDisplayDirectoryCode()) { //显示目录代码
			displayDirectoryCode(treeNodes);
		}
		return treeNodes;
	}
	
	/**
	 * 获取目录信息数量
	 * @param treeNodes
	 * @throws Exception
	 */
	private void countPublicInfo(List treeNodes) throws Exception {
		if(treeNodes==null || treeNodes.isEmpty()) {
			return;
		}
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
		for(Iterator iterator = treeNodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = (TreeNode)iterator.next();
			treeNode.setNodeText(treeNode.getNodeText() + "(" + publicInfoService.getInfosCount(treeNode.getNodeId(), true) + ")");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getExtendPropertyNames(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getExtendPropertyNames(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		SelectDirectory selectForm = (SelectDirectory)dialogForm;
		if(selectForm.isDisplayDirectoryCode()) {
			return "code";
		}
		return super.getExtendPropertyNames(dialogForm, request, sessionInfo);
	}

	/**
	 * 显示目录代码
	 * @param treeNodes
	 * @throws Exception
	 */
	private void displayDirectoryCode(List treeNodes) throws Exception {
		if(treeNodes==null || treeNodes.isEmpty()) {
			return;
		}
		for(Iterator iterator = treeNodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = (TreeNode)iterator.next();
			String code;
			if("info".equals(treeNode.getNodeType()) && (code=treeNode.getExtendPropertyValue("code"))!=null && !code.isEmpty()) {
				treeNode.setNodeText(code + " " + treeNode.getNodeText());
			}
		}
	}
}