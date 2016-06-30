package com.yuanluesoft.jeaf.rss.model;

/**
 * rss频道图片
 * @author linchuan
 *
 */
public class RSSImage {
	private String link; //必需。定义提供该频道的网站的超连接。 
	private String title; //必需。定义当图片不能显示时所显示的替代文本。 
	private String url; //必需。定义图像的 URL。 

	private String description; //可选。规定图片链接的 HTML 标题属性中的文本。 
	private int height; //可选。定义图像的高度。默认是 31。最大值是 400。 
	private int width; //可选。定义图像的宽度。默认是 88。最大值是 144。 
	
	/**
	 * rss频道图片
	 * @param link 超连接
	 * @param title 图片替代文本
	 * @param url 图像的 URL
	 */
	public RSSImage(String link, String title, String url) {
		super();
		this.link = link;
		this.title = title;
		this.url = url;
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
}
