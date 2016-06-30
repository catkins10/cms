package com.yuanluesoft.microblog.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ResponseMessage extends ActionForm {
	private long unitId; //单位ID
	private String type; //消息类型,text/voice/video
	private String responseType; //响应目标,subscribe(关注)、unsubscribe(取消关注)、menu_(菜单事件)、location(上报地理位置事件) 、qrscene(扫描带参数二维码)、talk(用户发言)
	private String keywords; //关键字,分隔符:或/空格,与/加号
	private String content; //消息内容
	
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
}