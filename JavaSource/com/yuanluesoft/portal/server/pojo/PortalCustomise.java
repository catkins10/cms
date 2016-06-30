package com.yuanluesoft.portal.server.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 门户:个性定制(portal_customise)
 * @author linchuan
 *
 */
public class PortalCustomise extends Record {
	private long userId = -1; //用户/组织机构ID
	private long siteId = -1; //站点ID
	private String applicationName; //应用名称
	private String pageName; //页面名称
	private String portalXml; //PORTAL配置XML
	
	/**
	 * @return the portalXml
	 */
	public String getPortalXml() {
		return portalXml;
	}
	/**
	 * @param portalXml the portalXml to set
	 */
	public void setPortalXml(String portalXml) {
		this.portalXml = portalXml;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}