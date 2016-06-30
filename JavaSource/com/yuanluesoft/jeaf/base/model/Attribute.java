/*
 * Created on 2005-1-12
 *
 */
package com.yuanluesoft.jeaf.base.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Attribute implements Serializable {
	private String name; //名称
	private String value; //值
	
	public Attribute() {
		super();
	}

	public Attribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
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
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
