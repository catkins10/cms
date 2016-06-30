package com.yuanluesoft.dpc.investmentproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 招商项目:参数配置(investment_project_parameter)
 * @author linchuan
 *
 */
public class InvestmentProjectParameter extends Record {
	private String area; //区域和开发区
	private String investMode; //利用外资方式
	private String currency; //币种,人民币,美元
	
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the investMode
	 */
	public String getInvestMode() {
		return investMode;
	}
	/**
	 * @param investMode the investMode to set
	 */
	public void setInvestMode(String investMode) {
		this.investMode = investMode;
	}
}