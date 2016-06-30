package com.yuanluesoft.im.model.message;

/**
 * 请求获取对话详情
 * @author linchuan
 *
 */
public class ChatDetailRequest extends Message {
	private long chatId; //对话ID

	public ChatDetailRequest() {
		super();
		setCommand(CMD_CHAT_DETAIL_REQUEST);
	}

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