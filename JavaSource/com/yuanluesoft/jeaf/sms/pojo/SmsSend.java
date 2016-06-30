package com.yuanluesoft.jeaf.sms.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 短信发送(sms_send)
 * @author linchuan
 *
 */
public class SmsSend extends Record {
	private String smsClientName; //短信客户端名称,多个短信客户端时,用来识别客户端
	private long taskId; //任务ID
	private long sourceRecordId; //源记录ID
	private long senderId; //发送人ID
	private String senderName; //发送人姓名
	private long senderUnitId; //单位ID
	private String senderUnit; //单位名称
	private long businessId; //短信业务ID
	private String businessName; //短信业务名称
	private long receiverId; //接收人ID
	private String receiverName; //接收人姓名
	private String receiverNumber; //接收人号码
	private String message; //短信内容
	private int splitCount; //分拆条数
	private String senderNumber; //短信发送号码
	private Timestamp created; //创建时间
	private Timestamp sendTime; //发送时间
	private String messageId; //短信ID,由短信客户端返回
	private Timestamp arriveTime; //短信到达时间,需要短信服务器支持
	private char arriveCheck = '0'; //是否需要到达检查
	private String remark; //备注

	/**
	 * @return the arriveTime
	 */
	public Timestamp getArriveTime() {
		return arriveTime;
	}
	/**
	 * @param arriveTime the arriveTime to set
	 */
	public void setArriveTime(Timestamp arriveTime) {
		this.arriveTime = arriveTime;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the receiverId
	 */
	public long getReceiverId() {
		return receiverId;
	}
	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}
	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return the receiverNumber
	 */
	public String getReceiverNumber() {
		return receiverNumber;
	}
	/**
	 * @param receiverNumber the receiverNumber to set
	 */
	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}
	/**
	 * @return the senderId
	 */
	public long getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
	 * @return the splitCount
	 */
	public int getSplitCount() {
		return splitCount;
	}
	/**
	 * @param splitCount the splitCount to set
	 */
	public void setSplitCount(int splitCount) {
		this.splitCount = splitCount;
	}
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the arriveCheck
	 */
	public char getArriveCheck() {
		return arriveCheck;
	}
	/**
	 * @param arriveCheck the arriveCheck to set
	 */
	public void setArriveCheck(char arriveCheck) {
		this.arriveCheck = arriveCheck;
	}
	/**
	 * @return the senderNumber
	 */
	public String getSenderNumber() {
		return senderNumber;
	}
	/**
	 * @param senderNumber the senderNumber to set
	 */
	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	/**
	 * @return the senderUnit
	 */
	public String getSenderUnit() {
		return senderUnit;
	}
	/**
	 * @param senderUnit the senderUnit to set
	 */
	public void setSenderUnit(String senderUnit) {
		this.senderUnit = senderUnit;
	}
	/**
	 * @return the senderUnitId
	 */
	public long getSenderUnitId() {
		return senderUnitId;
	}
	/**
	 * @param senderUnitId the senderUnitId to set
	 */
	public void setSenderUnitId(long senderUnitId) {
		this.senderUnitId = senderUnitId;
	}
	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the businessId
	 */
	public long getBusinessId() {
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the smsClientName
	 */
	public String getSmsClientName() {
		return smsClientName;
	}
	/**
	 * @param smsClientName the smsClientName to set
	 */
	public void setSmsClientName(String smsClientName) {
		this.smsClientName = smsClientName;
	}
	/**
	 * @return the sourceRecordId
	 */
	public long getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(long sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
}