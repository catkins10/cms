package com.yuanluesoft.jeaf.rss.model;

import java.sql.Timestamp;

/**
 * rss条目
 * @author linchuan
 *
 */
public class RSSItem {
	private String title; //必需的。定义此项目的标题。 
	private String link; //必需的。定义指向此项目的超链接。 
	private String description; //必需的。描述此项目。 
	private Timestamp pubDate; //可选的。定义此项目的最后发布日期。 
	private String guid; //可选的。为项目定义一个唯一的标识符。 

	private String author; //可选的。规定项目作者的电子邮件地址。 
	private String category; //可选的。定义项目所属的一个或多个类别。 
	private String comments; //可选的。允许项目连接到有关此项目的注释（文件）。 
	private String enclosure; //可选的。允许将一个媒体文件导入一个项中。 
	private String source; //可选的。为此项目指定一个第三方来源。
	
	/**
	 * 构造rss条目
	 * @param title 标题
	 * @param link 超链接
	 * @param description 描述
	 * @param pubDate 最后发布日期
	 * @param guid 唯一的标识符
	 */
	public RSSItem(String title, String link, String description, Timestamp pubDate, String guid) {
		super();
		this.title = title==null ? "无标题" : title;
		this.link = link==null ? "/" : link;
		this.description = description==null ? "" : description;
		this.pubDate = pubDate;
		this.guid = guid;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
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
	 * @return the enclosure
	 */
	public String getEnclosure() {
		return enclosure;
	}
	/**
	 * @param enclosure the enclosure to set
	 */
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}
	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the pubDate
	 */
	public Timestamp getPubDate() {
		return pubDate;
	}
	/**
	 * @param pubDate the pubDate to set
	 */
	public void setPubDate(Timestamp pubDate) {
		this.pubDate = pubDate;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
}
