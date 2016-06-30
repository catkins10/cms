package com.yuanluesoft.fdi.customer.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 客户联系人:往来情况(fdi_customer_contact_discuss)
 * @author linchuan
 *
 */
public class FdiCustomerContactDiscuss extends Record {
	private long contactId; //客户联系人ID
	private Timestamp discussTime; //时间
	private String discussAddress; //地点
	private String discussPerson; //洽谈人
	private String discussContent; //洽谈内容
	
	/**
	 * @return the contactId
	 */
	public long getContactId() {
		return contactId;
	}
	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	/**
	 * @return the discussAddress
	 */
	public String getDiscussAddress() {
		return discussAddress;
	}
	/**
	 * @param discussAddress the discussAddress to set
	 */
	public void setDiscussAddress(String discussAddress) {
		this.discussAddress = discussAddress;
	}
	/**
	 * @return the discussContent
	 */
	public String getDiscussContent() {
		return discussContent;
	}
	/**
	 * @param discussContent the discussContent to set
	 */
	public void setDiscussContent(String discussContent) {
		this.discussContent = discussContent;
	}
	/**
	 * @return the discussPerson
	 */
	public String getDiscussPerson() {
		return discussPerson;
	}
	/**
	 * @param discussPerson the discussPerson to set
	 */
	public void setDiscussPerson(String discussPerson) {
		this.discussPerson = discussPerson;
	}
	/**
	 * @return the discussTime
	 */
	public Timestamp getDiscussTime() {
		return discussTime;
	}
	/**
	 * @param discussTime the discussTime to set
	 */
	public void setDiscussTime(Timestamp discussTime) {
		this.discussTime = discussTime;
	}
}