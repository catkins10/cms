/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.view.statisticview.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class GroupField implements Serializable {
	private String name; //分组属性名称
	private String field; //分组字段名
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field The field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}
}
