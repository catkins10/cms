package com.yuanluesoft.jeaf.tree.util;

import java.util.Iterator;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.tree.model.TreeNode;

/**
 * 
 * @author linchuan
 *
 */
public class TreeUtils {

	/**
	 * 递归:按id查找节点
	 * @param dataTree
	 * @param nodeId
	 * @return
	 * @throws ServiceException
	 */
	public static TreeNode findDataTreeNodeById(TreeNode treeNode, String nodeId) throws ServiceException {
		if(treeNode.getNodeId().equals(nodeId)) {
			return treeNode;
		}
		//查找子节点
		if(treeNode.getChildNodes()==null) {
			return null;
		}
		for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
			treeNode = (TreeNode)iterator.next();
			TreeNode foundNode = findDataTreeNodeById(treeNode, nodeId);
			if(foundNode!=null) {
				return foundNode;
			}
		}
		return null;
	}
}
