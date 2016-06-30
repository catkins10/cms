/*
 * Created on 2007-7-1
 *
 */
package com.yuanluesoft.cms.templatemanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class Template extends Record {
	private long themeId; //主题ID
	private String templateName; //模板名称
	private String applicationName; //应用名称
	private String pageName; //页面名称
	private long siteId; //站点/栏目ID
	private char isSelected = '0'; //是否选中,是否选中
	private Timestamp lastModified; //最后修改时间
	private long lastModifierId; //最后修改人ID
	private String lastModifier; //最后修改人姓名
	
	//扩展属性
	private TemplateTheme theme; //主题
	
	/**
	 * 是否是默认模板
	 * @return
	 */
	public String getIsDefault() {
		return isSelected=='1' ? "√" : "";
	}
	/**
	 * @return the isSelected
	 */
	public char getIsSelected() {
		return isSelected;
	}
	/**
	 * @param isSelected the isSelected to set
	 */
	public void setIsSelected(char isSelected) {
		this.isSelected = isSelected;
	}
	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}

	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	/**
	 * @return the lastModifierId
	 */
	public long getLastModifierId() {
		return lastModifierId;
	}

	/**
	 * @param lastModifierId the lastModifierId to set
	 */
	public void setLastModifierId(long lastModifierId) {
		this.lastModifierId = lastModifierId;
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
	/**
	 * @return the theme
	 */
	public TemplateTheme getTheme() {
		return theme;
	}
	/**
	 * @param theme the theme to set
	 */
	public void setTheme(TemplateTheme theme) {
		this.theme = theme;
	}
}