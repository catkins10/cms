package com.yuanluesoft.cms.interview.forms;

import com.yuanluesoft.jeaf.sso.forms.LoginForm;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewLiveLogin extends LoginForm {
	private String userType = "guest"; //用户类型
	private String guest; //嘉宾选择

	/**
	 * @return the guest
	 */
	public String getGuest() {
		return guest;
	}

	/**
	 * @param guest the guest to set
	 */
	public void setGuest(String guest) {
		this.guest = guest;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
}