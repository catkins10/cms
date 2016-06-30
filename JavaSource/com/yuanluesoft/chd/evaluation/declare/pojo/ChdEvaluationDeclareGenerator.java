package com.yuanluesoft.chd.evaluation.declare.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价:机组综合数据上报(chd_eval_declar_generator)
 * @author linchuan
 *
 */
public class ChdEvaluationDeclareGenerator extends Record {
	private long declareId; //申报ID
	private String body; //机组综合数据
	
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
	 * @return the declareId
	 */
	public long getDeclareId() {
		return declareId;
	}
	/**
	 * @param declareId the declareId to set
	 */
	public void setDeclareId(long declareId) {
		this.declareId = declareId;
	}
}