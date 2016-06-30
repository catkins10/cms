package com.yuanluesoft.jeaf.point.model;

/**
 * 积分项目
 * @author linchuan
 *
 */
public class PointItem {
	private String itemName; //积分项目名称
	private int itemCount; //积分项目个数
	
	public PointItem() {
		super();
	}
	
	public PointItem(String itemName, int itemCount) {
		super();
		this.itemName = itemName;
		this.itemCount = itemCount;
	}

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
	 * @return the itemCount
	 */
	public int getItemCount() {
		return itemCount;
	}
	/**
	 * @param itemCount the itemCount to set
	 */
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
}