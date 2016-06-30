package com.yuanluesoft.cms.monitor.sms.forms;

import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.forms.MonitorRecord;

/**
 * 监察:短信发送(monitor_sms_send)
 * @author linchuan
 *
 */
public class SmsSend extends MonitorRecord {
	private String content; //短信内容
	private String category; //分类
	private String creator; //创建者
	private Timestamp created; //创建时间
	private int sendCount; //发送条数
	private Timestamp sendTime; //发送时间
	
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the sendCount
	 */
	public int getSendCount() {
		return sendCount;
	}
	/**
	 * @param sendCount the sendCount to set
	 */
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
}