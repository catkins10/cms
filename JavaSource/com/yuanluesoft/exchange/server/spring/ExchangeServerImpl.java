package com.yuanluesoft.exchange.server.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.exchange.client.io.ExchangePacketReader;
import com.yuanluesoft.exchange.client.io.ExchangePacketWriter;
import com.yuanluesoft.exchange.client.packet.ExchangeComplete;
import com.yuanluesoft.exchange.client.packet.ExchangePacket;
import com.yuanluesoft.exchange.client.packet.ExchangeRequest;
import com.yuanluesoft.exchange.client.packet.ExchangeRequestAck;
import com.yuanluesoft.exchange.client.packet.KeepAlive;
import com.yuanluesoft.exchange.client.packet.Login;
import com.yuanluesoft.exchange.client.packet.LoginAck;
import com.yuanluesoft.exchange.client.sender.ExchangeConnection;
import com.yuanluesoft.exchange.server.ExchangeServer;
import com.yuanluesoft.exchange.server.receiver.ExchangeDataListener;
import com.yuanluesoft.exchange.server.receiver.ExchangeDataReceiver;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 数据交换服务器
 * @author linchuan
 *
 */
public class ExchangeServerImpl implements ExchangeServer, ExchangeDataListener {
	private String validateKey = "20050718"; //校验密钥
	private List exchangeDataReceivers; //接收器列表
	
	//私有属性
	private List exchangeListenThreads = new ArrayList(); //侦听线程列表
	private Map exchangeConnections = new HashMap(); //数据交换连接
	private boolean stopped = false; //是否停止
	private KeepAliveThread keepAliveThread; //心跳线程
	
	/**
	 * 启动
	 */
	public void startup() {
		//启动心跳线程
		keepAliveThread = new KeepAliveThread();
		keepAliveThread.start();
		
		//启动交换线程
		for(Iterator iterator = exchangeDataReceivers.iterator(); iterator.hasNext();) {
			ExchangeDataReceiver exchangeDataReceiver = (ExchangeDataReceiver)iterator.next();
			ExchangeListenThread exchangeListenThread = new ExchangeListenThread(exchangeDataReceiver, this);
			exchangeListenThread.start();
			exchangeListenThreads.add(exchangeListenThread);
		}
	}
	
