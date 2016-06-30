package com.yuanluesoft.jeaf.view.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Link implements Serializable, Cloneable {
	private String title; //链接名称
	private String type; //链接类型,包括:recordLink/记录页面(默认),hostLink/宿主页面,点击“更多...”显示的页面,默认为applicationView.shtml
	private String hideCondition; //隐藏条件
	private String target; //默认的目标窗口
	private String width; //默认的窗口宽度
	private String height; //默认的窗口高度
	private boolean fullScreen; //是否全屏显示
	private String url; //URL
	
	public Link() {
		super();
	}
	
	public Link(String title, String url) {
		super();
		this.title = title;
		this.url = url;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
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
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return the fullScreen
	 */
	public boolean isFullScreen() {
		return fullScreen;
	}
	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
}