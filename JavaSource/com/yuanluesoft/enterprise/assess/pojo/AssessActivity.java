package com.yuanluesoft.enterprise.assess.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考核/汇报配置:考核步骤(assess_activity)
 * @author linchuan
 *
 */
public class AssessActivity extends Record {
	private long classifyId; //考核类型ID
	private String activity; //考核步骤,项目负责人评分/部门审核
	private double weight; //权重
	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}
	/**
	 * @return the classifyId
	 */
	public long getClassifyId() {
		return classifyId;
	}
	/**
	 * @param classifyId the classifyId to set
	 */
	public void setClassifyId(long classifyId) {
		this.classifyId = classifyId;
	}
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
}