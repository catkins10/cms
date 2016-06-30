package com.yuanluesoft.cms.templatemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 模板主题:使用(cms_template_theme_usage)
 * @author linchuan
 *
 */
public class TemplateThemeUsage extends Record {
	private long siteId; //站点/用户ID,如果站点没有配置自己主题,则使用父站点的配置
	private long themeId; //主题ID,iphine、ipad不支持flash
	private int isDefault; //是否默认主题,默认主题修改后重新生成本站的全部静态页面
	private int temporaryOpening; //是否临时启用
	
	/**
	 * @return the isDefault
	 */
	public int getIsDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
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
	 * @return the temporaryOpening
	 */
	public int getTemporaryOpening() {
		return temporaryOpening;
	}
	/**
	 * @param temporaryOpening the temporaryOpening to set
	 */
	public void setTemporaryOpening(int temporaryOpening) {
		this.temporaryOpening = temporaryOpening;
	}
	/**
	 * @return the themeId
	 */
	public long getThemeId() {
		return themeId;
	}
	/**
	 * @param themeId the themeId to set
	 */
	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}

}