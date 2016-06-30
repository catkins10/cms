package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:发报密级(telex_telegram_security_level)
 * @author linchuan
 *
 */
public class TelegramSecurityLevel extends Record {
	private String securityLevel; //密级

	/**
	 * @return the securityLevel
	 */
	public String getSecurityLevel() {
		return securityLevel;
	}
	/**
	 * @param securityLevel the securityLevel to set
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
}
