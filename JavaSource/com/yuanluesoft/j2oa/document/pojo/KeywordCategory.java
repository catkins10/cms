/*
 * Created on 2005-9-10
 *
 */
package com.yuanluesoft.j2oa.document.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公文配置:主题词类别(document_keyword_category)
 * @author linchuan
 *
 */
public class KeywordCategory extends Record {
	private char mainTable = '0'; //是否主表
	private String district; //一级分类
	private String category; //二级分类
	private java.util.Set keywords;
	
	/**
	 * @return Returns the district.
	 */
	public java.lang.String getDistrict() {
		return district;
	}
	/**
	 * @param district The district to set.
	 */
	public void setDistrict(java.lang.String district) {
		this.district = district;
	}
	/**
	 * @return Returns the mainTable.
	 */
	public char getMainTable() {
		return mainTable;
	}
	/**
	 * @param mainTable The mainTable to set.
	 */
	public void setMainTable(char mainTable) {
		this.mainTable = mainTable;
	}
	/**
	 * @return Returns the category.
	 */
	public java.lang.String getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(java.lang.String category) {
		this.category = category;
	}
	/**
	 * @return Returns the keywords.
	 */
	public java.util.Set getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(java.util.Set keywords) {
		this.keywords = keywords;
	}
}
