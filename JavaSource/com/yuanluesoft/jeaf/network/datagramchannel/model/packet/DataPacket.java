/*
 * Created on 2006-10-19
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.model.packet;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class DataPacket extends DatagramPacket implements Serializable {
	public final static int MAX_DATA_LENGTH = DatagramPacket.MAX_PACKET_LENGTH - 5; //数据包的最大长度,5个字节中,DatagramPacket 1字节,DataPacket 4字节
	public final static int MAX_DATA_TURN_LENGTH = DatagramPacket.MAX_PACKET_LENGTH - 5 -  39 - 2; //转发数据包的最大长度,39字节为IPV6的最大长度,2为端口
	
	private char sequence = 0; //顺序号
	private byte windowSize; //窗口大小,接收端需要根据发送方调整窗口大小,最大12
    private boolean firstPacket; //是否第一个数据包
    private boolean lastPacket; //是否最后一个数据包
	private boolean ack; //是否需要应答
	private boolean isResponse; //是否应答的数据
	private byte validateCode; //校验码
	private byte[] data; //数据
	
	public DataPacket() {
		setCommand(CMD_DATA);
	}

	/**
	 * @return the ack
	 */
	public boolean isAck() {
		return ack;
	}

	/**
	 * @param ack the ack to set
	 */
	public void setAck(boolean ack) {
		this.ack = ack;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the firstPacket
	 */
	public boolean isFirstPacket() {
		return firstPacket;
	}

	/**
	 * @param firstPacket the firstPacket to set
	 */
	public void setFirstPacket(boolean firstPacket) {
		this.firstPacket = firstPacket;
	}

	/**
	 * @return the isResponse
	 */
	public boolean isResponse() {
		return isResponse;
	}

	/**
	 * @param isResponse the isResponse to set
	 */
	public void setResponse(boolean isResponse) {
		this.isResponse = isResponse;
	}

	/**
	 * @return the lastPacket
	 */
	public boolean isLastPacket() {
		return lastPacket;
	}

	/**
	 * @param lastPacket the lastPacket to set
	 */
	public void setLastPacket(boolean lastPacket) {
		this.lastPacket = lastPacket;
	}

	/**
	 * @return the sequence
	 */
	public char getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(char sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the windowSize
	 */
	public byte getWindowSize() {
		return windowSize;
	}

	/**
	 * @param windowSize the windowSize to set
	 */
	public void setWindowSize(byte windowSize) {
		this.windowSize = windowSize;
	}

	/**
	 * @return the valdateCode
	 */
	public byte getValidateCode() {
		return validateCode;
	}

	/**
	 * @param valdateCode the valdateCode to set
	 */
	public void setValidateCode(byte valdateCode) {
		this.validateCode = valdateCode;
	}
}