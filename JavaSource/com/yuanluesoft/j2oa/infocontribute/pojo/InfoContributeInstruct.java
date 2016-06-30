package com.yuanluesoft.j2oa.infocontribute.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投稿:领导批示(info_contribute_instruct)
 * @author linchuan
 *
 */
public class InfoContributeInstruct extends Record {
	private long infoId; //稿件ID
	private long instructId; //批示ID,采编端ID
	private String leader; //领导姓名
	private int level; //领导级别,1/县,2/市,3/省,4/国
	private String instruct; //批示内容
	private Timestamp instructTime; //批示时间
	private long creatorId; //录入人ID
	private String creator; //录入人
	private Timestamp created; //录入时间
	private InfoContribute info; //投稿
	
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
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}
	/**
	 * @return the instruct
	 */
	public String getInstruct() {
		return instruct;
	}
	/**
	 * @param instruct the instruct to set
	 */
	public void setInstruct(String instruct) {
		this.instruct = instruct;
	}
	/**
	 * @return the instructTime
	 */
	public Timestamp getInstructTime() {
		return instructTime;
	}
	/**
	 * @param instructTime the instructTime to set
	 */
	public void setInstructTime(Timestamp instructTime) {
		this.instructTime = instructTime;
	}
	/**
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @return the instructId
	 */
	public long getInstructId() {
		return instructId;
	}
	/**
	 * @param instructId the instructId to set
	 */
	public void setInstructId(long instructId) {
		this.instructId = instructId;
	}
	/**
	 * @return the info
	 */
	public InfoContribute getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(InfoContribute info) {
		this.info = info;
	}
}