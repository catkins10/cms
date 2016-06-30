/*
 * Created on 2005-11-27
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.messenger.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class MessengerOnline extends Record {
	private long personId;
	private java.lang.String ip;
	private int port;
	
	/**
	 * @return Returns the personId.
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return Returns the ip.
	 */
	public java.lang.String getIp() {
		return ip;
	}
	/**
	 * @param ip The ip to set.
	 */
	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
