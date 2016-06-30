package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;


/**
 * 发送文件命令
 * @author linchuan
 *
 */
public class FileSend extends ExchangePacket {
	private String remoteFilePath; //文件存放路径
	private long fileLength; //文件长度
	private boolean createDirectoryIfNotExists; //如果对应的目录不存在,是否自动创建
	private boolean saveToTemporaryDirectory; //是否保存到临时目录
	
	public FileSend(String remoteFilePath, long fileLength, boolean createDirectoryIfNotExists, boolean saveToTemporaryDirectory) {
		super();
		this.remoteFilePath = remoteFilePath;
		this.fileLength = fileLength;
		this.createDirectoryIfNotExists = createDirectoryIfNotExists;
		this.saveToTemporaryDirectory = saveToTemporaryDirectory;
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
	/**
	 * @return the saveToTemporaryDirectory
	 */
	public boolean isSaveToTemporaryDirectory() {
		return saveToTemporaryDirectory;
	}
	/**
	 * @param saveToTemporaryDirectory the saveToTemporaryDirectory to set
	 */
	public void setSaveToTemporaryDirectory(boolean saveToTemporaryDirectory) {
		this.saveToTemporaryDirectory = saveToTemporaryDirectory;
	}
	/**
	 * @return the createDirectoryIfNotExists
	 */
	public boolean isCreateDirectoryIfNotExists() {
		return createDirectoryIfNotExists;
	}
	/**
	 * @param createDirectoryIfNotExists the createDirectoryIfNotExists to set
	 */
	public void setCreateDirectoryIfNotExists(boolean createDirectoryIfNotExists) {
		this.createDirectoryIfNotExists = createDirectoryIfNotExists;
	}
}