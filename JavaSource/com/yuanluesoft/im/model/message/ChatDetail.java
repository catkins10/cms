package com.yuanluesoft.im.model.message;

import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.usermanage.util.PersonUtils;

/**
 * IM对话
 * @author linchuan
 *
 */
public class ChatDetail extends Message {
	private long chatId; //对话ID
	private boolean isCustomerService; //是否客服对话
	private String chatPersonIds; //聊天用户ID列表,用逗号分隔,不包括自己
	private String chatPersonNames; //聊天用户姓名列表,用逗号分隔,不包括自己
	private byte chatPersonStatus; //对话用户的状态
	private int unreadTalkCount; //未读取的发言数量
	private long lastReadTime; //最后接收消息的时间
	
	public ChatDetail() {
		super();
		setCommand(CMD_CHAT_DETAIL);
	}

	/**
	 * 获取用户状态说明
	 * @return
	 */
	public String getChatPersonStatusText() {
		try {
			return IMService.IM_PERSON_STATUS_TEXTS[chatPersonStatus - IMService.IM_PERSON_STATUS_OFFLINE];
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取对话用户头像
	 * @return
	 */
	public String getChatPersonPortraitURL() {
		return PersonUtils.getPortraitURL(Long.parseLong(chatPersonIds.split(",")[0]));
	}
	
	/**
	 * @return the charId
	 */
	public long getChatId() {
		return chatId;
	}
	/**
	 * @param charId the charId to set
	 */
	public void setChatId(long charId) {
		this.chatId = charId;
	}
	/**
	 * @return the chatPersonStatus
	 */
	public byte getChatPersonStatus() {
		return chatPersonStatus;
	}
	/**
	 * @param chatPersonStatus the chatPersonStatus to set
	 */
	public void setChatPersonStatus(byte chatPersonStatus) {
		this.chatPersonStatus = chatPersonStatus;
	}

	/**
	 * @return the chatPersonIds
	 */
	public String getChatPersonIds() {
		return chatPersonIds;
	}

	/**
	 * @param chatPersonIds the chatPersonIds to set
	 */
	public void setChatPersonIds(String chatPersonIds) {
		this.chatPersonIds = chatPersonIds;
	}

	/**
	 * @return the chatPersonNames
	 */
	public String getChatPersonNames() {
		return chatPersonNames;
	}

	/**
	 * @param chatPersonNames the chatPersonNames to set
	 */
	public void setChatPersonNames(String chatPersonNames) {
		this.chatPersonNames = chatPersonNames;
	}

	/**
	 * @return the unreadTalkCount
	 */
	public int getUnreadTalkCount() {
		return unreadTalkCount;
	}

	/**
	 * @param unreadTalkCount the unreadTalkCount to set
	 */
	public void setUnreadTalkCount(int unreadTalkCount) {
		this.unreadTalkCount = unreadTalkCount;
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

	/**
	 * @return the isCustomerService
	 */
	public boolean isCustomerService() {
		return isCustomerService;
	}

	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setCustomerService(boolean isCustomerService) {
		this.isCustomerService = isCustomerService;
	}
}