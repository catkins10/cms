package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 站点应用配置
 * @author linchuan
 *
 */
public class SiteApplicationConfigView extends ViewForm {
	private String applicationName; //URL参数:应用名称
	private String viewApplicationName; //URL参数:视图所在应用名称,默认applicationName
	private String viewName; //URL参数:视图名称
	private long siteId; //URL参数:站点ID,如果没有指定,自动打开用户有管理权限的第一个站点
	private boolean showChildSiteData; //URL参数:是否显示子站数据
	
	private Tree siteTree; //站点目录树

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
	 * @return the siteTree
	 */
	public Tree getSiteTree() {
		return siteTree;
	}

	/**
	 * @param siteTree the siteTree to set
	 */
	public void setSiteTree(Tree siteTree) {
		this.siteTree = siteTree;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return the viewApplicationName
	 */
	public String getViewApplicationName() {
		return viewApplicationName;
	}

	/**
	 * @param viewApplicationName the viewApplicationName to set
	 */
	public void setViewApplicationName(String viewApplicationName) {
		this.viewApplicationName = viewApplicationName;
	}

	/**
	 * @return the showChildSiteData
	 */
	public boolean isShowChildSiteData() {
		return showChildSiteData;
	}

	/**
	 * @param showChildSiteData the showChildSiteData to set
	 */
	public void setShowChildSiteData(boolean showChildSiteData) {
		this.showChildSiteData = showChildSiteData;
	}
}