package com.yuanluesoft.cms.onlineservice.model;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceItemType {
	private String itemType; //类别

	public OnlineServiceItemType(String itemType) {
		super();
		this.itemType = itemType;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
}