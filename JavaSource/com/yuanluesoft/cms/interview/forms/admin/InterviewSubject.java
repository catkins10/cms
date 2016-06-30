package com.yuanluesoft.cms.interview.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewSubject extends ActionForm {
	private String subject; //主题
	private String background; //访谈背景
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间,创建时预设，实际时间由主持人设置
	private String guests; //嘉宾
	private String guestsIntro; //嘉宾介绍,逗号分隔
	private String guestsPassword; //嘉宾登录密码
	private String compereIds; //主持人ID列表
	private String compereNames; //主持人姓名列表
	private String speakFlow; //发言审核顺序,发言的审核，定义各种角色的顺序，如：审核人,复核人,嘉宾,主持人
	private String compereSpeakFlow; //主持人发言审核顺序
	private String guestsSpeakFlow; //嘉宾发言审核顺序
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private char isEnding = '0'; //是否结束
	private long siteId; //站点ID
	private Set roles; //主题对应的角色列表
	private Set interviewSpeaks; //访谈发言列表
	private Set interviewImages; //访谈图片列表
	private Set interviewVideos; //访谈视频列表
	
	private String roleNames; //审核人角色以及主持人和嘉宾
	
	/**
	 * @return the background
	 */
	public String getBackground() {
		return background;
	}
	/**
	 * @param background the background to set
	 */
	public void setBackground(String background) {
		this.background = background;
	}
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the compereIds
	 */
	public String getCompereIds() {
		return compereIds;
	}
	/**
	 * @param compereIds the compereIds to set
	 */
	public void setCompereIds(String compereIds) {
		this.compereIds = compereIds;
	}
	/**
	 * @return the compereNames
	 */
	public String getCompereNames() {
		return compereNames;
	}
	/**
	 * @param compereNames the compereNames to set
	 */
	public void setCompereNames(String compereNames) {
		this.compereNames = compereNames;
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
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the guests
	 */
	public String getGuests() {
		return guests;
	}
	/**
	 * @param guests the guests to set
	 */
	public void setGuests(String guests) {
		this.guests = guests;
	}
	/**
	 * @return the guestsIntro
	 */
	public String getGuestsIntro() {
		return guestsIntro;
	}
	/**
	 * @param guestsIntro the guestsIntro to set
	 */
	public void setGuestsIntro(String guestsIntro) {
		this.guestsIntro = guestsIntro;
	}
	/**
	 * @return the guestsPassword
	 */
	public String getGuestsPassword() {
		return guestsPassword;
	}
	/**
	 * @param guestsPassword the guestsPassword to set
	 */
	public void setGuestsPassword(String guestsPassword) {
		this.guestsPassword = guestsPassword;
	}
	/**
	 * @return the roles
	 */
	public Set getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set roles) {
		this.roles = roles;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the speakFlow
	 */
	public String getSpeakFlow() {
		return speakFlow;
	}
	/**
	 * @param speakFlow the speakFlow to set
	 */
	public void setSpeakFlow(String speakFlow) {
		this.speakFlow = speakFlow;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the isEnding
	 */
	public char getIsEnding() {
		return isEnding;
	}
	/**
	 * @param isEnding the isEnding to set
	 */
	public void setIsEnding(char isEnding) {
		this.isEnding = isEnding;
	}
	/**
	 * @return the roleNames
	 */
	public String getRoleNames() {
		return roleNames;
	}
	/**
	 * @param roleNames the roleNames to set
	 */
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	/**
	 * @return the compereSpeakFlow
	 */
	public String getCompereSpeakFlow() {
		return compereSpeakFlow;
	}
	/**
	 * @param compereSpeakFlow the compereSpeakFlow to set
	 */
	public void setCompereSpeakFlow(String compereSpeakFlow) {
		this.compereSpeakFlow = compereSpeakFlow;
	}
	/**
	 * @return the guestsSpeakFlow
	 */
	public String getGuestsSpeakFlow() {
		return guestsSpeakFlow;
	}
	/**
	 * @param guestsSpeakFlow the guestsSpeakFlow to set
	 */
	public void setGuestsSpeakFlow(String guestsSpeakFlow) {
		this.guestsSpeakFlow = guestsSpeakFlow;
	}
	/**
	 * @return the interviewImages
	 */
	public Set getInterviewImages() {
		return interviewImages;
	}
	/**
	 * @param interviewImages the interviewImages to set
	 */
	public void setInterviewImages(Set interviewImages) {
		this.interviewImages = interviewImages;
	}
	/**
	 * @return the interviewVideos
	 */
	public Set getInterviewVideos() {
		return interviewVideos;
	}
	/**
	 * @param interviewVideos the interviewVideos to set
	 */
	public void setInterviewVideos(Set interviewVideos) {
		this.interviewVideos = interviewVideos;
	}
	/**
	 * @return the interviewSpeaks
	 */
	public Set getInterviewSpeaks() {
		return interviewSpeaks;
	}
	/**
	 * @param interviewSpeaks the interviewSpeaks to set
	 */
	public void setInterviewSpeaks(Set interviewSpeaks) {
		this.interviewSpeaks = interviewSpeaks;
	}
}