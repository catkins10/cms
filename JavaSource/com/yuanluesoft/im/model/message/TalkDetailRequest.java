package com.yuanluesoft.im.model.message;


/**
 * 请求获取发言详细内容
 * @author linchuan
 *
 */
public class TalkDetailRequest extends Message {
	private long chatId; //对话ID
	private long customerServiceChatId; //客服对话ID
	private long beginTime; //开始时间
	
	public TalkDetailRequest() {
		super();
		setCommand(CMD_TALK_DETAIL_REQUEST);
	}

	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
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