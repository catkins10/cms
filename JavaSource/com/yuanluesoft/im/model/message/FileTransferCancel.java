package com.yuanluesoft.im.model.message;

/**
 * 放弃文件传输
 * @author linchuan
 *
 */
public class FileTransferCancel extends Message {
	private long chatId; //对话
	private long operatorId; //放弃传输的用户ID
	private String operatorName; //放弃传输的用户姓名
	private long fileId; //文件ID,由发送者生成,以避免应答时产生冲突
	
	public FileTransferCancel() {
		super();
		setCommand(CMD_FILE_TRANSFER_CANCEL);
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
	 * @return the operatorId
	 */
	public long getOperatorId() {
		return operatorId;
	}
	/**
	 * @param operatorId the operatorId to set
	 */
	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}
	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}