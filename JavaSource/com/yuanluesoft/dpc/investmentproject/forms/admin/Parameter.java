package com.yuanluesoft.dpc.investmentproject.forms.admin;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Parameter extends ActionForm {
	private String area; //区域和开发区
	private String investMode; //利用外资方式
	private String currency; //币种,人民币,美元
	
	private List industries; //行业列表
	
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
	/**
	 * @return the industries
	 */
	public List getIndustries() {
		return industries;
	}
	/**
	 * @param industries the industries to set
	 */
	public void setIndustries(List industries) {
		this.industries = industries;
	}
}