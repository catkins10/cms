package com.yuanluesoft.chd.evaluation.declare.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价:必备条件完成情况(chd_eval_declar_prerequisites)
 * @author linchuan
 *
 */
public class ChdEvaluationDeclarePrerequisites extends Record {
	private long declareId; //申报ID
	private long prerequisitesId; //必备条件ID
	private String prerequisites; //必备条件
	private String result; //完成情况说明
	private String remark; //备注
	
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
	 * @return the prerequisitesId
	 */
	public long getPrerequisitesId() {
		return prerequisitesId;
	}
	/**
	 * @param prerequisitesId the prerequisitesId to set
	 */
	public void setPrerequisitesId(long prerequisitesId) {
		this.prerequisitesId = prerequisitesId;
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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
}