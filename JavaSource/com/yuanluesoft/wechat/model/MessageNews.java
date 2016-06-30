package com.yuanluesoft.wechat.model;

/**
 * 微信图文消息
 * @author chuan
 *
 */
public class MessageNews {
	private String title; //标题
	private String description; //描述
	private String content; //内容,群发消息时有效,支持HTML标签
	private String author; //作者,群发消息时有效
	private String url; //链接
	private int showCoverPic = 2; //是否显示封面,0/不显示,1/显示,2/继承
	private String imageFilePath; //图片路径
	
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
	 * @return the imageFilePath
	 */
	public String getImageFilePath() {
		return imageFilePath;
	}
	/**
	 * @param imageFilePath the imageFilePath to set
	 */
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	/**
	 * @return the showCoverPic
	 */
	public int getShowCoverPic() {
		return showCoverPic;
	}
	/**
	 * @param showCoverPic the showCoverPic to set
	 */
	public void setShowCoverPic(int showCoverPic) {
		this.showCoverPic = showCoverPic;
	}
}