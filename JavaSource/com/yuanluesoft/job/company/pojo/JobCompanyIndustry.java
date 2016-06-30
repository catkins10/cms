package com.yuanluesoft.job.company.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业:所在行业(job_company_industry)
 * @author linchuan
 *
 */
public class JobCompanyIndustry extends Record {
	private long companyId; //企业ID
	private long industryId; //行业ID
	private String industry; //行业
	
	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
}