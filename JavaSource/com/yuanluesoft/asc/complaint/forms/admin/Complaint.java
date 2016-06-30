package com.yuanluesoft.asc.complaint.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Complaint extends PublicServiceAdminForm {
	private long itemId; //办理事项ID
	private String itemName; //办理事项名称
	
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
}