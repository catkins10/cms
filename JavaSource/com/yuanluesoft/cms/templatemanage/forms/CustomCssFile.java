package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 自定义CSS文件
 * @author linchuan
 *
 */
public class CustomCssFile extends ActionForm {
	private String cssUrl; //URL参数:CSS URL
	private long siteId; //URL参数:站点ID
	
	private String cssName; //名称
	private String fromCssFile; //源文件
	private String cssText; //CSS
	
	/**
	 * @return the cssText
	 */
	public String getCssText() {
		return cssText;
	}
	/**
	 * @param cssText the cssText to set
	 */
	public void setCssText(String cssText) {
		this.cssText = cssText;
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
}