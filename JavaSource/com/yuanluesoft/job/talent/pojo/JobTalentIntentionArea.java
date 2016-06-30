package com.yuanluesoft.job.talent.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:求职意向地点(job_talent_intention_area)
 * @author linchuan
 *
 */
public class JobTalentIntentionArea extends Record {
	private long intentionId; //求职意向ID
	private long areaId; //地点ID
	private String area; //地点
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the intentionId
	 */
	public long getIntentionId() {
		return intentionId;
	}
	/**
	 * @param intentionId the intentionId to set
	 */
	public void setIntentionId(long intentionId) {
		this.intentionId = intentionId;
	}
}