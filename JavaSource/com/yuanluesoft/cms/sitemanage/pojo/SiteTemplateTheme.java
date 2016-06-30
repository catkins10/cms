package com.yuanluesoft.cms.sitemanage.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;

/**
 * 模板管理:站点模板主题(cms_site_template_theme)
 * @author linchuan
 *
 */
public class SiteTemplateTheme extends TemplateTheme {
	private long siteId; //站点ID,如果站点没有配置自己主题,则使用父站点的配置

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
}