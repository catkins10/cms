/*
 * Created on 2006-8-9
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.serverchannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.network.exception.NetworkException;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.network.datagramchannel.model.DatagramConnection;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.ConnectAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.ConnectPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DataAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DataPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DatagramPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p.P2PConnectAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p.P2PConnectPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.parser.PacketParser;
import com.yuanluesoft.jeaf.network.datagramchannel.serverchannel.listener.DatagramChannelListener;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;

/**
 *
 * @author LinChuan
 *
 */
public class DatagramServerChannel implements Serializable {
	private ThreadPool threadPool = new ThreadPool(20, 10, 5000);
	private List outPacketQueue = new ArrayList(); //包发送队列
	
	private int port; //端口
	private Cache connectionCache; //连接缓存
	
	//发送缓存
	private byte[] sendDataArray = new byte[DatagramPacket.MAX_PACKET_LENGTH];
    private ByteBuffer sendBuffer = ByteBuffer.wrap(sendDataArray);
    
    //接收缓存
	private byte[] receiveDataArray = new byte[DatagramPacket.MAX_PACKET_LENGTH];
    private ByteBuffer receiveBuffer = ByteBuffer.wrap(receiveDataArray);
    private List ackPackets = new ArrayList(); //应答包队列
    
    private java.nio.channels.DatagramChannel datagramChannel; //UDP通道
    
    private PacketParser packetParser = new PacketParser(); //UDP包解析

    //接收相关
    private Selector receiveSelector; //接收选择器
    private SelectionKey receiveSelectionKey;
    private ReceiveThread receiveThread;
    private Set listeners= new HashSet(); //事件监听器
    
    //发送相关
    private Selector sendSelector; //发送选择器
    private SelectionKey sendSelectionKey;
    private SendThread sendThread;
    
    
    public DatagramServerChannel(Cache connectionCache) {
		super();
		this.connectionCache = connectionCache;
	}

	/**
     * 启动通道
     *
     */
    public DatagramServerChannel openChannel(int port) throws NetworkException {
        try {
        	this.port = port; //端口
        	//初始化UDP通道
        	datagramChannel = java.nio.channels.DatagramChannel.open();
        	datagramChannel.configureBlocking(false);
        	InetSocketAddress address = new InetSocketAddress(port);
        	datagramChannel.socket().bind(address);
        	datagramChannel.socket().setSendBufferSize(8192); //设置发送缓存
        	datagramChannel.socket().setReceiveBufferSize(Math.max(8192, DatagramPacket.MAX_PACKET_LENGTH * 12)); //设置接收缓存
        	
        	//启动接收线程
        	receiveSelector = Selector.open(); //注册到selector
        	receiveSelectionKey = datagramChannel.register(receiveSelector,  SelectionKey.OP_READ);
        	receiveThread = new ReceiveThread();
        	receiveThread.start();
        	
        	//启动发送线程
        	sendSelector = Selector.open();
        	sendSelectionKey = datagramChannel.register(sendSelector,  SelectionKey.OP_WRITE);
        	sendThread = new SendThread();
        	sendThread.start();
        	Logger.info("DatagramChannel: startup on port " + port + ".");
        	return this;
        }
        catch(Exception e) {
        	Logger.exception(e);
        	throw new NetworkException();
        }
    }
    
