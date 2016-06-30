/*
 * Created on 2004-9-14
 *
 */
package com.yuanluesoft.jeaf.dialog.forms;


import com.yuanluesoft.jeaf.view.model.ViewPackage;


/**
 *
 * @author LinChuan
 *
 */
public class ViewSelectDialog extends SelectDialog {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	private boolean paging; //URL参数:是否分页显示
	private boolean showPrintBuddon; //URL参数:是否显示打印按钮
	
	private ViewPackage viewPackage = new ViewPackage();
	
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
	 * @return the paging
	 */
	public boolean isPaging() {
		return paging;
	}
	/**
	 * @param paging the paging to set
	 */
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
	/**
	 * @return the viewPackage
	 */
	public ViewPackage getViewPackage() {
		return viewPackage;
	}
	/**
	 * @param viewPackage the viewPackage to set
	 */
	public void setViewPackage(ViewPackage viewPackage) {
		this.viewPackage = viewPackage;
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
	 * @return the showPrintBuddon
	 */
	public boolean isShowPrintBuddon() {
		return showPrintBuddon;
	}
	/**
	 * @param showPrintBuddon the showPrintBuddon to set
	 */
	public void setShowPrintBuddon(boolean showPrintBuddon) {
		this.showPrintBuddon = showPrintBuddon;
	}
}