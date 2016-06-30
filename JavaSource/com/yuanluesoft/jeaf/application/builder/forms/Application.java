package com.yuanluesoft.jeaf.application.builder.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Application extends ActionForm {
	private String name; //名称
	private String englishName; //英文名称
	private Timestamp buildTime; //生成时间
	private Timestamp created; //创建时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Set forms; //表单列表
	private Set navigators; //导航栏字段
	
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
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
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
	 * @return the forms
	 */
	public Set getForms() {
		return forms;
	}
	/**
	 * @param forms the forms to set
	 */
	public void setForms(Set forms) {
		this.forms = forms;
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
	 * @return the navigators
	 */
	public Set getNavigators() {
		return navigators;
	}
	/**
	 * @param navigators the navigators to set
	 */
	public void setNavigators(Set navigators) {
		this.navigators = navigators;
	}
}