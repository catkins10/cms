package com.yuanluesoft.enterprise.exam.learn.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 学习:任务(exam_learn_mission)
 * @author linchuan
 *
 */
public class LearnMission extends Record {
	private String name; //名称
	private long fileId; //学习资料ID
	private String fileName; //学习资料名称
	private Timestamp beginTime; //学习开始时间
	private Timestamp endTime; //学习结束时间,如果设置结束时间,达到截止时间后,不允许再进入
	private int learnTime; //学习时长,以分钟为单位
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Set visitors; //参与人员
	private Set accomplishs; //完成情况
	
	//扩展属性
	private LearnFile learnFile; //学习资料
	private int learnedTime; //已学习时长,以分钟为单位
	
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
	 * @return the fileId
	 */
	public long getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	/**
	 * @return the learnTime
	 */
	public int getLearnTime() {
		return learnTime;
	}
	/**
	 * @param learnTime the learnTime to set
	 */
	public void setLearnTime(int learnTime) {
		this.learnTime = learnTime;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the accomplishs
	 */
	public Set getAccomplishs() {
		return accomplishs;
	}
	/**
	 * @param accomplishs the accomplishs to set
	 */
	public void setAccomplishs(Set accomplishs) {
		this.accomplishs = accomplishs;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the learnFile
	 */
	public LearnFile getLearnFile() {
		return learnFile;
	}
	/**
	 * @param learnFile the learnFile to set
	 */
	public void setLearnFile(LearnFile learnFile) {
		this.learnFile = learnFile;
	}
	/**
	 * @return the learnedTime
	 */
	public int getLearnedTime() {
		return learnedTime;
	}
	/**
	 * @param learnedTime the learnedTime to set
	 */
	public void setLearnedTime(int learnedTime) {
		this.learnedTime = learnedTime;
	}
}