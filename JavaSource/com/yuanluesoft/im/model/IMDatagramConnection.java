package com.yuanluesoft.im.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class IMDatagramConnection implements Serializable {
	private byte channelIndex; //使用的通道号
	private String ip; //IP
	private char port; //端口
	
	/**
	 * @return the channelIndex
	 */
	public byte getChannelIndex() {
		return channelIndex;
	}
	/**
	 * @param channelIndex the channelIndex to set
	 */
	public void setChannelIndex(byte channelIndex) {
		this.channelIndex = channelIndex;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public char getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(char port) {
		this.port = port;
	}
}