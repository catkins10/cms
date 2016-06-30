package com.yuanluesoft.cms.infopublic.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class InfoDirectoryLocationProcessor implements PageElementProcessor {
	private PublicDirectoryService publicDirectoryService; //信息公开目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//目录ID
		Object value = sitePage.getAttribute("directoryId");
		long directoryId = value==null ? 0 : ((Long)value).longValue();
		String urn = pageElement.getAttribute("urn");

		pageElement.removeAttribute("href");
		//获取目录的完整路径
		String directoryName = publicDirectoryService.getDirectoryFullName(directoryId, "/", "main");
		String[] directoryNameNames = directoryName.split("/");
		if("true".equals(StringUtils.getPropertyValue(urn, "selfOnly"))) { //仅当前目录
			htmlParser.setTextContent(pageElement, directoryNameNames[directoryNameNames.length-1]);
		}
		else {
			htmlParser.setTextContent(pageElement, null);
			//解析分隔符
			String separator = StringUtils.getPropertyValue(urn, "separator");
			HTMLDocument document = htmlParser.parseHTMLString("<div id=\"separator\">" + separator + "</div>", "utf-8");
			NodeList separatorNodes = document.getElementById("separator").getChildNodes();
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
			
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
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