	/**
	 * 关闭
	 */
	public void shutdown() {
		stopped = true;
		//关闭心跳线程
		keepAliveThread.interrupt();
	
		//关闭交换线程
		for(Iterator iterator = exchangeListenThreads.iterator(); iterator.hasNext();) {
			ExchangeListenThread exchangeListenThread = (ExchangeListenThread)iterator.next();
			exchangeListenThread.interrupt();
		}
		
		//关闭连接
		for(Iterator iterator = exchangeConnections.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ExchangeConnection exchangeConnection = (ExchangeConnection)exchangeConnections.get(key);
			try {
				exchangeConnection.close();
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.server.receiver.ExchangeDataListener#onConnect(com.yuanluesoft.exchange.server.receiver.ExchangeDataReceiver, com.yuanluesoft.exchange.client.sender.ExchangeConnection)
	 */
	public void onConnect(final ExchangeDataReceiver exchangeDataReceiver, final ExchangeConnection exchangeConnection) {
		//创建新的线程处理连接
		new Thread() {
			public void run() {
				Thread.currentThread().setName(Thread.currentThread().getName() + "-ExchangeServer");
				try {
					//读取包
					ExchangePacket exchangePacket = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(exchangePacket instanceof Login) { //登录
						processLoginPacket((Login)exchangePacket, exchangeDataReceiver, exchangeConnection);
					}
					else if(exchangePacket instanceof ExchangeRequestAck) { //数据交换请求应答
						processExchangeRequestAck((ExchangeRequestAck)exchangePacket, exchangeDataReceiver, exchangeConnection);
					}
					else if(exchangePacket instanceof ExchangeRequest) { //数据交换请求
						processExchangeRequestPacket((ExchangeRequest)exchangePacket, exchangeDataReceiver, exchangeConnection);
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}.start();
	}
	
	/**
	 * 处理登录包
	 * @param loginPacket
	 * @param exchangeConnection
	 * @throws Exception
	 */
	private void processLoginPacket(Login loginPacket, ExchangeDataReceiver exchangeDataReceiver, ExchangeConnection exchangeConnection) throws Exception {
		if(Logger.isDebugEnabled()) {
			Logger.debug("ExchangeServer: process login request, client name is " + loginPacket.getSenderName() + ".");
		}
		//把连接写入通知列表
		exchangeConnections.put("CLIENT:" + loginPacket.getSenderName(), exchangeConnection);
		//发送登录应答
		ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new LoginAck(), validateKey);
	}
	
	/**
	 * 处理交换连接请求
	 * @param exchangeRequestPacket
	 * @param exchangeConnection
	 * @throws Exception
	 */
	private void processExchangeRequestPacket(ExchangeRequest exchangeRequestPacket, ExchangeDataReceiver exchangeDataReceiver, ExchangeConnection exchangeConnection) throws Exception {
		//交换过程:连接数据交换中心->发送交换请求包->交换中心转发请求包给接收者->接收者创建数据交换连接->接收者发送数据交换应答->交换中心转发交换应答->开始发送数据->等待交换结束包
		if(Logger.isDebugEnabled()) {
			Logger.debug("ExchangeServer: process exchange request, sender is " + exchangeRequestPacket.getSenderName() + ", receiver is " + exchangeRequestPacket.getReceiverName() + ", request id is " + exchangeRequestPacket.getRequestId() + ".");
		}
		try {
			//获取接收者连接
			ExchangeConnection receiverConnection = (ExchangeConnection)exchangeConnections.get("CLIENT:" + exchangeRequestPacket.getReceiverName());
			if(receiverConnection==null) { //接收者未上线
				ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new ExchangeRequestAck(exchangeRequestPacket.getSenderName(), exchangeRequestPacket.getReceiverName(), exchangeRequestPacket.getRequestId(), false), validateKey);
				return;
			}
			//写入请求队列
			exchangeConnections.put(exchangeRequestPacket.getSenderName() + "." + exchangeRequestPacket.getRequestId(), exchangeConnection);
			//转发给接收者
			ExchangePacketWriter.writePacket(receiverConnection.getOutputStream(), exchangeRequestPacket, validateKey);
			for(;;) {
				//等待数据包
				ExchangePacket exchangePacket = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
				if(exchangePacket instanceof ExchangeComplete) { //交换结束
					break;
				}
				//转发给接收者
				receiverConnection = (ExchangeConnection)exchangeConnections.get(exchangeRequestPacket.getReceiverName() + "." + exchangeRequestPacket.getRequestId());
				ExchangePacketWriter.writePacket(receiverConnection.getOutputStream(), exchangePacket, validateKey);
			}
		}
		finally {
			exchangeConnections.remove(exchangeRequestPacket.getSenderName() + "." + exchangeRequestPacket.getRequestId());
			exchangeConnection.close();
		}
	}
	
	/**
	 * 处理交换连接请求应答
	 * @param exchangeRequestAck
	 * @param exchangeConnection
	 * @throws Exception
	 */
	private void processExchangeRequestAck(ExchangeRequestAck exchangeRequestAck, ExchangeDataReceiver exchangeDataReceiver, ExchangeConnection exchangeConnection) throws Exception {
		//交换过程:连接数据交换中心->发送交换请求包->交换中心转发请求包给接收者->接收者创建数据交换连接->接收者发送数据交换应答->交换中心转发交换应答->开始发送数据->等待交换结束包
		if(Logger.isDebugEnabled()) {
			Logger.debug("ExchangeServer: process exchange request ack, sender is " + exchangeRequestAck.getSenderName() + ", receiver is " + exchangeRequestAck.getReceiverName() + ", request id is " + exchangeRequestAck.getRequestId() + ".");
		}
		try {
			//获取发送者连接
			ExchangeConnection senderConnection = (ExchangeConnection)exchangeConnections.get(exchangeRequestAck.getSenderName() + "." + exchangeRequestAck.getRequestId());
			//写入请求队列
			exchangeConnections.put(exchangeRequestAck.getReceiverName() + "." + exchangeRequestAck.getRequestId(), exchangeConnection);
			//转发给发送者
			ExchangePacketWriter.writePacket(senderConnection.getOutputStream(), exchangeRequestAck, validateKey);
			for(;;) {
				//等待数据包
				ExchangePacket exchangePacket = null;
				try {
					exchangePacket = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					//转发给发送者
					ExchangePacketWriter.writePacket(senderConnection.getOutputStream(), exchangePacket, validateKey);
				}
				finally {
					if(exchangePacket!=null && exchangePacket instanceof ExchangeComplete) { //交换结束
						break;
					}
				}
			}
		}
		finally {
			exchangeConnections.remove(exchangeRequestAck.getReceiverName() + "." + exchangeRequestAck.getRequestId());
			exchangeConnection.close();
		}
	}
	
	/**
	 * 侦听线程
	 * @author linchuan
	 *
	 */
	private class ExchangeListenThread extends Thread {
		private ExchangeDataReceiver exchangeDataReceiver; //接收器
		private ExchangeDataListener exchangeDataListener; //侦听器

		public ExchangeListenThread(ExchangeDataReceiver exchangeDataReceiver, ExchangeDataListener exchangeDataListener) {
			super();
			this.exchangeDataReceiver = exchangeDataReceiver;
			this.exchangeDataListener = exchangeDataListener;
		}


		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			Thread.currentThread().setName(Thread.currentThread().getName() + "-ExchangeServer");
			while(!stopped) {
				try {
					exchangeDataReceiver.setExchangeDataListener(exchangeDataListener);
					exchangeDataReceiver.listen(); //启动侦听
				}
				catch(Error e) {
					e.printStackTrace();
					Logger.error(e.getMessage());
					try {
						exchangeDataReceiver.stop();
					}
					catch(Exception ex) {
						
					}
				}
				catch(Exception e) {
					Logger.exception(e);
					try {
						exchangeDataReceiver.stop();
					}
					catch(Exception ex) {
						
					}
				}
			}
		}
	}
	
	/**
	 * 心跳线程
	 * @author linchuan
	 *
	 */
	private class KeepAliveThread extends Thread {
		public void run() {
			Thread.currentThread().setName(Thread.currentThread().getName() + "-ExchangeServer-KeepAlive");
			while(!stopped) {
				try {
					Thread.sleep(28000); //28秒
					for(Iterator iterator = exchangeConnections.keySet().iterator(); iterator.hasNext();) {
						String key = (String)iterator.next();
						if(key.startsWith("CLIENT:")) {
							ExchangeConnection exchangeConnection = (ExchangeConnection)exchangeConnections.get(key);
							ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new KeepAlive(), validateKey);
						}
					}
				}
				catch(Error e) {
					
				}
				catch(Exception e) {
					
				}
			}
		}
	}
	
	/**
	 * @return the exchangeDataReceivers
	 */
	public List getExchangeDataReceivers() {
		return exchangeDataReceivers;
	}
	/**
	 * @param exchangeDataReceivers the exchangeDataReceivers to set
	 */
	public void setExchangeDataReceivers(List exchangeDataReceivers) {
		this.exchangeDataReceivers = exchangeDataReceivers;
	}
	/**
	 * @return the validateKey
	 */
	public String getValidateKey() {
		return validateKey;
	}
	/**
	 * @param validateKey the validateKey to set
	 */
	public void setValidateKey(String validateKey) {
		this.validateKey = validateKey;
	}
}