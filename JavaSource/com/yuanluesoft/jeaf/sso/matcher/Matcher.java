package com.yuanluesoft.jeaf.sso.matcher;

/**
 * 
 * @author linchuan
 *
 */
public interface Matcher {

	/**
	 * 密码是否相同
	 * @param from 正确的密码
	 * @param toMatch 用户输入的密码
	 * @return
	 */
	public boolean matching(String from, String toMatch);
}