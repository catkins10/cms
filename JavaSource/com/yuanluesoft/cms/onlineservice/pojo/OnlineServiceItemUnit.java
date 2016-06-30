package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:办理机构(onlineservice_item_unit)
 * @author linchuan
 *
 */
public class OnlineServiceItemUnit extends Record {
	private long itemId; //办理事项ID
	private long unitId; //单位ID
	private String unitName; //单位名称
	
	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
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
}
