package com.yuanluesoft.j2oa.meeting.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 会议信息(meeting_meeting)
 * @author linchuan
 *
 */
public class Meeting extends WorkflowData {
	private String subject; //名称
	private String content; //议题
	private String address; //会议地点
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private char issued = '0'; //是否发布
	private Timestamp issueTime; //发布时间
	private long handlerId; //经办人ID
	private String handlerName; //经办人
	private Timestamp created; //创建时间
	private String reamrk; //备注
	
	/**
	 * @return Returns the beginTime.
	 */
	public java.sql.Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime The beginTime to set.
	 */
	public void setBeginTime(java.sql.Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return Returns the content.
	 */
	public java.lang.String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	/**
	 * @return Returns the endTime.
	 */
	public java.sql.Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(java.sql.Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return Returns the handlerId.
	 */
	public long getHandlerId() {
		return handlerId;
	}
	/**
	 * @param handlerId The handlerId to set.
	 */
	public void setHandlerId(long handlerId) {
		this.handlerId = handlerId;
	}
	/**
	 * @return Returns the reamrk.
	 */
	public java.lang.String getReamrk() {
		return reamrk;
	}
	/**
	 * @param reamrk The reamrk to set.
	 */
	public void setReamrk(java.lang.String reamrk) {
		this.reamrk = reamrk;
	}
	/**
	 * @return Returns the subject.
	 */
	public java.lang.String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(java.lang.String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the address.
	 */
	public java.lang.String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	/**
	 * @return Returns the issued.
	 */
	public char getIssued() {
		return issued;
	}
	/**
	 * @param issued The issued to set.
	 */
	public void setIssued(char issued) {
		this.issued = issued;
	}
	/**
	 * @return the handlerName
	 */
	public String getHandlerName() {
		return handlerName;
	}
	/**
	 * @param handlerName the handlerName to set
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
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
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
}