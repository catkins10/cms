package com.yuanluesoft.cms.infopublic.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfoSequence extends Record {
	private int year; //年度
	private int sequence; //序号
	private String category; //信息类目代码
	
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
