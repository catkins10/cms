package com.yuanluesoft.portal.container.internal;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author linchuan
 *
 */
public class PortletRequestDispatcherImpl implements javax.portlet.PortletRequestDispatcher {
	private RequestDispatcher requestDispatcher;
    
	public PortletRequestDispatcherImpl(RequestDispatcher requestDispatcher) {
		super();
		this.requestDispatcher = requestDispatcher;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequestDispatcher#include(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public void include(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			requestDispatcher.forward((HttpServletRequest)request, (HttpServletResponse)response);
		}
		catch (ServletException e) {
			throw new PortletException(e);
		}
	}
}