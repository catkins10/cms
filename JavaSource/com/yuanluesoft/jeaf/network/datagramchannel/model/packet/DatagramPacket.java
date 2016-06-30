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
public class DatagramPacket {
	public final static int MAX_PACKET_LENGTH = 528; //数据包的最大长度, Internet上的标准MTU值为576字节(EtherNet:1500, PPPoE/ADSL:1492, Dial Up/Modem:576),UDP的数据长度控件在548字节(576-8-20)以内

	//包命令
	public static final byte CMD_CONNECT = 1; //初始化连接包
	public static final byte CMD_CONNECT_ACK = 2; //连接应答包
	public static final byte CMD_CONNECT_COMPLETE = 3; //连接确认包
	public static final byte CMD_DATA = 10; //数据包
	public static final byte CMD_DATA_ACK = 11; //应答包
	//点对点连接相关包命令
	public static final byte CMD_P2P_CONNECT = 20; //p2p连接请求
	public static final byte CMD_P2P_CONNECT_ACK = 21; //p2p连接请求的应答
	public static final byte CMD_P2P_HOLE = 22; //p2p打洞
	public static final byte CMD_P2P_FORWARD_CONNECT = 30; //p2p(转发方式)连接请求
	public static final byte CMD_P2P_FORWARD_CONNECT_ACK = 31; //p2p(转发方式)连接请求的应答
	
	private byte command; //包类型
	
	private String turnIp; //转发IP
	private char turnPort = 0; //转发端口
	private String remoteIp; //接收人IP
	private char remotePort = 0; //接收人端口

	/**
	 * @return Returns the command.
	 */
	public byte getCommand() {
		return command;
	}
	/**
	 * @param command The command to set.
	 */
	public void setCommand(byte command) {
		this.command = command;
	}
	/**
	 * @return Returns the routeAddress.
	 */
	public String getTurnIp() {
		return turnIp;
	}
	/**
	 * @param routeAddress The routeAddress to set.
	 */
	public void setTurnIp(String turnIp) {
		this.turnIp = turnIp;
	}
	/**
	 * @return Returns the routePort.
	 */
	public char getTurnPort() {
		return turnPort;
	}
	/**
	 * @return Returns the remoteIp.
	 */
	public String getRemoteIp() {
		return remoteIp;
	}
	/**
	 * @param remoteIp The remoteIp to set.
	 */
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	/**
	 * @return Returns the remotePort.
	 */
	public char getRemotePort() {
		return remotePort;
	}
	/**
	 * @param remotePort The remotePort to set.
	 */
	public void setRemotePort(char remotePort) {
		this.remotePort = remotePort;
	}
	/**
	 * @param routePort The routePort to set.
	 */
	public void setTurnPort(char routePort) {
		this.turnPort = routePort;
	}
}