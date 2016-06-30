package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:设计完成情况(enterprise_project_quality)
 * @author linchuan
 *
 */
public class EnterpriseProjectQuality extends Record {
	private long teamId; //项目ID
	private Date completionDate; //设计完成时间,设计完成情况日期（设计人员填写）
	private String completionDescription; //设计完成情况
	private String designQuality; //设计质量,填写设计质量，分为优秀、良好、合格、不合格、原则性错误	、技术性错误、一般性错误等（总工）
	private long approverId; //审核人ID
	private String approver; //审核人
	private Timestamp approvalTime; //审核时间
	
	/**
	 * @return the approvalTime
	 */
	public Timestamp getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the approver
	 */
	public String getApprover() {
		return approver;
	}
	/**
	 * @param approver the approver to set
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}
	/**
	 * @return the approverId
	 */
	public long getApproverId() {
		return approverId;
	}
	/**
	 * @param approverId the approverId to set
	 */
	public void setApproverId(long approverId) {
		this.approverId = approverId;
	}
	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}
	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	/**
	 * @return the completionDescription
	 */
	public String getCompletionDescription() {
		return completionDescription;
	}
	/**
	 * @param completionDescription the completionDescription to set
	 */
	public void setCompletionDescription(String completionDescription) {
		this.completionDescription = completionDescription;
	}
	/**
	 * @return the designQuality
	 */
	public String getDesignQuality() {
		return designQuality;
	}
	/**
	 * @param designQuality the designQuality to set
	 */
	public void setDesignQuality(String designQuality) {
		this.designQuality = designQuality;
	}
	/**
	 * @return the teamId
	 */
	public long getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
}
