package com.yuanluesoft.im.webim.model;

import java.util.Set;

import com.yuanluesoft.im.model.message.ChatDetail;

/**
 * 
 * @author linchuan
 *
 */
public class WebimChat extends ChatDetail {
	private Set talks; //发言列表
	private String chatActionHTML; //对话按钮HTML

	/**
	 * @return the chatActionHTML
	 */
	public String getChatActionHTML() {
		return chatActionHTML;
	}

	/**
	 * @param chatActionHTML the chatActionHTML to set
	 */
	public void setChatActionHTML(String chatActionHTML) {
		this.chatActionHTML = chatActionHTML;
	}

	/**
	 * @return the talks
	 */
	public Set getTalks() {
		return talks;
	}

	/**
	 * @param talks the talks to set
	 */
	public void setTalks(Set talks) {
		this.talks = talks;
	}
}