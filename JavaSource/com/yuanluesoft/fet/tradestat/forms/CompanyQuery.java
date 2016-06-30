package com.yuanluesoft.fet.tradestat.forms;

import com.yuanluesoft.cms.base.forms.SiteViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class CompanyQuery extends SiteViewForm {
	private String queryCompany; //按企业名称查询
	
	/**
	 * @return the queryCompany
	 */
	public String getQueryCompany() {
		return queryCompany;
	}
	/**
	 * @param queryCompany the queryCompany to set
	 */
	public void setQueryCompany(String queryCompany) {
		this.queryCompany = queryCompany;
	}
}