/*
 * Created on 2004-12-26
 *
 */
package com.yuanluesoft.jeaf.view.forms;

import com.yuanluesoft.jeaf.tree.forms.TreeForm;

/**
 * 分类树
 * @author LinChuan
 *
 */
public class CategoryTree extends TreeForm {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	private String currentCategories; //URL参数:当前分类
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the currentCategories
	 */
	public String getCurrentCategories() {
		return currentCategories;
	}
	/**
	 * @param currentCategories the currentCategories to set
	 */
	public void setCurrentCategories(String currentCategories) {
		this.currentCategories = currentCategories;
	}
}