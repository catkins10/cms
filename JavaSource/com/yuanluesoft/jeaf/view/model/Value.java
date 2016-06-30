/*
 * Created on 2005-2-25
 *
 */
package com.yuanluesoft.jeaf.view.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Value implements Serializable {
	private String value; //值
	private String title; //显示的标题
	
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
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
