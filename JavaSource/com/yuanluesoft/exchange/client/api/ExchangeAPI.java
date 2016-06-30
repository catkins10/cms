package com.yuanluesoft.exchange.client.api;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.exchange.client.sender.tcp.ExchangeDataSenderImpl;
import com.yuanluesoft.exchange.client.spring.ExchangeClientImpl;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeAPI {

	/**
	 * 创建一个基于TCP协议的数据交换客户端
	 * @param exchangeServerHost
	 * @param exchangeServerPort
	 * @param receiverName
	 * @return
	 */
	public static ExchangeClient createExchangeTcpClient(String exchangeServerHost, int exchangeServerPort, String receiverName) {
		ExchangeClientImpl exchangeClient = new ExchangeClientImpl();
		exchangeClient.setName(UUIDLongGenerator.generateId() + "");
		//创建交换通道
		ExchangeDataSenderImpl dataSender = new ExchangeDataSenderImpl();
		dataSender.setReceiverNames(receiverName); //接收者名称
		dataSender.setExchangeServerHost(exchangeServerHost);
		dataSender.setExchangeServerPort(exchangeServerPort);
		List dataSenders = new ArrayList();
		dataSenders.add(dataSender);
		exchangeClient.setExchangeDataSenders(dataSenders);
		return exchangeClient;
	}
}