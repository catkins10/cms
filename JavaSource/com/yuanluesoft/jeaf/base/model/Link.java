package com.yuanluesoft.jeaf.base.model;


/**
 * 链接
 * @author linchuan
 *
 */
public class Link extends CloneableObject {
	private String url; //URL
	private String encoding; //编码,默认utf-8
	private String title; //标题
	private String iconUrl; //链接图片的URL
	
	public Link() {
		super();
	}
	
	public Link(String url, String encoding) {
		super();
		this.url = url;
		this.encoding = encoding;
	}
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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
}