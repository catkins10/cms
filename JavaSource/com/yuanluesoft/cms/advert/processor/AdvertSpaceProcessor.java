package com.yuanluesoft.cms.advert.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.advert.model.FixedAdvert;
import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 广告位处理器
 * @author linchuan
 *
 */
public class AdvertSpaceProcessor implements PageElementProcessor {
	private AdvertService advertService; //广告服务
	private HTMLParser htmlParser; //HTML解析器
	private PageBuilder pageBuilder; //页面生成器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(RequestInfo.PAGE_TYPE_NORMAL!=requestInfo.getPageType() && RequestInfo.PAGE_TYPE_EDIT!=requestInfo.getPageType() && RequestInfo.PAGE_TYPE_BUILD_STATIC_PAGE!=requestInfo.getPageType()) {
			pageElement.getParentNode().removeChild(pageElement);
    		return;
		}
		//解析配置
		String properties = pageElement.getAttribute("urn");
	    long advertSpaceId = StringUtils.getPropertyLongValue(properties, "advertSpaceId", 0);
    	int loadMode = StringUtils.getPropertyIntValue(properties, "loadMode", 0);
    	int hideMode = StringUtils.getPropertyIntValue(properties, "hideMode", 0);
    	int speed = StringUtils.getPropertyIntValue(properties, "speed", 1);
    	int displaySeconds = StringUtils.getPropertyIntValue(properties, "displaySeconds", 0);
    	
    	//获取广告
    	FixedAdvert fixedAdvert = advertService.loadFixedAdvert(advertSpaceId);
    	if(fixedAdvert==null || fixedAdvert.getContent()==null || fixedAdvert.getContent().isEmpty()) { //没有广告
    		pageElement.getParentNode().removeChild(pageElement);
    		return;
    	}
    	
    	//输出广告
    	long hostElementId = UUIDLongGenerator.generateId();
    	String js = writeAdvertContent(fixedAdvert.getContent(), "html", webDirectory.getId(), request) +
    				writeAdvertContent(fixedAdvert.getMinimizeContent(), "minimizeHtml", webDirectory.getId(), request) +
    				"Advert.adverts[Advert.adverts.length]=new Advert(" +
					"'" + fixedAdvert.getAdvertPutId() + "'," +
					"'static'," +
					"'" + loadMode + "'," +
					"'" + hideMode + "'," +
					"" + displaySeconds + "," +
					"html," +
					"minimizeHtml," +
					"0," +
					"0," +
					speed + "," +
					"'" + fixedAdvert.getWidth() + "'," +
					"'" + fixedAdvert.getHeight() + "'," +
					"'" + fixedAdvert.getMinimizeWidth() + "'," +
					"'" + fixedAdvert.getMinimizeHeight() + "'," +
					"'" + (fixedAdvert.getHref()==null || fixedAdvert.getHref().isEmpty() ? "" : Environment.getContextPath() + "/cms/advert/advert.shtml?advertPutId=" + fixedAdvert.getAdvertPutId()) + "'" +
					");\r\n" +
					"Advert.adverts[Advert.adverts.length-1].create(document.getElementById('ref_" + hostElementId + "'));\r\n";
    	HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
    	script.setLang("javascript");
    	htmlParser.setTextContent(script, js);
    	pageElement.getParentNode().replaceChild(script, pageElement);
    	//创建一个宿主对象,用来加载广告
    	HTMLElement span = (HTMLElement)script.getOwnerDocument().createElement("span");
    	span.setId("ref_" + hostElementId);
    	script.getParentNode().insertBefore(span, script);
	}
	
	/**
	 * 输出广告内容
	 * @param advertContent
	 * @param propertyName
	 * @param siteId
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private String writeAdvertContent(String advertContent, String propertyName, long siteId, HttpServletRequest request) throws ServiceException {
		try {
			String js = "var " + propertyName + " = '';\r\n";
			if(advertContent==null || advertContent.isEmpty()) {
				return js;
			}
			//解析广告内容
			HTMLDocument document = htmlParser.parseHTMLString(advertContent, "utf-8");
			document = pageBuilder.buildHTMLDocument(document, siteId, new SitePage(), request, false, false, false, false);
			String html = htmlParser.getBodyHTML(document, "utf-8", false);
			int beginIndex = 0;
	    	int endIndex;
	    	while((endIndex = html.indexOf('\n', beginIndex))!=-1) {
	    		js += propertyName + " += '" + Encoder.getInstance().utf8JsEncode(html.substring(beginIndex, endIndex)).replaceAll("SCRIPT", "SCR' + 'IPT") + "';\r\n";
	    		beginIndex = endIndex + 1;
	    	}
	    	js += propertyName + " += '" + Encoder.getInstance().utf8JsEncode(html.substring(beginIndex)).replaceAll("SCRIPT", "SCR' + 'IPT") + "';\r\n";
	    	return js;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRebuildBasis(long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageForModifiedObject(java.lang.Object, java.lang.String, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/**
	 * @return the advertService
	 */
	public AdvertService getAdvertService() {
		return advertService;
	}

	/**
	 * @param advertService the advertService to set
	 */
	public void setAdvertService(AdvertService advertService) {
		this.advertService = advertService;
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