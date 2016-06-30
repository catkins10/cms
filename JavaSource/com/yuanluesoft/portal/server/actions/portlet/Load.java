package com.yuanluesoft.portal.server.actions.portlet;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.portal.container.internal.MarkupResponse;
import com.yuanluesoft.portal.container.internal.PortletURLGenerator;
import com.yuanluesoft.portal.container.internal.PortletWindow;
import com.yuanluesoft.portal.container.service.PortletContainer;
import com.yuanluesoft.portal.server.forms.Portlet;
import com.yuanluesoft.portal.server.util.PortletUtils;
import com.yuanluesoft.portal.wsrp.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setCharacterEncoding("utf-8");
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		response.getWriter().write("<html><body onload=\"top.location.reload();\"></body></html>");
        	return null;
    	}
    	PortletContainer portletContainer = (PortletContainer)getService("portletContainer");
    	HTMLParser htmlParser = (HTMLParser)getService("htmlParser");
    	Portlet portletForm = (Portlet)form;
    	PortletWindow portletWindow = new PortletWindow(portletForm.getPortletInstanceKey(), portletForm.getWsrpProducerId(), portletForm.getPortletHandle(), new WindowState(portletForm.getWindowState()), new PortletMode(portletForm.getPortletMode()), portletForm.getPortalUserId(), portletForm.getPortalSiteId(), new PortletURLGenerator());
    	MarkupResponse markupResponse = null;
    	if(Constants.URL_TYPE_RENDER.equals(portletForm.getUrlType())) {
    		markupResponse = portletContainer.doRender(portletWindow, request);
    	}
    	else if(Constants.URL_TYPE_BLOCKINGACTION.equals(portletForm.getUrlType())) {
    		markupResponse = portletContainer.doAction(portletWindow, request);
    	}
    	HTMLDocument portletDocument = PortletUtils.createPortletDocument(markupResponse, portletWindow.getPortletInstanceId());
		if(portletDocument==null) {
			return null;
		}
		//重置脚本,避免被iframe执行
		NodeList scripts = portletDocument.getElementsByTagName("script");
		for(int i=(scripts==null ? -1 : scripts.getLength()-1); i>=0; i--) {
			HTMLScriptElement script = (HTMLScriptElement)scripts.item(i);
			if(script.getSrc()==null || script.getSrc().isEmpty()) { //不是js文件
				htmlParser.setTextContent(script, "//lazyload" + script.getTextContent().replaceAll("\n", "\n//lazyload"));
			}
		}
		htmlParser.writeHTMLDocument(portletDocument, response.getWriter(), "utf-8");
		return null;
    }
}