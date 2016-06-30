package com.yuanluesoft.wechat.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class ResponseMessage extends ActionForm {
	private long unitId; //单位ID
	private String type; //消息类型,text/voice/video/music/news
	private String responseType; //应答目标, subscribe(关注)、unsubscribe(取消关注)、menu_(菜单事件)、location(上报地理位置事件) 、qrscene(扫描带参数二维码)、talk(用户发言)
	private String keywords; //关键字,分隔符:或/空格,与/加号
	private String title; //消息标题,消息类型为视频时有效
	private String description; //消息描述,消息类型为视频时有效
	private String content; //消息内容
	private Set news; //图文消息
	
	//扩展属性
	private boolean appendNewsDisabled; //是否禁止添加新的图文消息
	
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}
	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
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
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
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
	 * @return the news
	 */
	public Set getNews() {
		return news;
	}
	/**
	 * @param news the news to set
	 */
	public void setNews(Set news) {
		this.news = news;
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
	 * @return the appendNewsDisabled
	 */
	public boolean isAppendNewsDisabled() {
		return appendNewsDisabled;
	}
	/**
	 * @param appendNewsDisabled the appendNewsDisabled to set
	 */
	public void setAppendNewsDisabled(boolean appendNewsDisabled) {
		this.appendNewsDisabled = appendNewsDisabled;
	}
}