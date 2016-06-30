package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 必备条件(chd_evaluation_prerequisites)
 * @author linchuan
 *
 */
public class ChdEvaluationPrerequisites extends Record {
	private long plantTypeId; //发电企业类型ID
	private String prerequisites; //条件
	private double priority; //优先级
	private Timestamp created; //创建时间
	private long sourceRecordId; //源记录ID
	private String remark; //备注
	private Set scores; //各等级对应的结果
	/**
	 * @return the plantTypeId
	 */
	public long getPlantTypeId() {
		return plantTypeId;
	}
	/**
	 * @param plantTypeId the plantTypeId to set
	 */
	public void setPlantTypeId(long plantTypeId) {
		this.plantTypeId = plantTypeId;
	}
	/**
	 * @return the prerequisites
	 */
	public String getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}
	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the scores
	 */
	public Set getScores() {
		return scores;
	}
	/**
	 * @param scores the scores to set
	 */
	public void setScores(Set scores) {
		this.scores = scores;
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
	 * @return the sourceRecordId
	 */
	public long getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(long sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
}