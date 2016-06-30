package com.yuanluesoft.dpc.keyproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 开发区分类(keyproject_dev_area_category)
 * @author linchuan
 *
 */
public class KeyProjectDevelopmentAreaCategory extends Record {
	private String category; //分类名称
	private String developmentAreas; //开发区列表
	
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
	/**
	 * @return the developmentAreas
	 */
	public String getDevelopmentAreas() {
		return developmentAreas;
	}
	/**
	 * @param developmentAreas the developmentAreas to set
	 */
	public void setDevelopmentAreas(String developmentAreas) {
		this.developmentAreas = developmentAreas;
	}
}