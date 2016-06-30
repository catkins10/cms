/*
 * Created on 2006-11-2
 *
 */
package com.yuanluesoft.im.model.message;

/**
 * 
 * @author linchuan
 *
 */
public class FileTransferAck extends Message {
	private long chatId; //对话ID
	private long fileId; //文件ID,由发送者生成,以避免应答时产生冲突
	private byte accept; //是否接收: 0/不接收, 1/接收, 2/离线接收(服务器转发)
	private long fileSeek; //从文件指定位置开始传输
	
	public FileTransferAck() {
		super();
		setCommand(CMD_FILE_TRANSFER_ACK);
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
	 * @return the fileSeek
	 */
	public long getFileSeek() {
		return fileSeek;
	}
	/**
	 * @param fileSeek the fileSeek to set
	 */
	public void setFileSeek(long fileSeek) {
		this.fileSeek = fileSeek;
	}
	/**
	 * @return the accept
	 */
	public byte getAccept() {
		return accept;
	}
	/**
	 * @param accept the accept to set
	 */
	public void setAccept(byte accept) {
		this.accept = accept;
	}
}