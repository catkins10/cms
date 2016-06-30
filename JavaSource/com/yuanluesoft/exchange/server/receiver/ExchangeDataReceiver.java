package com.yuanluesoft.exchange.server.receiver;

import com.yuanluesoft.exchange.client.exception.ExchangeException;


/**
 * 数据交换服务器:数据接收器
 * @author linchuan
 *
 */
public abstract class ExchangeDataReceiver {
	private ExchangeDataListener exchangeDataListener;

	/**
	 * 侦听
	 */
	public abstract void listen() throws ExchangeException;
	
	/**
	 * 停止
	 */
	public abstract void stop() throws ExchangeException;

	/**
	 * @return the exchangeDataListener
	 */
	public ExchangeDataListener getExchangeDataListener() {
		return exchangeDataListener;
	}

	/**
	 * @param exchangeDataListener the exchangeDataListener to set
	 */
	public void setExchangeDataListener(ExchangeDataListener exchangeDataListener) {
		this.exchangeDataListener = exchangeDataListener;
	}
}