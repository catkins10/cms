package com.yuanluesoft.cms.interview.model;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 访谈会话信息
 * @author linchuan
 *
 */
public class InterviewSessionInfo extends SessionInfo {
	private boolean guests; //是否嘉宾

	/**
	 * @return the guests
	 */
	public boolean isGuests() {
		return guests;
	}

	/**
	 * @param guests the guests to set
	 */
	public void setGuests(boolean guests) {
		this.guests = guests;
	}
}
