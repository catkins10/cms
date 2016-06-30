package com.yuanluesoft.jeaf.rss.model;

/**
 * 
 * @author linchuan
 *
 */
public class RSSTextInput {
	private String description; //必需。定义对文本输入域的描述。 
	private String name; //必需。定义在文本输入域中的文本对象的名称。 
	private String link; //必需。定义处理文本输入的 CGI 脚本的 URL。 
	private String title; //必需。定义文本输入域中的提交按钮的标注 (label)。 
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
