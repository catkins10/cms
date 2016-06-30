package com.yuanluesoft.jeaf.rss.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class OPMLOutline {
	private String text; //如：新闻要闻
	private String title; //如：新闻要闻
	private String type; //如：rss
	private String xmlUrl; //如：http://rss.sina.com.cn/news/marquee/ddt.xml
	private String htmlUrl; //如：www.sina.com.cn
	
	private List childOutlines; //下级大纲列表
	
	/**
	 * @return the htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}
	/**
	 * @param htmlUrl the htmlUrl to set
	 */
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * @return the xmlUrl
	 */
	public String getXmlUrl() {
		return xmlUrl;
	}
	/**
	 * @param xmlUrl the xmlUrl to set
	 */
	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}
	/**
	 * @return the childOutlines
	 */
	public List getChildOutlines() {
		return childOutlines;
	}
	/**
	 * @param childOutlines the childOutlines to set
	 */
	public void setChildOutlines(List childOutlines) {
		this.childOutlines = childOutlines;
	}
}