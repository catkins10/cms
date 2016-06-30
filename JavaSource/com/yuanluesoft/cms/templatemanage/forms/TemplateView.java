package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateView extends ViewForm {
	private long siteId; //URL参数:站点ID
	private String applicationName; //URL参数:应用名称
	private String pageName; //URL参数:页面
	
	private Tree sitePageTree; //站点页面树
	private long themeId; //主题ID
	private SiteTemplateTheme theme; //主题
	
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
	 * @return the sitePageTree
	 */
	public Tree getSitePageTree() {
		return sitePageTree;
	}
	/**
	 * @param sitePageTree the sitePageTree to set
	 */
	public void setSitePageTree(Tree sitePageTree) {
		this.sitePageTree = sitePageTree;
	}
	/**
	 * @return the theme
	 */
	public SiteTemplateTheme getTheme() {
		return theme;
	}
	/**
	 * @param theme the theme to set
	 */
	public void setTheme(SiteTemplateTheme theme) {
		this.theme = theme;
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