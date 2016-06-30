package com.yuanluesoft.jeaf.dialog.forms;

import java.util.List;

import com.yuanluesoft.jeaf.view.model.ViewPackage;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustPriority extends SelectDialog {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	private String selectedRecordIds; //选中的记录ID列表
	private List highPriorityRecords; //当前优先级别高的记录列表
	private ViewPackage viewPackage = new ViewPackage(); //当前对话框
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.forms.SelectDialog#isMultiSelect()
	 */
	public boolean isMultiSelect() {
		return true;
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
	 * @return the highPriorityRecords
	 */
	public List getHighPriorityRecords() {
		return highPriorityRecords;
	}

	/**
	 * @param highPriorityRecords the highPriorityRecords to set
	 */
	public void setHighPriorityRecords(List highPriorityRecords) {
		this.highPriorityRecords = highPriorityRecords;
	}

	/**
	 * @return the selectedRecordIds
	 */
	public String getSelectedRecordIds() {
		return selectedRecordIds;
	}

	/**
	 * @param selectedRecordIds the selectedRecordIds to set
	 */
	public void setSelectedRecordIds(String selectedRecordIds) {
		this.selectedRecordIds = selectedRecordIds;
	}

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
}