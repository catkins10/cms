package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;


/**
 * 删除文件命令
 * @author linchuan
 *
 */
public class FileDelete extends ExchangePacket {
	private String remoteFilePath; //文件路径

	public FileDelete(String remoteFilePath) {
		super();
		this.remoteFilePath = remoteFilePath;
	}

	/**
	 * @return the remoteFilePath
	 */
	public String getRemoteFilePath() {
		return remoteFilePath;
	}

	/**
	 * @param remoteFilePath the remoteFilePath to set
	 */
	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}
}