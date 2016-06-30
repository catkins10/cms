package com.yuanluesoft.asc.complaint.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 96123:在线投诉(asc_complaint)
 * @author linchuan
 *
 */
public class AscComplaint extends PublicService {
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