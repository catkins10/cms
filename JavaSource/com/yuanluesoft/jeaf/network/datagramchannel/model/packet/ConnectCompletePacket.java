/*
 * Created on 2006-10-30
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet;

/**
 * 
 * @author linchuan
 *
 */
public class ConnectCompletePacket extends DatagramPacket {

	public ConnectCompletePacket() {
		setCommand(CMD_CONNECT_COMPLETE);
	}
}