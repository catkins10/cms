package com.yuanluesoft.dpc.keyproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 重点项目:行业配置(keyproject_industry)
 * @author linchuan
 *
 */
public class KeyProjectIndustry extends Record {
	private String industry; //行业
	private String childIndustry; //子行业列表
	private float priority; //优先级
	
	/**
	 * @return the childIndustry
	 */
	public String getChildIndustry() {
		return childIndustry;
	}
	/**
	 * @param childIndustry the childIndustry to set
	 */
	public void setChildIndustry(String childIndustry) {
		this.childIndustry = childIndustry;
	}
	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
}
