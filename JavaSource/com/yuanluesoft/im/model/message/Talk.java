package com.yuanluesoft.im.model.message;


/**
 * 发言
 * @author linchuan
 *
 */
public class Talk extends Message {
	private long chatId; //会话ID
	private boolean selfTalk; //是否用户自己的发言
	private boolean isGroupChat; //是否多人对话
	private boolean isCustomerService; //是否客服对话
	private String chatPersonNames; //对话用户姓名
	
	public Talk() {
		super();
		setCommand(CMD_TALK);
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

	/**
	 * @return the chatPersonNames
	 */
	public String getChatPersonNames() {
		return chatPersonNames;
	}

	/**
	 * @param chatPersonNames the chatPersonNames to set
	 */
	public void setChatPersonNames(String chatPersonNames) {
		this.chatPersonNames = chatPersonNames;
	}

	/**
	 * @return the isCustomerService
	 */
	public boolean isCustomerService() {
		return isCustomerService;
	}

	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setCustomerService(boolean isCustomerService) {
		this.isCustomerService = isCustomerService;
	}

	/**
	 * @return the isGroupChat
	 */
	public boolean isGroupChat() {
		return isGroupChat;
	}

	/**
	 * @param isGroupChat the isGroupChat to set
	 */
	public void setGroupChat(boolean isGroupChat) {
		this.isGroupChat = isGroupChat;
	}

	/**
	 * @return the selfTalk
	 */
	public boolean isSelfTalk() {
		return selfTalk;
	}

	/**
	 * @param selfTalk the selfTalk to set
	 */
	public void setSelfTalk(boolean selfTalk) {
		this.selfTalk = selfTalk;
	}
}