    /**
     * 关闭通道
     *
     */
    public void closeChannel() {
        try {
           	//关闭接收进程
            receiveThread.interrupt();
            receiveSelector.wakeup();
            Thread.sleep(100);
            receiveSelectionKey.cancel();
            receiveSelector.keys().clear();
        	
        	//关闭接收进程
            sendThread.interrupt();
            sendSelector.wakeup();
            Thread.sleep(100);
            sendSelectionKey.cancel();
            sendSelector.keys().clear();
            
            //关闭SOCKET
            datagramChannel.socket().disconnect();
            datagramChannel.socket().close();
            datagramChannel.disconnect();
            datagramChannel.close();
            datagramChannel = null;
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }
    
    /**
     * 发送
     * @param connection
     * @param in
     * @param dataLength
     * @param ack 是否需要应答
     * @param isResponse 是否应答的数据
     * @throws NetworkException
     */
    public void sendData(DatagramConnection connection, InputStream in, int dataLength, boolean ack, boolean isResponse) throws NetworkException {
    	if(in!=null) {
    		if(ack) {
    			sendData(connection, new DataSource(in, dataLength), isResponse);
    		}
    		else {
    			sendUnackData(connection, new DataSource(in, dataLength), isResponse);
    		}
    	}
    }
    
    /**
     * 发送
     * @param connection
     * @param data
     * @param beginIndex
     * @param dataLength
     * @param ack 是否需要应答
     * @param isResponse 是否应答的数据
     * @throws NetworkException
     */
    public void sendData(DatagramConnection connection, byte[] data, int beginIndex, int dataLength, boolean ack, boolean isResponse) throws NetworkException {
    	if(data!=null) {
    		if(ack) {
    			sendData(connection, new DataSource(data, beginIndex, dataLength), isResponse);
    		}
    		else {
    			sendUnackData(connection, new DataSource(data, beginIndex, dataLength), isResponse);
    		}
    	}
    }
    
    /**
     * 发送
     * @param connection
     * @param dataSource
     * @param isResponse 是否应答的数据
     * @throws NetworkException
     */
    private void sendData(DatagramConnection connection, DataSource dataSource, boolean isResponse) throws NetworkException {
    	synchronized(connection.getSendMutex()) {
    		List sendWindow = new ArrayList();
    		//计算窗口大小
    		int dataLength = (connection.getTurnPort()>0 ? DataPacket.MAX_DATA_TURN_LENGTH : DataPacket.MAX_DATA_LENGTH);
    		int packetCount = (dataSource.getDataLength() + dataLength - 1) / dataLength;
    		int resendTimes = 0; //重发计数器
    		int unresendTimes = 0; //未重发计数器
    		for(int i=0; i<packetCount; ) {
    			sendWindow.clear();
    			int windowSize = Math.min(connection.getMaxSendWindowSize(), packetCount - i);
    			for(int j=0; j<windowSize; j++) {
    				DataPacket dataPacket = new DataPacket();
    				dataPacket.setWindowSize((byte)windowSize); //窗口大小
    				dataPacket.setRemoteIp(connection.getRemoteIp()); //接收人IP
    				dataPacket.setRemotePort(connection.getRemotePort()); //接收人端口
    				dataPacket.setTurnIp(connection.getTurnIp()); //转发服务器地址
    				dataPacket.setTurnPort(connection.getTurnPort()); //转发服务器端口
    				dataPacket.setSequence((char)(connection.getSendSequence() + j)); //包序号
    				dataPacket.setData(dataSource.readData(dataLength));
    				dataPacket.setFirstPacket(i==0 && j==0); //是否第一个包
    				dataPacket.setLastPacket(!dataSource.hasMoreData()); //是否最后一个包
    				dataPacket.setResponse(isResponse); //是否应答的数据
    				dataPacket.setAck(true); //需要应答
    				dataPacket.setValidateCode(generateValidateCode(dataPacket.getData())); //生成校验码
    				sendWindow.add(dataPacket);
    			}
    			i += windowSize;
    			//发送数据包
    			for(long sendTime=System.currentTimeMillis(); ;) {
    				if(Logger.isDebugEnabled()) {
						Logger.debug("DatagramChannel(" + connection.getRemoteIp() + ":" + (int)connection.getRemotePort() + "): send data packets, beign sequence is " + (int)connection.getSendSequence());
					}
    				putPacketQueue(sendWindow);
    				//读取数据应答包
    				DataAckPacket ackPacket = null;
    				try {
    					ackPacket = (DataAckPacket)readAckPacket(connection.getRemoteIp(), connection.getRemotePort(), DatagramPacket.CMD_DATA_ACK, windowSize * connection.getTimeout(), (char)(connection.getSendSequence() + sendWindow.size()));
    				}
    				catch(Exception e) {
    					
    				}
    				if(ackPacket!=null) {
    					if(ackPacket.isRefused()) { //数据被拒绝接收
    						sendWindow.clear();
    						throw new NetworkException("refused");
    					}
    					connection.setSendSequence((char)(connection.getSendSequence() + sendWindow.size())); //设置发送窗口起始包序号
    	    			sendWindow.clear(); //清除发送窗口
    	    			if(unresendTimes++ > 10) { //连续10次没有重发
    						unresendTimes = 0;
    						resendTimes = 0;
    						connection.setMaxSendWindowSize((byte)Math.min(12, connection.getMaxSendWindowSize() * 2)); //窗口尺寸翻倍
    						connection.setTimeout((char)(Math.max(100, connection.getTimeout() / 2))); //重发延时减半
						}
        	    		break;
    	    		}
    				if(System.currentTimeMillis() - sendTime > sendWindow.size() * connection.getTimeout() * 10) { //超时
						sendWindow.clear();
						throw new NetworkException("timeOut");
					}
					unresendTimes = 0;
					resendTimes++;
					if(Logger.isDebugEnabled()) {
						Logger.debug("DatagramChannel(" + connection.getRemoteIp() + ":" + (int)connection.getRemotePort() + "): resend data packet, sequence is " + (int)connection.getSendSequence());
					}
    			}
    			if(resendTimes>=3) { //重发超过3次
					resendTimes = 0;
					connection.setMaxSendWindowSize((byte)Math.max(1,  (int)(connection.getMaxSendWindowSize()/2))); //窗口尺寸减半
					connection.setTimeout((char)(Math.min(3000, connection.getTimeout() * 2))); //重发延时加倍
    			}
    		}
    	}
    }
    
    /**
     * 生成校验码
     * @param data
     * @return
     */
    private byte generateValidateCode(byte[] data) {
    	if(data==null) {
    		return 0;
    	}
    	byte validateCode = 0;
		for(int i=0; i<data.length; i+=10) {
			validateCode += data[i];
		}
		return validateCode;
    }
    
    /**
     * 发送数据应答包
     * @param connection
     * @throws NetworkException
     */
    private void sendDataAckPacket(DatagramConnection connection) throws NetworkException {
    	DataAckPacket ackPacket = new DataAckPacket();
		ackPacket.setRemoteIp(connection.getRemoteIp()); //接收人IP
		ackPacket.setRemotePort(connection.getRemotePort()); //接收人端口
		ackPacket.setTurnIp(connection.getTurnIp()); //转发地址
		ackPacket.setTurnPort(connection.getTurnPort()); //转发端口
		ackPacket.setNextReceiveSequence(connection.getReceiveSequence());
		putPacketQueue(ackPacket);
	}
    
    /**
     * 发送不需要应答的数据
     * @param connection
     * @param dataSource
     * @throws NetworkException
     */
    private void sendUnackData(DatagramConnection connection, DataSource dataSource, boolean isResponse) throws NetworkException {
    	while(dataSource.hasMoreData()) {
			DataPacket dataPacket = new DataPacket();
			dataPacket.setRemoteIp(connection.getRemoteIp()); //接收人IP
			dataPacket.setRemotePort(connection.getRemotePort()); //接收人端口
			dataPacket.setTurnIp(connection.getTurnIp()); //转发服务器地址
			dataPacket.setTurnPort(connection.getTurnPort()); //转发服务器端口
			dataPacket.setData(dataSource.readData(connection.getTurnPort()>0 ? DataPacket.MAX_DATA_TURN_LENGTH : DataPacket.MAX_DATA_LENGTH));
			dataPacket.setResponse(isResponse); //是否应答的数据
			dataPacket.setAck(false); //需要应答
			putPacketQueue(dataPacket);
		}
    }
    
    /**
     * 处理接收到的包
     * @param packet
     * @param remoteAddress
     * @throws NetworkException
     */
    private void processReceivedPacket(DatagramPacket packet, String remoteIp, char remotePort) throws NetworkException {
    	switch(packet.getCommand()) {
    	case DatagramPacket.CMD_CONNECT: //连接包
    		processConnectPacket((ConnectPacket)packet, remoteIp, remotePort);
    		break;
    	
    	case DatagramPacket.CMD_DATA: //数据包
    		processDataPacket((DataPacket)packet, remoteIp, remotePort);
    		break;
    	}
    }

    /**
     * 处理连接请求包
     * @param connectPacket
     * @param connection
     * @param remoteAddress
     * @throws NetworkException
     */
    private void processConnectPacket(ConnectPacket connectPacket, String remoteIp, char remotePort) throws NetworkException {
     	//发送连接应答包
    	ConnectAckPacket connectAckPacket = new ConnectAckPacket();
    	connectAckPacket.setRemoteIp(remoteIp);
    	connectAckPacket.setRemotePort(remotePort);
    	connectAckPacket.setConnecterIp(remoteIp); //连接者IP
    	connectAckPacket.setConnecterPort(remotePort); //连接者端口
   		//等待连接确认包,三次握手
    	long currentTimeMillis = System.currentTimeMillis();
    	int i=0;
    	for(; i<5; i++) { //重发5次
    		putPacketQueue(connectAckPacket); //送发送队列
    		//接收连接完成包
    		try {
    			readAckPacket(remoteIp, remotePort, DatagramPacket.CMD_CONNECT_COMPLETE, 2000, '0');
    			break;
    		}
    		catch(NetworkException e) {
    			
    		}
    	}
    	if(i>=5) {
    		Logger.info("DatagramChannel(" + remoteIp + ":" + (int)remotePort + "): connection create failed.");
			return;
    	}
       	//创建连接,并写入缓存
    	DatagramConnection connection = new DatagramConnection();
    	connection.setRemoteIp(remoteIp); //远程IP
    	connection.setRemotePort(remotePort); //远程端口
    	//根据实际连接时间调整超时时间和窗口大小
    	connection.setTimeout((char)Math.max(100, System.currentTimeMillis() - currentTimeMillis + 20));
    	connection.setMaxSendWindowSize((byte)Math.max(1, Math.min(12, 3000/connection.getTimeout())));
    	synchronized(connectionCache) {
			try {
				connectionCache.put(remoteIp + ":" + (int)remotePort, connection);
			}
			catch (CacheException e) {
				throw new NetworkException("cache exception");
			}
		}
    	Logger.info("DatagramChannel(" + remoteIp + ":" + (int)remotePort + "): connection " + remoteIp + ":" + (int)remotePort + " created, window size is " + connection.getMaxSendWindowSize() + ", timeout is " + (int)connection.getTimeout() + ".");
    }

    /**
     * 处理接收到的数据包
     * @param dataPacket
     * @param connection
     * @throws NetworkException
     */
    private void processDataPacket(DataPacket dataPacket, String remoteIp, char remotePort) throws NetworkException {
    	DatagramConnection connection = getConnection(remoteIp, remotePort);
    	if(connection==null) {
    		//回复数据被拒绝包
    		DataAckPacket ackPacket = new DataAckPacket();
    		ackPacket.setRemoteIp(remoteIp); //接收人IP
    		ackPacket.setRemotePort(remotePort); //接收人端口
    		ackPacket.setRefused(true);
    		putPacketQueue(ackPacket);
    		Logger.info("DatagramChannel(" + remoteIp + ":" + (int)remotePort + "): refused.");
    		return;
    	}
    	synchronized(connection.getReceiveWindow()) {
    		if(dataPacket.isAck()) { //数据需要应答
	    		char sequence = dataPacket.getSequence();
				byte windowSize = dataPacket.getWindowSize();
				if((char)(sequence - connection.getReceiveSequence()) >= windowSize) { //收到的包不在接收窗口中
					sendDataAckPacket(connection); //发送应答包
					return;
				}
				//检查校验码
				byte validateCode = generateValidateCode(dataPacket.getData());
				if(dataPacket.getValidateCode()!=validateCode) {
					sendDataAckPacket(connection); //发送应答包
					return;
				}
				
				//检查是否还有数据未被读取
				if(connection.isDataUnread()) { //数据还没有被处理,不继续接收新的数据
					return;
				}
				int i = connection.getReceiveWindow().size() - 1;
				for(; i>=0; i--) {
					DataPacket windowDataPacket = (DataPacket)connection.getReceiveWindow().get(i);
					char compare = (char)(sequence - windowDataPacket.getSequence()); 
					if(compare==0) {
						break;
					}
					else if(compare < windowSize) {
						connection.getReceiveWindow().add(i+1, dataPacket);
						break;
					}
				}
				if(i<0) {
					connection.getReceiveWindow().add(0, dataPacket);
				}
				//判断是否已经完成一个窗口数据包的接收
				if(connection.getReceiveWindow().size()<windowSize) { //尚未接收完一个窗口的数据
					return;
				}
    		}
			//已接收完毕,设置有数据需要被读取
			connection.setDataUnread(true);
			
			//获取窗口的第一个数据包
			dataPacket = (DataPacket)connection.getReceiveWindow().get(0);
			if(!dataPacket.isFirstPacket() || dataPacket.isResponse()) { //不是整个数据的第一个数据包,或者是应答数据
				//唤醒数据读取线程
				connection.getReceiveWindow().notifyAll();
			}
			else {
				//触发数据接收事件
				for(Iterator iterator = listeners.iterator(); iterator.hasNext();) {
					((DatagramChannelListener)iterator.next()).onDataArrive(this, connection);
				}
			}
    	}
    }

    /**
     * 读取数据,返回null表示数据接收完毕
     * @param connection
     * @return 
     * @throws NetworkException
     */
    public byte[] readData(DatagramConnection connection) throws NetworkException {
    	synchronized(connection.getReceiveWindow()) {
    		if(!connection.isDataUnread()) {
    			try {
    				connection.getReceiveWindow().wait(10000); //等待10秒
    			}
    			catch (InterruptedException e) {
					e.printStackTrace();
					throw new NetworkException();
				}
				if(!connection.isDataUnread()) {
					throw new NetworkException("time out");
				}
    		}
    		DataPacket dataPacket = (DataPacket)connection.getReceiveWindow().get(0);
    		byte[] data = dataPacket.getData();
    		if(dataPacket.isLastPacket() && data!=null) { //数据块的最后一个数据包
    			dataPacket.setData(null);
    		}
    		else {
    			connection.getReceiveWindow().remove(0);
    		}
    		if(connection.getReceiveWindow().isEmpty()) { //读取完毕
				//调整窗口起始顺序号
    			connection.setReceiveSequence((char)(dataPacket.getSequence() + 1));
    			connection.setDataUnread(false);
				sendDataAckPacket(connection); //发送应答包
    		}
    		return data;
    	}
    }
    
    /**
     * 添加包到发送队列
     * @param packet
     * @throws Exception
     */
    private void putPacketQueue(DatagramPacket packet) throws NetworkException {
    	synchronized(outPacketQueue) {
    		outPacketQueue.add(packet);
   			sendSelector.wakeup();
   			outPacketQueue.notify();
    	}
    }
    
    /**
     * 添加包列表到发送队列
     * @param packets
     * @throws NetworkException
     */
    private void putPacketQueue(List packets) throws NetworkException {
    	if(packets==null || packets.isEmpty()) {
    		return;
    	}
    	synchronized(outPacketQueue) {
    		outPacketQueue.addAll(packets);
    		sendSelector.wakeup();
    		outPacketQueue.notify();
    	}
    }
    
    /**
	 * 添加事件监听
	 * @param elementListener
	 */
	public void addListener(DatagramChannelListener listener) {
		synchronized(listeners){
			listeners.add(listener);
		}
	}
    
    /**
     * 处理请求
     * @param buffer
     * @param remoteAddress
     * @throws Exception
     */
    private void doReceive() throws NetworkException {
		SocketAddress remoteAddress;
        try {
        	receiveBuffer.clear();
        	remoteAddress = datagramChannel.receive(receiveBuffer);
			receiveBuffer.flip();
	        //解析接收到的包
	        int len = receiveBuffer.remaining();
	        if(len>0) {
	        	DatagramPacket packet = packetParser.parsePacket(receiveDataArray, len);
	        	String remoteIp;
	        	char remotePort;
	        	if(remoteAddress instanceof InetSocketAddress) {
		    		InetSocketAddress inetSocketAddress = (InetSocketAddress)remoteAddress;
		    		remoteIp = inetSocketAddress.getAddress().getHostAddress();
		    		remotePort = (char)inetSocketAddress.getPort();
		    	}
		    	else {
		    		try {
		    			remoteIp = PropertyUtils.getProperty(remoteAddress, "hostName").toString();
		    			remotePort = (char)((Integer)PropertyUtils.getProperty(remoteAddress, "port")).intValue();
		    		}
		    		catch(Exception e) {
		    			Logger.exception(e);
		    			return;
		    		}
		    	}
	        	
	        	if(Logger.isDebugEnabled()) {
	        		Logger.debug("DatagramChannel(" + remoteIp + ":" + (int)remotePort + "): receive a packet, class name is " + packet.getClass().getName() + (packet instanceof DataAckPacket ? ", ack sequence is " + (int)((DataAckPacket)packet).getNextReceiveSequence() : ""));
	        	}
	        	if("TURN".equals(packet.getTurnIp())) { //转发包,直接转发接收到的包
	        		packet.setTurnIp(packet.getRemoteIp());
	        		packet.setTurnPort(packet.getRemotePort());
	        		packet.setRemoteIp(remoteIp);
	        		packet.setRemotePort(remotePort);
	        		putPacketQueue(packet);
	        		return;
	        	}
	        	switch(packet.getCommand()) {
	        	case DatagramPacket.CMD_DATA_ACK: //数据应答包
	        	case DatagramPacket.CMD_CONNECT_COMPLETE: //连接完成包
	        		synchronized(ackPackets) {
	        			ackPackets.add(new DatagramAckPacket(packet, System.currentTimeMillis(), remoteIp, remotePort));
	        			ackPackets.notifyAll();
					}
	        		break;
	        	
	        	case DatagramPacket.CMD_P2P_CONNECT: //p2p连接包
	        	case DatagramPacket.CMD_P2P_FORWARD_CONNECT: //p2p(转发方式)连接包
	        		//穿透防火墙,建立P2P连接,具体步骤:
	        		//1.A向服务器发送P2P连接请求(CP2PConnectPacket)
	        		//2.服务器接收到请求(CP2PConnectPacket)后,修改对方地址为A的公网地址,转发请求给B
	        		//3.B发送应答(CP2PConnectAckPacket)给服务器
	        		//4.B同时向A发送打洞包(CP2PHolePacket)
	        		//5.服务器接收到B的应答后,修改对方地址为B的公网地址,向A发送P2P连接应答包(CP2PConnectAckPacket)
	        		//6.A如果没有收到应答包,返回第1步
	        		//7.A调用createConnection建立连接
	        		
	        		//建立转发方式的P2P连接,过程如下:
	        		//1.A向服务器发送转发方式P2P连接请求(CP2PConnectPacket)
	        		//2.服务器接收到请求(CP2PConnectPacket)后,修改对方地址为A的公网地址,转发请求给B
	        		//3.B发送连接应答包到服务器(CP2PConnectAckPacket)
	        		//4.服务器修改对方地址为B的公网地址,转发连接应答到A(CP2PConnectAckPacket)
	        		//5.A发送连完成包到服务器(CConnectCompletePacket)
	        		//6.服务器转发连接应答到B(CConnectCompletePacket)
	        		
	        		//转发请求到被连接方
	        		P2PConnectPacket p2pConnectPacket = (P2PConnectPacket)packet;
	        		p2pConnectPacket.setRemoteIp(p2pConnectPacket.getPeerIp());
	        		p2pConnectPacket.setRemotePort(p2pConnectPacket.getPeerPort());
	        		p2pConnectPacket.setPeerIp(remoteIp);
	        		p2pConnectPacket.setPeerPort(remotePort);
	        		putPacketQueue(p2pConnectPacket);
	        		break;
	        		
	        	case DatagramPacket.CMD_P2P_CONNECT_ACK: //p2p连接应答包
	        	case DatagramPacket.CMD_P2P_FORWARD_CONNECT_ACK: //p2p(转发方式)连接应答包
	        		//转发请求应答到被连接方
	        		P2PConnectAckPacket p2pConnectAckPacket = (P2PConnectAckPacket)packet;
	        		p2pConnectAckPacket.setRemoteIp(p2pConnectAckPacket.getPeerIp());
	        		p2pConnectAckPacket.setRemotePort(p2pConnectAckPacket.getPeerPort());
	        		p2pConnectAckPacket.setPeerIp(remoteIp);
	        		p2pConnectAckPacket.setPeerPort(remotePort);
	        		putPacketQueue(p2pConnectAckPacket);
	        		break;
	        		
	        	default:
	        		threadPool.execute(new ReceiveTask(this, packet, remoteIp, remotePort)); //启动接收进程处理接收到的包,以便使得接收通道可以继续处理别的请求
	        	}
	        }
		}
        catch(Exception e) {
			Logger.exception(e);
		}
	}
    
    /**
     * 数据发送
     * @throws NetworkException
     */
    private void doSend() throws NetworkException {
		synchronized(outPacketQueue) {
			for(Iterator iterator = outPacketQueue.iterator(); iterator.hasNext();) {
	            DatagramPacket packet = (DatagramPacket)iterator.next();
	           try {
					int len = packetParser.putPacketBuffer(packet, sendDataArray);
					sendBuffer.flip();
					sendBuffer.limit(len);
					
					while(sendBuffer.hasRemaining()) {
						if(packet.getTurnPort()>0) {
							datagramChannel.send(sendBuffer, new InetSocketAddress(packet.getTurnIp(), (int)packet.getTurnPort()));
							if(Logger.isDebugEnabled()) {
								Logger.debug("DatagramChannel(" + packet.getTurnIp() + ":" + (int)packet.getTurnPort() + "): forward a packet, class name is " + packet.getClass().getName() + ".");
							}
						}
						else {
							datagramChannel.send(sendBuffer, new InetSocketAddress(packet.getRemoteIp(), (int)packet.getRemotePort()));
							if(Logger.isDebugEnabled()) {
								Logger.debug("DatagramChannel(" + packet.getRemoteIp() + ":" + (int)packet.getRemotePort() + "): send a packet, class name is " + packet.getClass().getName() + ".");
							}
						}
					}
					sendBuffer.clear();
				}
				catch (Exception e) {
					Logger.info("DatagramChannel: send packet to " + packet.getRemoteIp() + ":" + (int)packet.getRemotePort() + " failed.");
					Logger.exception(e);
				}
				iterator.remove(); //删除包
			}
        }
    }
    
    /**
     * 读取应答包
     * @param fromIp
     * @param fromPort
     * @param command
     * @param timeout
     * @param nextDataPacketSequence
     * @return
     * @throws NetworkException
     */
    private DatagramPacket readAckPacket(String fromIp, char fromPort, byte command, int timeout, char nextDataPacketSequence) throws NetworkException {
    	synchronized(ackPackets) {
    		long waitTime = System.currentTimeMillis();
    		for(boolean first = true; ; first = false) {
				if(ackPackets.isEmpty() || !first) {
					try {
						ackPackets.wait(timeout);
					}
					catch (InterruptedException e) {
						Logger.exception(e);
						throw new NetworkException();
					}
				}
				if(ackPackets.isEmpty()) {
					throw new NetworkException("timeout");
				}
				long now = System.currentTimeMillis();
    			for(Iterator iterator = ackPackets.iterator(); iterator.hasNext();) {
    				DatagramAckPacket ackPacket = (DatagramAckPacket)iterator.next();
    				if(ackPacket.receiveTime + 5000 < now) { //超过5秒钟还没处理的包
    					iterator.remove();
    					continue;
    				}
    				//判断是否是需要的包
    				if(ackPacket.getPacket().getCommand()==command && ackPacket.getRemotePort()==fromPort && ackPacket.getRemoteIp().equals(fromIp)) {
    					iterator.remove();
    					if(command!=DatagramPacket.CMD_DATA_ACK || //不是数据应答包
    					   ((DataAckPacket)ackPacket.getPacket()).isRefused() || //数据被拒绝接收
    					   ((DataAckPacket)ackPacket.getPacket()).getNextReceiveSequence()==nextDataPacketSequence) { //或者数据包的序号一致
    						return ackPacket.getPacket();
    					}
    				}
    			}
    			timeout -= (now - waitTime);
    			if(timeout<=0) {
    				throw new NetworkException("timeout");
    			}
    			waitTime = now;
    		}
    	}
    }
    
    /**
     * 获取连接
     * @param remoteAddress
     * @return
     * @throws NetworkException
     */
    public DatagramConnection getConnection(String remoteIp, char remotePort) throws NetworkException {
    	synchronized(connectionCache) {
    		try {
    			DatagramConnection connection = (DatagramConnection)connectionCache.get(remoteIp + ":" + (int)remotePort);
    			return connection;
			}
    		catch (CacheException e) {
				throw new NetworkException("cache exception");
			}
    	}
    }
    
	/**
	 * 应答包
	 * @author linchuan
	 * 
	 */
	private class DatagramAckPacket {
		private DatagramPacket packet; //应答包
		private long receiveTime; //接收时间 
		private String remoteIp; //发送人IP
		private char remotePort; //发送人端口
		
		/**
		 * @param packet
		 * @param receiveTime
		 * @param senderIp
		 * @param senderPort
		 */
		public DatagramAckPacket(DatagramPacket packet, long receiveTime, String remoteIp, char remotePort) {
			this.packet = packet;
			this.receiveTime = receiveTime;
			this.remoteIp = remoteIp;
			this.remotePort = remotePort;
		}
		/**
		 * @return Returns the packet.
		 */
		public DatagramPacket getPacket() {
			return packet;
		}
		/**
		 * @param packet The packet to set.
		 */
		public void setPacket(DatagramPacket packet) {
			this.packet = packet;
		}
		/**
		 * @return Returns the receiveTime.
		 */
		public long getReceiveTime() {
			return receiveTime;
		}
		/**
		 * @param receiveTime The receiveTime to set.
		 */
		public void setReceiveTime(long receiveTime) {
			this.receiveTime = receiveTime;
		}
		/**
		 * @return the remoteIp
		 */
		public String getRemoteIp() {
			return remoteIp;
		}
		/**
		 * @param remoteIp the remoteIp to set
		 */
		public void setRemoteIp(String remoteIp) {
			this.remoteIp = remoteIp;
		}
		/**
		 * @return the remotePort
		 */
		public char getRemotePort() {
			return remotePort;
		}
		/**
		 * @param remotePort the remotePort to set
		 */
		public void setRemotePort(char remotePort) {
			this.remotePort = remotePort;
		}
	}
	
	/**
	 * 数据源
	 * @author linchuan
	 * 
	 */
	private class DataSource {
		private InputStream in = null;
		private byte[] bytes = null;
		private int beginIndex;
		private int dataLength;
		
		public DataSource(InputStream in, int dataLength) {
			this.in = in;
			this.dataLength = dataLength;
			beginIndex = 0;
		}
		
		public DataSource(byte[] bytes, int beginIndex, int dataLength) {
			this.bytes = bytes;
			this.beginIndex = beginIndex;
			this.dataLength = dataLength;
		}
		
		/**
		 * 读取数据
		 * @return
		 */
		public byte[] readData(int readLen) throws NetworkException {
			readLen = Math.min(readLen, dataLength - beginIndex);
			byte[] buffer = new byte[readLen];
			if(in!=null) {
				try {
					in.read(buffer);
				} catch (IOException e) {
					e.printStackTrace();
					throw new NetworkException();
				}
			}
			else if(bytes!=null) {
				System.arraycopy(bytes, beginIndex, buffer, 0, readLen);
			}
			beginIndex += readLen;
			return buffer;
		}
		
		/**
		 * 判断是否还有数据可读
		 * @return
		 */
		public boolean hasMoreData() {
			return beginIndex<dataLength;
		}
		
		/**
		 * @return Returns the dataLength.
		 */
		public int getDataLength() {
			return dataLength;
		}
	}
	
	/**
	 * 任务:处理接收到的包
	 * @author linchuan
	 * 
	 */
	private class ReceiveTask implements Task {
		private DatagramServerChannel datagramChannel;
		private DatagramPacket packet;
		private String remoteIp;
		private char remotePort;
		
		public ReceiveTask(DatagramServerChannel datagramChannel, DatagramPacket packet, String remoteIp, char remotePort) {
			this.datagramChannel = datagramChannel;
			this.packet = packet;
			this.remoteIp = remoteIp;
			this.remotePort = remotePort;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void process() {
			try {
				datagramChannel.processReceivedPacket(packet, remoteIp, remotePort);
			}
			catch (NetworkException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 接收线程
	 * @author linchuan
	 *
	 */
	private class ReceiveThread extends Thread {

		public void run() {
			Logger.info("DatagramChannel: data receive thread startup.");
			while (!Thread.interrupted()) {
	    		try {
	    			if(receiveSelector.select()>0) { //5000
	    				if(Logger.isTraceEnabled()) {
	    					Logger.trace("DatagramChannel: check receive data.");
	    				}
	    				for(Iterator readyIterator = receiveSelector.selectedKeys().iterator(); readyIterator.hasNext();) {
	    					SelectionKey key = (SelectionKey)readyIterator.next();
	    					if(key.isReadable()) {
	    						doReceive();
	    					}
	    					readyIterator.remove();
	    				}
	    			}	
	    		}
	    		catch (Exception e) {
					Logger.exception(e);
				}
			}
		}
	}
	
	/**
	 * 发送线程
	 * @author linchuan
	 *
	 */
	private class SendThread extends Thread {

		public void run() {
			Logger.info("DatagramChannel: data send thread startup.");
			while (!Thread.interrupted()) {
				try {
					synchronized(outPacketQueue) { //等待新数据
	    				if(outPacketQueue.isEmpty()) {
	    					outPacketQueue.wait();
	    				}
	    			}
		    		if(sendSelector.select()>0) { //5000
	    				for(Iterator readyIterator = sendSelector.selectedKeys().iterator(); readyIterator.hasNext();) {
	    					SelectionKey key = (SelectionKey)readyIterator.next();
	    					if(key.isWritable()) {
	    						doSend();
	    					}
	    					readyIterator.remove();
	    				}
	    			}
	    		}
	    		catch (Exception e) {
					Logger.exception(e);
				}
			}
		}
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}