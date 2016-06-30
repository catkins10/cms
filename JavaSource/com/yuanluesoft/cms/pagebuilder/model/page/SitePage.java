package com.yuanluesoft.cms.pagebuilder.model.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 站点应用页面
 * @author linchuan
 *
 */
public class SitePage extends CloneableObject {
	private String applicationName; //应用名称
	private String name; //名称,在所有应用中必须唯一,页面字段在模板对应的配置元素ID为{name}Field,如name="article",字段ID为:articleField
	private String title; //标题
	private String recordClassName; //记录类名称,只对记录页面有效
	private String url; //动态页面的URL,如果不指定systemLink="false"则为系统预置链接,CMS只生成第一页的静态页面,翻页采用动态页面实现
	private boolean systemLink; //是否系统链接,默认为true
	private boolean searchPage; //是否搜索页面
	private String searchResults; //搜索结果对应的记录列表
	private String parentPage; //父页面
	private List subPages; //子表单列表
	private List links; //专有链接列表
	private SiteTemplateView templateView; //模板视图
	private SiteTemplatePlugin templatePlugin; //模板插件
	private String js; //需要引入的脚本,多个时用逗号分隔
	private boolean staticPageSupport = true; //是否支持静态页面,默认true
	private boolean pagingSupport; //是否支持上一篇/下一篇
	private boolean internal; //是否后台页面
	private boolean recordPage; //是否记录页面,如果配置了recordClassName，默认true
	private boolean realtimeStaticPage; //是否需要实时生成静态页面,非记录页面（如：首页）默认true,记录页面（如：文章、政府信息）默认false
	
	private Map extendedParameters; //扩展参数列表
	private Map attributes; //属性列表
	
	public SitePage() {
		super();
	}

	public SitePage(String name, String title, String recordClassName, String url, boolean systemLink, boolean searchPage, String searchResults, boolean pagingSupport, boolean internal, boolean recordPage, boolean realtimeStaticPage) {
		super();
		this.name = name;
		this.title = title;
		this.recordClassName = recordClassName;
		this.url = url;
		this.systemLink = systemLink;
		this.searchPage = searchPage;
		this.searchResults = searchResults;
		this.pagingSupport = pagingSupport;
		this.internal = internal;
		this.recordPage = recordPage;
		this.realtimeStaticPage = realtimeStaticPage;
	}

	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public Object getExtendedParameter(String parameterName) {
		if(extendedParameters==null) {
			return null;
		}
		return extendedParameters.get(parameterName); 
	}
	
	/**
	 * 设置参数值
	 * @param parameterName
	 * @param parameterValue
	 */
	public void setExtendedParameter(String parameterName, Object parameterValue) {
		if(extendedParameters==null) {
			extendedParameters = new HashMap();
		}
		if(parameterValue==null) {
			extendedParameters.remove(parameterName);
		}
		else {
			extendedParameters.put(parameterName, parameterValue);
		}
	}
	
	/**
	 * 设置属性值
	 * @param attributeName
	 * @param attributeValue
	 */
	public void setAttribute(String attributeName, Object attributeValue) {
		if(attributes==null) {
			attributes = new HashMap();
		}
		if(attributeValue==null) {
			attributes.remove(attributeName);
		}
		else {
			attributes.put(attributeName, attributeValue);
		}
	}
	
	/**
	 * 获取属性值
	 * @param attributeName
	 * @return
	 */
	public Object getAttribute(String attributeName) {
		return (attributes==null ? null : attributes.get(attributeName));
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
	 * @return the subPages
	 */
	public List getSubPages() {
		return subPages;
	}
	/**
	 * @param subPages the subPages to set
	 */
	public void setSubPages(List subPages) {
		this.subPages = subPages;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the searchResults
	 */
	public String getSearchResults() {
		return searchResults;
	}
	/**
	 * @param searchResults the searchResults to set
	 */
	public void setSearchResults(String searchResults) {
		this.searchResults = searchResults;
	}

	/**
	 * @return the systemLink
	 */
	public boolean isSystemLink() {
		return systemLink;
	}

	/**
	 * @param systemLink the systemLink to set
	 */
	public void setSystemLink(boolean systemLink) {
		this.systemLink = systemLink;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @return the parentPage
	 */
	public String getParentPage() {
		return parentPage;
	}

	/**
	 * @param parentPage the parentPage to set
	 */
	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
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
	 * @return the siteTemplatePlugin
	 */
	public SiteTemplatePlugin getTemplatePlugin() {
		return templatePlugin;
	}

	/**
	 * @param siteTemplatePlugin the siteTemplatePlugin to set
	 */
	public void setTemplatePlugin(SiteTemplatePlugin siteTemplatePlugin) {
		this.templatePlugin = siteTemplatePlugin;
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
	 * @return the js
	 */
	public String getJs() {
		return js;
	}

	/**
	 * @param js the js to set
	 */
	public void setJs(String js) {
		this.js = js;
	}
	
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}

	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}

	/**
	 * @return the pagingSupport
	 */
	public boolean isPagingSupport() {
		return pagingSupport;
	}

	/**
	 * @param pagingSupport the pagingSupport to set
	 */
	public void setPagingSupport(boolean pagingSupport) {
		this.pagingSupport = pagingSupport;
	}

	/**
	 * @return the internal
	 */
	public boolean isInternal() {
		return internal;
	}

	/**
	 * @param internal the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	
	/**
	 * @return the realtimeStaticPage
	 */
	public boolean isRealtimeStaticPage() {
		return realtimeStaticPage;
	}

	/**
	 * @param realtimeStaticPage the realtimeStaticPage to set
	 */
	public void setRealtimeStaticPage(boolean realtimeStaticPage) {
		this.realtimeStaticPage = realtimeStaticPage;
	}

	/**
	 * @return the recordPage
	 */
	public boolean isRecordPage() {
		return recordPage;
	}

	/**
	 * @param recordPage the recordPage to set
	 */
	public void setRecordPage(boolean recordPage) {
		this.recordPage = recordPage;
	}

	/**
	 * @return the staticPageSupport
	 */
	public boolean isStaticPageSupport() {
		return staticPageSupport;
	}

	/**
	 * @param staticPageSupport the staticPageSupport to set
	 */
	public void setStaticPageSupport(boolean staticPageSupport) {
		this.staticPageSupport = staticPageSupport;
	}

	/**
	 * @return the extendedParameters
	 */
	public Map getExtendedParameters() {
		return extendedParameters;
	}

	/**
	 * @param extendedParameters the extendedParameters to set
	 */
	public void setExtendedParameters(Map extendedParameters) {
		this.extendedParameters = extendedParameters;
	}
}