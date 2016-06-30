package com.yuanluesoft.telex.receive.base.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:签收清退(telex_sign)
 * @author linchuan
 *
 */
public class TelegramSign extends Record {
	private long telegramId; //电报ID
	private long receiverId; //签收单位/个人ID
	private String receiverName; //签收单位/个人
	private Timestamp created; //设置时间
	private long creatorId; //设置人ID
	private String creator; //设置人姓名
	private Timestamp signTime; //签收时间,空则为待签收
	private long signPersonId; //签收人ID
	private String signPersonName; //签收人姓名
	private char isAgentSign = '0'; //是否代签收
	private long signOperatorId; //签收时经办人ID
	private String signOperatorName; //签收时经办人姓名
	private Timestamp returnTime; //清退时间
	private long returnPersonId; //清退人ID
	private String returnPersonName; //清退人姓名
	private char isAgentReturn = '0'; //是否代清退
	private long returnOperatorId; //清退时经办人ID
	private String returnOperatorName; //清退时经办人姓名
	private char needReturn = '0'; //是否需要清退
	
	private ReceiveTelegram telegram; //电报
	
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
	 * @return the isAgentReturn
	 */
	public char getIsAgentReturn() {
		return isAgentReturn;
	}
	/**
	 * @param isAgentReturn the isAgentReturn to set
	 */
	public void setIsAgentReturn(char isAgentReturn) {
		this.isAgentReturn = isAgentReturn;
	}
	/**
	 * @return the isAgentSign
	 */
	public char getIsAgentSign() {
		return isAgentSign;
	}
	/**
	 * @param isAgentSign the isAgentSign to set
	 */
	public void setIsAgentSign(char isAgentSign) {
		this.isAgentSign = isAgentSign;
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
	 * @return the returnOperatorId
	 */
	public long getReturnOperatorId() {
		return returnOperatorId;
	}
	/**
	 * @param returnOperatorId the returnOperatorId to set
	 */
	public void setReturnOperatorId(long returnOperatorId) {
		this.returnOperatorId = returnOperatorId;
	}
	/**
	 * @return the returnOperatorName
	 */
	public String getReturnOperatorName() {
		return returnOperatorName;
	}
	/**
	 * @param returnOperatorName the returnOperatorName to set
	 */
	public void setReturnOperatorName(String returnOperatorName) {
		this.returnOperatorName = returnOperatorName;
	}
	/**
	 * @return the returnPersonId
	 */
	public long getReturnPersonId() {
		return returnPersonId;
	}
	/**
	 * @param returnPersonId the returnPersonId to set
	 */
	public void setReturnPersonId(long returnPersonId) {
		this.returnPersonId = returnPersonId;
	}
	/**
	 * @return the returnPersonName
	 */
	public String getReturnPersonName() {
		return returnPersonName;
	}
	/**
	 * @param returnPersonName the returnPersonName to set
	 */
	public void setReturnPersonName(String returnPersonName) {
		this.returnPersonName = returnPersonName;
	}
	/**
	 * @return the returnTime
	 */
	public Timestamp getReturnTime() {
		return returnTime;
	}
	/**
	 * @param returnTime the returnTime to set
	 */
	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}
	/**
	 * @return the signOperatorId
	 */
	public long getSignOperatorId() {
		return signOperatorId;
	}
	/**
	 * @param signOperatorId the signOperatorId to set
	 */
	public void setSignOperatorId(long signOperatorId) {
		this.signOperatorId = signOperatorId;
	}
	/**
	 * @return the signOperatorName
	 */
	public String getSignOperatorName() {
		return signOperatorName;
	}
	/**
	 * @param signOperatorName the signOperatorName to set
	 */
	public void setSignOperatorName(String signOperatorName) {
		this.signOperatorName = signOperatorName;
	}
	/**
	 * @return the signPersonId
	 */
	public long getSignPersonId() {
		return signPersonId;
	}
	/**
	 * @param signPersonId the signPersonId to set
	 */
	public void setSignPersonId(long signPersonId) {
		this.signPersonId = signPersonId;
	}
	/**
	 * @return the signPersonName
	 */
	public String getSignPersonName() {
		return signPersonName;
	}
	/**
	 * @param signPersonName the signPersonName to set
	 */
	public void setSignPersonName(String signPersonName) {
		this.signPersonName = signPersonName;
	}
	/**
	 * @return the signTime
	 */
	public Timestamp getSignTime() {
		return signTime;
	}
	/**
	 * @param signTime the signTime to set
	 */
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	/**
	 * @return the telegramId
	 */
	public long getTelegramId() {
		return telegramId;
	}
	/**
	 * @param telegramId the telegramId to set
	 */
	public void setTelegramId(long telegramId) {
		this.telegramId = telegramId;
	}
	/**
	 * @return the telegram
	 */
	public ReceiveTelegram getTelegram() {
		return telegram;
	}
	/**
	 * @param telegram the telegram to set
	 */
	public void setTelegram(ReceiveTelegram telegram) {
		this.telegram = telegram;
	}
	/**
	 * @return the needReturn
	 */
	public char getNeedReturn() {
		return needReturn;
	}
	/**
	 * @param needReturn the needReturn to set
	 */
	public void setNeedReturn(char needReturn) {
		this.needReturn = needReturn;
	}
}
