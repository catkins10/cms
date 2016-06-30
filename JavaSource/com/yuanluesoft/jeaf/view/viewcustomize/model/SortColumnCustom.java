/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.model;


/**
 * 
 * @author linchuan
 *
 */
public class SortColumnCustom {
	private String columnName; //列名称
	private String columnTitle; //列标题
	private boolean desc;
	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the desc
	 */
	public boolean isDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(boolean desc) {
		this.desc = desc;
	}
	/**
	 * @return the columnTitle
	 */
	public String getColumnTitle() {
		return columnTitle;
	}
	/**
	 * @param columnTitle the columnTitle to set
	 */
	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}
}