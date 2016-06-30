package com.yuanluesoft.wechat.forms;

import com.yuanluesoft.wechat.pojo.WechatMessageNews;


/**
 * 
 * @author linchuan
 *
 */
public class ResponseMessageNews extends ResponseMessage {
	private WechatMessageNews messageNews = new WechatMessageNews(); //图文消息

	/**
	 * @return the messageNews
	 */
	public WechatMessageNews getMessageNews() {
		return messageNews;
	}

	/**
	 * @param messageNews the messageNews to set
	 */
	public void setMessageNews(WechatMessageNews messageNews) {
		this.messageNews = messageNews;
	}
}