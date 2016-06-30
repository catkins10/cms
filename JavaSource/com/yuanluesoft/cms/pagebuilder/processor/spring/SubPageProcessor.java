package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteSubPage;
import com.yuanluesoft.cms.pagebuilder.model.subpage.SubPage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SubPageProcessor implements PageElementProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析应用
		SubPage subPage = (SubPage)BeanUtils.generateBeanByProperties(SubPage.class, pageElement.getAttribute("urn"), null);
		//获取子页面设置
		SiteSubPage subPageModel = (SiteSubPage)ListUtils.findObjectByProperty(sitePage.getSubPages(), "name", subPage.getPageName());
		//样式表
		String cssFile = subPage.getCssUrl();
		if(subPageModel!=null && (cssFile==null || cssFile.equals(""))) {
			cssFile = Environment.getContextPath() + (subPageModel.getNormalCssFile()==null ? "/cms/css/application.css" : subPageModel.getNormalCssFile());
		}	
		//输出子页面
		if(subPageModel!=null && "iframe".equals(subPageModel.getType())) { //IFRAME方式
			htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/cms/js/subPage.js");
			//输出iframe
			HTMLIFrameElement iframe = (HTMLIFrameElement)pageElement.getOwnerDocument().createElement("iframe");
			iframe.setFrameBorder("0");
			iframe.setId(subPage.getPageName());
			iframe.setName(subPage.getPageName());
			iframe.setAttribute("allowTransparency", "true");
			pageElement.getParentNode().replaceChild(iframe, pageElement);
			
			String src = getIFrameSrc(subPageModel, sitePage, request);
			if(cssFile!=null) {
				try {
					src += (src.lastIndexOf('?')==-1 ? "?" : "&") + "cssFile=" + URLEncoder.encode(cssFile, "utf-8");
				} 
				catch (UnsupportedEncodingException e) {
					
				}
			}
			iframe.setSrc(src);
			if(!subPage.isHorizontalScroll() && !subPage.isVerticalScroll()) {
				iframe.setScrolling("no");
			}
			//设置帧宽度
			if("percentage".equals(subPage.getWidthMode())) { //按百分比
				iframe.setWidth(subPage.getWidth() + "%");
			}
			else if("absolute".equals(subPage.getWidthMode())) { //固定宽度
				iframe.setWidth(subPage.getWidth() + "px");
			}
			//设置帧高度
			if("percentage".equals(subPage.getHeightMode())) { //按百分比
				iframe.setHeight(subPage.getHeight() + "%");
			}
			else if("absolute".equals(subPage.getHeightMode())) { //固定宽度
				iframe.setHeight(subPage.getHeight() + "px");
			}
			iframe.setAttribute("onload", "onSubPageLoad('" + subPage.getPageName() + "', '" + subPage.getWidthMode() + "', '" + subPage.getHeightMode() + "', " + subPage.isHorizontalScroll() + ", " + subPage.isVerticalScroll() + ")");
		}
		else { //页面植入方式
			//引入用户自定义的样式表
			htmlParser.appendCssFile((HTMLDocument)pageElement.getOwnerDocument(), cssFile, true);
			HTMLElement parentElement = (HTMLElement)pageElement.getOwnerDocument().createElement("div");
			parentElement.setId(subPage.getPageName());
			pageElement.getParentNode().replaceChild(parentElement, pageElement);
			if(!"auto".equals(subPage.getWidthMode()) || !"auto".equals(subPage.getHeightMode())) {
				String style = null;
				//处理宽度
				if("percentage".equals(subPage.getWidthMode())) {
					style = "width:" + subPage.getWidth() + "%";
				}
				else if("absolute".equals(subPage.getWidthMode())) {
					style = "width:" + subPage.getWidth() + "px";
				}
				if(subPage.isHorizontalScroll()) { //水平滚动
					style = (style==null ? "" : style + ";") + "overflow-x:auto";
				}
				//处理高度
				if("percentage".equals(subPage.getHeightMode())) {
					style = (style==null ? "" : style + ";") + "height:" + subPage.getHeight() + "%";
				}
				else if("absolute".equals(subPage.getHeightMode())) {
					style = (style==null ? "" : style + ";") + "height:" + subPage.getHeight() + "px";
				}
				if(subPage.isVerticalScroll()) { //垂直滚动
					style = (style==null ? "" : style + ";") + "overflow-y:auto";
				}
				if(style!=null) {
					parentElement.setAttribute("style", style);
				}
			}
			Node node;
			//输出子页面
			if("base".equals(subPage.getPageName())) { //基础页面,输出<ext:applicationForm></ext:applicationForm>之间的内容
				//替换成$FORM$,作为解析ApplicationForm的header和fiooter的标记
				node = pageElement.getOwnerDocument().createTextNode("##APPLICATON_FORM_BODY##");
			}
			else {
				//引入jsp子页面
				node = pageElement.getOwnerDocument().createTextNode("##INCLUDE_JSP:" + subPage.getPageName() + "##");
			}
			parentElement.appendChild(node);
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
	 * 生成IFrame的SRC
	 * @param subPageModel
	 * @param sitePage
	 * @param request
	 * @return
	 */
	protected String getIFrameSrc(SiteSubPage subPageModel, SitePage sitePage, HttpServletRequest request) {
		String url = subPageModel.getIframeUrl();
		if(url==null || "about:blank".equals(url)) {
			return  Environment.getContextPath() + "/blank.html";
		}
		url = StringUtils.fillParameters(url, true, false, false, "utf-8", (sitePage==null ? null : sitePage.getAttribute("record")), request, null);
		String qureyString = request.getQueryString();
		if(qureyString!=null && !"".equals(qureyString)) {
			qureyString = StringUtils.removeQueryParameters(qureyString, "staticPageId,client.system,client.model,client.systemVersion,client.deviceId,client.retrieveClientPage,client.pageWidth");
			url += (url.lastIndexOf('?')==-1 ? "?" : "&") + qureyString;
		}
		return Environment.getContextPath() + url;
	}
}