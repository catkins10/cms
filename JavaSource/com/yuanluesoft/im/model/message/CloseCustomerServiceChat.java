package com.yuanluesoft.im.model.message;

/**
 * 关闭客服对话
 * @author linchuan
 *
 */
public class CloseCustomerServiceChat extends Message {
	private long customerServiceChatId; //对话ID

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