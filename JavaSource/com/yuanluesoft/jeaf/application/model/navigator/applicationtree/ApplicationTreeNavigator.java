package com.yuanluesoft.jeaf.application.model.navigator.applicationtree;

import java.io.Serializable;

import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 应用导航：目录树
 * @author linchuan
 *
 */
public class ApplicationTreeNavigator extends ApplicationNavigator implements Serializable {
	private Tree tree; //树

	public ApplicationTreeNavigator(Tree tree) {
		super();
		this.tree = tree;
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