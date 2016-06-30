package com.yuanluesoft.cms.onlineservice.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.StaticPageOnlineServiceLocation;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceLocationProcessor implements PageElementProcessor {
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//目录ID
		Object value = sitePage.getAttribute("directoryId");
		long directoryId = value==null ? RequestUtils.getParameterLongValue(request, "id") : ((Long)value).longValue();
		//解析站点位置
		String urn = pageElement.getAttribute("urn");
		//解析分隔符
		String separator = StringUtils.getPropertyValue(urn, "separator");

		HTMLDocument document = htmlParser.parseHTMLString("<div id=\"separator\">" + separator + "</div>", "utf-8");
		NodeList separatorNodes = document.getElementById("separator").getChildNodes();
		pageElement.removeAttribute("href");
		htmlParser.setTextContent(pageElement, null);
		
		//获取目录的完整路径
		String directoryName = onlineServiceDirectoryService.getDirectoryFullName(directoryId, "/", "mainDirectory");
		if(directoryName==null || directoryName.isEmpty()) {
			pageElement.getParentNode().removeChild(pageElement);
			return;
		}
		String[] directoryNameNames = directoryName.split("/");
		for(int i=0; i<directoryNameNames.length; i++) {
			pageElement.appendChild(pageElement.getOwnerDocument().createTextNode(directoryNameNames[i]));
			//插入分隔符
			if(separatorNodes!=null && i<directoryNameNames.length-1) {
				for(int j=0; j<separatorNodes.getLength(); j++) {
					pageElement.appendChild(pageElement.getOwnerDocument().importNode(separatorNodes.item(j), true));
				}
			}
		}
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
		//目录ID
		Object value = sitePage.getAttribute("directoryId");
		long directoryId = value==null ? RequestUtils.getParameterLongValue(request, "id") : ((Long)value).longValue();
		//检查是否已经创建过
		String hql = "select StaticPageOnlineServiceLocation.id" +
					 " from StaticPageOnlineServiceLocation StaticPageOnlineServiceLocation" +
					 " where StaticPageOnlineServiceLocation.pageId=" + staticPageId +
					 " and StaticPageOnlineServiceLocation.elementName='" + JdbcUtils.resetQuot(pageElement.getId()) + "'" +
					 " and StaticPageOnlineServiceLocation.directoryId=" + directoryId;
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的页面元素
			StaticPageOnlineServiceLocation staticPageOnlineServiceLocation = new StaticPageOnlineServiceLocation();
			staticPageOnlineServiceLocation.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageOnlineServiceLocation.setPageId(staticPageId); //页面ID
			staticPageOnlineServiceLocation.setElementName(pageElement.getId()); //页面元素名称
			staticPageOnlineServiceLocation.setDirectoryId(directoryId); //目录ID
			databaseService.saveRecord(staticPageOnlineServiceLocation);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof OnlineServiceDirectory) || !StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE.equals(modifyAction)) {
			return null;
		}
		OnlineServiceDirectory directory = (OnlineServiceDirectory)object;
		//获取第一级子目录
		String hql = "select OnlineServiceDirectory.id" +
					 " from OnlineServiceDirectory OnlineServiceDirectory" +
					 " where OnlineServiceDirectory.parentDirectoryId=" + directory.getId() +
					 " and OnlineServiceDirectory.directoryType='directory'";
		String childDirectoryIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
		//获取配置了目录位置的页面列表
		hql = "select distinct StaticPage" +
			  " from StaticPage StaticPage, StaticPageOnlineServiceLocation StaticPageOnlineServiceLocation" +
			  " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
			  " and StaticPageOnlineServiceLocation.pageId=StaticPage.id" +
			  " and StaticPageOnlineServiceLocation.elementName='siteLocation'" +
			  " and (StaticPageOnlineServiceLocation.directoryId=" + directory.getId() + //目录就是当前目录
			  (childDirectoryIds==null ? "" : " or StaticPageOnlineServiceLocation.directoryId in (select OnlineServiceDirectorySubjection.directoryId from OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection where OnlineServiceDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(childDirectoryIds) + "))") + //其他子目录
			  ")";
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/**
	 * @return the onlineServiceDirectoryService
	 */
	public OnlineServiceDirectoryService getOnlineServiceDirectoryService() {
		return onlineServiceDirectoryService;
	}

	/**
	 * @param onlineServiceDirectoryService the onlineServiceDirectoryService to set
	 */
	public void setOnlineServiceDirectoryService(
			OnlineServiceDirectoryService onlineServiceDirectoryService) {
		this.onlineServiceDirectoryService = onlineServiceDirectoryService;
	}
}