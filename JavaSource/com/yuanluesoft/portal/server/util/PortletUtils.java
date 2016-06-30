package com.yuanluesoft.portal.server.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLStyleElement;

import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.portal.container.internal.MarkupResponse;

/**
 * 
 * @author linchuan
 *
 */
public class PortletUtils {

	/**
	 * 重设PORTLET链接
	 * @param portletDocument
	 */
	public static HTMLDocument createPortletDocument(MarkupResponse markupResponse, String portletInstanceId) throws Exception {
		if(markupResponse.getMarkup()==null || markupResponse.getMarkup().isEmpty()) {
			return null;
		}
		HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
		HTMLDocument portletDocument = htmlParser.parseHTMLString(markupResponse.getMarkup(), "utf-8");
		
		//重设地址含portlet.shtml的链接
		NodeList nodes = portletDocument.getElementsByTagName("a");
		for(int i=0; i<(nodes==null ? 0 : nodes.getLength()); i++) {
			HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(i);
			String href = a.getHref();
			if(href==null || href.indexOf("/portal/portlet.shtml")==-1) {
				continue;
			}
			a.setHref("#");
			a.setAttribute("onclick", "window.portal.openPortletURL('" + portletInstanceId + "', '" + href + "');return false;");
		}
		//重设action含portlet.shtml的表单
		nodes = portletDocument.getElementsByTagName("form");
		for(int i=0; i<(nodes==null ? 0 : nodes.getLength()); i++) {
			HTMLFormElement form = (HTMLFormElement)nodes.item(i);
			String action = form.getAction();
			if(action==null || action.indexOf("/portal/portlet.shtml")==-1) {
				continue;
			}
			//创建iframe,用来处理请求
			HTMLIFrameElement iframe = (HTMLIFrameElement)portletDocument.createElement("iframe");
			iframe.setAttribute("style", "display:none");
			iframe.setAttribute("onload", "if(this.contentWindow.location.href.indexOf('portlet.shtml')!=-1 && window.portal)window.portal.onFormActionProcessed(this, '" + portletInstanceId + "')");
			iframe.setName("form_" + UUIDLongGenerator.generateId());
			form.setTarget(iframe.getName());
			form.getParentNode().insertBefore(iframe, form);
		}
		//将css、js引入body
		HTMLHeadElement header = htmlParser.getHTMLHeader(portletDocument, false);
		nodes = header==null ? null : header.getChildNodes();
		for(int i=(nodes==null ? -1 : nodes.getLength()-1); i>=0; i--) {
			Node node = nodes.item(i);
			if((node instanceof HTMLScriptElement) || (node instanceof HTMLLinkElement) || (node instanceof HTMLStyleElement)) {
				NodeList bodyNodes = portletDocument.getBody().getChildNodes();
				if(bodyNodes==null || bodyNodes.getLength()==0) {
					portletDocument.getBody().appendChild(node);
				}
				else {
					portletDocument.getBody().insertBefore(node, bodyNodes.item(0));
				}
			}
		}
		return portletDocument;
	}
}