package com.yuanluesoft.cms.evaluation.model;

import java.util.List;

/**
 * 测评项目分类
 * @author linchuan
 *
 */
public class EvaluationItemCategory {
	private String category; //分类
	private List items; //对应的测评项目列表
	private int firstItemIndex; //项目序号
	
	/**
	 * 获取项目数量
	 * @return
	 */
	public int getItemCount() {
		return items.size();
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the firstItemIndex
	 */
	public int getFirstItemIndex() {
		return firstItemIndex;
	}
	/**
	 * @param firstItemIndex the firstItemIndex to set
	 */
	public void setFirstItemIndex(int firstItemIndex) {
		this.firstItemIndex = firstItemIndex;
	}
	/**
	 * @return the items
	 */
	public List getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List items) {
		this.items = items;
	}
}