package com.yuanluesoft.bidding.project.ask.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 提问(bidding_project_ask)
 * @author linchuan
 *
 */
public class BiddingProjectAsk extends WorkflowData {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private long enterpriseId; //企业ID
	private String enterpriseName; //企业名称
	private long askPersonId; //提问用户ID
	private String askPersonName; //提问用户名称
	private String askFrom; //发起点,如:招标公告、中标公示等
	private Timestamp askTime; //提问时间
	private String question; //内容
	private String reply; //答复
	private long replierId; //答复人ID
	private String replierName; //答复人
	private char isPublic = '0'; //是否公开,不公开时，只有提问人自己看得到
	
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
	/**
	 * @return the isPublic
	 */
	public char getIsPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(char isPublic) {
		this.isPublic = isPublic;
	}
	/**
	 * @return the replierId
	 */
	public long getReplierId() {
		return replierId;
	}
	/**
	 * @param replierId the replierId to set
	 */
	public void setReplierId(long replierId) {
		this.replierId = replierId;
	}
	/**
	 * @return the replierName
	 */
	public String getReplierName() {
		return replierName;
	}
	/**
	 * @param replierName the replierName to set
	 */
	public void setReplierName(String replierName) {
		this.replierName = replierName;
	}
	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
}
