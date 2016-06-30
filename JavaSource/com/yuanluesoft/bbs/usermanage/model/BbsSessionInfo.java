package com.yuanluesoft.bbs.usermanage.model;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class BbsSessionInfo extends SessionInfo {
	private boolean systemUser; //是否系统用户
	private String nickname; //昵称
	private char vip = '0';

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the vip
	 */
	public char getVip() {
		return vip;
	}

	/**
	 * @param vip the vip to set
	 */
	public void setVip(char vip) {
		this.vip = vip;
	}

	/**
	 * @return the systemUser
	 */
	public boolean isSystemUser() {
		return systemUser;
	}

	/**
	 * @param systemUser the systemUser to set
	 */
	public void setSystemUser(boolean systemUser) {
		this.systemUser = systemUser;
	}
}
