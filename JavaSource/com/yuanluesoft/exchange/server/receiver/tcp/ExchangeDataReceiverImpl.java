package com.yuanluesoft.exchange.server.receiver.tcp;

import java.net.ServerSocket;
import java.net.Socket;

import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.exchange.client.sender.tcp.ExchangeTcpConnection;
import com.yuanluesoft.exchange.server.receiver.ExchangeDataReceiver;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeDataReceiverImpl extends ExchangeDataReceiver {
	private int listenPort = 10086; //侦听端口
	private int maxExchangeSeconds = 3600; //最长数据交换时间
	//私有属性
	private final int RECEIVE_BUFFER_SZIE = 8192; //8K,不宜设置太大,太大会影响同步的及时性
	private ServerSocket receiverSocket;
	private boolean stopped = false;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.server.receiver.ExchangeDataReceiver#listen()
	 */
	public void listen() throws ExchangeException {
		try {
			if(Logger.isInfoEnabled()) {
				Logger.info("ExchangeServer: listening on " + listenPort + ".");
			}
			receiverSocket = new ServerSocket(listenPort);
			while(!stopped) {
				Socket socket = receiverSocket.accept();
				//修改接收缓存大小
				socket.setReceiveBufferSize(RECEIVE_BUFFER_SZIE);
				socket.setTcpNoDelay(true); //禁用Nagle算法,保证传输的实时性
				socket.setSoLinger(true, 60); //设置发送数据超时
				socket.setSoTimeout(maxExchangeSeconds*1000); //数据接收超时时间3600秒,最长数据交换时间
				//通知交换服务处理
				getExchangeDataListener().onConnect(this, new ExchangeTcpConnection(socket.getInputStream(), socket.getOutputStream(), socket));
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ExchangeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.server.receiver.ExchangeDataReceiver#stop()
	 */
	public void stop() throws ExchangeException {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeServer: stop on " + listenPort + ".");
		}
		stopped = true;
		try {
			receiverSocket.close();
		}
		catch(Exception e) {
		
		}
		receiverSocket = null;
	}
	
	/**
	 * @return the listenPort
	 */
	public int getListenPort() {
		return listenPort;
	}

	/**
	 * @param listenPort the listenPort to set
	 */
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
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