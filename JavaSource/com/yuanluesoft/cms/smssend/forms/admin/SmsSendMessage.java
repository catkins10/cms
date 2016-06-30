package com.yuanluesoft.cms.smssend.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 短信发送(sms_send_message)
 * @author linchuan
 *
 */
public class SmsSendMessage extends WorkflowForm {
	private String content; //短信内容
	private String sourceRecordId; //源记录ID
	private String sourceRecordClassName; //源记录类名称
	private String sourceRecordUrl; //源记录URL
	private String smsBusinessName; //短信业务
	private long creatorId; //创建者ID
	private String creator; //创建者
	private long unitId; //创建者所在单位ID
	private String unitName; //创建者所在单位名称
	private Timestamp created; //创建时间
	private int sendCount; //发送条数
	private Timestamp sendTime; //发送时间
	
	//扩展属性
	private RecordVisitorList receivers = new RecordVisitorList(); //短信接收人
	
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
	 * @return the sourceRecordClassName
	 */
	public String getSourceRecordClassName() {
		return sourceRecordClassName;
	}
	/**
	 * @param sourceRecordClassName the sourceRecordClassName to set
	 */
	public void setSourceRecordClassName(String sourceRecordClassName) {
		this.sourceRecordClassName = sourceRecordClassName;
	}
	/**
	 * @return the sourceRecordId
	 */
	public String getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(String sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
	/**
	 * @return the sourceRecordUrl
	 */
	public String getSourceRecordUrl() {
		return sourceRecordUrl;
	}
	/**
	 * @param sourceRecordUrl the sourceRecordUrl to set
	 */
	public void setSourceRecordUrl(String sourceRecordUrl) {
		this.sourceRecordUrl = sourceRecordUrl;
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
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the receivers
	 */
	public RecordVisitorList getReceivers() {
		return receivers;
	}
	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(RecordVisitorList receivers) {
		this.receivers = receivers;
	}
	/**
	 * @return the smsBusinessName
	 */
	public String getSmsBusinessName() {
		return smsBusinessName;
	}
	/**
	 * @param smsBusinessName the smsBusinessName to set
	 */
	public void setSmsBusinessName(String smsBusinessName) {
		this.smsBusinessName = smsBusinessName;
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