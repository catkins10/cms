package com.yuanluesoft.jeaf.tree.model;

import java.io.Serializable;



/**
 * 
 * @author linchuan
 *
 */
public class Tree implements Serializable {
	private TreeNode rootNode; //根节点
	
	public Tree() {
		super();
	}
	
	public Tree(String rootNodeId, String rootNodeText, String rootNodeType, String rootNodeIcon) {
		super();
		rootNode = new TreeNode();
		rootNode.setNodeId(rootNodeId);
		rootNode.setNodeText(rootNodeText);
		rootNode.setNodeType(rootNodeType);
		rootNode.setNodeIcon(rootNodeIcon);
		rootNode.setHasChildNodes(true);
		rootNode.setExpandTree(true);
	}
	
	/**
	 * 添加子节点
	 * @param nodeId
	 * @param nodeText
	 * @param nodeType
	 * @param nodeIcon
	 * @param hasChildNodes
	 * @return
	 */
	public TreeNode appendChildNode(String nodeId, String nodeText, String nodeType, String nodeIcon, boolean hasChildNodes) {
		return rootNode.appendChildNode(nodeId, nodeText, nodeType, nodeIcon, hasChildNodes);
	}
	
	/**
	 * @return the rootNode
	 */
	public TreeNode getRootNode() {
		return rootNode;
	}
	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}
}