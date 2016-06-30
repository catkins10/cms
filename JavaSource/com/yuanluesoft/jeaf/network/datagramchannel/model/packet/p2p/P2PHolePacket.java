/*
 * Created on 2006-11-28
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p;

import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DatagramPacket;

/**
 * p2p打洞包
 * @author LinChuan
 *
 */
public class P2PHolePacket extends DatagramPacket {
	
	public P2PHolePacket() {
		setCommand(CMD_P2P_HOLE);
	}
}
