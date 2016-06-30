package com.yuanluesoft.bidding.biddingroom.forms;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;

/**
 * 选择开评标室
 * @author linchuan
 *
 */
public class Select extends SelectDialog {
	private String roomType; //开标/评标
	private String city; //所在地区
	private Timestamp beginTime; //开始使用时间
	private Timestamp endTime; //结束使用时间
	private List freeRooms; //空闲开评标室
	private List allRooms; //全部开评标室
	private boolean freeRoomOnly; //只显示空闲开评标室
	private long forProjectId; //需要分配开评标室的项目
	
	/**
	 * @return the freeRooms
	 */
	public List getFreeRooms() {
		return freeRooms;
	}
	/**
	 * @param freeRooms the freeRooms to set
	 */
	public void setFreeRooms(List freeRooms) {
		this.freeRooms = freeRooms;
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
	 * @return the allRooms
	 */
	public List getAllRooms() {
		return allRooms;
	}
	/**
	 * @param allRooms the allRooms to set
	 */
	public void setAllRooms(List allRooms) {
		this.allRooms = allRooms;
	}
	/**
	 * @return the freeRoomOnly
	 */
	public boolean isFreeRoomOnly() {
		return freeRoomOnly;
	}
	/**
	 * @param freeRoomOnly the freeRoomOnly to set
	 */
	public void setFreeRoomOnly(boolean freeRoomOnly) {
		this.freeRoomOnly = freeRoomOnly;
	}
	/**
	 * @return the forProjectId
	 */
	public long getForProjectId() {
		return forProjectId;
	}
	/**
	 * @param forProjectId the forProjectId to set
	 */
	public void setForProjectId(long forProjectId) {
		this.forProjectId = forProjectId;
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