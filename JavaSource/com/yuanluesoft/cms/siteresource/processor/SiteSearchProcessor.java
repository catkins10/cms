package com.yuanluesoft.cms.siteresource.processor;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 站点搜索处理器,保持和旧系统兼容
 * @author linchuan
 *
 */
public class SiteSearchProcessor implements PageElementProcessor {
	private SiteService siteService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String elementName = pageElement.getAttribute("name");
		if(pageElement.getTitle()==null || pageElement.getTitle().equals("")) {
			return;
		}
		String siteId = pageElement.getTitle().split("\\x7c")[1];
		if("-1".equals(siteId)) {
			siteId = (webDirectory==null ? "0" : webDirectory.getId() + "");
		}
		pageElement.setTitle(null);
		if("searchKey".equals(elementName)) { //处理搜索条件
			pageElement.setAttribute("name", "searchKey_" + siteId);
			String key = request.getParameter("key");
			if(key==null) {
				String value = pageElement.getAttribute("value");
				if(value!=null && !value.equals("")) {
					pageElement.setAttribute("onfocus", "if(value=='" + value + "')value=''");
				}
			}
			else {
				pageElement.setAttribute("value",key);
			}
		}
		else if("searchColumn".equals(elementName)) { //处理栏目列表
			pageElement.setAttribute("name", "searchColumn_" + siteId);
			//设置栏目列表
			HTMLSelectElement selectElement = (HTMLSelectElement)pageElement;
			HTMLCollection options = selectElement.getOptions();
			if(options.getLength()>0) {
				//获取第一个选项
				HTMLOptionElement optionElement = (HTMLOptionElement)options.item(0);
				optionElement.setValue(siteId);
				optionElement.setAttribute("selected", "false");
				//删除其他选项
				for(int j=options.getLength()-1; j>0; j--) {
					selectElement.remove(j);
				}
			}
			else { //增加站点本身
				HTMLOptionElement optionElement = (HTMLOptionElement)selectElement.getOwnerDocument().createElement("OPTION");
				if(webDirectory!=null && siteId.equals(webDirectory.getId() + "")) {
					htmlParser.setTextContent(optionElement, webDirectory.getDirectoryName());
				}
				else {
					htmlParser.setTextContent(optionElement, siteService.getDirectory(Long.parseLong(siteId)).getDirectoryName());
				}
				optionElement.setValue(siteId);
				selectElement.add(optionElement, null);
			}
			String value = request.getParameter("searchSiteId");
			long searchSiteId = (value==null ? 0 : Long.parseLong(value));
			//获取下级栏目列表
			List childColumns = siteService.listChildDirectories(Long.parseLong(siteId), null, null, null, false, false, null, 0, 0);
			//增加选项
			if(childColumns!=null && !childColumns.isEmpty()) {
				for(Iterator iterator = childColumns.iterator(); iterator.hasNext();) {
					WebDirectory childColumn = (WebDirectory)iterator.next();
					HTMLOptionElement optionElement = (HTMLOptionElement)selectElement.getOwnerDocument().createElement("OPTION");
					htmlParser.setTextContent(optionElement, childColumn.getDirectoryName());
					optionElement.setValue(childColumn.getId() + "");
					if(childColumn.getId()==searchSiteId) {
						optionElement.setAttribute("selected", "true");
					}
					selectElement.add(optionElement, null);
				}
			}
		}
		else if("searchButton".equals(elementName)) { //处理栏目列表
			htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), "/jeaf/common/js/common.js");
			String action = "if(document.getElementsByName('searchKey_" + siteId + "')[0].value!='')location.href='" + Environment.getContextPath() + "/cms/sitemanage/search.shtml" +
							"?siteId=" + siteId +
							"&pageName=quickSiteSearch" +
							"&searchSite=' + (document.getElementsByName('searchColumn_" + siteId + "')[0] ? document.getElementsByName('searchColumn_" + siteId + "')[0].options[document.getElementsByName('searchColumn_" + siteId + "')[0].selectedIndex].value : " + siteId + ")+" +
							"'&key=' + StringUtils.utf8Encode(document.getElementsByName('searchKey_" + siteId + "')[0].value)";
			pageElement.setAttribute("onclick", action);
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