package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;

/**
 * 开标公示(bidding_project_bidopening)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectBidopening extends SuperviseBiddingProjectComponent {
	private String unitCode; //单位编码
	private String unitName; //单位名称
	private String room; //开标室
	private Timestamp beginTime; //开标时间
	private Timestamp endTime; //截标时间
	private String linkman; //联系人
	private String tel; //联系电话
	private int timeLimit; //工期
	private String serviceMan; //服务人员
	private String status; //开标状态
	private String description; //开标情况描述
	private String compere; //主持人
	private String callouter; //唱标人
	private String recorder; //记录人
	private String surverllant; //监标人
	private char synch = '0'; //是否同步过
	private String body; //正文,没有固定模板时使用
	
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
	 * @return the callouter
	 */
	public String getCallouter() {
		return callouter;
	}
	/**
	 * @param callouter the callouter to set
	 */
	public void setCallouter(String callouter) {
		this.callouter = callouter;
	}
	/**
	 * @return the compere
	 */
	public String getCompere() {
		return compere;
	}
	/**
	 * @param compere the compere to set
	 */
	public void setCompere(String compere) {
		this.compere = compere;
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
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the recorder
	 */
	public String getRecorder() {
		return recorder;
	}
	/**
	 * @param recorder the recorder to set
	 */
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}
	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	/**
	 * @return the serviceMan
	 */
	public String getServiceMan() {
		return serviceMan;
	}
	/**
	 * @param serviceMan the serviceMan to set
	 */
	public void setServiceMan(String serviceMan) {
		this.serviceMan = serviceMan;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the surverllant
	 */
	public String getSurverllant() {
		return surverllant;
	}
	/**
	 * @param surverllant the surverllant to set
	 */
	public void setSurverllant(String surverllant) {
		this.surverllant = surverllant;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the timeLimit
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
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
	 * @return the synch
	 */
	public char getSynch() {
		return synch;
	}
	/**
	 * @param synch the synch to set
	 */
	public void setSynch(char synch) {
		this.synch = synch;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
