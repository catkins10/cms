package com.yuanluesoft.cms.pagebuilder.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteLink;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePageElement;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteSubPage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteTemplatePlugin;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteTemplateView;
import com.yuanluesoft.cms.pagebuilder.parser.PageDefineParser;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class PageDefineParserImpl extends XmlParser implements PageDefineParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.parser.PageDefineParser#parseSiteApplication(java.lang.String, java.lang.String)
	 */
	public SiteApplication parseSiteApplication(String applicationName, String fileName) throws ParseException {
		Element applicationElement = (Element)parseXmlFile(fileName);
		//解析站点应用
		SiteApplication siteApplication = new SiteApplication();
		siteApplication.setName(applicationName);
		//解析站点页面列表
		siteApplication.setPages(parseSitePages(applicationElement.element("pages"), applicationName));
		//解析站点链接列表
		siteApplication.setLinks(parseSiteLinks(applicationElement.element("links")));
		//解析模板视图
		siteApplication.setTemplateView(parseSiteTemplateView(applicationElement.element("templateView")));
		//解析插件列表
		siteApplication.setTemplatePlugins(parseSiteTemplatePlugins(applicationElement.element("templatePlugins")));
		//解析页面元素列表
		siteApplication.setPageElements(parseSitePageElements(applicationElement.element("pageElements")));
		return siteApplication;
	}
	
	/**
	 * 解析站点页面列表
	 * @param pagesElement
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	private List parseSitePages(Element pagesElement, String applicationName) throws ParseException {
		if(pagesElement==null) {
			return null;
		}
		List pages = new ArrayList();
		for(Iterator iteratorForm = pagesElement.elementIterator("page"); iteratorForm.hasNext();) {
			Element element = (Element)iteratorForm.next();
			SitePage sitePage = new SitePage();
			sitePage.setApplicationName(applicationName); //应用名称
			sitePage.setName(element.attributeValue("name")); //名称
			sitePage.setTitle(element.attributeValue("title")); //标题
			sitePage.setUrl(element.attributeValue("url")); //链接
			if(sitePage.getUrl()!=null) {
				sitePage.setSystemLink(!"false".equals(element.attributeValue("systemLink")));
			}
			sitePage.setJs(element.attributeValue("js")); //js引用
			sitePage.setTemplateView(parseSiteTemplateView(element.element("templateView"))); //模板视图
			sitePage.setSearchPage("true".equals(element.attributeValue("searchPage"))); //是否搜索模板
			sitePage.setSearchResults(element.attributeValue("searchResults")); //搜索结果使用的记录列表
			sitePage.setParentPage(element.attributeValue("parentPage")); //搜索父页面
			sitePage.setRecordClassName(element.attributeValue("recordClass")); //记录类名称
			sitePage.setPagingSupport("true".equals(element.attributeValue("pagingSupport"))); //是否支持上一篇/下一篇
			sitePage.setInternal("true".equals(element.attributeValue("internal"))); //是否后台页面
			//是否记录页面,如果配置了recordClassName，默认true
			String attributeValue = element.attributeValue("recordPage");
			sitePage.setRecordPage("true".equals(attributeValue) || (sitePage.getRecordClassName()!=null && !"false".equals(attributeValue)));
			//是否需要实时生成静态页面,非记录页面（如：首页）默认true,记录页面（如：文章、政府信息）默认false
			attributeValue = element.attributeValue("realtimeStaticPage");
			sitePage.setRealtimeStaticPage("true".equals(attributeValue) || (!sitePage.isRecordPage() && !"false".equals(attributeValue)));
			//是否支持静态页面
			sitePage.setStaticPageSupport(!"false".equals(element.attributeValue("staticPageSupport")));
			
			//解析子页面列表
			List subPages = new ArrayList();
			for(Iterator iteratorSubForm = element.elementIterator("subPage"); iteratorSubForm.hasNext();) {
				Element subPageElement = (Element)iteratorSubForm.next();
				SiteSubPage subPage = new SiteSubPage();
				subPage.setName(subPageElement.attributeValue("name")); //名称
				subPage.setTitle(subPageElement.attributeValue("title")); //标题
				subPage.setType(subPageElement.attributeValue("type")); //子页面类型
				if(subPage.getType()==null) {
					subPage.setType("jsp"); //默认jsp
				}
				subPage.setIframeUrl(subPageElement.attributeValue("iframeUrl")); //iframe指向的链接
				subPage.setNormalCssFile(subPageElement.attributeValue("normalCssFile")); //默认引用的CSS,如果没有指定,则默认为/cms/css/application.css
				subPages.add(subPage);
			}
			sitePage.setSubPages(subPages);
			
			//解析专有链接列表
			List links = new ArrayList();
			for(Iterator iteratorLink = element.elementIterator("link"); iteratorLink.hasNext();) {
				Element linkElement = (Element)iteratorLink.next();
				links.add(parseSiteLink(linkElement));
			}
			sitePage.setLinks(links);
			
			//解析模板插件
			sitePage.setTemplatePlugin(parseSiteTemplatePlugin(element.element("templatePlugin")));
			pages.add(sitePage);
			
			//扩展参数列表,用来增加应用程序需要的其他参数
			Element extendedParametersElement = element.element("extendedParameters");
			if(extendedParametersElement!=null) {
				for(Iterator iterator = extendedParametersElement.elementIterator("parameter"); iterator.hasNext();) {
					Element xmlParameter = (Element)iterator.next();
					sitePage.setExtendedParameter(xmlParameter.attributeValue("name"), xmlParameter.getText());
				}
			}
		}
		return pages;
	}
	
	/**
	 * 解析站点链接列表
	 * @param recordListsElement
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	private List parseSiteLinks(Element linksElement) throws ParseException {
		if(linksElement==null) {
			return null;
		}
		List links = new ArrayList();
		for(Iterator iterator = linksElement.elementIterator("link"); iterator.hasNext();) {
			Element element = (Element)iterator.next();
			links.add(parseSiteLink(element));
		}
		return links;
	}
	
	/**
	 * 解析链接
	 * @param linkElement
	 * @return
	 * @throws ServiceException
	 */
	private SiteLink parseSiteLink(Element linkElement) throws ParseException {
		SiteLink link = new SiteLink();
		link.setTitle(linkElement.attributeValue("title")); //标题
		link.setUrl(linkElement.attributeValue("url")); //URL
		link.setDialogURL(linkElement.attributeValue("dialogURL")); //配置时使用的对话框
		return link;
	}
	
	/**
	 * 解析模板视图
	 * @param templateViewElement
	 * @return
	 * @throws ServiceException
	 */
	private SiteTemplateView parseSiteTemplateView(Element templateViewElement) throws ParseException {
		if(templateViewElement==null) {
			return null;
		}
		SiteTemplateView siteTemplateView = new SiteTemplateView();
		siteTemplateView.setViewApplication(templateViewElement.attributeValue("application")); //应用名称
		siteTemplateView.setViewName(templateViewElement.attributeValue("name")); //应用名称
		return siteTemplateView;
	}
	
	/**
	 * 解析插件列表
	 * @param pluginsElement
	 * @return
	 * @throws ServiceException
	 */
	private List parseSiteTemplatePlugins(Element pluginsElement) throws ParseException {
		if(pluginsElement==null) {
			return null;
		}
		List plugins = new ArrayList();
		for(Iterator iterator = pluginsElement.elementIterator("templatePlugin"); iterator.hasNext();) {
			Element templatePluginElement = (Element)iterator.next();
			plugins.add(parseSiteTemplatePlugin(templatePluginElement));
		}
		return plugins;
	}
	
	/**
	 * 解析模板插件
	 * @param templatePluginElement
	 * @return
	 * @throws ServiceException
	 */
	private SiteTemplatePlugin parseSiteTemplatePlugin(Element templatePluginElement) throws ParseException {
		if(templatePluginElement==null) {
			return null;
		}
		SiteTemplatePlugin siteTemplatePlugin = new SiteTemplatePlugin();
		siteTemplatePlugin.setPath(templatePluginElement.attributeValue("path")); //插件路径
		siteTemplatePlugin.setPrivatePlugin("true".equals(templatePluginElement.attributeValue("private"))); //是否私有
		//插件包含的模板操作列表
		Element actionsElement = templatePluginElement.element("templateActions");
		if(actionsElement!=null) {
			siteTemplatePlugin.setTemplateActions(actionsElement.getText());
		}
		//插件包含的页面元素操作列表
		actionsElement = templatePluginElement.element("pageElementActions");
		if(actionsElement!=null) {
			siteTemplatePlugin.setPageElementActions(actionsElement.getText());
		}
		return siteTemplatePlugin;
	}
	
	/**
	 * 解析页面元素列表
	 * @param pageElementElement
	 * @return
	 * @throws ServiceException
	 */
	private List parseSitePageElements(Element pageElementsElement) throws ParseException {
		if(pageElementsElement==null) {
			return null;
		}
		List pageElements = new ArrayList();
		for(Iterator iterator = pageElementsElement.elementIterator("pageElement"); iterator.hasNext();) {
			Element pageElementElement = (Element)iterator.next();
			SitePageElement sitePageElement = new SitePageElement();
			sitePageElement.setName(pageElementElement.attributeValue("name"));
			sitePageElement.setProcessor(pageElementElement.attributeValue("processor"));
			sitePageElement.setStaticPageSupport(!"false".equals(pageElementElement.attributeValue("staticPageSupport")));
			pageElements.add(sitePageElement);
		}
		return pageElements;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.parser.PageDefineParser#savePageDefine(com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication, java.lang.String)
	 */
	public void savePageDefine(SiteApplication siteApplication, String defineFilePath) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlApplication = xmlDocument.addElement("application");
		//页面列表
		generateSitePagesXML(siteApplication.getPages(), xmlApplication);
		//链接列表
		generateSiteLinksXML(siteApplication.getLinks(), xmlApplication);
		//站点模板视图配置
		generateSiteTemplateViewXML(siteApplication.getTemplateView(), xmlApplication);
		//插件列表
		generateSiteTemplatePluginsXML(siteApplication.getTemplatePlugins(), xmlApplication);
		//本应用增加的页面元素列表
		generateSitePageElementsXML(siteApplication.getPageElements(), xmlApplication);
		//保存XML文件
		saveXmlFile(xmlDocument, defineFilePath);
	}
	
	/**
	 * 生成页面XML
	 * @param sitePage
	 * @param xmlPage
	 * @throws ParseException
	 */
	private void generateSitePagesXML(List sitePages, Element xmlParent) throws ParseException {
		if(sitePages==null || sitePages.isEmpty()) {
			return;
		}
		Element xmlPages = xmlParent.addElement("pages");
		for(Iterator iterator = sitePages.iterator(); iterator.hasNext();) {
			SitePage sitePage = (SitePage)iterator.next();
			Element xmlPage = xmlPages.addElement("page");
			xmlPage.addAttribute("name", sitePage.getName()); //名称
			xmlPage.addAttribute("title", sitePage.getTitle()); //标题
			xmlPage.addAttribute("url", sitePage.getUrl()); //链接
			if(sitePage.isSystemLink()) {
				xmlPage.addAttribute("systemLink", "true");
			}
			xmlPage.addAttribute("js", sitePage.getJs()); //js引用
			generateSiteTemplateViewXML(sitePage.getTemplateView(), xmlPage); //模板视图
			if(sitePage.isSearchPage()) {
				xmlPage.addAttribute("searchPage", "true"); //是否搜索模板
			}
			xmlPage.addAttribute("searchResults", sitePage.getSearchResults()); //搜索结果使用的记录列表
			xmlPage.addAttribute("parentPage", sitePage.getParentPage()); //搜索父页面
			xmlPage.addAttribute("recordClass", sitePage.getRecordClassName()); //记录类名称
			if(sitePage.isPagingSupport()) {
				xmlPage.addAttribute("pagingSupport", "true"); //是否支持上一篇/下一篇
			}
			if(sitePage.isInternal()) {
				xmlPage.addAttribute("internal", "true"); //是否后台页面
			}
			if(sitePage.getRecordClassName()!=null) { //记录页面
				if(!sitePage.isRecordPage()) {
					xmlPage.addAttribute("recordPage", "false"); //是否记录页面,如果配置了recordClassName，默认true
				}
				if(sitePage.isRealtimeStaticPage()) {
					xmlPage.addAttribute("realtimeStaticPage", "true"); //是否需要实时生成静态页面,非记录页面（如：首页）默认true,记录页面（如：文章、政府信息）默认false
				}
			}
			else { 
				if(sitePage.isRecordPage()) {
					xmlPage.addAttribute("recordPage", "true"); //是否记录页面,如果配置了recordClassName，默认true
				}
				if(!sitePage.isRealtimeStaticPage()) {
					xmlPage.addAttribute("realtimeStaticPage", "false"); //是否需要实时生成静态页面,非记录页面（如：首页）默认true,记录页面（如：文章、政府信息）默认false
				}
			}
			if(!sitePage.isStaticPageSupport()) {
				xmlPage.addAttribute("staticPageSupport", "false"); //是否支持静态页面
			}
			
			//子页面列表
			for(Iterator iteratorSubPage = sitePage.getSubPages()==null ? null : sitePage.getSubPages().iterator(); iteratorSubPage!=null && iteratorSubPage.hasNext();) {
				SiteSubPage subPage = (SiteSubPage)iteratorSubPage.next();
				Element xmlSubPage = xmlPage.addElement("subPage");
				xmlSubPage.addAttribute("name", subPage.getName()); //名称
				xmlSubPage.addAttribute("title", subPage.getTitle()); //标题
				xmlSubPage.addAttribute("type", subPage.getType()); //子页面类型
				xmlSubPage.addAttribute("iframeUrl", subPage.getIframeUrl()); //iframe指向的链接
				xmlSubPage.addAttribute("normalCssFile", subPage.getNormalCssFile()); //默认引用的CSS,如果没有指定,则默认为/cms/css/application.css
			}
			
			//专有链接列表
			for(Iterator iteratorLink = sitePage.getLinks()==null ? null : sitePage.getLinks().iterator(); iteratorLink!=null && iteratorLink.hasNext();) {
				SiteLink siteLink = (SiteLink)iteratorLink.next();
				generateSiteLinkXML(siteLink, xmlPage);
			}
			
			//模板插件
			generateSiteTemplatePluginXML(sitePage.getTemplatePlugin(), xmlPage);
			
			//扩展参数列表,用来增加应用程序需要的其他参数
			if(sitePage.getExtendedParameters()!=null && !sitePage.getExtendedParameters().isEmpty()) {
				Element xmlExtendedParameters = xmlPage.addElement("extendedParameters");
				for(Iterator iteratorParameter = sitePage.getExtendedParameters().keySet().iterator(); iteratorParameter.hasNext();) {
					String parameterName = (String)iteratorParameter.next();
					Element xmlParameter = xmlExtendedParameters.addElement("parameter");
					xmlParameter.addAttribute("name", parameterName);
					xmlParameter.setText((String)sitePage.getExtendedParameters().get(parameterName));
				}
			}
		}
	}
	
	/**
	 * 生成链接列表XML
	 * @param siteLinks
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSiteLinksXML(List siteLinks, Element xmlParent) throws ParseException {
		if(siteLinks==null || siteLinks.isEmpty()) {
			return;
		}
		Element xmlLinks = xmlParent.addElement("links");
		for(Iterator iterator = siteLinks.iterator(); iterator.hasNext();) {
			SiteLink link = (SiteLink)iterator.next();
			generateSiteLinkXML(link, xmlLinks);
		}
	}
	
	/**
	 * 生成模板插件列表XML
	 * @param templatePlugins
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSiteTemplatePluginsXML(List templatePlugins, Element xmlParent) throws ParseException {
		if(templatePlugins==null || templatePlugins.isEmpty()) {
			return;
		}
		Element xmlPlugins = xmlParent.addElement("templatePlugins");
		for(Iterator iterator = templatePlugins.iterator(); iterator.hasNext();) {
			SiteTemplatePlugin plugin = (SiteTemplatePlugin)iterator.next();
			generateSiteTemplatePluginXML(plugin, xmlPlugins);
		}
	}
	
	/**
	 * 生成页面元素列表XML
	 * @param pageElements
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSitePageElementsXML(List pageElements, Element xmlParent) throws ParseException {
		if(pageElements==null || pageElements.isEmpty()) {
			return;
		}
		Element xmlPageElements = xmlParent.addElement("pageElements");
		for(Iterator iterator = pageElements.iterator(); iterator.hasNext();) {
			SitePageElement sitePageElement = (SitePageElement)iterator.next();
			Element xmlPageElement = xmlPageElements.addElement("pageElement");
			xmlPageElement.addAttribute("name", sitePageElement.getName());
			xmlPageElement.addAttribute("processor", sitePageElement.getProcessor());
			if(!sitePageElement.isStaticPageSupport()) {
				xmlPageElement.addAttribute("staticPageSupport", "false");
			}
		}
	}
	
	/**
	 * 生成链接XML
	 * @param siteLink
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSiteLinkXML(SiteLink siteLink, Element xmlParent) throws ParseException {
		if(siteLink==null) {
			return;
		}
		Element xmlLink = xmlParent.addElement("link");
		xmlLink.addAttribute("title", siteLink.getTitle()); //标题
		xmlLink.addAttribute("url", siteLink.getUrl()); //URL
		xmlLink.addAttribute("dialogURL", siteLink.getDialogURL()); //配置时使用的对话框
	}
	
	/**
	 * 生成模板视图XML
	 * @param siteTemplateView
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSiteTemplateViewXML(SiteTemplateView siteTemplateView, Element xmlParent) throws ParseException {
		if(siteTemplateView==null) {
			return;
		}
		Element xmlTemplateView = xmlParent.addElement("templateView");
		xmlTemplateView.addAttribute("application", siteTemplateView.getViewApplication()); //应用名称
		xmlTemplateView.addAttribute("name", siteTemplateView.getViewName()); //应用名称
	}
	
	/**
	 * 生成模板插件XML
	 * @param siteTemplatePlugin
	 * @param xmlParent
	 * @throws ParseException
	 */
	private void generateSiteTemplatePluginXML(SiteTemplatePlugin siteTemplatePlugin, Element xmlParent) throws ParseException {
		if(siteTemplatePlugin==null) {
			return;
		}
		Element xmlSiteTemplatePlugin = xmlParent.addElement("templatePlugin");
		xmlSiteTemplatePlugin.addAttribute("path", siteTemplatePlugin.getPath()); //插件路径
		if(siteTemplatePlugin.isPrivatePlugin()) {
			xmlSiteTemplatePlugin.addAttribute("private", "true"); //是否私有
		}
		//插件包含的模板操作列表
		if(siteTemplatePlugin.getTemplateActions()!=null && !siteTemplatePlugin.getTemplateActions().isEmpty()) {
			xmlSiteTemplatePlugin.addElement("templateActions").setText(siteTemplatePlugin.getTemplateActions());
		}
		//插件包含的页面元素操作列表
		if(siteTemplatePlugin.getPageElementActions()!=null && !siteTemplatePlugin.getPageElementActions().isEmpty()) {
			xmlSiteTemplatePlugin.addElement("pageElementActions").setText(siteTemplatePlugin.getPageElementActions());
		}
	}
}