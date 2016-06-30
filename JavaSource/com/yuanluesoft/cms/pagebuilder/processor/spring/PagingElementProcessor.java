package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 分页处理器,只支持get方式,暂不支持post方式
 * @author linchuan
 *
 */
public class PagingElementProcessor implements PageElementProcessor {
	//分页需要的属性名称
	public static final String PAGING_ATTRIBUTE_RECORD_COUNT = "PAGING_ATTRIBUTE_RECORD_COUNT";
	public static final String PAGING_ATTRIBUTE_PAGE_INDEX = "PAGING_ATTRIBUTE_PAGE_INDEX";
	public static final String PAGING_ATTRIBUTE_PAGE_ROWS = "PAGING_ATTRIBUTE_PAGE_ROWS";

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//获取记录总数
		Number number = (Number)sitePage.getAttribute(PAGING_ATTRIBUTE_RECORD_COUNT);
		final int recordCount = number==null ? 0 : number.intValue();
		//获取当前页面
		number = (Number)sitePage.getAttribute(PAGING_ATTRIBUTE_PAGE_INDEX);
		int pageIndex = number==null ? 1 : number.intValue();
		//获取每页记录数
		number = (Number)sitePage.getAttribute(PAGING_ATTRIBUTE_PAGE_ROWS);
		int pageRows = number==null ? 10 : number.intValue();
		//最大页码
		int maxPage = ((recordCount - 1) / pageRows) + 1;
		//生成URL
		String pagingURL = (String)sitePage.getAttribute("pagingURL");
		if(pagingURL==null) {
			pagingURL = generatePagingURL(sitePage, request);
			sitePage.setAttribute("pagingURL", pagingURL); //写入sitePage,避免重复获取
		}
		char delimiter = (pagingURL.lastIndexOf('?')==-1 ? '?' : '&');
		//页面元素名称或ID
		String elementName = pageElement.getAttribute("name");
		if("pageSwitch".equals(elementName)) { //处理页面跳转输入框
			pageElement.setAttribute("value", pageIndex + "/" + maxPage);
			pageElement.setAttribute("onfocus", "oldValue=value;value='';");
			pageElement.setAttribute("onblur", "if(value=='')value=oldValue;");
			pageElement.setAttribute("onkeypress", "if(window.event && window.event.keyCode==13){var page=Number(value);if(!isNaN(page) && page>0 && page<=" + maxPage + ")location.href='" + pagingURL + delimiter + "page='+ page;}"); // + '#listtop'
		}
		else if("pageSwitchButton".equals(elementName)) { //处理页页面跳转按钮
			pageElement.setAttribute("onclick", "var page=new Number(document.getElementsByName('pageSwitch')[0].value);if(!isNaN(page) && page>0 && page<=" + maxPage + ")location.href='" + pagingURL + delimiter + "page='+ page;"); // + '#listtop'
		}
		else { //处理"上一页"、"下一页"等
			String actionName = pageElement.getAttribute("urn");
			pageElement.removeAttribute("id");
			pageElement.removeAttribute("urn");
			if("上一页".equals(actionName)) {
				if(pageIndex>1) {
					pageElement.setAttribute("href", pagingURL + delimiter + "page=" + (pageIndex-1)); // + "#listtop"
				}
			}
			else if("下一页".equals(actionName) || "最后一页".equals(actionName)) {
				if(pageIndex < maxPage) { //判断是否已经是最后一页
					if("下一页".equals(actionName)) {
						pageElement.setAttribute("href", pagingURL + delimiter + "page=" + (pageIndex+1)); // + "#listtop"
					}
					else { //最后一页
						pageElement.setAttribute("href", pagingURL + delimiter + "page=" + maxPage); // + "#listtop"
					}
				}
			}
			else if("第一页".equals(actionName)) {
				if(pageIndex>1) {
					pageElement.setAttribute("href", pagingURL); // + "#listtop"
				}
			}
			else if("记录总数".equals(actionName)) {
				pageElement.getParentNode().replaceChild(pageElement.getOwnerDocument().createTextNode("" + recordCount), pageElement);
			}
			else if("当前页码".equals(actionName)) {
				pageElement.getParentNode().replaceChild(pageElement.getOwnerDocument().createTextNode("" + pageIndex), pageElement);
			}
			else if("总页数".equals(actionName)) {
				pageElement.getParentNode().replaceChild(pageElement.getOwnerDocument().createTextNode("" + maxPage), pageElement);
			}
			else if(actionName.startsWith("页码")) {
				String[] values = actionName.split("\\x7C");
				int pageNumber = Integer.parseInt(values[1]);
				int maxPageIndex = Math.min(pageIndex + pageNumber/2, maxPage);
				int minPageIndex = maxPageIndex - pageNumber + 1;
				if(minPageIndex<1) {
					minPageIndex = 1;
					maxPageIndex = Math.min(pageNumber, maxPage);
				}
				if(minPageIndex>1) {
					//插入第一页和...
					HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement.getOwnerDocument().createElement("A");
					anchorElement.setAttribute("href", pagingURL); // + "#listtop"
					htmlParser.setTextContent(anchorElement, "1");
					pageElement.getParentNode().insertBefore(anchorElement, pageElement);
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().createTextNode(" ... "), pageElement);
				}
				for(int i=minPageIndex; i<=maxPageIndex; i++) {
					HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement.getOwnerDocument().createElement("A");
					htmlParser.setTextContent(anchorElement, i + "");
					if(i==pageIndex) { //当前页
						if(values.length>2 && !values[2].equals("")) {
							anchorElement.setAttribute("style", values[2]);
						}
						anchorElement.setClassName("currentPage");
					}
					else {
						anchorElement.setAttribute("href", pagingURL + delimiter + "page=" + i); // + "#listtop"
					}
					pageElement.getParentNode().insertBefore(anchorElement, pageElement);
					if(i<maxPageIndex) {//插入空格
						pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().createTextNode(" "), pageElement);
					}
				}
				if(maxPageIndex < maxPage) {
					//插入...和最后的页码
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().createTextNode(" ... "), pageElement);
					HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement.getOwnerDocument().createElement("A");
					anchorElement.setAttribute("href", pagingURL + delimiter + "page=" + maxPage); // + "#listtop"
					htmlParser.setTextContent(anchorElement, maxPage + "");
					pageElement.getParentNode().insertBefore(anchorElement, pageElement);
				}
				pageElement.getParentNode().removeChild(pageElement);
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
	 * 生成URL
	 * @param sitePage
	 * @param request
	 * @param site
	 * @return
	 */
	private String generatePagingURL(SitePage sitePage, HttpServletRequest request) {
		if(request==null) {
			return Environment.getContextPath() + sitePage.getUrl();
		}
		String url = (String)request.getAttribute("javax.servlet.forward.request_uri");
		if(url==null) {
			url = request.getRequestURI();
		}
		String queryString = request.getQueryString();
		if(queryString==null || queryString.equals("")) {
			return url;
		}
		//从queryString中删除page属性
		int index = queryString.indexOf("page=");
		if(index==-1) {
			return url + "?" + queryString;
		}
		int endIndex = queryString.indexOf('&', index + 5);
		if(endIndex==-1) {
			queryString = queryString.substring(0, Math.max(0, index - 1));
		}
		else {
			queryString = queryString.substring(0, index) + queryString.substring(endIndex + 1);
		}
		queryString = StringUtils.removeQueryParameters(queryString, "staticPageId,client.system,client.model,client.systemVersion,client.deviceId,client.retrieveClientPage,client.pageWidth");
		return url + (queryString.equals("") ? "" : "?" + queryString);
	}
}