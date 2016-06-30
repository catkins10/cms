package com.yuanluesoft.exchange.client.packet;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeRequestAck extends ExchangeRequest {
	private boolean exchangeAbled; //是否可以交换

	public ExchangeRequestAck(String senderName, String receiverName, long requestId, boolean exchangeAbled) {
		super(senderName, receiverName, requestId);
		this.exchangeAbled = exchangeAbled;
	}

	/**
	 * @return the exchangeAbled
	 */
	public boolean isExchangeAbled() {
		return exchangeAbled;
	}

	/**
	 * @param exchangeAbled the exchangeAbled to set
	 */
	public void setExchangeAbled(boolean exchangeAbled) {
		this.exchangeAbled = exchangeAbled;
	}
}