package com.yuanluesoft.fet.tradestat.forms;

import com.yuanluesoft.cms.base.forms.SiteViewForm;


/**
 * 
 * @author linchuan
 *
 */
public class ExportQuery extends SiteViewForm {
	private String queryYear; //查询年度
	private String queryMonth; //查询月份
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
	/**
	 * @return the queryMonth
	 */
	public String getQueryMonth() {
		return queryMonth;
	}
	/**
	 * @param queryMonth the queryMonth to set
	 */
	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}
	/**
	 * @return the queryYear
	 */
	public String getQueryYear() {
		return queryYear;
	}
	/**
	 * @param queryYear the queryYear to set
	 */
	public void setQueryYear(String queryYear) {
		this.queryYear = queryYear;
	}
	
}