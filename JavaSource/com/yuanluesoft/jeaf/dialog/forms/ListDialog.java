/*
 * Created on 2004-9-14
 *
 */
package com.yuanluesoft.jeaf.dialog.forms;


/**
 *
 * @author LinChuan
 *
 */
public class ListDialog extends SelectDialog {
	private String source; //URL参数:对话框数据源
	private String itemsText; //选择项条目
	
	/**
	 * @return Returns the source.
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source The source to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the itemsText
	 */
	public String getItemsText() {
		return itemsText;
	}
	/**
	 * @param itemsText the itemsText to set
	 */
	public void setItemsText(String itemsText) {
		this.itemsText = itemsText;
	}
}
