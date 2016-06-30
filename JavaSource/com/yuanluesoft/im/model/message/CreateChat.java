package com.yuanluesoft.im.model.message;

/**
 * 创建一个对话
 * @author linchuan
 *
 */
public class CreateChat extends Message {
	private long chatPersonId; //对话用户ID

	public CreateChat() {
		super();
		setCommand(CMD_CREATE_CHAT);
	}

	/**
	 * @return the chatPersonId
	 */
	public long getChatPersonId() {
		return chatPersonId;
	}

	/**
	 * @param chatPersonId the chatPersonId to set
	 */
	public void setChatPersonId(long chatPersonId) {
		this.chatPersonId = chatPersonId;
	}
}