package com.yuanluesoft.im.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ChatHistory extends ViewForm {
	private long chatId; //对话ID

	/**
	 * @return the chatId
	 */
	public long getChatId() {
		return chatId;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
}