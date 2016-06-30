package com.yuanluesoft.exchange.client.packet;

/**
 * 数据交换请求
 * @author linchuan
 *
 */
public class ExchangeRequest extends ExchangePacket {
	private String senderName; //发送者名称
	private String receiverName; //接收者名称
	private long requestId; //请求ID,由发送方生成,不允许重复
	
	public ExchangeRequest(String senderName, String receiverName, long requestId) {
		super();
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.requestId = requestId;
	}
	/**
	 * @return the requestId
	 */
	public long getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
}