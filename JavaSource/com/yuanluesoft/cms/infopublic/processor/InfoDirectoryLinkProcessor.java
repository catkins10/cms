package com.yuanluesoft.cms.infopublic.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.infopublic.pojo.PublicArticleDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageElement;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面元素处理器：信息公开目录链接
 * @author linchuan
 *
 */
public class InfoDirectoryLinkProcessor implements PageElementProcessor {
	public PublicDirectoryService publicDirectoryService; //信息公开目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//解析站点链接
		String urn = pageElement.getAttribute("urn");
		//解析目录
		PublicDirectory directory;
		long siteId;
		if(!"true".equals(StringUtils.getPropertyValue(urn, "linkByName"))) { //不是按名称链接
			directory = (PublicDirectory)publicDirectoryService.getDirectory(StringUtils.getPropertyLongValue(urn, "directoryId", -1));
			siteId = parentSite.getId();
		}
		else {
			String directoryName = StringUtils.getPropertyValue(urn, "directoryName");
			//获取站点ID
			siteId = StringUtils.getPropertyLongValue(urn, "siteId", -1);
			if(siteId==-1) {
				siteId = parentSite.getId();
			}
			//获取主目录
			PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectoryBySite(siteId);
			//获取目录
			directory = (PublicDirectory)publicDirectoryService.getPublicDirectoryByName(mainDirectory.getId(), directoryName);
		}
		if(directory==null) { //没有找到目录
			pageElement.getParentNode().removeChild(pageElement);
			return;
		}
		String url;
		if(directory instanceof PublicArticleDirectory) { //文章目录
			PublicArticleDirectory articleDirectory = (PublicArticleDirectory)directory;
			if(articleDirectory.getRedirectUrl()==null || articleDirectory.getRedirectUrl().isEmpty()) { //没有重定向
				url = "/cms/infopublic/articleDirectory.shtml?directoryId=" + directory.getId();
			}
			else {
				url = "{FINAL}" + articleDirectory.getRedirectUrl();
			}
		}
		else {
			url = "/cms/infopublic/index.shtml?directoryId=" + directory.getId();
		}
		//解析打开方式
		String openMode = StringUtils.getPropertyValue(urn, "openMode");
		//删除配置属性
		pageElement.removeAttribute("id");
		pageElement.removeAttribute("urn");
		//输出链接
		LinkUtils.writeLink(anchorElement, url, openMode, parentSite.getId(), siteId, null, false, true, sitePage, request);
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
		String urn = pageElement.getAttribute("urn");
		long siteId = parentSite.getId();
		String elementName = null;
		if("true".equals(StringUtils.getPropertyValue(urn, "linkByName"))) { //按名称链接 
			siteId = StringUtils.getPropertyLongValue(urn, "siteId", -1);
			if(siteId==-1) {
				siteId = parentSite.getId();
			}
			elementName = "infoDirectoryLink_" + StringUtils.getPropertyValue(urn, "directoryName");
		}
		else { //不是按名称链接 
			PublicDirectory directory = (PublicDirectory)publicDirectoryService.getDirectory(StringUtils.getPropertyLongValue(urn, "directoryId", -1));
			if(directory instanceof PublicArticleDirectory) { //文章目录
				elementName = "infoDirectoryLink_" + directory.getId();
			}
		}
		if(elementName!=null) {
			//检查是否已经创建过
			String hql = "select StaticPageElement.id" +
						 " from StaticPageElement StaticPageElement" +
						 " where StaticPageElement.pageId=" + staticPageId +
						 " and StaticPageElement.elementName='" + JdbcUtils.resetQuot(elementName) + "'" +
						 " and StaticPageElement.siteId=" + siteId;
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的页面元素
				StaticPageElement staticPageElement = new StaticPageElement();
				staticPageElement.setId(UUIDLongGenerator.generateId()); //页面ID
				staticPageElement.setPageId(staticPageId); //页面ID
				staticPageElement.setElementName(elementName); //页面元素名称
				staticPageElement.setSiteId(siteId); //隶属的站点/栏目ID
				databaseService.saveRecord(staticPageElement);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof PublicDirectory)) {
			return null;
		}
		PublicDirectory directory = (PublicDirectory)object;
		//获取主目录
		PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectory(directory.getId());
		String hql = "select distinct StaticPage" +
		  			 " from StaticPage StaticPage, StaticPageElement StaticPageElement" +
		  			 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
		  			 " and StaticPageElement.pageId=StaticPage.id" +
		  			 " and StaticPageElement.elementName in ('infoDirectoryLink_" + directory.getId() + "', 'infoDirectoryLink_" + directory.getDirectoryName() + "')" +
		  			 " and StaticPageElement.siteId=" + mainDirectory.getSiteId();
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/**
	 * @return the publicDirectoryService
	 */
	public PublicDirectoryService getPublicDirectoryService() {
		return publicDirectoryService;
	}

	/**
	 * @param publicDirectoryService the publicDirectoryService to set
	 */
	public void setPublicDirectoryService(
			PublicDirectoryService publicDirectoryService) {
		this.publicDirectoryService = publicDirectoryService;
	}
}