package com.yuanluesoft.jeaf.point.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 积分:各单项积分(point_item)
 * @author linchuan
 *
 */
public class ItemPoint extends Record {
	private long personId; //用户ID
	private String itemName; //项目名称
	private int point; //积分
	
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
}