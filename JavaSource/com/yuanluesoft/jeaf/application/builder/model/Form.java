package com.yuanluesoft.jeaf.application.builder.model;

import java.util.List;


/**
 * 
 * @author linchuan
 *
 */
public class Form extends com.yuanluesoft.jeaf.form.model.Form {
	private Class parentClass; //父类
	private String strutsFormBeanName; //STRUTS表单BEAN名称
	private List strutsActions; //STRUTS操作列表
	
	/**
	 * @return the parentClass
	 */
	public Class getParentClass() {
		return parentClass;
	}
	/**
	 * @param parentClass the parentClass to set
	 */
	public void setParentClass(Class parentClass) {
		this.parentClass = parentClass;
	}
	/**
	 * @return the strutsActions
	 */
	public List getStrutsActions() {
		return strutsActions;
	}
	/**
	 * @param strutsActions the strutsActions to set
	 */
	public void setStrutsActions(List strutsActions) {
		this.strutsActions = strutsActions;
	}
	/**
	 * @return the strutsFormBeanName
	 */
	public String getStrutsFormBeanName() {
		return strutsFormBeanName;
	}
	/**
	 * @param strutsFormBeanName the strutsFormBeanName to set
	 */
	public void setStrutsFormBeanName(String strutsFormBeanName) {
		this.strutsFormBeanName = strutsFormBeanName;
	}
}