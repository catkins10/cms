package com.yuanluesoft.cms.comment.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.comment.pojo.CmsComment;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 评论引用页面服务
 * @author linchuan
 *
 */
public class CitationCommentPageService extends PageService {
	private DatabaseService databaseService;
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		//获取评论列表
		HTMLElement commentListElement = null;
		NodeList nodes = template.getElementsByTagName("a");
		for(int i=0; i<nodes.getLength(); i++) {
			HTMLElement a = (HTMLElement)nodes.item(i);
			if("recordList".equals(a.getId()) && "comments".equals(StringUtils.getPropertyValue(a.getAttribute("urn"), "recordListName"))) {
				commentListElement = a;
				break;
			}
		}
		//获取引用时的记录格式
		String properties = commentListElement.getAttribute("urn");
		String format = StringUtils.getPropertyValue(properties, "extendProperties");
		if(format!=null) {
			format = StringUtils.getPropertyValue(format, "citation");
		}
		if(format==null) {
			format = StringUtils.getPropertyValue(properties, "recordFormat");
		}
		HTMLDocument citationDocument = htmlParser.parseHTMLString(format, "utf-8");
		//重设BODY
		Node parentNode = template.getBody().getParentNode();
		parentNode.removeChild(template.getBody());
		parentNode.appendChild(template.importNode(citationDocument.getBody(), true));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getSitePage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	protected SitePage getSitePage(String applicationName, String pageName, HttpServletRequest request) throws ServiceException {
		SitePage recordPage = new SitePage();
		recordPage.setName("comments");
		recordPage.setApplicationName(applicationName);
		return recordPage;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		CmsComment comment = (CmsComment)databaseService.findRecordById(CmsComment.class.getName(), RequestUtils.getParameterLongValue(request, "commentId"), ListUtils.generateList("lazyBody"));
		if(comment==null || comment.getPublicPass()!='1') {
			throw new PageNotFoundException();
		}
		sitePage.setAttribute("record", comment);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		return getPageBuilder().buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode, false, false, false); //不做日志
	}
	
	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}