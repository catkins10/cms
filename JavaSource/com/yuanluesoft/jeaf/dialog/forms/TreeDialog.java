package com.yuanluesoft.jeaf.dialog.forms;

import java.util.List;

import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 *
 * @author LinChuan
 *
 */
public class TreeDialog extends SelectDialog {
	private String parentNodeId; //URL参数:父目录ID
	private String selectNodeTypes; //URL参数:选择的节点类型
	private boolean leafNodeOnly; //URL参数:是否只允许选择叶节点
	private Tree tree; //树
	private List childNodes; //获取下级节点列表
	private String listChildNodesUrl; //获取子节点的URL
	
	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * @return the parentNodeId
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * @param parentNodeId the parentNodeId to set
	 */
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * @return the selectNodeTypes
	 */
	public String getSelectNodeTypes() {
		return selectNodeTypes;
	}

	/**
	 * @param selectNodeTypes the selectNodeTypes to set
	 */
	public void setSelectNodeTypes(String selectNodeTypes) {
		this.selectNodeTypes = selectNodeTypes;
	}

	/**
	 * @return the childNodes
	 */
	public List getChildNodes() {
		return childNodes;
	}

	/**
	 * @param childNodes the childNodes to set
	 */
	public void setChildNodes(List childNodes) {
		this.childNodes = childNodes;
	}

	/**
	 * @return the listChildNodesUrl
	 */
	public String getListChildNodesUrl() {
		return listChildNodesUrl;
	}

	/**
	 * @param listChildNodesUrl the listChildNodesUrl to set
	 */
	public void setListChildNodesUrl(String listChildNodesUrl) {
		this.listChildNodesUrl = listChildNodesUrl;
	}

	/**
	 * @return the leafNodeOnly
	 */
	public boolean isLeafNodeOnly() {
		return leafNodeOnly;
	}

	/**
	 * @param leafNodeOnly the leafNodeOnly to set
	 */
	public void setLeafNodeOnly(boolean leafNodeOnly) {
		this.leafNodeOnly = leafNodeOnly;
	}
}
