package com.yuanluesoft.jeaf.dataimport.model;

import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 
 * @author linchuan
 *
 */
public class DataDirectoryTree {
	private String name; //名称
	private Tree sourceTree; //源目录树
	private Tree targetTree; //目标目录树
	
	/**
	 * @return the sourceTree
	 */
	public Tree getSourceTree() {
		return sourceTree;
	}
	/**
	 * @param sourceTree the sourceTree to set
	 */
	public void setSourceTree(Tree sourceTree) {
		this.sourceTree = sourceTree;
	}
	/**
	 * @return the targetTree
	 */
	public Tree getTargetTree() {
		return targetTree;
	}
	/**
	 * @param targetTree the targetTree to set
	 */
	public void setTargetTree(Tree targetTree) {
		this.targetTree = targetTree;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
