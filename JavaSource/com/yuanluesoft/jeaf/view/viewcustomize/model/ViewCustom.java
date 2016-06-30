/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustom {
	private int pageRows; //每页记录数
	private List columns; //列
	private List sortColumns; //排序列
	
	/**
	 * @return Returns the sortColumnList.
	 */
	public List getSortColumns() {
		return sortColumns;
	}
	/**
	 * @param sortColumnList The sortColumnList to set.
	 */
	public void setSortColumns(List sortColumns) {
		this.sortColumns = sortColumns;
	}
	/**
	 * @return the columns
	 */
	public List getColumns() {
		return columns;
	}
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List columns) {
		this.columns = columns;
	}
	/**
	 * @return the pageRows
	 */
	public int getPageRows() {
		return pageRows;
	}
	/**
	 * @param pageRows the pageRows to set
	 */
	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}
}
