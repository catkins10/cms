package com.yuanluesoft.bidding.biddingroom.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent;

/**
 * 开标室安排(bidding_room_schedule)
 * @author linchuan
 *
 */
public class BiddingRoomSchedule extends BiddingProjectComponent {
	private String roomType; //开标/评标
	private long roomId; //开标室ID
	private String roomName; //开标室名称
	private Timestamp beginTime; //使用开始时间
	private Timestamp endTime; //使用结束时间
	
	private BiddingProjectAgent agent; //中选代理
	
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
	 * @return the agent
	 */
	public BiddingProjectAgent getAgent() {
		return agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(BiddingProjectAgent agent) {
		this.agent = agent;
	}
}
