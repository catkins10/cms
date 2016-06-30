package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 文件校验,当文件交换使用拷贝方式且交换不可靠时,需要通过本命令校验文件传输是否完成
 * @author linchuan
 *
 */
public class FileValidate extends ExchangePacket {
	private String remoteFilePath; //文件路径
	private boolean validateDeleted; //检查是否删除
	private long fileLength; //文件大小

	public FileValidate(String remoteFilePath, boolean validateDeleted, long fileLength) {
		super();
		this.remoteFilePath = remoteFilePath;
		this.validateDeleted = validateDeleted;
		this.fileLength = fileLength;
	}

	/**
	 * @return the fileLength
	 */
	public long getFileLength() {
		return fileLength;
	}

	/**
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
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

	/**
	 * @return the validateDeleted
	 */
	public boolean isValidateDeleted() {
		return validateDeleted;
	}

	/**
	 * @param validateDeleted the validateDeleted to set
	 */
	public void setValidateDeleted(boolean validateDeleted) {
		this.validateDeleted = validateDeleted;
	}
}