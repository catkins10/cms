/*
 * Created on 2007-7-5
 *
 */
package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Template extends ActionForm {
	private long themeId; //主题ID
	private String templateName; //模板名称
	private String applicationName; //应用名称
	private String templateAction; //模板操作: upload/上传, restore/还原, copy/拷贝, setAsDefault/设为默认, loadNormal/加载预置的模板
	private long copiedTemplateId; //被拷贝的模板ID,templateAction=copy时有效
	private String pageName; //页面名称
	private long siteId; //站点/栏目ID
	private char isSelected = '0'; //是否选中,是否选中
	
	private boolean searchPage; //是否搜索结果页面
	private String searchResultsName; //搜索结果列表名称
	private int themePageWidth; //主题页面宽度
	private int themeType; //主题类型
	private String pageHTML; //页面HTML
	
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
	 * @return the pageHTML
	 */
	public String getPageHTML() {
		return pageHTML;
	}
	/**
	 * @param pageHTML the pageHTML to set
	 */
	public void setPageHTML(String pageHTML) {
		this.pageHTML = pageHTML;
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
	 * @return the templateAction
	 */
	public String getTemplateAction() {
		return templateAction;
	}
	/**
	 * @param templateAction the templateAction to set
	 */
	public void setTemplateAction(String templateAction) {
		this.templateAction = templateAction;
	}
	/**
	 * @return the copiedTemplateId
	 */
	public long getCopiedTemplateId() {
		return copiedTemplateId;
	}
	/**
	 * @param copiedTemplateId the copiedTemplateId to set
	 */
	public void setCopiedTemplateId(long copiedTemplateId) {
		this.copiedTemplateId = copiedTemplateId;
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
	 * @return the themePageWidth
	 */
	public int getThemePageWidth() {
		return themePageWidth;
	}
	/**
	 * @param themePageWidth the themePageWidth to set
	 */
	public void setThemePageWidth(int themePageWidth) {
		this.themePageWidth = themePageWidth;
	}
	/**
	 * @return the themeType
	 */
	public int getThemeType() {
		return themeType;
	}
	/**
	 * @param themeType the themeType to set
	 */
	public void setThemeType(int themeType) {
		this.themeType = themeType;
	}
	/**
	 * @return the searchPage
	 */
	public boolean isSearchPage() {
		return searchPage;
	}
	/**
	 * @param searchPage the searchPage to set
	 */
	public void setSearchPage(boolean searchPage) {
		this.searchPage = searchPage;
	}
	/**
	 * @return the searchResultsName
	 */
	public String getSearchResultsName() {
		return searchResultsName;
	}
	/**
	 * @param searchResultsName the searchResultsName to set
	 */
	public void setSearchResultsName(String searchResultsName) {
		this.searchResultsName = searchResultsName;
	}
}
