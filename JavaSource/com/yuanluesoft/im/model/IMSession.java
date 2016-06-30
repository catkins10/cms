package com.yuanluesoft.im.model;

import java.io.Serializable;

/**
 * IM会话
 * @author linchuan
 *
 */
public class IMSession implements Serializable {
	private byte status;
	private char serverIndex;
	
	/**
	 * @return the serverIndex
	 */
	public char getServerIndex() {
		return serverIndex;
	}
	/**
	 * @param serverIndex the serverIndex to set
	 */
	public void setServerIndex(char serverIndex) {
		this.serverIndex = serverIndex;
	}
	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
}