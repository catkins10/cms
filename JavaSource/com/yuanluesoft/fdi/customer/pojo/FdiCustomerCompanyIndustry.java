package com.yuanluesoft.fdi.customer.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 客户单位:所属行业(fdi_customer_company_industry)
 * @author linchuan
 *
 */
public class FdiCustomerCompanyIndustry extends Record {
	private long companyId; //单位ID
	private long industryId; //行业ID
	private String industry; //行业名称
	
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