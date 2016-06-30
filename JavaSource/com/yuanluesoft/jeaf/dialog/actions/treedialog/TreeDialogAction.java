package com.yuanluesoft.jeaf.dialog.actions.treedialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;

/**
 * 
 * @author linchuan
 *
 */
public abstract class TreeDialogAction extends SelectDialogAction {
	
	/**
	 * 获取目录服务
	 * @param dialogForm
	 * @param request
	 * @return
	 * @throws SystemUnregistException
	 */
	protected abstract DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException;
	
	/**
	 * 设置对话框标题
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected abstract String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo);
	
	/**
	 * 设置获取子节点的URL
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected abstract String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo);
	
	/**
	 * 获取需要过滤的目录类型
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		try {
			return getDirectoryService(dialogForm, request, sessionInfo).appendParentDirectoryTypes(dialogForm.getSelectNodeTypes());
		} 
		catch (SystemUnregistException e) {
			return null;
		}
	}
	
	/**
	 * 是否需要扩展树
	 * @return
	 */
	protected boolean needExtendTree() {
		return false;
	}
	
	/**
	 * 获取需要过滤的权限类型
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected String getPopedomFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "all"; //默认有任何权限都可以
	}
	
	/**
	 * 获取其他过滤条件HQL
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected String getOtherFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}
	
	/**
	 * 获取需要输出的扩展属性
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected String getExtendPropertyNames(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return null; //默认没有扩展属性
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		TreeDialog dialogForm = (TreeDialog)dialog;
    	dialogForm.setTree(createTree(dialogForm, request, sessionInfo));
    	dialogForm.setTitle(getDialogTitle(dialogForm, request, sessionInfo));
    	String url = getListChildNodesUrl(dialogForm, request, sessionInfo);
    	if(dialogForm.isAnonymousAlways() && url.indexOf("anonymousAlways=")==-1) { //总是匿名
    		url += (url.indexOf('?')==-1 ? "?" : "&") + "anonymousAlways=true";
    	}
    	dialogForm.setListChildNodesUrl(url);
	}
	
	/**
	 * 执行获取下级节点列表操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeListChildNodesAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TreeDialog dialogForm = (TreeDialog)form;
		anonymousAlways = dialogForm.isAnonymousAlways(); //是否强制匿名
		SessionInfo sessionInfo;
		try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		dialogForm.setActionResult("NOSESSIONINFO");
    		return mapping.findForward("load");
        }
    	dialogForm.setChildNodes(listChildNodes(dialogForm, request, sessionInfo));
    	dialogForm.setActionResult("SUCCESS");
    	return mapping.findForward("load");
	}
	
	/**
	 * 创建树
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		DirectoryService directoryService = getDirectoryService(dialogForm, request, sessionInfo);
		String directoryTypeFilters = getDirectoryTypeFilters(dialogForm, request, sessionInfo);
		String popedomFilters = getPopedomFilters(dialogForm, request, sessionInfo);
		String otherFilters = getOtherFilters(dialogForm, request, sessionInfo);
		String extendPropertyNames = getExtendPropertyNames(dialogForm, request, sessionInfo);
		Tree tree = directoryService.createDirectoryTree((dialogForm.getParentNodeId()==null ? 0 : Long.parseLong(dialogForm.getParentNodeId())), directoryTypeFilters, popedomFilters, otherFilters, extendPropertyNames, sessionInfo);
		//重设子节点列表
		if(tree.getRootNode().getChildNodes()!=null && !tree.getRootNode().getChildNodes().isEmpty()) {
			resetChildNodes(tree.getRootNode().getChildNodes(), dialogForm, request, sessionInfo);
		}
		if(needExtendTree()) {
			tree.getRootNode().setChildNodes(extendTree(tree.getRootNode().getChildNodes(), dialogForm, Long.parseLong(tree.getRootNode().getNodeId()), sessionInfo));
		}
		return tree;
	}
	
	/**
	 * 获取子节点列表
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List listChildNodes(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		DirectoryService directoryService = getDirectoryService(dialogForm, request, sessionInfo);
		String directoryTypeFilters = getDirectoryTypeFilters(dialogForm, request, sessionInfo);
		String popedomFilters = getPopedomFilters(dialogForm, request, sessionInfo);
		String otherFilters = getOtherFilters(dialogForm, request, sessionInfo);
		String extendPropertyNames = getExtendPropertyNames(dialogForm, request, sessionInfo);
		List treeNodes = null;
		long parentDirectoryId = -1;
		try {
			parentDirectoryId = Long.parseLong(dialogForm.getParentNodeId());
			treeNodes = directoryService.listChildTreeNodes(parentDirectoryId, directoryTypeFilters, popedomFilters, otherFilters, extendPropertyNames, sessionInfo);
		}
		catch(Exception e) {
			
		}
		//重设子节点列表
		if(treeNodes!=null && !treeNodes.isEmpty()) {
			resetChildNodes(treeNodes, dialogForm, request, sessionInfo);
		}
		if(needExtendTree()) {
			treeNodes = extendTree(treeNodes, dialogForm, parentDirectoryId, sessionInfo);
		}
		return treeNodes;
	}
	
	/**
	 * 重设子节点列表
	 * @param childNodes
	 * @param dialogForm
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void resetChildNodes(List childNodes, TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		
	}
	
	/**
	 * 扩展树：把所有节点都设置为有子节点,并添加当前目录下的其他节点
	 * @param treeNodes
	 * @param dialogForm
	 * @param parentDirectoryId
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	private List extendTree(List treeNodes, TreeDialog dialogForm, long parentDirectoryId, SessionInfo sessionInfo) throws Exception {
		if(treeNodes==null) {
			treeNodes = new ArrayList();
		}
		else {
			//把所有节点都设置为有子节点
			for(Iterator iterator=treeNodes.iterator(); iterator.hasNext();) {
				TreeNode treeNode = (TreeNode)iterator.next();
				treeNode.setHasChildNodes(true);
			}
		}
		//获取扩展的节点列表
		List extendNodes = listExtendTreeNodes(dialogForm, parentDirectoryId, sessionInfo);
		if(extendNodes!=null && !extendNodes.isEmpty()) {
			treeNodes.addAll(extendNodes);
		}
		return treeNodes;
	}
	
	/**
	 * 获取扩展的树节点列表
	 * @param dialogForm
	 * @param parentDirectoryId
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List listExtendTreeNodes(TreeDialog dialogForm, long parentDirectoryId, SessionInfo sessionInfo) throws Exception {
		return null;
	}
}