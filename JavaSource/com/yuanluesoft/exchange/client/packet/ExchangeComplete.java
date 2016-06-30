package com.yuanluesoft.exchange.client.packet;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeComplete extends ExchangePacket {
	private boolean success; //交换是否成功

	public ExchangeComplete(boolean success) {
		super();
		this.success = success;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}