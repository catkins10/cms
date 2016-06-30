package com.yuanluesoft.portal.container.internal;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import oasis.names.tc.wsrp.v1.types.PortletDescription;

/**
 * 
 * @author linchuan
 *
 */
public class RenderResponseImpl extends PortletResponseImpl implements RenderResponse {
	private PortalContext portalContext;
	private PortletDescription portletDescription;
	private PortletWindow portletWindow;

	public RenderResponseImpl(HttpServletResponse response, PortalContext portalContext, PortletDescription portletDescription, PortletWindow portletWindow) {
		super(response);
		this.portalContext = portalContext;
		this.portletDescription = portletDescription;
		this.portletWindow = portletWindow;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.RenderResponse#createActionURL()
	 */
	public PortletURL createActionURL() {
		return createURL(true);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.RenderResponse#createRenderURL()
	 */
	public PortletURL createRenderURL() {
		return createURL(false);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.RenderResponse#getNamespace()
	 */
	public String getNamespace() {
		return "portlet" +
			   "_" + portletWindow.getPortletInstanceId() +
			   "_";
	}

	/* (non-Javadoc)
	 * @see javax.portlet.RenderResponse#getPortletOutputStream()
	 */
	public OutputStream getPortletOutputStream() throws IOException {
		return super.getOutputStream();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.RenderResponse#setTitle(java.lang.String)
	 */
	public void setTitle(String title) { //动态设置PORTLET标题
		portletWindow.setPortletTitle(title);
	}
	
	/**
	 * 创建URL
	 * @param isAction
	 * @return
	 */
	private PortletURL createURL(boolean isAction) {
        return new PortletURLImpl(portalContext, portletWindow, portletDescription, portletWindow.getWindowState(), PortletMode.VIEW, isAction);
    }
}