package com.yuanluesoft.im.model.message;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.usermanage.util.PersonUtils;

/**
 * 发言详细内容
 * @author linchuan
 *
 */
public class TalkDetail extends Message {
	private long chatId; //会话ID
	private String content; //内容
	private long creatorId; //发言人ID
	private String creator; //发言人
	private long createdMillis; //发言时间
	
	public TalkDetail() {
		super();
		setCommand(CMD_TALK_DETAIL);
	}
	
	public TalkDetail(long chatId, String content, long creatorId, String creator, long createdMillis) {
		super();
		setCommand(CMD_TALK_DETAIL);
		this.chatId = chatId;
		this.content = content;
		this.creatorId = creatorId;
		this.creator = creator;
		this.createdMillis = createdMillis;
	}
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return new Timestamp(createdMillis);
	}

	/**
	 * 获取对话用户头像
	 * @return
	 */
	public String getTalkPersonPortraitURL() {
		return PersonUtils.getPortraitURL(creatorId);
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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