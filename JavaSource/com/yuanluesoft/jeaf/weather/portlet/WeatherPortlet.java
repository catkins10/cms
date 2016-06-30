package com.yuanluesoft.jeaf.weather.portlet;

import javax.portlet.RenderRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.portlet.TemplateBasedPortlet;

/**
 * 
 * @author linchuan
 *
 */
public class WeatherPortlet extends TemplateBasedPortlet {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#getTemplateHTMLDocument(com.yuanluesoft.portal.container.pojo.PortletEntity, long, javax.portlet.RenderRequest, java.lang.String, java.lang.String)
	 */
	protected HTMLDocument getTemplateHTMLDocument(PortletEntity portletEntity, long siteId, RenderRequest request, String pageApplication, String pageName) throws Exception {
		HTMLDocument template = super.getTemplateHTMLDocument(portletEntity, siteId, request, pageApplication, pageName);
		HTMLAnchorElement weathers = (HTMLAnchorElement)template.getElementById("recordList");
		String urn = StringUtils.removeQueryParameters(weathers.getAttribute("urn"), "extendProperties");
		String extendProperties = "areas=" + request.getPreferences().getValue("area", "") + "&forecastDays=" + request.getPreferences().getValue("days", "3");
		weathers.setAttribute("urn", urn + "&extendProperties=" + StringUtils.encodePropertyValue(extendProperties));
		return template;
	}
}