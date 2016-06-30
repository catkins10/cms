/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.workflow.client.model.resource;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class FormalParameter implements Serializable {
	private String id; //ID
	private int index; //参数序号
	private String mode; //IN,OUT,INOUT
	private boolean basicType;
	private String type; //类型:INTEGER/FLOAT/STRING/DATETIME/BOOLEAN
	private String description; //描述
	private String trueTitle; //当类型为BOOLEAN时,值为true时的标题
	private String falseTitle; //当类型为BOOLEAN时,值为false时的标题
	
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
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
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
	
	/**
	 * @return Returns the basicType.
	 */
	public boolean isBasicType() {
		return basicType;
	}
	/**
	 * @param basicType The basicType to set.
	 */
	public void setBasicType(boolean basicType) {
		this.basicType = basicType;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return Returns the falseTitle.
	 */
	public String getFalseTitle() {
		return falseTitle;
	}
	/**
	 * @param falseTitle The falseTitle to set.
	 */
	public void setFalseTitle(String falseTitle) {
		this.falseTitle = falseTitle;
	}
	/**
	 * @return Returns the trueTitle.
	 */
	public String getTrueTitle() {
		return trueTitle;
	}
	/**
	 * @param trueTitle The trueTitle to set.
	 */
	public void setTrueTitle(String trueTitle) {
		this.trueTitle = trueTitle;
	}
}
