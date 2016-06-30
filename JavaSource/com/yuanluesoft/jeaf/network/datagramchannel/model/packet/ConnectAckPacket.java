/*
 * Created on 2006-10-24
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet;

/**
 * 
 * @author linchuan
 *
 */
public class ConnectAckPacket extends DatagramPacket {
	private String connecterIp; //连接者IP
	private char connecterPort = 0; //连接者端口

	public ConnectAckPacket() {
		setCommand(CMD_CONNECT_ACK);
	}
	/**
	 * @return the connecterIp
	 */
	public String getConnecterIp() {
		return connecterIp;
	}
	/**
	 * @param connecterIp the connecterIp to set
	 */
	public void setConnecterIp(String connecterIp) {
		this.connecterIp = connecterIp;
	}
	/**
	 * @return the connecterPort
	 */
	public char getConnecterPort() {
		return connecterPort;
	}
	/**
	 * @param connecterPort the connecterPort to set
	 */
	public void setConnecterPort(char connecterPort) {
		this.connecterPort = connecterPort;
	}
}