package com.yuanluesoft.job.talent.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:求职意向行业(job_talent_intention_industry)
 * @author linchuan
 *
 */
public class JobTalentIntentionIndustry extends Record {
	private long intentionId; //求职意向ID
	private long industryId; //行业ID
	private String industry; //行业
	
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
	 * @return the industryId
	 */
	public long getIndustryId() {
		return industryId;
	}
	/**
	 * @param industryId the industryId to set
	 */
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
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