package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 日志处理器
 * @author linchuan
 *
 */
public class LoggerProcessor implements PageElementProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		boolean counter = false;
		if("counter".equals(pageElement.getId())) { //输出访问次数
			counter = true;
		}
		else if("true".equals(sitePage.getAttribute("loggerOnce"))) { //已经在输出访问次数时做了访问日志
			return;
		}
		String recordId = null;
		Object record = sitePage.getAttribute("record");
		if(record!=null) {
			if(!(record instanceof ActionForm) || (((ActionForm)record).getAct()!=null && !"create".equals(((ActionForm)record).getAct()))) {
				try {
					recordId = "" + PropertyUtils.getProperty(record, "id");
				}
				catch (Exception e) {
					
				}
			}
		}
		if(recordId==null) {
			if(request.getParameter("directoryId")!=null) {
				recordId = RequestUtils.getParameterLongValue(request, "directoryId") + "";
			}
			else {
				recordId = RequestUtils.getParameterLongValue(request, "siteId") + "";
			}
		}
		HTMLScriptElement scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.setCharset("utf-8");
		String script = "var screenWidth, screenHeight, colorDepth, javaEnabled, language;" +
						"var n = navigator;" +
						"if(self.screen) {" +
						"	screenWidth = screen.width;" +
						"	screenHeight = screen.height;" +
						"	colorDepth = screen.colorDepth;" +
						"}" +
						"else if(self.java) {" +
						"	var j = java.awt.Toolkit.getDefaultToolkit();" +
						"	var s = j.getScreenSize();" +
	  					"	screenWidth = s.width;" +
						"	screenHeight = s.height;" +
	  					"	colorDepth = 0;" +
						"}" +
						"if(n.language) {" +
						"	language = n.language.toLowerCase();" +
						"}" +
						"else if(n.browserLanguage) {" +
						"	language = n.browserLanguage.toLowerCase();" +
						"}" +
						"javaEnabled = n.javaEnabled() ? 'true' : 'false';" +
						"var src = '" + Environment.getContextPath() + "/jeaf/stat/track.shtml' +" +
						" '?applicationName=" + sitePage.getApplicationName() + "' +" + 
						" '&pageName=" + sitePage.getName() + "' +" +
						" '&siteId=" + webDirectory.getId() + "' +" + 
						(recordId==null ? "" : " '&recordId=" + recordId + "' +") +
						" '&screenWidth=' + screenWidth +" +
						" '&screenHeight=' + screenHeight +" +
						" '&colorDepth=' + colorDepth +" +
						" '&javaEnabled=' + javaEnabled +" +
						" '&language=' + language +" +
						" '&countDisable=' + (window.loggerOnce ? true : false) +" +
						" '&writeAccessCount=" + counter + "';" +
						"window.loggerOnce=true;" +
						"document.write('<script src=\"' + src + '\" language=\"JavaScript\" charset=\"utf-8\" type=\"text/javascript\"></scr' + 'ipt>');";
		scriptElement.setText(script);
		sitePage.setAttribute("loggerOnce", "true"); //做标记,避免多次调用
		if(counter) {
			pageElement.getParentNode().replaceChild(scriptElement, pageElement);
		}
		else {
			pageElement.appendChild(scriptElement);
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
}
