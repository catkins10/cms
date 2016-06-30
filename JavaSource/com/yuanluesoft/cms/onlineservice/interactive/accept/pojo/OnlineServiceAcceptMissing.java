package com.yuanluesoft.cms.onlineservice.interactive.accept.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:缺件通知(onlineservice_accept_missing)
 * @author linchuan
 *
 */
public class OnlineServiceAcceptMissing extends Record {
	private long acceptId; //收件ID
	private String description; //缺件说明
	private Timestamp created; //创建时间
	private long creatorId; //通知人ID
	private String creator; //通知人
	
	/**
	 * @return the acceptId
	 */
	public long getAcceptId() {
		return acceptId;
	}
	/**
	 * @param acceptId the acceptId to set
	 */
	public void setAcceptId(long acceptId) {
		this.acceptId = acceptId;
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
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
