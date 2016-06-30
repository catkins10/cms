package com.yuanluesoft.im.model.message;

/**
 * 提交发言 
 * @author linchuan
 *
 */
public class TalkSubmit extends Message {
	private long chatId; //对话ID
	private long customerServiceChatId; //客服对话ID
	private String content; //内容

	public TalkSubmit() {
		super();
		setCommand(CMD_TALK_SUBMIT);
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
	 * @return the customerServiceChatId
	 */
	public long getCustomerServiceChatId() {
		return customerServiceChatId;
	}

	/**
	 * @param customerServiceChatId the customerServiceChatId to set
	 */
	public void setCustomerServiceChatId(long customerServiceChatId) {
		this.customerServiceChatId = customerServiceChatId;
	}
}