package com.yuanluesoft.im.model.message;



/**
 * 在线用户数变更通知
 * @author linchuan
 *
 */
public class OnlineCountChanged extends Message {
	private int onlineCount; //在线用户数

	public OnlineCountChanged(int onlineCount) {
		super();
		this.onlineCount = onlineCount;
	}

	/**
	 * @return the onlineCount
	 */
	public int getOnlineCount() {
		return onlineCount;
	}

	/**
	 * @param onlineCount the onlineCount to set
	 */
	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}
}