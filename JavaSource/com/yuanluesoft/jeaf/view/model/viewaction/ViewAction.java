/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.view.model.viewaction;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class ViewAction extends CloneableObject {
	private String title; //标题
	private String groupTitle; //分组时的标题
	private String hideCondition; //隐藏条件
	private String execute; //执行的操作
	private String image; //操作按钮图标
	
	/**
	 * @return Returns the execute.
	 */
	public String getExecute() {
		return execute;
	}
	/**
	 * @param execute The execute to set.
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}
	/**
	 * @return Returns the hideCondition.
	 */
	public String getHideCondition() {
		return hideCondition;
	}
	/**
	 * @param hideCondition The hideCondition to set.
	 */
	public void setHideCondition(String hideCondition) {
		this.hideCondition = hideCondition;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @return the groupTitle
	 */
	public String getGroupTitle() {
		return groupTitle;
	}
	/**
	 * @param groupTitle the groupTitle to set
	 */
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
}
