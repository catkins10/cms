package com.yuanluesoft.exchange.server.receiver;

import com.yuanluesoft.exchange.client.sender.ExchangeConnection;

/**
 * 
 * @author linchuan
 *
 */
public interface ExchangeDataListener {
	
	/**
	 * 处理数据交换连接
	 * @param exchangeDataReceiver
	 * @param exchangeConnection
	 */
	public void onConnect(ExchangeDataReceiver exchangeDataReceiver,  ExchangeConnection exchangeConnection);
}