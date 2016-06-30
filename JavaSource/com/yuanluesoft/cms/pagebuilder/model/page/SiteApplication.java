package com.yuanluesoft.cms.pagebuilder.model.page;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 站点应用,需要对外展现的应用程序
 * @author linchuan
 *
 */
public class SiteApplication extends CloneableObject {
	private String name; //应用名称
	private List pages; //页面列表
	private List links; //链接列表
	private SiteTemplateView templateView; //站点模板视图配置
	private List templatePlugins; //插件列表
	private List pageElements; //本应用增加的页面元素列表
	
	public SiteApplication() {
		super();
	}

	public SiteApplication(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * 是否有系统链接
	 * @return
	 */
	public boolean isSystemLinkExist() {
		return ListUtils.findObjectByProperty(pages, "systemLink", Boolean.TRUE)!=null;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the pages
	 */
	public List getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(List pages) {
		this.pages = pages;
	}
	
	/**
	 * @return the links
	 */
	public List getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(List links) {
		this.links = links;
	}

	/**
	 * @return the pageElements
	 */
	public List getPageElements() {
		return pageElements;
	}

	/**
	 * @param pageElements the pageElements to set
	 */
	public void setPageElements(List pageElements) {
		this.pageElements = pageElements;
	}

	/**
	 * @return the siteTemplateView
	 */
	public SiteTemplateView getTemplateView() {
		return templateView;
	}

	/**
	 * @param siteTemplateView the siteTemplateView to set
	 */
	public void setTemplateView(SiteTemplateView siteTemplateView) {
		this.templateView = siteTemplateView;
	}

	/**
	 * @return the templatePlugins
	 */
	public List getTemplatePlugins() {
		return templatePlugins;
	}

	/**
	 * @param templatePlugins the templatePlugins to set
	 */
	public void setTemplatePlugins(List templatePlugins) {
		this.templatePlugins = templatePlugins;
	}
}