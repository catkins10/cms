/*
 * Created on 2006-10-19
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet;


/**
 * 
 * @author linchuan
 *
 */
public class DataAckPacket extends DatagramPacket {
	private char nextReceiveSequence = '0'; //下一次要接收的包顺序号
	private boolean refused; //是否拒绝
	
	public DataAckPacket() {
		setCommand(CMD_DATA_ACK);
	}
	
	/**
	 * @return Returns the nextReceiveSequence.
	 */
	public char getNextReceiveSequence() {
		return nextReceiveSequence;
	}
	/**
	 * @param nextReceiveSequence The nextReceiveSequence to set.
	 */
	public void setNextReceiveSequence(char nextReceiveSequence) {
		this.nextReceiveSequence = nextReceiveSequence;
	}

	public boolean isRefused() {
		return refused;
	}

	public void setRefused(boolean refused) {
		this.refused = refused;
	}
}