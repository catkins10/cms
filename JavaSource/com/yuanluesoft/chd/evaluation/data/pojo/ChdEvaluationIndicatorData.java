package com.yuanluesoft.chd.evaluation.data.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 指标完成情况(chd_eval_indicator_data)
 * @author linchuan
 *
 */
public class ChdEvaluationIndicatorData extends Record {
	private int dataYear; //年度
	private int dataMonth; //月份
	private long indicatorId; //指标ID
	private String indicator; //指标名称
	private String data; //完成情况
	private long creatorId; //填报人ID
	private String creator; //填报人
	private String remark; //备注
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
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the dataMonth
	 */
	public int getDataMonth() {
		return dataMonth;
	}
	/**
	 * @param dataMonth the dataMonth to set
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}
	/**
	 * @return the dataYear
	 */
	public int getDataYear() {
		return dataYear;
	}
	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
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
}