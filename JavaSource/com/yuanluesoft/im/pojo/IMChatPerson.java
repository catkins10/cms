package com.yuanluesoft.im.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;


/**
 * IM对话:参与者(im_chat_person)
 * @author linchuan
 *
 */
public class IMChatPerson extends Record {
	private long chatId; //对话ID
	private long personId; //用户ID
	private String personName; //用户名
	private Timestamp joinTime; //加入对话时间
	private long lastReadTime; //最后接收消息的时间,以毫秒为单位
	
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
	 * @return the joinTime
	 */
	public Timestamp getJoinTime() {
		return joinTime;
	}
	/**
	 * @param joinTime the joinTime to set
	 */
	public void setJoinTime(Timestamp joinTime) {
		this.joinTime = joinTime;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	/**
	 * @return the lastReadTime
	 */
	public long getLastReadTime() {
		return lastReadTime;
	}
	/**
	 * @param lastReadTime the lastReadTime to set
	 */
	public void setLastReadTime(long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}
}