package com.yuanluesoft.portal.container.model;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class PortletSupport extends CloneableObject {
	private String mimeType; //MIME类型
	private List portletModes; //支持的模式
	
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	/**
	 * @return the portletModes
	 */
	public List getPortletModes() {
		return portletModes;
	}
	/**
	 * @param portletModes the portletModes to set
	 */
	public void setPortletModes(List portletModes) {
		this.portletModes = portletModes;
	}
}