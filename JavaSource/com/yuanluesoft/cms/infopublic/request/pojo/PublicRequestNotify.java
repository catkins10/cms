package com.yuanluesoft.cms.infopublic.request.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息公开:申请告知书(public_request_notify)
 * @author linchuan
 *
 */
public class PublicRequestNotify extends Record {
	private long requestId; //申请ID
	private String notify; //申请告知书
	private Timestamp created; //告知时间
	private long creatorId; //告知人ID
	private String creator; //告知人
	private String creatorUnit; //告知人所在单位
	private PublicRequest publicRequest; //信息公开申请
	
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
	 * @return the creatorUnit
	 */
	public String getCreatorUnit() {
		return creatorUnit;
	}
	/**
	 * @param creatorUnit the creatorUnit to set
	 */
	public void setCreatorUnit(String creatorUnit) {
		this.creatorUnit = creatorUnit;
	}
	/**
	 * @return the notify
	 */
	public String getNotify() {
		return notify;
	}
	/**
	 * @param notify the notify to set
	 */
	public void setNotify(String notify) {
		this.notify = notify;
	}
	/**
	 * @return the requestId
	 */
	public long getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the publicRequest
	 */
	public PublicRequest getPublicRequest() {
		return publicRequest;
	}
	/**
	 * @param publicRequest the publicRequest to set
	 */
	public void setPublicRequest(PublicRequest publicRequest) {
		this.publicRequest = publicRequest;
	}
}