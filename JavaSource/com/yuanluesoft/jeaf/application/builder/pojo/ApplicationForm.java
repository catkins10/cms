package com.yuanluesoft.jeaf.application.builder.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 应用(application_form)
 * @author linchuan
 *
 */
public class ApplicationForm extends Record {
	private long applicationId; //应用ID
	private String name; //表单名称
	private String englishName; //英文名称
	private String newActionName; //新建操作名称,如:发布公告、登记收文
	private String templateName; //模板名称
	private String editPrivilege; //编辑权限,不需要工作流时有效
	private String deletePrivilege; //删除权限,为空时,有编辑权限的用户可以删除
	private String visitPrivilege; //查询权限
	private String custom; //表单定制
	private Timestamp buildTime; //生成时间
	private Set fields; //字段列表
	private Set views; //视图字段
	private Set indexes; //索引
	
	/**
	 * @return the applicationId
	 */
	public long getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	/**
	 * @return the custom
	 */
	public String getCustom() {
		return custom;
	}
	/**
	 * @param custom the custom to set
	 */
	public void setCustom(String custom) {
		this.custom = custom;
	}
	/**
	 * @return the deletePrivilege
	 */
	public String getDeletePrivilege() {
		return deletePrivilege;
	}
	/**
	 * @param deletePrivilege the deletePrivilege to set
	 */
	public void setDeletePrivilege(String deletePrivilege) {
		this.deletePrivilege = deletePrivilege;
	}
	/**
	 * @return the editPrivilege
	 */
	public String getEditPrivilege() {
		return editPrivilege;
	}
	/**
	 * @param editPrivilege the editPrivilege to set
	 */
	public void setEditPrivilege(String editPrivilege) {
		this.editPrivilege = editPrivilege;
	}
	/**
	 * @return the fields
	 */
	public Set getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set fields) {
		this.fields = fields;
	}
	/**
	 * @return the indexes
	 */
	public Set getIndexes() {
		return indexes;
	}
	/**
	 * @param indexes the indexes to set
	 */
	public void setIndexes(Set indexes) {
		this.indexes = indexes;
	}
	/**
	 * @return the newActionName
	 */
	public String getNewActionName() {
		return newActionName;
	}
	/**
	 * @param newActionName the newActionName to set
	 */
	public void setNewActionName(String newActionName) {
		this.newActionName = newActionName;
	}
	/**
	 * @return the views
	 */
	public Set getViews() {
		return views;
	}
	/**
	 * @param views the views to set
	 */
	public void setViews(Set views) {
		this.views = views;
	}
	/**
	 * @return the visitPrivilege
	 */
	public String getVisitPrivilege() {
		return visitPrivilege;
	}
	/**
	 * @param visitPrivilege the visitPrivilege to set
	 */
	public void setVisitPrivilege(String visitPrivilege) {
		this.visitPrivilege = visitPrivilege;
	}
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
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	/**
	 * @return the buildTime
	 */
	public Timestamp getBuildTime() {
		return buildTime;
	}
	/**
	 * @param buildTime the buildTime to set
	 */
	public void setBuildTime(Timestamp buildTime) {
		this.buildTime = buildTime;
	}
}