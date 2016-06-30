package com.yuanluesoft.jeaf.sso.matcher;

/**
 * 
 * @author linchuan
 *
 */
public class PlainPasswordMatcher implements Matcher {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sso.service.passwordcomparator.PasswordMatcher#matching(java.lang.String, java.lang.String)
	 */
	public boolean matching(String fromPassword, String toPassword) {
		return toPassword.equals(fromPassword);
	}
}