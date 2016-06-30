package com.yuanluesoft.cms.pagebuilder.model.subpage;

/**
 * 子页面配置
 * @author linchuan
 *
 */
public class SubPage {
	private String pageName; //页面名称
	private String widthMode;
	private int width;
	private String heightMode;
	private int height;
	private String cssName;
	private String cssUrl;
	private boolean horizontalScroll; //是否显示水平滚动条
	private boolean verticalScroll; //是否显示垂直滚动条
	
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the heightMode
	 */
	public String getHeightMode() {
		return heightMode;
	}
	/**
	 * @param heightMode the heightMode to set
	 */
	public void setHeightMode(String heightMode) {
		this.heightMode = heightMode;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the widthMode
	 */
	public String getWidthMode() {
		return widthMode;
	}
	/**
	 * @param widthMode the widthMode to set
	 */
	public void setWidthMode(String widthMode) {
		this.widthMode = widthMode;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the horizontalScroll
	 */
	public boolean isHorizontalScroll() {
		return horizontalScroll;
	}
	/**
	 * @param horizontalScroll the horizontalScroll to set
	 */
	public void setHorizontalScroll(boolean horizontalScroll) {
		this.horizontalScroll = horizontalScroll;
	}
	/**
	 * @return the verticalScroll
	 */
	public boolean isVerticalScroll() {
		return verticalScroll;
	}
	/**
	 * @param verticalScroll the verticalScroll to set
	 */
	public void setVerticalScroll(boolean verticalScroll) {
		this.verticalScroll = verticalScroll;
	}
	/**
	 * @return the cssName
	 */
	public String getCssName() {
		return cssName;
	}
	/**
	 * @param cssName the cssName to set
	 */
	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
	/**
	 * @return the cssUrl
	 */
	public String getCssUrl() {
		return cssUrl;
	}
	/**
	 * @param cssUrl the cssUrl to set
	 */
	public void setCssUrl(String cssUrl) {
		this.cssUrl = cssUrl;
	}
}