package com.yuanluesoft.bidding.project.forms.admin;

/**
 * 
 * @author linchuan
 *
 */
public class Template extends com.yuanluesoft.cms.templatemanage.forms.Template {
	private String categories; //适用的工程分类列表,系统预设，如建安、市政
	private String procedures; //适用的招标内容列表,系统预设，如施工、监理、设计
	private String cities; //适用的地区列表
	private String biddingModes; //适用的招标方式,公开招标/邀请招标
	
	/**
	 * @return the biddingModes
	 */
	public String getBiddingModes() {
		return biddingModes;
	}
	/**
	 * @param biddingModes the biddingModes to set
	 */
	public void setBiddingModes(String biddingModes) {
		this.biddingModes = biddingModes;
	}
	/**
	 * @return the categories
	 */
	public String getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(String categories) {
		this.categories = categories;
	}
	/**
	 * @return the cities
	 */
	public String getCities() {
		return cities;
	}
	/**
	 * @param cities the cities to set
	 */
	public void setCities(String cities) {
		this.cities = cities;
	}
	/**
	 * @return the procedures
	 */
	public String getProcedures() {
		return procedures;
	}
	/**
	 * @param procedures the procedures to set
	 */
	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}
}
