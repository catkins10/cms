package com.yuanluesoft.appraise.model;

/**
 * 参评单位
 * @author linchuan
 *
 */
public class ParticipateUnit {
	private long areaId; //地区ID
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String category; //分类
	
	public ParticipateUnit(long areaId, long unitId, String unitName, String category) {
		super();
		this.areaId = areaId;
		this.unitId = unitId;
		this.unitName = unitName;
		this.category = category;
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
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
}