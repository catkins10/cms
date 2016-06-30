package com.yuanluesoft.appraise.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:投票结果(appraise_vote)
 * @author linchuan
 *
 */
public class AppraiseVote extends Record {
	private long unitAppraiseId; //单位评议ID
	private long appraiserId; //评议员ID
	private long appraiserOrgId; //评议员所在组织ID
	private String appraiser; //评议员姓名
	private String appraiserNumber; //评议员手机号码
	private int appraiserType; //评议员类型
	private long optionId; //选项ID
	private String option; //选项名称
	private double score; //分数
	private String propose; //意见或建议
	private Timestamp created; //投票时间
	private int voteMode; //投票方式,0/短信,1/网络
	private UnitAppraise unitAppraise; //单位评议
	
	/**
	 * @return the appraiser
	 */
	public String getAppraiser() {
		return appraiser;
	}
	/**
	 * @param appraiser the appraiser to set
	 */
	public void setAppraiser(String appraiser) {
		this.appraiser = appraiser;
	}
	/**
	 * @return the appraiserId
	 */
	public long getAppraiserId() {
		return appraiserId;
	}
	/**
	 * @param appraiserId the appraiserId to set
	 */
	public void setAppraiserId(long appraiserId) {
		this.appraiserId = appraiserId;
	}
	/**
	 * @return the appraiserNumber
	 */
	public String getAppraiserNumber() {
		return appraiserNumber;
	}
	/**
	 * @param appraiserNumber the appraiserNumber to set
	 */
	public void setAppraiserNumber(String appraiserNumber) {
		this.appraiserNumber = appraiserNumber;
	}
	/**
	 * @return the appraiserType
	 */
	public int getAppraiserType() {
		return appraiserType;
	}
	/**
	 * @param appraiserType the appraiserType to set
	 */
	public void setAppraiserType(int appraiserType) {
		this.appraiserType = appraiserType;
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
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
	/**
	 * @return the propose
	 */
	public String getPropose() {
		return propose;
	}
	/**
	 * @param propose the propose to set
	 */
	public void setPropose(String propose) {
		this.propose = propose;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return the voteMode
	 */
	public int getVoteMode() {
		return voteMode;
	}
	/**
	 * @param voteMode the voteMode to set
	 */
	public void setVoteMode(int voteMode) {
		this.voteMode = voteMode;
	}
	/**
	 * @return the appraiserOrgId
	 */
	public long getAppraiserOrgId() {
		return appraiserOrgId;
	}
	/**
	 * @param appraiserOrgId the appraiserOrgId to set
	 */
	public void setAppraiserOrgId(long appraiserOrgId) {
		this.appraiserOrgId = appraiserOrgId;
	}
	/**
	 * @return the unitAppraiseId
	 */
	public long getUnitAppraiseId() {
		return unitAppraiseId;
	}
	/**
	 * @param unitAppraiseId the unitAppraiseId to set
	 */
	public void setUnitAppraiseId(long unitAppraiseId) {
		this.unitAppraiseId = unitAppraiseId;
	}
	/**
	 * @return the unitAppraise
	 */
	public UnitAppraise getUnitAppraise() {
		return unitAppraise;
	}
	/**
	 * @param unitAppraise the unitAppraise to set
	 */
	public void setUnitAppraise(UnitAppraise unitAppraise) {
		this.unitAppraise = unitAppraise;
	}
}