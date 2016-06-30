package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 提问(bidding_project_ask)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectAsk extends Record {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private long enterpriseId; //企业ID
	private String enterpriseName; //企业名称
	private long askPersonId; //提问用户ID
	private String askPersonName; //提问用户名称
	private String askFrom; //发起点,如:招标公告、中标公示等
	private Timestamp askTime; //提问时间
	private String question; //内容
	
	/**
	 * @return the askPersonId
	 */
	public long getAskPersonId() {
		return askPersonId;
	}
	/**
	 * @param askPersonId the askPersonId to set
	 */
	public void setAskPersonId(long askPersonId) {
		this.askPersonId = askPersonId;
	}
	/**
	 * @return the askPersonName
	 */
	public String getAskPersonName() {
		return askPersonName;
	}
	/**
	 * @param askPersonName the askPersonName to set
	 */
	public void setAskPersonName(String askPersonName) {
		this.askPersonName = askPersonName;
	}
	/**
	 * @return the askTime
	 */
	public Timestamp getAskTime() {
		return askTime;
	}
	/**
	 * @param askTime the askTime to set
	 */
	public void setAskTime(Timestamp askTime) {
		this.askTime = askTime;
	}
	/**
	 * @return the enterpriseId
	 */
	public long getEnterpriseId() {
		return enterpriseId;
	}
	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
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
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the askFrom
	 */
	public String getAskFrom() {
		return askFrom;
	}
	/**
	 * @param askFrom the askFrom to set
	 */
	public void setAskFrom(String askFrom) {
		this.askFrom = askFrom;
	}
}
