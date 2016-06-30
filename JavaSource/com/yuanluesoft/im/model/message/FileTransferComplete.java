package com.yuanluesoft.im.model.message;

/**
 * 文件传输完成
 * @author linchuan
 *
 */
public class FileTransferComplete extends Message {
	private long chatId; //对话
	private long fileId; //文件ID,由发送者生成,以避免应答时产生冲突
	private String offlineFileName; //离线文件名称
	
	public FileTransferComplete() {
		super();
		setCommand(CMD_FILE_TRANSFER_COMPLETE);
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
	 * @return the offlineFileName
	 */
	public String getOfflineFileName() {
		return offlineFileName;
	}
	/**
	 * @param offlineFileName the offlineFileName to set
	 */
	public void setOfflineFileName(String offlineFileName) {
		this.offlineFileName = offlineFileName;
	}
}