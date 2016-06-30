package com.yuanluesoft.jeaf.network.datagramchannel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class DatagramConnection implements Serializable {
	private String remoteIp; //远程IP
	private char remotePort = 0; //远程端口
	
	private String turnIp; //转发IP,如果客户端之间不能建立P2P连接,由服务器负责转发
	private char turnPort = 0; //转发端口
	
	private String secureKey = null; //安全密钥,TODO
	
	private Byte sendMutex = new Byte((byte)0); //发送MUTEX
	private char sendSequence = 0; //发送窗口起始顺序号
    
	private List receiveWindow = new ArrayList();	//接收窗口
	private char receiveSequence = 0;	//接收窗口起始顺序号
	private boolean isDataUnread; //是否有数据需要被读取
	
	private byte maxSendWindowSize = 12; //最大发送窗口大小,默认为12,在重发次数过多时窗口大小减半
    private char timeout = 100; //重发延时时间,发送一个包时的延时时间,如果发送n个包,则延时时间乘以n,以毫秒为单位,在重发次数过多时延时加倍
    
	/**
	 * @return the isDataUnread
	 */
	public boolean isDataUnread() {
		return isDataUnread;
	}
	/**
	 * @param isDataUnread the isDataUnread to set
	 */
	public void setDataUnread(boolean isDataUnread) {
		this.isDataUnread = isDataUnread;
	}
	/**
	 * @return the maxSendWindowSize
	 */
	public byte getMaxSendWindowSize() {
		return maxSendWindowSize;
	}
	/**
	 * @param maxSendWindowSize the maxSendWindowSize to set
	 */
	public void setMaxSendWindowSize(byte maxSendWindowSize) {
		this.maxSendWindowSize = maxSendWindowSize;
	}
	/**
	 * @return the receiveSequence
	 */
	public char getReceiveSequence() {
		return receiveSequence;
	}
	/**
	 * @param receiveSequence the receiveSequence to set
	 */
	public void setReceiveSequence(char receiveSequence) {
		this.receiveSequence = receiveSequence;
	}
	/**
	 * @return the receiveWindow
	 */
	public List getReceiveWindow() {
		return receiveWindow;
	}
	/**
	 * @param receiveWindow the receiveWindow to set
	 */
	public void setReceiveWindow(List receiveWindow) {
		this.receiveWindow = receiveWindow;
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
	/**
	 * @return the secureKey
	 */
	public String getSecureKey() {
		return secureKey;
	}
	/**
	 * @param secureKey the secureKey to set
	 */
	public void setSecureKey(String secureKey) {
		this.secureKey = secureKey;
	}
	/**
	 * @return the sendMutex
	 */
	public Byte getSendMutex() {
		return sendMutex;
	}
	/**
	 * @param sendMutex the sendMutex to set
	 */
	public void setSendMutex(Byte sendMutex) {
		this.sendMutex = sendMutex;
	}
	/**
	 * @return the timeout
	 */
	public char getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(char timeout) {
		this.timeout = timeout;
	}
	/**
	 * @return the turnIp
	 */
	public String getTurnIp() {
		return turnIp;
	}
	/**
	 * @param turnIp the turnIp to set
	 */
	public void setTurnIp(String turnIp) {
		this.turnIp = turnIp;
	}
	/**
	 * @return the turnPort
	 */
	public char getTurnPort() {
		return turnPort;
	}
	/**
	 * @param turnPort the turnPort to set
	 */
	public void setTurnPort(char turnPort) {
		this.turnPort = turnPort;
	}
	/**
	 * @return the sendSequence
	 */
	public char getSendSequence() {
		return sendSequence;
	}
	/**
	 * @param sendSequence the sendSequence to set
	 */
	public void setSendSequence(char sendSequence) {
		this.sendSequence = sendSequence;
	}
}