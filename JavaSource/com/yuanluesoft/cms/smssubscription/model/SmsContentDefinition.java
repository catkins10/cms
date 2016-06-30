package com.yuanluesoft.cms.smssubscription.model;

/**
 * 短信内容定义
 * @author linchuan
 *
 */
public class SmsContentDefinition {
	private String name; //名称
	private String description; //描述
	private String sendMode; //内容发送模式, SmsContentService.SEND_MODE_REPLY/SEND_MODE_NEWS
	private String contentFields; //用户订购时需要输入的字段列表, 如：受理编号, 对SEND_MODE_NEWS有效
	private String contentServiceName; //内容服务名称 
	
	public SmsContentDefinition() {
		
	}

	public SmsContentDefinition(String name, String description, String sendMode, String contentFields) {
		this.name = name;
		this.description = description;
		this.sendMode = sendMode;
		this.contentFields = contentFields;
	}
	
	/**
	 * @return the contentFields
	 */
	public String getContentFields() {
		return contentFields;
	}
	/**
	 * @param contentFields the contentFields to set
	 */
	public void setContentFields(String contentFields) {
		this.contentFields = contentFields;
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
	 * @return the sendMode
	 */
	public String getSendMode() {
		return sendMode;
	}
	/**
	 * @param sendMode the sendMode to set
	 */
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	/**
	 * @return the contentServiceName
	 */
	public String getContentServiceName() {
		return contentServiceName;
	}

	/**
	 * @param contentServiceName the contentServiceName to set
	 */
	public void setContentServiceName(String contentServiceName) {
		this.contentServiceName = contentServiceName;
	}
}