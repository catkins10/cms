/*
 * Created on 2005-9-10
 *
 */
package com.yuanluesoft.j2oa.document.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Keyword extends ActionForm {
	private String district; //区域分类
	private String category; //类别词
	private char mainTable = '0'; //是否主表，TODO:暂时不用
	private String keywordList; //主题词列表
	
	/**
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return Returns the district.
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district The district to set.
	 */
	public void setDistrict(String district) {
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
	 * @return Returns the keywordList.
	 */
	public String getKeywordList() {
		return keywordList;
	}
	/**
	 * @param keywordList The keywordList to set.
	 */
	public void setKeywordList(String keywordList) {
		this.keywordList = keywordList;
	}
}