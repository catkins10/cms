package com.yuanluesoft.chd.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 机组综合数据表模板(chd_evaluation_generator)
 * @author linchuan
 *
 */
public class ChdEvaluationGenerator extends Record {
	private long plantTypeId; //发电企业类型ID
	private String body; //机组综合数据
	private long sourceRecordId; //源记录ID
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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