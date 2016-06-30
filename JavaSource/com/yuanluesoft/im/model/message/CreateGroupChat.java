package com.yuanluesoft.im.model.message;

/**
 * 创建多人对话
 * @author linchuan
 *
 */
public class CreateGroupChat extends Message {
	private long fromChatId; //源对话ID
	private String chatPersonIds; //对话用户ID列表
	
	public CreateGroupChat() {
		super();
		setCommand(CMD_CREATE_GROUP_CHAT);
	}
	/**
	 * @return the chatPersonIds
	 */
	public String getChatPersonIds() {
		return chatPersonIds;
	}
	/**
	 * @param chatPersonIds the chatPersonIds to set
	 */
	public void setChatPersonIds(String chatPersonIds) {
		this.chatPersonIds = chatPersonIds;
	}
	/**
	 * @return the fromChatId
	 */
	public long getFromChatId() {
		return fromChatId;
	}
	/**
	 * @param fromChatId the fromChatId to set
	 */
	public void setFromChatId(long fromChatId) {
		this.fromChatId = fromChatId;
	}
}