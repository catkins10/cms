package com.yuanluesoft.jeaf.usermanage.security.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 用户安全(user_security)
 * @author linchuan
 *
 */
public class UserSecurity extends Record {
	private long userId; //用户ID
	private int passwordWrong; //密码输错次数
	private Timestamp lastSetPassword; //密码修改时间,上次设置密码的时间
	private String resetCode; //重置验证码
	private Timestamp resetCodeCreated; //重置验证码生成时间
	private char halt = '0'; //是否停用
	
	/**
	 * @return the lastSetPassword
	 */
	public Timestamp getLastSetPassword() {
		return lastSetPassword;
	}
	/**
	 * @param lastSetPassword the lastSetPassword to set
	 */
	public void setLastSetPassword(Timestamp lastSetPassword) {
		this.lastSetPassword = lastSetPassword;
	}
	/**
	 * @return the passwordWrong
	 */
	public int getPasswordWrong() {
		return passwordWrong;
	}
	/**
	 * @param passwordWrong the passwordWrong to set
	 */
	public void setPasswordWrong(int passwordWrong) {
		this.passwordWrong = passwordWrong;
	}
	/**
	 * @return the resetCode
	 */
	public String getResetCode() {
		return resetCode;
	}
	/**
	 * @param resetCode the resetCode to set
	 */
	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}
	/**
	 * @return the resetCodeCreated
	 */
	public Timestamp getResetCodeCreated() {
		return resetCodeCreated;
	}
	/**
	 * @param resetCodeCreated the resetCodeCreated to set
	 */
	public void setResetCodeCreated(Timestamp resetCodeCreated) {
		this.resetCodeCreated = resetCodeCreated;
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
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}
	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
	}
}