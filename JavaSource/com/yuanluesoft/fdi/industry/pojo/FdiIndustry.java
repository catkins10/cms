package com.yuanluesoft.fdi.industry.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 行业分类配置(fdi_industry)
 * @author linchuan
 *
 */
public class FdiIndustry extends Record {
	private String category; //分类名称
	private long parentCategoryId; //父分类ID
	private Set visitors; //权限控制
	private Set childCategories; //下级分类
	
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
	 * @return the parentCategoryId
	 */
	public long getParentCategoryId() {
		return parentCategoryId;
	}
	/**
	 * @param parentCategoryId the parentCategoryId to set
	 */
	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the childCategories
	 */
	public Set getChildCategories() {
		return childCategories;
	}
	/**
	 * @param childCategories the childCategories to set
	 */
	public void setChildCategories(Set childCategories) {
		this.childCategories = childCategories;
	}
}