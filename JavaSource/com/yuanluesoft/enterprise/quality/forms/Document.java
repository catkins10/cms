package com.yuanluesoft.enterprise.quality.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 质量管理:文档审批(quality_document)
 * @author linchuan
 *
 */
public class Document extends WorkflowForm {
	private long projectId; //项目ID
	private String projectName; //项目名称
	private String stage; //勘察、设计阶段
	private String scale; //项目规模
	private String projectLeader; //项目负责人
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private String remark; //备注
	
	private Set bodies; //正文
	
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
	 * @return the projectLeader
	 */
	public String getProjectLeader() {
		return projectLeader;
	}
	/**
	 * @param projectLeader the projectLeader to set
	 */
	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}
	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	/**
	 * @return the bodies
	 */
	public Set getBodies() {
		return bodies;
	}
	/**
	 * @param bodies the bodies to set
	 */
	public void setBodies(Set bodies) {
		this.bodies = bodies;
	}
}
