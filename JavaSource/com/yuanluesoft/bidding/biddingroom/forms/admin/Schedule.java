package com.yuanluesoft.bidding.biddingroom.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Schedule extends ActionForm {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private String biddingMode; //招标方式
	private Timestamp publicBeginTime; //公示时间
	private int publicLimit; //公示期限,以天为单位
	private Timestamp publicEndTime; //公示截止时间
	private long publicPersonId; //发布人ID
	private String publicPersonName; //发布人
	private String remark; //备注
	
	private String roomType; //开标/评标
	private long roomId; //开标室ID
	private String roomName; //开标室名称
	private Timestamp beginTime; //使用开始时间
	private Timestamp endTime; //使用结束时间
	
	//扩展参数
	private String city;
	
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
	 * @return the biddingMode
	 */
	public String getBiddingMode() {
		return biddingMode;
	}
	/**
	 * @param biddingMode the biddingMode to set
	 */
	public void setBiddingMode(String biddingMode) {
		this.biddingMode = biddingMode;
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
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the publicBeginTime
	 */
	public Timestamp getPublicBeginTime() {
		return publicBeginTime;
	}
	/**
	 * @param publicBeginTime the publicBeginTime to set
	 */
	public void setPublicBeginTime(Timestamp publicBeginTime) {
		this.publicBeginTime = publicBeginTime;
	}
	/**
	 * @return the publicEndTime
	 */
	public Timestamp getPublicEndTime() {
		return publicEndTime;
	}
	/**
	 * @param publicEndTime the publicEndTime to set
	 */
	public void setPublicEndTime(Timestamp publicEndTime) {
		this.publicEndTime = publicEndTime;
	}
	/**
	 * @return the publicLimit
	 */
	public int getPublicLimit() {
		return publicLimit;
	}
	/**
	 * @param publicLimit the publicLimit to set
	 */
	public void setPublicLimit(int publicLimit) {
		this.publicLimit = publicLimit;
	}
	/**
	 * @return the publicPersonId
	 */
	public long getPublicPersonId() {
		return publicPersonId;
	}
	/**
	 * @param publicPersonId the publicPersonId to set
	 */
	public void setPublicPersonId(long publicPersonId) {
		this.publicPersonId = publicPersonId;
	}
	/**
	 * @return the publicPersonName
	 */
	public String getPublicPersonName() {
		return publicPersonName;
	}
	/**
	 * @param publicPersonName the publicPersonName to set
	 */
	public void setPublicPersonName(String publicPersonName) {
		this.publicPersonName = publicPersonName;
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
	 * @return the roomId
	 */
	public long getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	/**
	 * @return the roomType
	 */
	public String getRoomType() {
		return roomType;
	}
	/**
	 * @param roomType the roomType to set
	 */
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
}