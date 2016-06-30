package com.yuanluesoft.j2oa.info.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class MagazineColumn {
	private String columnName; //栏目名称
	private List columnUseInfos; //栏目使用的信息(非简讯)(InfoMagazineUse)列表
	private List columnUseBriefs; //栏目使用的信息(简讯)(InfoMagazineUse)列表
	
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
	 * @return the columnUseInfos
	 */
	public List getColumnUseInfos() {
		return columnUseInfos;
	}
	/**
	 * @param columnUseInfos the columnUseInfos to set
	 */
	public void setColumnUseInfos(List columnUseInfos) {
		this.columnUseInfos = columnUseInfos;
	}
	/**
	 * @return the columnUseBriefs
	 */
	public List getColumnUseBriefs() {
		return columnUseBriefs;
	}
	/**
	 * @param columnUseBriefs the columnUseBriefs to set
	 */
	public void setColumnUseBriefs(List columnUseBriefs) {
		this.columnUseBriefs = columnUseBriefs;
	}
}