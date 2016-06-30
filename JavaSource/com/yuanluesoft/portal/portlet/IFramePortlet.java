package com.yuanluesoft.portal.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * 
 * @author linchuan
 *
 */
public class IFramePortlet extends BasePortlet {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		String html = "<iframe" +
					  " frameborder=\"0\"" +
					  " width=\"100%\"" +
					  " height=\"" + request.getPreferences().getValue("iframeHeight", "300") + "\"" +
					  " src=\"" + request.getPreferences().getValue("url", "about:blank") + "\">" +
					  "</iframe>";
		response.getWriter().write(html);
		String siteName = request.getPreferences().getValue("siteName", "");
		if(siteName!=null && !siteName.isEmpty()) {
			response.setTitle(siteName);
		}
	}
}