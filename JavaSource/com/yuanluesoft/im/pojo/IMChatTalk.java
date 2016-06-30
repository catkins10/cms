package com.yuanluesoft.im.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * IM对话:发言(im_chat_talk)
 * @author linchuan
 *
 */
public class IMChatTalk extends Record {
	private long chatId; //对话ID
	private String content; //发言内容
	private long createdMillis; //发言时间
	private long creatorId; //发言人ID
	private String creatorName; //发言人姓名
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return new Timestamp(createdMillis);
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the creatorName
	 */
	public String getCreatorName() {
		return creatorName;
	}
	/**
	 * @param creatorName the creatorName to set
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the createdMillis
	 */
	public long getCreatedMillis() {
		return createdMillis;
	}
	/**
	 * @param createdMillis the createdMillis to set
	 */
	public void setCreatedMillis(long createdMillis) {
		this.createdMillis = createdMillis;
	}
}