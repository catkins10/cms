/*
 * Created on 2006-10-20
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet;

/**
 * 
 * @author linchuan
 *
 */
public class ConnectPacket extends DatagramPacket {
	
	public ConnectPacket() {
		setCommand(CMD_CONNECT);
	}
}