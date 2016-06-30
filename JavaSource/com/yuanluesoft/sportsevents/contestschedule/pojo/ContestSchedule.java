package com.yuanluesoft.sportsevents.contestschedule.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
/**
 * 体育赛程安排
 * @author kangshiwei
 *
 */
public class ContestSchedule extends Record {
	private String gameName;//比赛名称
	private Timestamp beginTime;//开始时间
	private Timestamp endTime;//结束时间
	private String address;//比赛地点
	private long creatorId;//发布人ID
	private String creator;//发布人
	private Timestamp created;//发布时间
	private String remark;//备注
	
	/**
	 * @return beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime 要设置的 beginTime
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created 要设置的 created
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator 要设置的 creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId 要设置的 creatorId
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime 要设置的 endTime
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return gameName
	 */
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName 要设置的 gameName
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address 要设置的 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark 要设置的 remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
