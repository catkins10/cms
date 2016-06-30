package com.yuanluesoft.cms.sitemanage.model;

import java.io.Serializable;

/**
 * 同步设置默认主题
 * @author linchuan
 *
 */
public class SynchSetDefaultTheme implements Serializable {
	private long themeId;
	private long siteId;
	private boolean isDefault;
	private boolean isTemporaryOpening;
	
	public SynchSetDefaultTheme(long themeId, long siteId, boolean isDefault, boolean isTemporaryOpening) {
		super();
		this.themeId = themeId;
		this.siteId = siteId;
		this.isDefault = isDefault;
		this.isTemporaryOpening = isTemporaryOpening;
	}
	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * @return the isTemporaryOpening
	 */
	public boolean isTemporaryOpening() {
		return isTemporaryOpening;
	}
	/**
	 * @param isTemporaryOpening the isTemporaryOpening to set
	 */
	public void setTemporaryOpening(boolean isTemporaryOpening) {
		this.isTemporaryOpening = isTemporaryOpening;
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