package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;


/**
 * 发送文件命令
 * @author linchuan
 *
 */
public class FileData extends ExchangePacket {
	private byte[] data; //数据
	private boolean isLast; //是否最后的数据块
	
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
	 * @return the isLast
	 */
	public boolean isLast() {
		return isLast;
	}
	/**
	 * @param isLast the isLast to set
	 */
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
}