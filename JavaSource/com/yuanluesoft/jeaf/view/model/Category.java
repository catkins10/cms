/*
 * Created on 2004-12-22
 *
 */
package com.yuanluesoft.jeaf.view.model;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class Category extends CloneableObject {
	private String titleProperty; //显示属性
	private String valueProperty; //值属性
	private String titleHql; //显示属性对应的HQL
	private String valueType; //值类型,string(默认)/number/其他
	private String where; //条件
	private String orderBy; //排序
	private String viewDataFilter; //视图数据过滤条件
	
	/**
	 * @return Returns the orderBy.
	 */
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy The orderBy to set.
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * @return Returns the titleProperty.
	 */
	public String getTitleProperty() {
		return titleProperty;
	}
	/**
	 * @param titleProperty The titleProperty to set.
	 */
	public void setTitleProperty(String titleProperty) {
		this.titleProperty = titleProperty;
	}
	/**
	 * @return Returns the valueProperty.
	 */
	public String getValueProperty() {
		return valueProperty;
	}
	/**
	 * @param valueProperty The valueProperty to set.
	 */
	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}
	/**
	 * @return Returns the valueType.
	 */
	public String getValueType() {
		return valueType;
	}
	/**
	 * @param valueType The valueType to set.
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	/**
	 * @return Returns the viewDataFilter.
	 */
	public String getViewDataFilter() {
		return viewDataFilter;
	}
	/**
	 * @param viewDataFilter The viewDataFilter to set.
	 */
	public void setViewDataFilter(String viewDataFilter) {
		this.viewDataFilter = viewDataFilter;
	}
	/**
	 * @return Returns the where.
	 */
	public String getWhere() {
		return where;
	}
	/**
	 * @param where The where to set.
	 */
	public void setWhere(String where) {
		this.where = where;
	}
	/**
	 * @return the titleHql
	 */
	public String getTitleHql() {
		return titleHql;
	}
	/**
	 * @param titleHql the titleHql to set
	 */
	public void setTitleHql(String titleHql) {
		this.titleHql = titleHql;
	}
}
