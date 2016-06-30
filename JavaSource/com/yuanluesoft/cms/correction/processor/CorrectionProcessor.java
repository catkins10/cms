package com.yuanluesoft.cms.correction.processor;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class CorrectionProcessor implements PageElementProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//添加脚本
		Object record = sitePage.getAttribute("record");
		long recordId = -1;
		if(record!=null) {
			try {
				recordId = ((Number)PropertyUtils.getProperty(record, "id")).longValue();
			}
			catch (Exception e) {
				
			}
		}
		if(recordId==-1) {
			recordId = webDirectory.getId();
		}
		String url = null;
		try {
			String pageUrl = URLEncoder.encode(RequestUtils.getRequestURL(request, true), "utf-8");
			String pageTitle = ((HTMLDocument)pageElement.getOwnerDocument()).getTitle();
			url = Environment.getContextPath() + "/cms/correction/correction.shtml" + 
				  "?act=create" +
				  "&applicationName=" + sitePage.getApplicationName() +
				  "&pageName=" + sitePage.getName() +
				  "&recordId=" + recordId +
				  "&pageTitle=" + (pageTitle==null || pageTitle.equals("") ? pageUrl : URLEncoder.encode(pageTitle, "utf-8")) +
				  "&pageUrl=" + pageUrl +
				  "&siteId=" + parentSite.getId();
		}
		catch (Exception e) {
			
		}
		String script = "function createCorrection() {\r\n" +
						"	try {\r\n" +
						"		document.execCommand('copy');\r\n" + //firefox不支持
						"	} catch(e) {\r\n" +
						"	}\r\n" +
						"	window.top.location = '" + url + "';\r\n" +
						"}\r\n" +
						"document.onkeypress = function(e) {\r\n" + //ctrl + enter 触发纠错页面
						"	if(window.event && window.event.keyCode==10 && window.event.ctrlKey) {\r\n" + //ie
						"		createCorrection();\r\n" +
						"	}\r\n" +
						"	else if(e && e['ctrlKey'] && e.which==13) {\r\n" + //firefox
						"		createCorrection();\r\n" +
						"	}\r\n" +
						"};\r\n";
		htmlParser.appendScript((HTMLDocument)pageElement.getOwnerDocument(), script);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageGenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
}