package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:项目组工作计划(enterprise_project_team_plan)
 * @author linchuan
 *
 */
public class EnterpriseProjectTeamPlan extends Record {
	private long teamId; //项目组ID
	private String memberIds; //参与的项目组成员ID列表
	private String memberNames; //参与的项目组成员列表
	private String workContent; //工作内容
	private Date beginDate; //计划开始时间
	private Date endDate; //计划结束时间
	private Date completionDate; //实际完成时间
	private String completionDescription; //实际完成情况
	
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
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
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the memberIds
	 */
	public String getMemberIds() {
		return memberIds;
	}
	/**
	 * @param memberIds the memberIds to set
	 */
	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}
	/**
	 * @return the memberNames
	 */
	public String getMemberNames() {
		return memberNames;
	}
	/**
	 * @param memberNames the memberNames to set
	 */
	public void setMemberNames(String memberNames) {
		this.memberNames = memberNames;
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
	/**
	 * @return the workContent
	 */
	public String getWorkContent() {
		return workContent;
	}
	/**
	 * @param workContent the workContent to set
	 */
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}
}