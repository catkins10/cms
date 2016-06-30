package com.yuanluesoft.portal.container.internal;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.portal.wsrp.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class PortletURLGenerator {

	/**
	 * 生成URL
	 * @param portletWindow
	 * @param windowState
	 * @param portletMode
	 * @param parameters
	 * @param isAction
	 * @return
	 */
	public String generatePortletURL(PortletWindow portletWindow, WindowState windowState, PortletMode portletMode, Map parameters, boolean isAction) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Environment.getContextPath() + "/portal/portlet.shtml");
		//PORTLET实例ID
		buffer.append("?portletInstanceKey=" + portletWindow.getPortletInstanceId());
		//WRSP生产者ID
		if(portletWindow.getWsrpProducerId()!=null) {
			buffer.append("&wsrpProducerId=" + portletWindow.getWsrpProducerId());
		}
		//url类型
		buffer.append("&urlType=" + (isAction ? Constants.URL_TYPE_BLOCKINGACTION : Constants.URL_TYPE_RENDER));
		//PORTLET句柄
		buffer.append("&portletHandle=" + portletWindow.getPortletHandle());
		//模式
    	if(portletMode!=null) {
    		buffer.append("&portletMode=" + portletMode.toString());
    	}
    	//状态
		if(windowState!=null) {
			buffer.append("&windowState=" + windowState.toString());
		}
		//参数
		if(parameters!=null && !parameters.isEmpty()) {
			buffer.append(isAction ? "&interactionState=" : "&navigationalState=");
			try {
				buffer.append(URLEncoder.encode(new Base64Encoder().encode(ObjectSerializer.serialize((Serializable)parameters)), "utf-8")); //base64编码后的参数
			}
	        catch (Exception e) {
				Logger.exception(e);
			}
		}
		//用户ID
		if(portletWindow.getUserId()!=-1) {
			buffer.append("&portalUserId=" + portletWindow.getUserId());
		}
		//站点ID
		if(portletWindow.getSiteId()!=-1) {
			buffer.append("&portalSiteId=" + portletWindow.getSiteId());
		}
        return buffer.toString();
	}
}