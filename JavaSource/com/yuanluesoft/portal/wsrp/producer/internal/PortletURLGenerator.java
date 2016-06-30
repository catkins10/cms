package com.yuanluesoft.portal.wsrp.producer.internal;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import oasis.names.tc.wsrp.v1.types.RuntimeContext;
import oasis.names.tc.wsrp.v1.types.Templates;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.portal.container.internal.PortletWindow;
import com.yuanluesoft.portal.wsrp.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class PortletURLGenerator extends com.yuanluesoft.portal.container.internal.PortletURLGenerator {
	private RuntimeContext runtimeContext;

	public PortletURLGenerator(RuntimeContext runtimeContext) {
		super();
		this.runtimeContext = runtimeContext;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.portal.container.internal.PortletURLGenerator#generatePortletURL(com.yuanluesoft.portal.container.internal.PortletWindow, javax.portlet.WindowState, javax.portlet.PortletMode, java.util.Map, boolean)
	 */
	public String generatePortletURL(PortletWindow portletWindow, WindowState windowState, PortletMode portletMode, Map parameters, boolean isAction) {
		Templates templates = runtimeContext.getTemplates();
		if(templates==null) {
			return super.generatePortletURL(portletWindow, windowState, portletMode, parameters, isAction);
		}
		String template = isAction ? templates.getBlockingActionTemplate() : templates.getRenderTemplate();
		if(template==null) {
			template = templates.getDefaultTemplate();
		}
		if(template==null) {
			return super.generatePortletURL(portletWindow, windowState, portletMode, parameters, isAction);
		}
		String encodedParameters;
		try {
			encodedParameters = URLEncoder.encode(new Base64Encoder().encode(ObjectSerializer.serialize((Serializable)parameters)), "utf-8");
		}
        catch (Exception e) {
			Logger.exception(e);
			encodedParameters = "";
		}
        template = template.replaceAll("\\{" + Constants.URL_TYPE + "\\}", isAction ? Constants.URL_TYPE_BLOCKINGACTION : Constants.URL_TYPE_RENDER)
						   .replaceAll("\\{" + Constants.PORTLET_MODE + "\\}", portletMode.toString())
						   .replaceAll("\\{" + Constants.NAVIGATIONAL_STATE + "\\}", isAction ? "" : encodedParameters) //base64编码后的render参数
						   .replaceAll("\\{" + Constants.INTERACTION_STATE + "\\}", isAction ? encodedParameters : "") //base64编码后的action参数
						   .replaceAll("\\{" + Constants.WINDOW_STATE + "\\}", windowState.toString())
						   .replaceAll("\\{" + Constants.SECURE_URL + "\\}", "")
						   .replaceAll("\\{" + Constants.URL + "\\}", "") //资源URL
						   .replaceAll("\\{" + Constants.REWRITE_RESOURCE + "\\}", "")
						   .replaceAll("\\{" + Constants.PORTLET_HANDLE + "\\}", portletWindow.getPortletHandle())
						   .replaceAll("\\{" + Constants.USER_CONTEXT_KEY + "\\}", "")
						   .replaceAll("\\{" + Constants.PORTLET_INSTANCE_KEY + "\\}", portletWindow.getPortletInstanceId())
						   .replaceAll("\\{" + Constants.SESSION_ID + "\\}", "");
		return template;
	}
}