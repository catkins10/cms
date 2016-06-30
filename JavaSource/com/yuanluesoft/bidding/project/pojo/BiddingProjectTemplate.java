package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 模板配置(bidding_project_template)
 * @author linchuan
 *
 */
public class BiddingProjectTemplate extends Template {
	private String categories; //适用的工程分类列表,系统预设，如建安、市政
	private String procedures; //适用的招标内容列表,系统预设，如施工、监理、设计
	private String cities; //适用的地区列表
	private String biddingModes; //适用的招标方式,公开招标/邀请招标
	
	/**
	 * 获取适用的工程分类列表
	 * @return
	 */
	public String getCategoriesText() {
		return categories==null || categories.equals("") ? null : categories.substring(1, categories.length() - 1);
	}
	
	/**
	 * 获取适用的招标内容列表
	 * @return
	 */
	public String getProceduresText() {
		return procedures==null || procedures.equals("") ? null : procedures.substring(1, procedures.length() - 1);
	}
	
	/**
	 * 获取适用的地区列表
	 * @return
	 */
	public String getCitiesText() {
		return cities==null || cities.equals("") ? null : cities.substring(1, cities.length() - 1);
	}
	
	/**
	 * 获取适用的招标方式
	 * @return
	 */
	public String getBiddingModesText() {
		return biddingModes==null || biddingModes.equals("") ? null : biddingModes.substring(1, biddingModes.length() - 1);
	}
	
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
