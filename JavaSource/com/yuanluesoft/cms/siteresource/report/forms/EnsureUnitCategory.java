package com.yuanluesoft.cms.siteresource.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class EnsureUnitCategory extends ActionForm {
	private long siteId; //站点ID
	private String category; //分类名称,如：第一系列、第二系列、第三系列
	private String unitIds; //单位ID
	private String unitNames; //单位名称
	private float priority; //优先级
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the unitIds
	 */
	public String getUnitIds() {
		return unitIds;
	}
	/**
	 * @param unitIds the unitIds to set
	 */
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	/**
	 * @return the unitNames
	 */
	public String getUnitNames() {
		return unitNames;
	}
	/**
	 * @param unitNames the unitNames to set
	 */
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}
}