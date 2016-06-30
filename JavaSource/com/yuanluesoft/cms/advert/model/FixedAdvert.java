package com.yuanluesoft.cms.advert.model;

import java.io.Serializable;

/**
 * 固定位置广告
 * @author linchuan
 *
 */
public class FixedAdvert implements Serializable {
	private long advertPutId; //投放ID
	private String content; //广告内容HTML
	private String minimizeContent; //最小化时HTML
	private String width; //宽度
	private String height; //高度
	private String minimizeWidth; //最小化时宽度
	private String minimizeHeight; //最小化时高度
	private String href; //链接地址
	
	/**
	 * @return the advertPutId
	 */
	public long getAdvertPutId() {
		return advertPutId;
	}
	/**
	 * @param advertPutId the advertPutId to set
	 */
	public void setAdvertPutId(long advertPutId) {
		this.advertPutId = advertPutId;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the minimizeContent
	 */
	public String getMinimizeContent() {
		return minimizeContent;
	}
	/**
	 * @param minimizeContent the minimizeContent to set
	 */
	public void setMinimizeContent(String minimizeContent) {
		this.minimizeContent = minimizeContent;
	}
	/**
	 * @return the minimizeHeight
	 */
	public String getMinimizeHeight() {
		return minimizeHeight;
	}
	/**
	 * @param minimizeHeight the minimizeHeight to set
	 */
	public void setMinimizeHeight(String minimizeHeight) {
		this.minimizeHeight = minimizeHeight;
	}
	/**
	 * @return the minimizeWidth
	 */
	public String getMinimizeWidth() {
		return minimizeWidth;
	}
	/**
	 * @param minimizeWidth the minimizeWidth to set
	 */
	public void setMinimizeWidth(String minimizeWidth) {
		this.minimizeWidth = minimizeWidth;
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
}