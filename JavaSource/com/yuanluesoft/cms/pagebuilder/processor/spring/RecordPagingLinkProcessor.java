package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 上一篇/下一篇链接处理器
 * @author linchuan
 *
 */
public class RecordPagingLinkProcessor  implements PageElementProcessor { 
	private PageBuilder pageBuilder; //页面生成器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement a = (HTMLAnchorElement)pageElement;
		//获取上一个/下一个记录
		Object record = sitePage.getAttribute(a.getId().equals("nextRecordLink") ? PageService.PAGE_ATTRIBUTE_NEXT_RECORD : PageService.PAGE_ATTRIBUTE_PREVIOUS_RECORD);
		if(record==null) { //没有上一个或下一个记录
			a.getParentNode().removeChild(a);
			return;
		}
		
		//输出链接
		htmlParser.setTextContent(a, null); //清除原来的元素
		htmlParser.importNodes(htmlParser.parseHTMLString(a.getAttribute("urn"), "utf-8").getBody().getChildNodes(), a, false); //引入显示格式配置的内容
		//删除配置属性
		a.removeAttribute("id");
		a.removeAttribute("urn");
		//输出字段
		try {
			sitePage = (SitePage)sitePage.clone();
		}
		catch (CloneNotSupportedException e) {
			
		}
		sitePage.setAttribute(PageService.PAGE_ATTRIBUTE_RECORD, record);
		pageBuilder.processPageElement(a, webDirectory, parentSite, sitePage, requestInfo, request); 
		
		//输出链接
		LinkUtils.writeLink(a, sitePage.getUrl(), a.getTarget(), webDirectory.getId(), parentSite.getId(), record, true, true, sitePage, request);
		
		//处理内置的链接,如果没有指定href和onclick,则重设链接
		NodeList nodes = a.getElementsByTagName("a");
		for(int i=0; i<(nodes==null ? 0 : nodes.getLength()); i++) {
			HTMLAnchorElement link = (HTMLAnchorElement)nodes.item(i);
			String onclick = link.getAttribute("onclick");
			if((link.getHref()==null || link.getHref().isEmpty() || "#".equals(link.getHref())) && (onclick==null || onclick.isEmpty())) {
				link.setHref(a.getHref());
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
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}
}