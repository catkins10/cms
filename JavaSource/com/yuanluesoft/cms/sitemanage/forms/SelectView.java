package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class SelectView extends TreeDialog {
	private String currentApplicationName; //URL参数:当前应用
	private String currentPageName; //URL参数:当前页面
	private boolean rssChannelSupportOnly;
	private boolean totalSupportOnly;
	private boolean navigatorSupportOnly;
	private boolean conatinsEmbedViewOnly;
	private boolean siteReferenceSupportOnly; //是否支持引用
	
	/**
	 * @return the conatinsEmbedViewOnly
	 */
	public boolean isConatinsEmbedViewOnly() {
		return conatinsEmbedViewOnly;
	}
	/**
	 * @param conatinsEmbedViewOnly the conatinsEmbedViewOnly to set
	 */
	public void setConatinsEmbedViewOnly(boolean conatinsEmbedViewOnly) {
		this.conatinsEmbedViewOnly = conatinsEmbedViewOnly;
	}
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
	 * @return the navigatorSupportOnly
	 */
	public boolean isNavigatorSupportOnly() {
		return navigatorSupportOnly;
	}
	/**
	 * @param navigatorSupportOnly the navigatorSupportOnly to set
	 */
	public void setNavigatorSupportOnly(boolean navigatorSupportOnly) {
		this.navigatorSupportOnly = navigatorSupportOnly;
	}
	/**
	 * @return the rssChannelSupportOnly
	 */
	public boolean isRssChannelSupportOnly() {
		return rssChannelSupportOnly;
	}
	/**
	 * @param rssChannelSupportOnly the rssChannelSupportOnly to set
	 */
	public void setRssChannelSupportOnly(boolean rssChannelSupportOnly) {
		this.rssChannelSupportOnly = rssChannelSupportOnly;
	}
	/**
	 * @return the totalSupportOnly
	 */
	public boolean isTotalSupportOnly() {
		return totalSupportOnly;
	}
	/**
	 * @param totalSupportOnly the totalSupportOnly to set
	 */
	public void setTotalSupportOnly(boolean totalSupportOnly) {
		this.totalSupportOnly = totalSupportOnly;
	}
	/**
	 * @return the currentPageName
	 */
	public String getCurrentPageName() {
		return currentPageName;
	}
	/**
	 * @param currentPageName the currentPageName to set
	 */
	public void setCurrentPageName(String currentPageName) {
		this.currentPageName = currentPageName;
	}
	/**
	 * @return the siteReferenceSupportOnly
	 */
	public boolean isSiteReferenceSupportOnly() {
		return siteReferenceSupportOnly;
	}
	/**
	 * @param siteReferenceSupportOnly the siteReferenceSupportOnly to set
	 */
	public void setSiteReferenceSupportOnly(boolean siteReferenceSupportOnly) {
		this.siteReferenceSupportOnly = siteReferenceSupportOnly;
	}
}