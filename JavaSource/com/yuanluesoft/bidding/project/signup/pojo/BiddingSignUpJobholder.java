package com.yuanluesoft.bidding.project.signup.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投标:参与人员(bidding_signup_jobholder)
 * @author linchuan
 *
 */
public class BiddingSignUpJobholder extends Record {
	private long signUpId; //报名ID
	private String jobholderCategory; //人员类别
	private long jobholderId; //人员ID
	private String jobholderName; //人员姓名
	private String qualification; //资质等级
	private String certificateNumber; //资质证书编号
	private int locked; //是否锁定
	private Timestamp unlockTime; //解锁时间
	
	/**
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}
	/**
	 * @param certificateNumber the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	/**
	 * @return the jobholderCategory
	 */
	public String getJobholderCategory() {
		return jobholderCategory;
	}
	/**
	 * @param jobholderCategory the jobholderCategory to set
	 */
	public void setJobholderCategory(String jobholderCategory) {
		this.jobholderCategory = jobholderCategory;
	}
	/**
	 * @return the jobholderId
	 */
	public long getJobholderId() {
		return jobholderId;
	}
	/**
	 * @param jobholderId the jobholderId to set
	 */
	public void setJobholderId(long jobholderId) {
		this.jobholderId = jobholderId;
	}
	/**
	 * @return the jobholderName
	 */
	public String getJobholderName() {
		return jobholderName;
	}
	/**
	 * @param jobholderName the jobholderName to set
	 */
	public void setJobholderName(String jobholderName) {
		this.jobholderName = jobholderName;
	}
	/**
	 * @return the locked
	 */
	public int getLocked() {
		return locked;
	}
	/**
	 * @param locked the locked to set
	 */
	public void setLocked(int locked) {
		this.locked = locked;
	}
	/**
	 * @return the qualification
	 */
	public String getQualification() {
		return qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	/**
	 * @return the signUpId
	 */
	public long getSignUpId() {
		return signUpId;
	}
	/**
	 * @param signUpId the signUpId to set
	 */
	public void setSignUpId(long signUpId) {
		this.signUpId = signUpId;
	}
	/**
	 * @return the unlockTime
	 */
	public Timestamp getUnlockTime() {
		return unlockTime;
	}
	/**
	 * @param unlockTime the unlockTime to set
	 */
	public void setUnlockTime(Timestamp unlockTime) {
		this.unlockTime = unlockTime;
	}
}