package com.yuanluesoft.portal.container.internal;

import java.util.Iterator;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author linchuan
 *
 */
public class ActionResponseImpl extends PortletResponseImpl implements ActionResponse {
	private PortletWindow portletWindow;
	private RenderRequestImpl renderRequest;

	public ActionResponseImpl(HttpServletResponse response, PortletWindow portletWindow, RenderRequestImpl renderRequest) {
		super(response);
		this.portletWindow = portletWindow;
		this.renderRequest = renderRequest;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionResponse#setPortletMode(javax.portlet.PortletMode)
	 */
	public void setPortletMode(PortletMode mode) throws PortletModeException {
		portletWindow.setPortletMode(mode);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionResponse#setWindowState(javax.portlet.WindowState)
	 */
	public void setWindowState(WindowState state) throws WindowStateException {
		portletWindow.setWindowState(state);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionResponse#setRenderParameter(java.lang.String, java.lang.String)
	 */
	public void setRenderParameter(String name, String value) {
		renderRequest.setParameter(name, value);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionResponse#setRenderParameter(java.lang.String, java.lang.String[])
	 */
	public void setRenderParameter(String name, String[] values) {
		renderRequest.setParameter(name, values);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionResponse#setRenderParameters(java.util.Map)
	 */
	public void setRenderParameters(Map parameters) {
		for(Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
			String name = (String)iterator.next();
			String[] values = (String[])parameters.get(name);
			setRenderParameter(name, values);
		}
	}
}