package com.yuanluesoft.portal.container.internal;

import java.util.Enumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author linchuan
 *
 */
public class PortletSessionImpl implements PortletSession {
    private HttpSession httpSession;
    private PortletContext portletContext;
    
	public PortletSessionImpl(HttpSession httpSession, PortletContext portletContext) {
		super();
		this.httpSession = httpSession;
		this.portletContext = portletContext;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getAttribute(java.lang.String, int)
	 */
	public Object getAttribute(String name, int scope) {
		return httpSession.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return getAttribute(name, PortletSession.PORTLET_SCOPE);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		return getAttributeNames(PortletSession.PORTLET_SCOPE);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getAttributeNames(int)
	 */
	public Enumeration getAttributeNames(int scope) {
		return httpSession.getAttributeNames();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#removeAttribute(java.lang.String, int)
	 */
	public void removeAttribute(String name, int scope) {
		httpSession.removeAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		removeAttribute(name, PortletSession.PORTLET_SCOPE);
	}
	
	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#setAttribute(java.lang.String, java.lang.Object, int)
	 */
	public void setAttribute(String name, Object value, int scope) {
		httpSession.setAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		setAttribute(name, value, PortletSession.PORTLET_SCOPE);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getCreationTime()
	 */
	public long getCreationTime() {
		return httpSession.getCreationTime();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getId()
	 */
	public String getId() {
		return httpSession.getId();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		return httpSession.getLastAccessedTime();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		return httpSession.getMaxInactiveInterval();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#getPortletContext()
	 */
	public PortletContext getPortletContext() {
		return portletContext;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#invalidate()
	 */
	public void invalidate() {
		httpSession.invalidate();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#isNew()
	 */
	public boolean isNew() {
		return httpSession.isNew();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int interval) {
		httpSession.setMaxInactiveInterval(interval);
	}
}