package com.yuanluesoft.im.webim.model;

import com.yuanluesoft.im.model.message.ChatDetail;

/**
 * 
 * @author linchuan
 *
 */
public class Webim {
	private int onlinePersonCount; //在线用户数
	private String userName; //用户名
	private String portraitURL; //用户头像
	private String status; //用户状态
	private int unreadSystemMessageCount; //未读系统消息数量
	private ChatDetail chat; //对话,配置对话按钮用
	
	/**
	 * @return the onlinePersonCount
	 */
	public int getOnlinePersonCount() {
		return onlinePersonCount;
	}
	/**
	 * @param onlinePersonCount the onlinePersonCount to set
	 */
	public void setOnlinePersonCount(int onlinePersonCount) {
		this.onlinePersonCount = onlinePersonCount;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the portraitURL
	 */
	public String getPortraitURL() {
		return portraitURL;
	}
	/**
	 * @param portraitURL the portraitURL to set
	 */
	public void setPortraitURL(String portraitURL) {
		this.portraitURL = portraitURL;
	}
	/**
	 * @return the chat
	 */
	public ChatDetail getChat() {
		return chat;
	}
	/**
	 * @param chat the chat to set
	 */
	public void setChat(ChatDetail chat) {
		this.chat = chat;
	}
	/**
	 * @return the unreadSystemMessageCount
	 */
	public int getUnreadSystemMessageCount() {
		return unreadSystemMessageCount;
	}
	/**
	 * @param unreadSystemMessageCount the unreadSystemMessageCount to set
	 */
	public void setUnreadSystemMessageCount(int unreadSystemMessageCount) {
		this.unreadSystemMessageCount = unreadSystemMessageCount;
	}
}