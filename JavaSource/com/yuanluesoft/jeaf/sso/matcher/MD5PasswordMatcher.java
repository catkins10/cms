package com.yuanluesoft.jeaf.sso.matcher;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.util.Encoder;

/**
 * 
 * @author linchuan
 *
 */
public class MD5PasswordMatcher implements Matcher {
	private HttpServletRequest request;

	public MD5PasswordMatcher(HttpServletRequest request) {
		super();
		this.request = request;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sso.service.passwordcomparator.PasswordMatcher#matching(java.lang.String, java.lang.String)
	 */
	public boolean matching(String fromPassword, String toPassword) {
		try {
			return toPassword.equalsIgnoreCase(Encoder.getInstance().md5Encode(fromPassword + request.getSession().getId()));
		}
		catch (Exception e) {
			return false;
		}
	}
}