/*
 * Created on 2004-12-22
 *
 */
package com.yuanluesoft.jeaf.application.model.navigator.definition;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class Link extends CloneableObject {
	private String title; //标题
	private String href; //链接
	private String hideCondition; //隐藏条件
	private String target; //链接目标
	private String iconURL; //链接图标URL
	
	/**
	 * @return the hideCondition
	 */
	public String getHideCondition() {
		return hideCondition;
	}
	/**
	 * @param hideCondition the hideCondition to set
	 */
	public void setHideCondition(String hideCondition) {
		this.hideCondition = hideCondition;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the iconURL
	 */
	public String getIconURL() {
		return iconURL;
	}
	/**
	 * @param iconURL the iconURL to set
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
}