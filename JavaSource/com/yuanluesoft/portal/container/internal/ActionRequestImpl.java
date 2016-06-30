package com.yuanluesoft.portal.container.internal;

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v1.types.PortletDescription;

import com.yuanluesoft.portal.container.service.PortletContainer;

/**
 * 
 * @author linchuan
 *
 */
public class ActionRequestImpl extends PortletRequestImpl implements ActionRequest {

	public ActionRequestImpl(HttpServletRequest request, PortletContainer portletContainer, PortalContext portalContext, PortletContext portletContext, PortletDescription portletDescription, PortletWindow portletWindow) {
		super(request, portletContainer, portalContext, portletContext, portletDescription, portletWindow);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.ActionRequest#getPortletInputStream()
	 */
	public InputStream getPortletInputStream() throws IOException {
		return super.getInputStream();
	}
}