package com.yuanluesoft.jeaf.htmlparser.model;

/**
 * 样式定义
 * @author linchuan
 *
 */
public class StyleDefine {
	private String styleName; //样式名称
	private String iconUrl; //图标URL
	private int iconWidth; //图标宽度
	private int iconHeight; //图标高度
	private String cssFileName; //样式表文件名称
	private String cssUrl; //样式表URL
	
	/**
	 * @return the cssFileName
	 */
	public String getCssFileName() {
		return cssFileName;
	}
	/**
	 * @param cssFileName the cssFileName to set
	 */
	public void setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
	}
	/**
	 * @return the iconHeight
	 */
	public int getIconHeight() {
		return iconHeight;
	}
	/**
	 * @param iconHeight the iconHeight to set
	 */
	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}
	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	/**
	 * @return the iconWidth
	 */
	public int getIconWidth() {
		return iconWidth;
	}
	/**
	 * @param iconWidth the iconWidth to set
	 */
	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}
	/**
	 * @return the styleName
	 */
	public String getStyleName() {
		return styleName;
	}
	/**
	 * @param styleName the styleName to set
	 */
	public void setStyleName(String styleName) {
		this.styleName = styleName;
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