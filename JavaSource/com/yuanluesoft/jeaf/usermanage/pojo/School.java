/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

/**
 * 组织机构:学校(user_school)
 * @author linchuan
 *
 */
public class School extends Org {
	private String category; //类别,小学/中学
	private String fullName; //全称
	
	/**
	 * @return Returns the categoty.
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param categoty The categoty to set.
	 */
	public void setCategory(String categoty) {
		this.category = categoty;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
