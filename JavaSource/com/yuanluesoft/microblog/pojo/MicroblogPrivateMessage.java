package com.yuanluesoft.microblog.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微博:私信(microblog_private_message)
 * @author linchuan
 *
 */
public class MicroblogPrivateMessage extends Record {
	private long unitId; //单位ID
	private long accountId; //微博帐号ID
	private String platform; //微博平台,新浪微博,腾讯微博,搜狐微博
	private String receiverId; //消息的接收者ID
	private String sender; //消息的发送者
	private String senderId; //消息的发送者ID
	private Timestamp created; //消息创建时间
	private String type; //消息类型,text/position/voice/image
	private String content; //私信内容
	private String locationX; //地理位置纬度
	private String locationY; //地理位置经度
	private String locationLabel; //地理位置信息
	private Timestamp replyTime; //答复时间
	private long replierId; //答复人ID
	private String replier; //答复人
	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
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
	 * @return the locationLabel
	 */
	public String getLocationLabel() {
		return locationLabel;
	}
	/**
	 * @param locationLabel the locationLabel to set
	 */
	public void setLocationLabel(String locationLabel) {
		this.locationLabel = locationLabel;
	}
	/**
	 * @return the locationX
	 */
	public String getLocationX() {
		return locationX;
	}
	/**
	 * @param locationX the locationX to set
	 */
	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}
	/**
	 * @return the locationY
	 */
	public String getLocationY() {
		return locationY;
	}
	/**
	 * @param locationY the locationY to set
	 */
	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	/**
	 * @return the receiverId
	 */
	public String getReceiverId() {
		return receiverId;
	}
	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	/**
	 * @return the replier
	 */
	public String getReplier() {
		return replier;
	}
	/**
	 * @param replier the replier to set
	 */
	public void setReplier(String replier) {
		this.replier = replier;
	}
	/**
	 * @return the replierId
	 */
	public long getReplierId() {
		return replierId;
	}
	/**
	 * @param replierId the replierId to set
	 */
	public void setReplierId(long replierId) {
		this.replierId = replierId;
	}
	/**
	 * @return the replyTime
	 */
	public Timestamp getReplyTime() {
		return replyTime;
	}
	/**
	 * @param replyTime the replyTime to set
	 */
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}