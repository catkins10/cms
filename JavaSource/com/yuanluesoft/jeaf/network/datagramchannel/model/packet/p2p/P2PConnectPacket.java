/*
 * Created on 2006-11-28
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p;

import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DatagramPacket;

/**
 * p2p连接请求包
 * @author LinChuan
 *
 */
public class P2PConnectPacket extends DatagramPacket {
	private String peerIp; //对方IP
	private char peerPort = 0; //对方端口

	public P2PConnectPacket(boolean forwardMode) {
		setCommand(forwardMode ? CMD_P2P_FORWARD_CONNECT_ACK : CMD_P2P_CONNECT_ACK);
	}
	
	/**
	 * @return Returns the peerIp.
	 */
	public String getPeerIp() {
		return peerIp;
	}
	/**
	 * @param peerIp The peerIp to set.
	 */
	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}
	/**
	 * @return Returns the peerPort.
	 */
	public char getPeerPort() {
		return peerPort;
	}
	/**
	 * @param peerPort The peerPort to set.
	 */
	public void setPeerPort(char peerPort) {
		this.peerPort = peerPort;
	}
}