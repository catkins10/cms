package com.yuanluesoft.job.talent.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:求职意向公司性质(job_talent_intention_co_type)
 * @author linchuan
 *
 */
public class JobTalentIntentionCompanyType extends Record {
	private long intentionId; //求职意向ID
	private String companyType; //公司性质,外资（欧美）,外资（非欧美）,合资,国企,民营公司,国内上市公司,外企代表处,政府机关,事业单位,非营利机构,其它性质
	
	/**
	 * @return the companyType
	 */
	public String getCompanyType() {
		return companyType;
	}
	/**
	 * @param companyType the companyType to set
	 */
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
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