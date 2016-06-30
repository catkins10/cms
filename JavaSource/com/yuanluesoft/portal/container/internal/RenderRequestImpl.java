package com.yuanluesoft.portal.container.internal;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v1.types.PortletDescription;

import com.yuanluesoft.portal.container.service.PortletContainer;

/**
 * 
 * @author linchuan
 *
 */
public class RenderRequestImpl extends PortletRequestImpl implements RenderRequest {

	public RenderRequestImpl(HttpServletRequest request, PortletContainer portletContainer, PortalContext portalContext, PortletContext portletContext, PortletDescription portletDescription, PortletWindow portletWindow) {
		super(request, portletContainer, portalContext, portletContext, portletDescription, portletWindow);
	}
}