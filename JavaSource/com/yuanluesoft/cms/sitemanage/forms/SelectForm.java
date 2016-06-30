package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 选择表单
 * @author linchuan
 *
 */
public class SelectForm extends TreeDialog {
	private String currentApplicationName; //URL参数:当前应用

	/**
	 * @return the currentApplicationName
	 */
	public String getCurrentApplicationName() {
		return currentApplicationName;
	}

	/**
	 * @param currentApplicationName the currentApplicationName to set
	 */
	public void setCurrentApplicationName(String currentApplicationName) {
		this.currentApplicationName = currentApplicationName;
	}
}