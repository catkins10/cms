package com.yuanluesoft.im.model;

/**
 * IM客户端登录
 * @author linchuan
 *
 */
public class IMClientLogin {
	private String ticket; //登录钥匙
	private String assignUdpChannelIP; //分配的UDP通道IP地址
	private char assignUdpChannelPort; //分配的UDP通道端口
	
	/**
	 * @return the assignUdpChannelIP
	 */
	public String getAssignUdpChannelIP() {
		return assignUdpChannelIP;
	}
	/**
	 * @param assignUdpChannelIP the assignUdpChannelIP to set
	 */
	public void setAssignUdpChannelIP(String assignUdpChannelIP) {
		this.assignUdpChannelIP = assignUdpChannelIP;
	}
	/**
	 * @return the assignUdpChannelPort
	 */
	public char getAssignUdpChannelPort() {
		return assignUdpChannelPort;
	}
	/**
	 * @param assignUdpChannelPort the assignUdpChannelPort to set
	 */
	public void setAssignUdpChannelPort(char assignUdpChannelPort) {
		this.assignUdpChannelPort = assignUdpChannelPort;
	}
	/**
	 * @return the ticket
	 */
	public String getTicket() {
		return ticket;
	}
	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}