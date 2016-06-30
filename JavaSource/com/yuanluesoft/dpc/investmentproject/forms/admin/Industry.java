package com.yuanluesoft.dpc.investmentproject.forms.admin;

import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProjectIndustry;



/**
 * 
 * @author linchuan
 *
 */
public class Industry extends Parameter {
	private InvestmentProjectIndustry industry = new InvestmentProjectIndustry(); //行业

	/**
	 * @return the industry
	 */
	public InvestmentProjectIndustry getIndustry() {
		return industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(InvestmentProjectIndustry industry) {
		this.industry = industry;
	}
}