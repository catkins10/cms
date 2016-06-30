package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;


/**
 * 发送文件命令
 * @author linchuan
 *
 */
public class FileSendAck extends ExchangePacket {
	private String remoteFilePath; //文件存放路径
	
	public FileSendAck(String remoteFilePath) {
		super();
		this.remoteFilePath = remoteFilePath;
	}
	/**
	 * @return the filePath
	 */
	public String getRemoteFilePath() {
		return remoteFilePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setRemoteFilePath(String filePath) {
		this.remoteFilePath = filePath;
	}
}