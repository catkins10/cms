package com.yuanluesoft.portal.server.model;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Portal implements Serializable {
	private List portalPages; //PORTAL页面列表
	
	/**
	 * 获取当前页面
	 * @return
	 */
	public PortalPage getCurrentPortalPage() {
		return (PortalPage)ListUtils.findObjectByProperty(portalPages, "selected", Boolean.TRUE);
	}

	/**
	 * @return the portalPages
	 */
	public List getPortalPages() {
		return portalPages;
	}

	/**
	 * @param portalPages the portalPages to set
	 */
	public void setPortalPages(List portalPages) {
		this.portalPages = portalPages;
	}
}