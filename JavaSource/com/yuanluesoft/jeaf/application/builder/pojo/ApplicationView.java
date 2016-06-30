package com.yuanluesoft.jeaf.application.builder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 视图(application_view)
 * @author linchuan
 *
 */
public class ApplicationView extends Record {
	private long formId; //表单ID
	private String name; //视图名称
	private String englishName; //视图英文名称
	private String filterMode; //过滤方式
	private String hqlWhere; //条件,where子句
	private String viewFieldIds; //视图字段ID
	private String viewFieldNames; //视图字段名称
	private String sortFieldIds; //排序字段ID
	private String sortFieldNames; //排序字段名称
	private String sortFieldDirections; //排序字段排序方式,asc/desc
	private String accessPrivilege; //访问权限
	
	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}
	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	/**
	 * @return the filterMode
	 */
	public String getFilterMode() {
		return filterMode;
	}
	/**
	 * @param filterMode the filterMode to set
	 */
	public void setFilterMode(String filterMode) {
		this.filterMode = filterMode;
	}
	/**
	 * @return the formId
	 */
	public long getFormId() {
		return formId;
	}
	/**
	 * @param formId the formId to set
	 */
	public void setFormId(long formId) {
		this.formId = formId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sortFieldDirections
	 */
	public String getSortFieldDirections() {
		return sortFieldDirections;
	}
	/**
	 * @param sortFieldDirections the sortFieldDirections to set
	 */
	public void setSortFieldDirections(String sortFieldDirections) {
		this.sortFieldDirections = sortFieldDirections;
	}
	/**
	 * @return the sortFieldIds
	 */
	public String getSortFieldIds() {
		return sortFieldIds;
	}
	/**
	 * @param sortFieldIds the sortFieldIds to set
	 */
	public void setSortFieldIds(String sortFieldIds) {
		this.sortFieldIds = sortFieldIds;
	}
	/**
	 * @return the sortFieldNames
	 */
	public String getSortFieldNames() {
		return sortFieldNames;
	}
	/**
	 * @param sortFieldNames the sortFieldNames to set
	 */
	public void setSortFieldNames(String sortFieldNames) {
		this.sortFieldNames = sortFieldNames;
	}
	/**
	 * @return the viewFieldIds
	 */
	public String getViewFieldIds() {
		return viewFieldIds;
	}
	/**
	 * @param viewFieldIds the viewFieldIds to set
	 */
	public void setViewFieldIds(String viewFieldIds) {
		this.viewFieldIds = viewFieldIds;
	}
	/**
	 * @return the viewFieldNames
	 */
	public String getViewFieldNames() {
		return viewFieldNames;
	}
	/**
	 * @param viewFieldNames the viewFieldNames to set
	 */
	public void setViewFieldNames(String viewFieldNames) {
		this.viewFieldNames = viewFieldNames;
	}
	/**
	 * @return the hqlWhere
	 */
	public String getHqlWhere() {
		return hqlWhere;
	}
	/**
	 * @param hqlWhere the hqlWhere to set
	 */
	public void setHqlWhere(String hqlWhere) {
		this.hqlWhere = hqlWhere;
	}
	/**
	 * @return the accessPrivilege
	 */
	public String getAccessPrivilege() {
		return accessPrivilege;
	}
	/**
	 * @param accessPrivilege the accessPrivilege to set
	 */
	public void setAccessPrivilege(String accessPrivilege) {
		this.accessPrivilege = accessPrivilege;
	}
}