/**
 * 
 */
package com.yuanluesoft.jeaf.usermanage.member.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 用户管理:网上用户注册设置(user_member_regist_setting)
 * @author linchuan
 *
 */
public class MemberRegistSetting extends Record {
	private char registDisabled = '0'; //是否禁止注册
	private String registDisabledReason; //禁止注册的原因
	
	/**
	 * @return the registDisabled
	 */
	public char getRegistDisabled() {
		return registDisabled;
	}
	/**
	 * @param registDisabled the registDisabled to set
	 */
	public void setRegistDisabled(char registDisabled) {
		this.registDisabled = registDisabled;
	}
	/**
	 * @return the registDisabledReason
	 */
	public String getRegistDisabledReason() {
		return registDisabledReason;
	}
	/**
	 * @param registDisabledReason the registDisabledReason to set
	 */
	public void setRegistDisabledReason(String registDisabledReason) {
		this.registDisabledReason = registDisabledReason;
	}
}
