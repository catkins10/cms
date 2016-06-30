package com.yuanluesoft.chd.evaluation.declare.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 申报(chd_eval_declare)
 * @author linchuan
 *
 */
public class ChdEvaluationDeclare extends Record {
	private long plantId; //发电企业ID
	private int declareYear; //申报年度
	private long objectiveLevelId; //申报等级ID
	private String objectiveLevel; //申报等级
	private long creatorId; //提交人ID
	private String creator; //提交人
	private Timestamp created; //提交时间
	private long approvedLevelId; //核准等级ID
	private String approvedLevel; //核准等级
	private Timestamp approveTime; //核准时间
	private long approverId; //核准人ID
	private String approver; //核准人
	private Set rules; //申报:评价细则完成情况
	private Set prerequisites; //申报:必备条件完成情况
	private Set indicators; //申报:主要指标完成情况
	private Set generators; //申报:机组综合数据上报

	/**
	 * @return the approvedLevel
	 */
	public String getApprovedLevel() {
		return approvedLevel;
	}
	/**
	 * @param approvedLevel the approvedLevel to set
	 */
	public void setApprovedLevel(String approvedLevel) {
		this.approvedLevel = approvedLevel;
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
	 * @return the declareYear
	 */
	public int getDeclareYear() {
		return declareYear;
	}
	/**
	 * @param declareYear the declareYear to set
	 */
	public void setDeclareYear(int declareYear) {
		this.declareYear = declareYear;
	}
	/**
	 * @return the objectiveLevel
	 */
	public String getObjectiveLevel() {
		return objectiveLevel;
	}
	/**
	 * @param objectiveLevel the objectiveLevel to set
	 */
	public void setObjectiveLevel(String objectiveLevel) {
		this.objectiveLevel = objectiveLevel;
	}
	/**
	 * @return the objectiveLevelId
	 */
	public long getObjectiveLevelId() {
		return objectiveLevelId;
	}
	/**
	 * @param objectiveLevelId the objectiveLevelId to set
	 */
	public void setObjectiveLevelId(long objectiveLevelId) {
		this.objectiveLevelId = objectiveLevelId;
	}
	/**
	 * @return the plantId
	 */
	public long getPlantId() {
		return plantId;
	}
	/**
	 * @param plantId the plantId to set
	 */
	public void setPlantId(long plantId) {
		this.plantId = plantId;
	}
	/**
	 * @return the approvedLevelId
	 */
	public long getApprovedLevelId() {
		return approvedLevelId;
	}
	/**
	 * @param approvedLevelId the approvedLevelId to set
	 */
	public void setApprovedLevelId(long approvedLevelId) {
		this.approvedLevelId = approvedLevelId;
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
	 * @return the approveTime
	 */
	public Timestamp getApproveTime() {
		return approveTime;
	}
	/**
	 * @param approveTime the approveTime to set
	 */
	public void setApproveTime(Timestamp approveTime) {
		this.approveTime = approveTime;
	}
	/**
	 * @return the generators
	 */
	public Set getGenerators() {
		return generators;
	}
	/**
	 * @param generators the generators to set
	 */
	public void setGenerators(Set generators) {
		this.generators = generators;
	}
	/**
	 * @return the indicators
	 */
	public Set getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators the indicators to set
	 */
	public void setIndicators(Set indicators) {
		this.indicators = indicators;
	}
	/**
	 * @return the prerequisites
	 */
	public Set getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(Set prerequisites) {
		this.prerequisites = prerequisites;
	}
	/**
	 * @return the rules
	 */
	public Set getRules() {
		return rules;
	}
	/**
	 * @param rules the rules to set
	 */
	public void setRules(Set rules) {
		this.rules = rules;
	}
}