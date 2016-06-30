package com.yuanluesoft.portal.container.internal;

import javax.portlet.PortletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * @author linchuan
 *
 */
public class PortletResponseImpl extends HttpServletResponseWrapper implements PortletResponse { 

	public PortletResponseImpl(HttpServletResponse response) {
		super(response);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletResponse#addProperty(java.lang.String, java.lang.String)
	 */
	public void addProperty(String name, String value) {
		((HttpServletResponse)super.getResponse()).addCookie(new Cookie(name, value));
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletResponse#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String name, String value) {
		addProperty(name, value);
	}
}