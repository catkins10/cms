package com.yuanluesoft.fdi.industry.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class Industry extends ActionForm {
	private String category; //分类名称
	private long parentCategoryId; //父分类ID
	private Set visitors; //权限控制
	private Set childCategories; //下级分类
	
	//扩展属性
	private RecordVisitorList editors = new RecordVisitorList(); //编辑权限
	private RecordVisitorList readers = new RecordVisitorList(); //查看权限
	private String parentCategory; //上级分类

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
	 * @return the editors
	 */
	public RecordVisitorList getEditors() {
		return editors;
	}
	/**
	 * @param editors the editors to set
	 */
	public void setEditors(RecordVisitorList editors) {
		this.editors = editors;
	}
	/**
	 * @return the readers
	 */
	public RecordVisitorList getReaders() {
		return readers;
	}
	/**
	 * @param readers the readers to set
	 */
	public void setReaders(RecordVisitorList readers) {
		this.readers = readers;
	}
	/**
	 * @return the parentCategory
	 */
	public String getParentCategory() {
		return parentCategory;
	}
	/**
	 * @param parentCategory the parentCategory to set
	 */
	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
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