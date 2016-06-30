package com.yuanluesoft.appraise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:参评单位(appraise_participate_unit)
 * @author linchuan
 *
 */
public class AppraiseParticipateUnit extends Record {
	private long areaId; //地区ID
	private String area; //地区
	private int year; //年度
	private String category; //分类,经济和社会管理部门、行政执法监督部门、公共服务行业
	private String unitIds; //参评单位ID列表
	private String unitNames; //参评单位列表
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the unitIds
	 */
	public String getUnitIds() {
		return unitIds;
	}
	/**
	 * @param unitIds the unitIds to set
	 */
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	/**
	 * @return the unitNames
	 */
	public String getUnitNames() {
		return unitNames;
	}
	/**
	 * @param unitNames the unitNames to set
	 */
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
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