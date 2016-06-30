/*
 * Created on 2005-2-18
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationReturn implements Serializable {
	private String id; //返回值ID
	private Object value; //返回值
	private String type; //类型:INTEGER/FLOAT/STRING/DATETIME/BOOLEAN
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
