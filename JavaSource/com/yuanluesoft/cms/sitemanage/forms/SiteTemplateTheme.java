package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.cms.templatemanage.forms.TemplateTheme;

/**
 * 
 * @author linchuan
 *
 */
public class SiteTemplateTheme extends TemplateTheme {
	private long siteId; //站点ID,如果站点没有配置自己主题,则使用父站点的配置
	private String siteName; //站点名称
	private long pageSiteId; //当前页面所属站点ID
	private String pageSiteName; //当前页面所属站点名称

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
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return the pageSiteId
	 */
	public long getPageSiteId() {
		return pageSiteId;
	}

	/**
	 * @param pageSiteId the pageSiteId to set
	 */
	public void setPageSiteId(long pageSiteId) {
		this.pageSiteId = pageSiteId;
	}

	/**
	 * @return the pageSiteName
	 */
	public String getPageSiteName() {
		return pageSiteName;
	}

	/**
	 * @param pageSiteName the pageSiteName to set
	 */
	public void setPageSiteName(String pageSiteName) {
		this.pageSiteName = pageSiteName;
	}
}