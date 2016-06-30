package com.yuanluesoft.enterprise.exam.learn.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 学习:任务完成情况(exam_learn_mission_accomplish)
 * @author linchuan
 *
 */
public class LearnMissionAccomplish extends Record {
	private long missionId; //任务ID
	private long personId; //用户ID
	private String personName; //用户名
	private Timestamp beginTime; //开始学习时间
	private Timestamp lastTime; //最后学习时间
	private int learnSeconds; //已学习时间数,以秒为单位
	private LearnMission mission; //任务
	
	/**
	 * 获取学习分钟数
	 * @return
	 */
	public int getLearnMinutes() {
		return learnSeconds/60;
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
	 * @return the lastTime
	 */
	public Timestamp getLastTime() {
		return lastTime;
	}
	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}
	/**
	 * @return the learnSeconds
	 */
	public int getLearnSeconds() {
		return learnSeconds;
	}
	/**
	 * @param learnSeconds the learnSeconds to set
	 */
	public void setLearnSeconds(int learnSeconds) {
		this.learnSeconds = learnSeconds;
	}
	/**
	 * @return the missionId
	 */
	public long getMissionId() {
		return missionId;
	}
	/**
	 * @param missionId the missionId to set
	 */
	public void setMissionId(long missionId) {
		this.missionId = missionId;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/**
	 * @return the mission
	 */
	public LearnMission getMission() {
		return mission;
	}

	/**
	 * @param mission the mission to set
	 */
	public void setMission(LearnMission mission) {
		this.mission = mission;
	}
}