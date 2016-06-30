package com.yuanluesoft.enterprise.assess.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 绩效考核(assess_assess)
 * @author linchuan
 *
 */
public class Assess extends WorkflowData {
	private long teamId; //项目组ID,非项目组考核时为0
	private String projectName; //项目名称
	private String projectStage; //项目阶段
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	
	private Set results; //成绩列表
	
	//扩展
	private List assessClassifies; //引用到的考核类别列表
	
	/**
	 * 获取项目标题
	 * @return
	 */
	public String getProjectTitle() {
		return teamId==0 ? null : (projectName + "(" + projectStage + ")");
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
	 * @return the results
	 */
	public Set getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(Set results) {
		this.results = results;
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
	 * @return the projectStage
	 */
	public String getProjectStage() {
		return projectStage;
	}
	/**
	 * @param projectStage the projectStage to set
	 */
	public void setProjectStage(String projectStage) {
		this.projectStage = projectStage;
	}
	/**
	 * @return the assessClassifies
	 */
	public List getAssessClassifies() {
		return assessClassifies;
	}
	/**
	 * @param assessClassifies the assessClassifies to set
	 */
	public void setAssessClassifies(List assessClassifies) {
		this.assessClassifies = assessClassifies;
	}
}