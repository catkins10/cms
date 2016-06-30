package com.yuanluesoft.cms.sitemanage.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageElement;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面元素处理器：站点位置
 * @author linchuan
 *
 */
public class SiteLocationProcessor implements PageElementProcessor {
	private SiteService siteService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析站点位置
		String urn = pageElement.getAttribute("urn");
		//解析是否只显示自己
		boolean selfOnly = "true".equals(StringUtils.getPropertyValue(urn, "selfOnly"));
		//解析分隔符
		String separator = StringUtils.getPropertyValue(urn, "separator");
		
		HTMLDocument document = htmlParser.parseHTMLString("<div id=\"separator\">" + separator + "</div>", "utf-8");
		NodeList separatorNodes = document.getElementById("separator").getChildNodes();
		Object record = sitePage.getAttribute("record");
		if(record!=null && record instanceof SiteResource) { //文章页面
			SiteResource siteResource = (SiteResource)record;
			long resourceColumnId = -1;
			for(Iterator iterator = siteResource.getSubjections().iterator(); iterator.hasNext();) {
				SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
				if(siteService.getParentSite(subjection.getSiteId()).getId()==parentSite.getId()) { //检查文章所在栏目是否属于当前站点
					resourceColumnId = subjection.getSiteId();
					break;
				}
			}
			if(resourceColumnId!=-1) {
				webDirectory = (WebDirectory)siteService.getDirectory(resourceColumnId);
			}
		}
		
		List webDirectories = null;
		if(!selfOnly && webDirectory.getId()!=parentSite.getId()) { //不只显示本站点/栏目
			webDirectories = getSiteService().listParentDirectories(webDirectory.getId(), "site");
		}
		if(webDirectories==null) {
			webDirectories = new ArrayList();
		}
		webDirectories.add(webDirectory);
		if("cms/sitemanage".equals(sitePage.getApplicationName()) && 
		   ("index".equals(sitePage.getName()) || "leafColumn".equals(sitePage.getName()) || "branchColumn".equals(sitePage.getName()))) { //当前页面是站点或栏目首页
			String columnName = request==null ? null : request.getParameter("columnName");
			if(columnName!=null) { //按栏目名称
				WebDirectory directory = new WebDirectory();
				directory.setId(-1);
				directory.setDirectoryName(columnName);
				if(selfOnly) {
					webDirectories.set(0, directory);
				}
				else {
					webDirectories.add(directory);
				}
			}
		}
		boolean rssSubscribe = request.getRequestURI().indexOf("rssSubscribe")!=-1; //是否RSS订阅页面
		for(Iterator iterator = webDirectories.iterator(); iterator.hasNext();) {
			webDirectory = (WebDirectory)iterator.next();
			if(!iterator.hasNext() && //输出当前站点或栏目
			   "cms/sitemanage".equals(sitePage.getApplicationName()) && 
			   ("index".equals(sitePage.getName()) || "leafColumn".equals(sitePage.getName()) || "branchColumn".equals(sitePage.getName()))) {
				//当前页面是站点或栏目首页,只插入文本,不显示链接
				pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().createTextNode(webDirectory.getDirectoryName()), pageElement);
				break;
			}
			if(webDirectory.getHalt()=='1') { //已经停用
				continue;
			}
			HTMLAnchorElement siteAnchor = (HTMLAnchorElement)pageElement.getOwnerDocument().createElement("A");
			//设置名称
			htmlParser.setTextContent(siteAnchor, webDirectory.getDirectoryName());
			//设置链接
			if(rssSubscribe) { //RSS订阅
				siteAnchor.setHref(Environment.getContextPath() + "/cms/rsssubscription/rssSubscribe.shtml" + (webDirectory.getId()==0 ? "" : "?siteId=" + webDirectory.getId()));
			}
			else if(webDirectory.getRedirectUrl()!=null && !webDirectory.getRedirectUrl().isEmpty()) { //重定向
				siteAnchor.setHref(webDirectory.getRedirectUrl());
			}
			else {
				siteAnchor.setHref(Environment.getContextPath() + "/cms/sitemanage/index.shtml" + (webDirectory.getId()==0 ? "" : "?siteId=" + webDirectory.getId()));
			}
			pageElement.getParentNode().insertBefore(siteAnchor, pageElement);
			//插入分隔符
			if(iterator.hasNext() && separatorNodes!=null) {
				for(int i=0; i<separatorNodes.getLength(); i++) {
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().importNode(separatorNodes.item(i), true), pageElement);
				}
			}
		}
		//删除配置元素
		pageElement.getParentNode().removeChild(pageElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		if(!sitePage.isRealtimeStaticPage()) { //不需要实时生成静态页面,避免过多的重建静态页面
			return;
		}
		long siteId =  webDirectory.getId(); 
		//检查是否已经创建过
		String hql = "select StaticPageElement.id" +
					 " from StaticPageElement StaticPageElement" +
					 " where StaticPageElement.pageId=" + staticPageId +
					 " and StaticPageElement.elementName='" + JdbcUtils.resetQuot(pageElement.getId()) + "'" +
					 " and StaticPageElement.siteId=" + siteId;
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的页面元素
			StaticPageElement staticPageElement = new StaticPageElement();
			staticPageElement.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageElement.setPageId(staticPageId); //页面ID
			staticPageElement.setElementName(pageElement.getId()); //页面元素名称
			staticPageElement.setSiteId(siteId); //隶属的站点/栏目ID
			databaseService.saveRecord(staticPageElement);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof WebDirectory) || !StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE.equals(modifyAction)) {
			return null;
		}
		WebDirectory webDirectory = (WebDirectory)object;
		//获取第一级子栏目
		String hql = "select WebDirectory.id" +
					 " from WebDirectory WebDirectory" +
					 " where WebDirectory.parentDirectoryId=" + webDirectory.getId() +
					 " and WebDirectory.directoryType='column'";
		String childColumnIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
		//获取配置了栏目位置的页面列表
		hql = "select distinct StaticPage" +
			  " from StaticPage StaticPage, StaticPageElement StaticPageElement" +
			  " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
			  " and StaticPageElement.pageId=StaticPage.id" +
			  " and StaticPageElement.elementName='siteLocation'" +
			  " and (StaticPageElement.siteId=" + webDirectory.getId() + //栏目就是当前栏目
			  (childColumnIds==null ? "" : " or StaticPageElement.siteId in (select WebDirectorySubjection.directoryId from WebDirectorySubjection WebDirectorySubjection where WebDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(childColumnIds) + "))") + //其他子栏目
			  ")";
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}