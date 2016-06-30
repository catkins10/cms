package com.yuanluesoft.cms.onlineservice.faq.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 96123:常见问题解答隶属事项(onlineservice_faq_item)
 * @author linchuan
 *
 */
public class OnlineServiceFaqItem extends Record {
	private long faqId; //FAQID
	private long itemId; //办理事项ID
	private String itemName; //办理事项名称
	
	/**
	 * @return the faqId
	 */
	public long getFaqId() {
		return faqId;
	}
	/**
	 * @param faqId the faqId to set
	 */
	public void setFaqId(long faqId) {
		this.faqId = faqId;
	}
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