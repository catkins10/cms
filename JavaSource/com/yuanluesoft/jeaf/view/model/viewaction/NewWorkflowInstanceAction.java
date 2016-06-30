/*
 * Created on 2006-9-8
 *
 */
package com.yuanluesoft.jeaf.view.model.viewaction;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class NewWorkflowInstanceAction  extends ViewAction implements Serializable {
	private String applicationName; //指定应用名称,不设置时继承视图的应用名称
	private String openFeatures; //窗口打开打开方式,不设置时继承视图的打开方式
	private String form; //表单名称,不设置时继承视图的表单名称
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the form
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return the openFeatures
	 */
	public String getOpenFeatures() {
		return openFeatures;
	}
	/**
	 * @param openFeatures the openFeatures to set
	 */
	public void setOpenFeatures(String openFeatures) {
		this.openFeatures = openFeatures;
	}
}
