package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价细则:评价要点(chd_evaluation_point)
 * @author linchuan
 *
 */
public class ChdEvaluationPoint extends Record {
	private long detailId; //评价细则ID
	private String point; //评价要点,从查评方法、细则中分解
	private double priority; //优先级
	private Timestamp created; //创建时间
	private long sourceRecordId; //源记录ID
	private String remark; //备注
	
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
	 * @return the detailId
	 */
	public long getDetailId() {
		return detailId;
	}
	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(long detailId) {
		this.detailId = detailId;
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
	 * @return the point
	 */
	public String getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(String point) {
		this.point = point;
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