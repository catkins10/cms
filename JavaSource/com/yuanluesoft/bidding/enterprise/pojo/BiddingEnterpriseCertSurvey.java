package com.yuanluesoft.bidding.enterprise.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业:资质年检(bidding_enterprise_cert_survey)
 * @author linchuan
 *
 */
public class BiddingEnterpriseCertSurvey extends Record {
	private long certId; //资质ID
	private Date surveyDate; //年检时间
	private String surveyResult; //年检情况
	private int surveyYear; //年份
	private String remark; //备注
	private long alterId; //变更记录ID
	
	/**
	 * @return the certId
	 */
	public long getCertId() {
		return certId;
	}
	/**
	 * @param certId the certId to set
	 */
	public void setCertId(long certId) {
		this.certId = certId;
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
	 * @return the surveyDate
	 */
	public Date getSurveyDate() {
		return surveyDate;
	}
	/**
	 * @param surveyDate the surveyDate to set
	 */
	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	/**
	 * @return the surveyResult
	 */
	public String getSurveyResult() {
		return surveyResult;
	}
	/**
	 * @param surveyResult the surveyResult to set
	 */
	public void setSurveyResult(String surveyResult) {
		this.surveyResult = surveyResult;
	}
	/**
	 * @return the surveyYear
	 */
	public int getSurveyYear() {
		return surveyYear;
	}
	/**
	 * @param surveyYear the surveyYear to set
	 */
	public void setSurveyYear(int surveyYear) {
		this.surveyYear = surveyYear;
	}
	/**
	 * @return the alterId
	 */
	public long getAlterId() {
		return alterId;
	}
	/**
	 * @param alterId the alterId to set
	 */
	public void setAlterId(long alterId) {
		this.alterId = alterId;
	}
}
