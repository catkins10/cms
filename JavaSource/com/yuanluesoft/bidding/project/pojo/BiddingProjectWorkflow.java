package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 招标流程配置(bidding_project_workflow)
 * @author linchuan
 *
 */
public class BiddingProjectWorkflow extends Record {
	private String categories; //适用的工程分类列表,系统预设，如建安、市政
	private String procedures; //适用的招标内容列表,系统预设，如施工、监理、设计
	private String cities; //适用的地区列表
	private long workflowId; //流程ID
	private String workflowName; //流程名称

	/**
	 * @return the categoryArray
	 */
	public String[] getCategoryArray() {
		return categories==null ? null : categories.split(",");
	}
	/**
	 * @param categoryArray the categoryArray to set
	 */
	public void setCategoryArray(String[] categoryArray) {
		this.categories = "," + ListUtils.join(categoryArray, ",", false) + ",";
	}
	/**
	 * @return the cityArray
	 */
	public String[] getCityArray() {
		return cities==null ? null : cities.split(",");
	}
	/**
	 * @param cityArray the cityArray to set
	 */
	public void setCityArray(String[] cityArray) {
		this.cities = "," + ListUtils.join(cityArray, ",", false) + ",";
	}
	/**
	 * @return the procedureArray
	 */
	public String[] getProcedureArray() {
		return procedures==null ? null : procedures.split(",");
	}
	/**
	 * @param procedureArray the procedureArray to set
	 */
	public void setProcedureArray(String[] procedureArray) {
		this.procedures = "," + ListUtils.join(procedureArray, ",", false) + ",";
	}
	
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
	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
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
	 * @return the workflowId
	 */
	public long getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
}