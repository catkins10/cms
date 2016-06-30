package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class SelectPage extends TreeDialog {
	private String currentApplicationName; //URL参数:当前应用
	private boolean sitePageOnly; //URL参数:是否仅站点页面
	private long siteId; //URL参数:当前站点/栏目ID
	private boolean internalPageOnly; //URL参数:是否仅内部页面
	private long userId; //URL参数:当前用户ID
	private boolean advertPutSupportOnly; //URL参数:是否支持广告投放
	
	/**
	 * @return the currentApplicationName
	 */
	public String getCurrentApplicationName() {
		return currentApplicationName;
	}
	/**
	 * @param currentApplicationName the currentApplicationName to set
	 */
	public void setCurrentApplicationName(String currentApplicationName) {
		this.currentApplicationName = currentApplicationName;
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
	 * @return the internalPageOnly
	 */
	public boolean isInternalPageOnly() {
		return internalPageOnly;
	}
	/**
	 * @param internalPageOnly the internalPageOnly to set
	 */
	public void setInternalPageOnly(boolean internalPageOnly) {
		this.internalPageOnly = internalPageOnly;
	}
	/**
	 * @return the sitePageOnly
	 */
	public boolean isSitePageOnly() {
		return sitePageOnly;
	}
	/**
	 * @param sitePageOnly the sitePageOnly to set
	 */
	public void setSitePageOnly(boolean sitePageOnly) {
		this.sitePageOnly = sitePageOnly;
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
	 * @return the advertPutSupportOnly
	 */
	public boolean isAdvertPutSupportOnly() {
		return advertPutSupportOnly;
	}
	/**
	 * @param advertPutSupportOnly the advertPutSupportOnly to set
	 */
	public void setAdvertPutSupportOnly(boolean advertPutSupportOnly) {
		this.advertPutSupportOnly = advertPutSupportOnly;
	}
}