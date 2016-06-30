package com.yuanluesoft.jeaf.tree.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 
 * @author linchuan
 *
 */
public class TreeForm extends ActionForm {
	private Tree tree; //树
	private String parentNodeId; //URL参数:父目录ID
	private List childNodes; //获取下级节点列表
	private String listChildNodesUrl; //获取子节点的URL
	
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
}
