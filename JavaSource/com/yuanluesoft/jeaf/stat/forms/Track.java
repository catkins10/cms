package com.yuanluesoft.jeaf.stat.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Track extends ActionForm {
	private String applicationName; //应用名称
	private String pageName; //页面名称
	private long siteId;
	private long recordId;
	private int screenWidth;
	private int screenHeight;
	private int colorDepth;
	private boolean javaEnabled;
	private String language;
	private boolean countDisable;
	private boolean writeAccessCount;
	
	
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
	 * @return the colorDepth
	 */
	public int getColorDepth() {
		return colorDepth;
	}
	/**
	 * @param colorDepth the colorDepth to set
	 */
	public void setColorDepth(int colorDepth) {
		this.colorDepth = colorDepth;
	}
	/**
	 * @return the javaEnabled
	 */
	public boolean isJavaEnabled() {
		return javaEnabled;
	}
	/**
	 * @param javaEnabled the javaEnabled to set
	 */
	public void setJavaEnabled(boolean javaEnabled) {
		this.javaEnabled = javaEnabled;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
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
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	/**
	 * @param screenHeight the screenHeight to set
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	/**
	 * @return the screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * @param screenWidth the screenWidth to set
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
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
	 * @return the writeAccessCount
	 */
	public boolean isWriteAccessCount() {
		return writeAccessCount;
	}
	/**
	 * @param writeAccessCount the writeAccessCount to set
	 */
	public void setWriteAccessCount(boolean writeAccessCount) {
		this.writeAccessCount = writeAccessCount;
	}
	/**
	 * @return the countDisable
	 */
	public boolean isCountDisable() {
		return countDisable;
	}
	/**
	 * @param countDisable the countDisable to set
	 */
	public void setCountDisable(boolean countDisable) {
		this.countDisable = countDisable;
	}
}