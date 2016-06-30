package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 主要指标(chd_evaluation_indicator)
 * @author linchuan
 *
 */
public class ChdEvaluationIndicator extends Record {
	private long plantTypeId; //发电企业类型ID
	private String name; //指标名称
	private String unit; //单位
	private double priority; //优先级
	private Timestamp created; //创建时间
	private long sourceRecordId; //源记录ID
	private String remark; //备注
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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