package com.yuanluesoft.appraise.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:评议短信(appraise_sms)
 * @author linchuan
 *
 */
public class AppraiseSms extends Record {
	private long appraiseId; //评议ID
	private long appraiserId; //评议员ID
	private long appraiserOrgId; //评议员所在组织ID
	private String appraiser; //评议员姓名
	private String appraiserNumber; //评议员手机号码
	private int appraiserType; //评议员类型
	private long appraiseUnitId; //被评议单位ID
	private String appraiseCode; //评议验证码,网上评议时使用
	private String inviteSms; //邀请短信内容
	private Timestamp inviteSmsSent; //邀请短信发送时间
	private String replySms; //回复内容
	private Timestamp replyTime; //回复时间
	private String applauseSms; //答谢短信内容
	private Timestamp applauseSmsSent; //答谢短信发送时间
	
	/**
	 * @return the applauseSms
	 */
	public String getApplauseSms() {
		return applauseSms;
	}
	/**
	 * @param applauseSms the applauseSms to set
	 */
	public void setApplauseSms(String applauseSms) {
		this.applauseSms = applauseSms;
	}
	/**
	 * @return the applauseSmsSent
	 */
	public Timestamp getApplauseSmsSent() {
		return applauseSmsSent;
	}
	/**
	 * @param applauseSmsSent the applauseSmsSent to set
	 */
	public void setApplauseSmsSent(Timestamp applauseSmsSent) {
		this.applauseSmsSent = applauseSmsSent;
	}
	/**
	 * @return the appraiseCode
	 */
	public String getAppraiseCode() {
		return appraiseCode;
	}
	/**
	 * @param appraiseCode the appraiseCode to set
	 */
	public void setAppraiseCode(String appraiseCode) {
		this.appraiseCode = appraiseCode;
	}
	/**
	 * @return the appraiseId
	 */
	public long getAppraiseId() {
		return appraiseId;
	}
	/**
	 * @param appraiseId the appraiseId to set
	 */
	public void setAppraiseId(long appraiseId) {
		this.appraiseId = appraiseId;
	}
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
	 * @return the inviteSms
	 */
	public String getInviteSms() {
		return inviteSms;
	}
	/**
	 * @param inviteSms the inviteSms to set
	 */
	public void setInviteSms(String inviteSms) {
		this.inviteSms = inviteSms;
	}
	/**
	 * @return the inviteSmsSent
	 */
	public Timestamp getInviteSmsSent() {
		return inviteSmsSent;
	}
	/**
	 * @param inviteSmsSent the inviteSmsSent to set
	 */
	public void setInviteSmsSent(Timestamp inviteSmsSent) {
		this.inviteSmsSent = inviteSmsSent;
	}
	/**
	 * @return the replySms
	 */
	public String getReplySms() {
		return replySms;
	}
	/**
	 * @param replySms the replySms to set
	 */
	public void setReplySms(String replySms) {
		this.replySms = replySms;
	}
	/**
	 * @return the replyTime
	 */
	public Timestamp getReplyTime() {
		return replyTime;
	}
	/**
	 * @param replyTime the replyTime to set
	 */
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
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
	 * @return the appraiseUnitId
	 */
	public long getAppraiseUnitId() {
		return appraiseUnitId;
	}
	/**
	 * @param appraiseUnitId the appraiseUnitId to set
	 */
	public void setAppraiseUnitId(long appraiseUnitId) {
		this.appraiseUnitId = appraiseUnitId;
	}
}