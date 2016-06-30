package com.yuanluesoft.im.model;

import com.yuanluesoft.jeaf.usermanage.util.PersonUtils;

/**
 * IM
 * @author linchuan
 *
 */
public class IM {
	private long userId; //用户ID
	private String userName; //用户名
	private String status; //用户状态
	
	/**
	 * 获取用户头像
	 * @return
	 */
	public String getPortraitURL() {
		return PersonUtils.getPortraitURL(userId);
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
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
}