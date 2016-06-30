package com.yuanluesoft.exchange.client.sender.tcp;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.exchange.client.sender.ExchangeConnection;
import com.yuanluesoft.exchange.client.sender.ExchangeDataSender;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeDataSenderImpl extends ExchangeDataSender {
	private final int SOCKET_BUFFER_SZIE = 8192; //8K,不宜设置太大,太大会影响同步的及时性
	private int maxExchangeSeconds = 3600; //最长数据交换时间
	private String exchangeServerHost; //数据交换服务器地址
	private int exchangeServerPort; //数据交换服务器端口

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.sender.ExchangeDataSender#connect()
	 */
	public ExchangeConnection connect() throws ExchangeException {
		try {
			Socket senderSocket = new Socket();
			senderSocket.setSoLinger(true, 60); //设置发送数据超时
			senderSocket.setSoTimeout(maxExchangeSeconds * 1000); //数据接收超时时间3600秒,最长数据交换时间
			senderSocket.setTcpNoDelay(true); //禁用Nagle算法,保证传输的实时性
			senderSocket.setSendBufferSize(SOCKET_BUFFER_SZIE); //设置发送缓存大小
			senderSocket.setReceiveBufferSize(SOCKET_BUFFER_SZIE); //设置接收缓存大小
			InetSocketAddress address = new InetSocketAddress(exchangeServerHost, exchangeServerPort);
			senderSocket.connect(address, 20000); //连接时间20秒
			if(Logger.isInfoEnabled()) {
				Logger.info("ExchangeClient: create new connection, remote port is " + exchangeServerHost + ":" + exchangeServerPort + ", local port is " + senderSocket.getLocalPort() + ".");
			}
			return new ExchangeTcpConnection(senderSocket.getInputStream(), senderSocket.getOutputStream(), senderSocket);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ExchangeException(e.getMessage());
		}
	}

	/**
	 * @return the exchangeServerHost
	 */
	public String getExchangeServerHost() {
		return exchangeServerHost;
	}

	/**
	 * @param exchangeServerHost the exchangeServerHost to set
	 */
	public void setExchangeServerHost(String exchangeServerHost) {
		this.exchangeServerHost = exchangeServerHost;
	}

	/**
	 * @return the exchangeServerPort
	 */
	public int getExchangeServerPort() {
		return exchangeServerPort;
	}

	/**
	 * @param exchangeServerPort the exchangeServerPort to set
	 */
	public void setExchangeServerPort(int exchangeServerPort) {
		this.exchangeServerPort = exchangeServerPort;
	}

	/**
	 * @return the maxExchangeSeconds
	 */
	public int getMaxExchangeSeconds() {
		return maxExchangeSeconds;
	}

	/**
	 * @param maxExchangeSeconds the maxExchangeSeconds to set
	 */
	public void setMaxExchangeSeconds(int maxExchangeSeconds) {
		this.maxExchangeSeconds = maxExchangeSeconds;
	}
}