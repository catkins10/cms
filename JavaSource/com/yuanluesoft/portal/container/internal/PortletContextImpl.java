package com.yuanluesoft.portal.container.internal;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class PortletContextImpl implements javax.portlet.PortletContext {
    private ServletContext servletContext;

	public PortletContextImpl(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getServerInfo()
	 */
	public String getServerInfo() {
		return PortalContextImpl.PORTAL_INFO;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getRequestDispatcher(java.lang.String)
	 */
	public PortletRequestDispatcher getRequestDispatcher(String path) {
        if (path == null || !path.startsWith("/")) { //不能为空,且必须以“/”开始
            if(Logger.isDebugEnabled()) {
            	Logger.debug("Failed to retrieve PortletRequestDispatcher: path name must begin with a slash '/'.");
            }
            return null;
        }
        // Construct PortletRequestDispatcher.
        PortletRequestDispatcher portletRequestDispatcher = null;
        try {
            RequestDispatcher servletRequestDispatcher = servletContext.getRequestDispatcher(path);
            if(servletRequestDispatcher != null) {
                portletRequestDispatcher = new PortletRequestDispatcherImpl(servletRequestDispatcher);
            }
        }
        catch (Exception ex) {
            Logger.exception(ex);
        }
        return portletRequestDispatcher;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getNamedDispatcher(java.lang.String)
	 */
	public PortletRequestDispatcher getNamedDispatcher(String name) {
		RequestDispatcher dispatcher = servletContext.getNamedDispatcher(name);
        if(dispatcher != null) {
            return new PortletRequestDispatcherImpl(dispatcher);
        }
        return null;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream(String path) {
		return servletContext.getResourceAsStream(path);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }
        return servletContext.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		if (name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }
        servletContext.removeAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object object) {
		if(name == null) {
            throw new IllegalArgumentException("Attribute name == null");
        }
        servletContext.setAttribute(name, object);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		return servletContext.getAttributeNames();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String name) {
		if (name == null) {
            throw new IllegalArgumentException("Parameter name == null");
        }
        return servletContext.getInitParameter(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames() {
		return servletContext.getInitParameterNames();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getMajorVersion()
	 */
	public int getMajorVersion() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getMinorVersion()
	 */
	public int getMinorVersion() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getMimeType(java.lang.String)
	 */
	public String getMimeType(String file) {
		 return servletContext.getMimeType(file);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getPortletContextName()
	 */
	public String getPortletContextName() {
		return servletContext.getServletContextName();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getRealPath(java.lang.String)
	 */
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getResource(java.lang.String)
	 */
	public URL getResource(String path) throws MalformedURLException {
		if(path == null || !path.startsWith("/")) {
            throw new MalformedURLException("path must start with a '/'");
        }
        return servletContext.getResource(path);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#getResourcePaths(java.lang.String)
	 */
	public Set getResourcePaths(String path) {
		return servletContext.getResourcePaths(path);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#log(java.lang.String, java.lang.Throwable)
	 */
	public void log(String message, Throwable throwable) {
		servletContext.log(message, throwable);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletContext#log(java.lang.String)
	 */
	public void log(String msg) {
		servletContext.log(msg);
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}
}