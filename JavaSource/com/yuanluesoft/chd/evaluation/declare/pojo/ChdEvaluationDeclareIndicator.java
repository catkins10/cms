package com.yuanluesoft.chd.evaluation.declare.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价:主要指标完成情况(chd_eval_declar_indicator)
 * @author linchuan
 *
 */
public class ChdEvaluationDeclareIndicator extends Record {
	private long declareId; //申报ID
	private long indicatorId; //指标ID
	private String indicator; //指标名称
	private String standard; //标准值
	private String result; //实际值
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
	 * @return the indicator
	 */
	public String getIndicator() {
		return indicator;
	}
	/**
	 * @param indicator the indicator to set
	 */
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	/**
	 * @return the indicatorId
	 */
	public long getIndicatorId() {
		return indicatorId;
	}
	/**
	 * @param indicatorId the indicatorId to set
	 */
	public void setIndicatorId(long indicatorId) {
		this.indicatorId = indicatorId;
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
	/**
	 * @return the standard
	 */
	public String getStandard() {
		return standard;
	}
	/**
	 * @param standard the standard to set
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}
}