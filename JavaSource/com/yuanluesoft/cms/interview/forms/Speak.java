package com.yuanluesoft.cms.interview.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Speak extends ActionForm {
	private long subjectId; //主题ID
	private String speaker; //发言人姓名
	private String speakerIP; //发言人IP
	private String speakerContacts; //发言人联系方式
	private int speakerType; //发言人类型,主持人、嘉宾、网络用户
	private String content; //内容
	private Timestamp speakTime; //发言时间
	private char isLeave = '0'; //是否留言
	private String approvalRole; //当前审核人角色,如果是空的,表示已经审核完成
	private Timestamp publishTime; //发布时间,注：如果审核没通过，直接删除
	
	private String validateCode; //验证码
	
	/**
	 * @return the approvalRole
	 */
	public String getApprovalRole() {
		return approvalRole;
	}
	/**
	 * @param approvalRole the approvalRole to set
	 */
	public void setApprovalRole(String approvalRole) {
		this.approvalRole = approvalRole;
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
	 * @return the speaker
	 */
	public String getSpeaker() {
		return speaker;
	}
	/**
	 * @param speaker the speaker to set
	 */
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	/**
	 * @return the speakerContacts
	 */
	public String getSpeakerContacts() {
		return speakerContacts;
	}
	/**
	 * @param speakerContacts the speakerContacts to set
	 */
	public void setSpeakerContacts(String speakerContacts) {
		this.speakerContacts = speakerContacts;
	}
	/**
	 * @return the speakerIP
	 */
	public String getSpeakerIP() {
		return speakerIP;
	}
	/**
	 * @param speakerIP the speakerIP to set
	 */
	public void setSpeakerIP(String speakerIP) {
		this.speakerIP = speakerIP;
	}
	/**
	 * @return the speakerType
	 */
	public int getSpeakerType() {
		return speakerType;
	}
	/**
	 * @param speakerType the speakerType to set
	 */
	public void setSpeakerType(int speakerType) {
		this.speakerType = speakerType;
	}
	/**
	 * @return the speakTime
	 */
	public Timestamp getSpeakTime() {
		return speakTime;
	}
	/**
	 * @param speakTime the speakTime to set
	 */
	public void setSpeakTime(Timestamp speakTime) {
		this.speakTime = speakTime;
	}
	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}
	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	/**
	 * @return the publishTime
	 */
	public Timestamp getPublishTime() {
		return publishTime;
	}
	/**
	 * @param publishTime the publishTime to set
	 */
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}
	/**
	 * @return the isLeave
	 */
	public char getIsLeave() {
		return isLeave;
	}
	/**
	 * @param isLeave the isLeave to set
	 */
	public void setIsLeave(char isLeave) {
		this.isLeave = isLeave;
	}
}