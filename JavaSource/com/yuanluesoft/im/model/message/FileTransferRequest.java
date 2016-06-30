/*
 * Created on 2006-11-2
 *
 */
package com.yuanluesoft.im.model.message;

import java.io.Serializable;


/**
 * 请求发送文件
 * @author linchuan
 *
 */
public class FileTransferRequest extends Message implements Serializable {
	private long chatId; //对话ID
	private String senderServerIp; //发送人连接的服务器IP
	private char senderServerPort; //发送人连接的服务器端口
	private String senderInternetIp; //发送人公网IP
	private char senderInternetPort; //发送人公网端口
	private String senderIntranetIp; //发送人内网IP
	private char senderIntranetPort; //发送人内网端口
	private long fileId; //文件ID,由发送者生成,以避免应答时产生冲突
	public long fileSize; //文件长度
	public String fileName; //文件名
	
	public FileTransferRequest() {
		super();
		setCommand(CMD_FILE_TRANSFER_REQUEST);
	}
	/**
	 * @return the chatId
	 */
	public long getChatId() {
		return chatId;
	}
	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
	/**
	 * @return the fileId
	 */
	public long getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the senderInternetIp
	 */
	public String getSenderInternetIp() {
		return senderInternetIp;
	}
	/**
	 * @param senderInternetIp the senderInternetIp to set
	 */
	public void setSenderInternetIp(String senderInternetIp) {
		this.senderInternetIp = senderInternetIp;
	}
	/**
	 * @return the senderInternetPort
	 */
	public char getSenderInternetPort() {
		return senderInternetPort;
	}
	/**
	 * @param senderInternetPort the senderInternetPort to set
	 */
	public void setSenderInternetPort(char senderInternetPort) {
		this.senderInternetPort = senderInternetPort;
	}
	/**
	 * @return the senderIntranetIp
	 */
	public String getSenderIntranetIp() {
		return senderIntranetIp;
	}
	/**
	 * @param senderIntranetIp the senderIntranetIp to set
	 */
	public void setSenderIntranetIp(String senderIntranetIp) {
		this.senderIntranetIp = senderIntranetIp;
	}
	/**
	 * @return the senderServerIp
	 */
	public String getSenderServerIp() {
		return senderServerIp;
	}
	/**
	 * @param senderServerIp the senderServerIp to set
	 */
	public void setSenderServerIp(String senderServerIp) {
		this.senderServerIp = senderServerIp;
	}
	/**
	 * @return the senderServerPort
	 */
	public char getSenderServerPort() {
		return senderServerPort;
	}
	/**
	 * @param senderServerPort the senderServerPort to set
	 */
	public void setSenderServerPort(char senderServerPort) {
		this.senderServerPort = senderServerPort;
	}
	/**
	 * @return the senderIntranetPort
	 */
	public char getSenderIntranetPort() {
		return senderIntranetPort;
	}
	/**
	 * @param senderIntranetPort the senderIntranetPort to set
	 */
	public void setSenderIntranetPort(char senderIntranetPort) {
		this.senderIntranetPort = senderIntranetPort;
	}
}