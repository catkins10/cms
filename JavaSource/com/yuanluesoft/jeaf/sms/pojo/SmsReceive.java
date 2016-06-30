package com.yuanluesoft.jeaf.sms.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 短信接收(sms_receive)
 * @author linchuan
 *
 */
public class SmsReceive extends Record {
	private String smsClientName; //短信客户端名称,多个短信客户端时,用来识别客户端
	private String senderNumber; //发送人号码
	private String receiverNumber; //短信接收号码
	private String message; //短信内容
	private Timestamp receiveTime; //接收时间
	private String businessName; //短信业务名称
	private long businessId; //短信业务ID
	private String receiverUnit; //单位名称
	private long receiverUnitId; //单位ID
	
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
	 * @return the receiveTime
	 */
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
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
	 * @return the receiverUnit
	 */
	public String getReceiverUnit() {
		return receiverUnit;
	}
	/**
	 * @param receiverUnit the receiverUnit to set
	 */
	public void setReceiverUnit(String receiverUnit) {
		this.receiverUnit = receiverUnit;
	}
	/**
	 * @return the receiverUnitId
	 */
	public long getReceiverUnitId() {
		return receiverUnitId;
	}
	/**
	 * @param receiverUnitId the receiverUnitId to set
	 */
	public void setReceiverUnitId(long receiverUnitId) {
		this.receiverUnitId = receiverUnitId;
	}
}
