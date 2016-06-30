/*
 * Created on 2006-5-26
 *
 */
package com.yuanluesoft.eai.client.model;


/**
 * 
 * @author linchuan
 *
 */
public class Application extends Link {
	private String description;	//描述
	private boolean navigateDisabled; //不在导航时显示
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the navigateDisabled
	 */
	public boolean isNavigateDisabled() {
		return navigateDisabled;
	}
	/**
	 * @param navigateDisabled the navigateDisabled to set
	 */
	public void setNavigateDisabled(boolean navigateDisabled) {
		this.navigateDisabled = navigateDisabled;
	}
}