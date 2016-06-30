package com.yuanluesoft.jeaf.application.model.navigator;

/**
 * 链接
 * @author linchuan
 *
 */
public class Link {
	private String title; //标题
	private String href; //链接
	private String target; //链接目标
	private String iconURL; //链接图标URL
	private boolean viewLink; //是否视图链接
	private boolean isSelected; //是否处于选中状态
	
	public Link(String title, String href, String target, String iconURL, boolean viewLink, boolean isSelected) {
		super();
		this.title = title;
		this.href = href;
		this.target = target;
		this.iconURL = iconURL;
		this.viewLink = viewLink;
		this.isSelected = isSelected;
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
	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the viewLink
	 */
	public boolean isViewLink() {
		return viewLink;
	}

	/**
	 * @param viewLink the viewLink to set
	 */
	public void setViewLink(boolean viewLink) {
		this.viewLink = viewLink;
	}
}