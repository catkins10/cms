package com.yuanluesoft.portal.container.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;


/**
 * 
 * @author linchuan
 *
 */
public class PortalContextImpl implements javax.portlet.PortalContext {
	public static final String PORTAL_INFO = "YuanlueSoft Portal Server/1.0";
	private Map properties; //属性列表

	public PortalContextImpl(ServletContext servletContext, Map properties) {
		super();
		this.properties = properties;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortalContext#getPortalInfo()
	 */
	public String getPortalInfo() {
		return PORTAL_INFO;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortalContext#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		if(name == null) {
            throw new IllegalArgumentException("The given property name is null");
        }
        return (String)properties.get(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortalContext#getPropertyNames()
	 */
	public Enumeration getPropertyNames() {
		return Collections.enumeration(properties.keySet());
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortalContext#getSupportedPortletModes()
	 */
	public Enumeration getSupportedPortletModes() {
		List supportedPortletModes = new ArrayList();
		supportedPortletModes.add(PortletMode.VIEW);
		supportedPortletModes.add(PortletMode.EDIT);
		//supportedPortletModes.add(PortletMode.HELP);
		return Collections.enumeration(supportedPortletModes);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortalContext#getSupportedWindowStates()
	 */
	public Enumeration getSupportedWindowStates() {
		List supportedWindowStates = new ArrayList();
		supportedWindowStates.add(WindowState.NORMAL);
		supportedWindowStates.add(WindowState.MINIMIZED);
		//supportedWindowStates.add(WindowState.MAXIMIZED);
		return Collections.enumeration(supportedWindowStates);
	}
}