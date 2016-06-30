package com.yuanluesoft.cms.templatemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * CSS样式表文件(cms_css_file)
 * @author linchuan
 *
 */
public class CssFile extends Record {
	private String cssName; //名称
	private String cssUrl; //URL
	private String fromCssFile; //源文件
	private long siteId; //隶属站点ID

	/**
	 * @return the cssName
	 */
	public String getCssName() {
		return cssName;
	}
	/**
	 * @param cssName the cssName to set
	 */
	public void setCssName(String cssName) {
		this.cssName = cssName;
	}
	/**
	 * @return the fromCssFile
	 */
	public String getFromCssFile() {
		return fromCssFile;
	}
	/**
	 * @param fromCssFile the fromCssFile to set
	 */
	public void setFromCssFile(String fromCssFile) {
		this.fromCssFile = fromCssFile;
	}
	/**
	 * @return the cssUrl
	 */
	public String getCssUrl() {
		return cssUrl;
	}
	/**
	 * @param cssUrl the cssUrl to set
	 */
	public void setCssUrl(String cssUrl) {
		this.cssUrl = cssUrl;
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
}
