package com.yuanluesoft.bidding.project.report.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class AgentSalesReport extends ActionForm {
	private Date beginDate; //开始时间
	private Date endDate; //结束时间
	private long agentId; //代理ID
	private String agentName; //代理名称
	private String[] paymentBanks; //支付银行
	private String[] cities; //地区列表
	
	/**
	 * @return the agentId
	 */
	public long getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the paymentBanks
	 */
	public String[] getPaymentBanks() {
		return paymentBanks;
	}
	/**
	 * @param paymentBanks the paymentBanks to set
	 */
	public void setPaymentBanks(String[] paymentBanks) {
		this.paymentBanks = paymentBanks;
	}
	/**
	 * @return the cities
	 */
	public String[] getCities() {
		return cities;
	}
	/**
	 * @param cities the cities to set
	 */
	public void setCities(String[] cities) {
		this.cities = cities;
	}
}