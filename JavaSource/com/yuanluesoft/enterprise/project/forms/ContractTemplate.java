package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class ContractTemplate extends ActionForm {
	private String name; //模板名称
	private String appliedProjectTypes; //适用的项目类型
	
	private String[] appliedProjectTypeArray; //适用的项目类型转换为数组
	
	/**
	 * @return the appliedProjectTypes
	 */
	public String getAppliedProjectTypes() {
		return appliedProjectTypes;
	}
	/**
	 * @param appliedProjectTypes the appliedProjectTypes to set
	 */
	public void setAppliedProjectTypes(String appliedProjectTypes) {
		this.appliedProjectTypes = appliedProjectTypes;
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
	 * @return the appliedProjectTypeArray
	 */
	public String[] getAppliedProjectTypeArray() {
		return appliedProjectTypeArray;
	}
	/**
	 * @param appliedProjectTypeArray the appliedProjectTypeArray to set
	 */
	public void setAppliedProjectTypeArray(String[] appliedProjectTypeArray) {
		this.appliedProjectTypeArray = appliedProjectTypeArray;
	}
}