/*
 * Created on 2005-2-6
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Filter implements Cloneable, Serializable {
    public static final int SIMPLE_TYPE = 1;
    public static final int SQL_TYPE = 2;

	private int type; //包括基本类型和SQL字符串
	private String attributeName; //属性名 
	private int comparison; //<, >, =, !=, <=, <=, ismemeber, contains等
	private Object filterValue; //如果type为SQL_TYPE字符串，filterValue 将指向SQL 子句，和SQL92 标准语言规范中WHERE 子句的句法一致
	
	public Filter() {
	}
	/**
	 * @param attributeName
	 * @param comparison
	 * @param filterValue
	 */

	public Filter(String attributeName, int comparison, Object filterValue) {
		this.type = SIMPLE_TYPE;
		this.attributeName = attributeName;
		this.comparison = comparison;
		this.filterValue = filterValue;
	}
	
	
	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * @param attributeName The attributeName to set.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	/**
	 * @return Returns the comparison.
	 */
	public int getComparison() {
		return comparison;
	}
	/**
	 * @param comparison The comparison to set.
	 */
	public void setComparison(int comparison) {
		this.comparison = comparison;
	}
	/**
	 * @return Returns the filterValue.
	 */
	public Object getFilterValue() {
		return filterValue;
	}
	/**
	 * @param filterValue The filterValue to set.
	 */
	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}
