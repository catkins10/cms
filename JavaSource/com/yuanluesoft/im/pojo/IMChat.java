package com.yuanluesoft.im.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * IM对话(im_chat)
 * @author linchuan
 *
 */
public class IMChat extends Record {
	private int isGroupChat; //是否讨论组
	private int isCustomerService; //是否客服对话
	private int isEnd; //是否结束,给客服使用
	private long siteId; //站点ID,给客服使用
	private Timestamp created; //创建时间
	private long lastReadTime; //最后读取消息的时间,非数据库字段,动态获取
	private Set chatPersons; //对话用户
	
	/**
	 * 获取客服人员姓名
	 * @return
	 */
	public String getSpecialistName() {
		return ((IMChatPerson)chatPersons.iterator().next()).getPersonName();
	}
	
	/**
	 * @return the chatPersons
	 */
	public Set getChatPersons() {
		return chatPersons;
	}
	/**
	 * @param chatPersons the chatPersons to set
	 */
	public void setChatPersons(Set chatPersons) {
		this.chatPersons = chatPersons;
	}
	/**
	 * @return the isGroupChat
	 */
	public int getIsGroupChat() {
		return isGroupChat;
	}
	/**
	 * @param isGroupChat the isGroupChat to set
	 */
	public void setIsGroupChat(int isGroupChat) {
		this.isGroupChat = isGroupChat;
	}
	/**
	 * @return the isCustomerService
	 */
	public int getIsCustomerService() {
		return isCustomerService;
	}
	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setIsCustomerService(int isCustomerService) {
		this.isCustomerService = isCustomerService;
	}
	/**
	 * @return the isEnd
	 */
	public int getIsEnd() {
		return isEnd;
	}
	/**
	 * @param isEnd the isEnd to set
	 */
	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
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