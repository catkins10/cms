package com.yuanluesoft.cms.pagebuilder.spring;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.config.ActionConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.SiteFormService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SiteFormServiceImpl implements SiteFormService {
	private TemplateService templateService;
	private PageBuilder pageBuilder; //页面生成器
	private PageDefineService pageDefineService; //站点应用服务
	private HTMLParser htmlParser; //页面解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.ApplicationFormService#generateApplicationForm(java.lang.String, java.lang.String, long, long, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public String generateApplicationForm(String applicationName, String pageName, long siteId, long templateId, ActionForm actionForm, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		//从预设模板获取
		Map pageTemplates = (Map)request.getAttribute(PageService.PAGE_TEMPLATE_MAP);
		if(pageTemplates!=null) {
			HTMLDocument template = (HTMLDocument)pageTemplates.get(applicationName + "_" + pageName);
			if(template!=null) {
				templateDocument = template;
			}
		}
		if(templateDocument==null && templateId>0) {
			templateDocument = templateService.getTemplateHTMLDocument(templateId, siteId, false, request);
		}
		else if(templateDocument==null) {
			RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
			int themeType = TemplateThemeService.THEME_TYPE_COMPUTER;
			if(requestInfo.isClientRequest()) { //客户端
				themeType = TemplateThemeService.THEME_TYPE_CLIENT;
			}
			else if(requestInfo.isWapRequest()) { //WAP
				themeType = TemplateThemeService.THEME_TYPE_WAP;
			}
			else if(requestInfo.isWechatRequest()) { //微信
				themeType = TemplateThemeService.THEME_TYPE_WECHAT;
			}
			else if(requestInfo.getPageWidth()>0) { //手机或者平板
				themeType = TemplateThemeService.THEME_TYPE_SMART_PHONE;
			}
			long themeId = 0;
			try {
				themeId = Long.parseLong(CookieUtils.getCookie(request, "THEMEID"));
			}
			catch(Exception e) {
				
			}
			boolean temporaryOpeningFirst = true; //是否允许优先使用临时模板
			SitePage sitePage = pageDefineService.getSitePage(applicationName, pageName);
			if(!sitePage.isRealtimeStaticPage() && request.getParameter("staticPageId")!=null) { //页面不需要实时更新,且在创建静态页面
				temporaryOpeningFirst = false;
			}
			templateDocument = getTemplateDocument(applicationName, pageName, siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), temporaryOpeningFirst, actionForm, request);
			if(templateDocument==null) {
				if(sitePage.getParentPage()!=null) { //有父页面
					//按父页面查找模板
					templateDocument = getTemplateDocument(applicationName, sitePage.getParentPage(), siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), temporaryOpeningFirst, actionForm, request);
				}
			}
		}
		if(templateDocument==null) {
			return "<html><head>" +
				   "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + Environment.getContextPath() + "/cms/css/application.css" + "\">" + 
				   "</head><body>" +
				   "##APPLICATON_FORM####APPLICATON_FORM_BODY####APPLICATON_FORM_END##" +
				   "</body></html>";
		}
		if(actionForm!=null && (templateDocument.getTitle()==null || templateDocument.getTitle().isEmpty())) {
			templateDocument.setTitle(actionForm.getFormTitle());
		}
		SitePage sitePage = pageDefineService.getSitePage(applicationName, pageName);
		//自动将struts form bean设置为record
		ActionConfig actionConfig = (ActionConfig)request.getAttribute("org.apache.struts.action.mapping.instance");
		if(actionConfig!=null && actionConfig.getName()!=null) {
			sitePage.setAttribute("record", request.getAttribute(actionConfig.getName()));
		}
		templateDocument = pageBuilder.buildHTMLDocument(templateDocument, siteId, sitePage, request, false, !(templateService instanceof UserPageTemplateService), true, true);
		//在body后面插入form元素
		HTMLBodyElement htmlBody = (HTMLBodyElement)templateDocument.getBody();
		if(htmlBody==null) {
			//创建body
			htmlBody = (HTMLBodyElement)templateDocument.createElement("body");
			templateDocument.appendChild(htmlBody);
		}
		//在body前面加##APPLICATON_FORM
		Node formBegin = templateDocument.createTextNode("##APPLICATON_FORM##");
		NodeList nodes = htmlBody.getChildNodes();
		if(nodes==null || nodes.getLength()==0) {
			htmlBody.appendChild(formBegin);
		}
		else {
			htmlBody.insertBefore(formBegin, nodes.item(0));
		}
		//在body后面加##APPLICATON_FORM_END
		htmlBody.appendChild(templateDocument.createTextNode("##APPLICATON_FORM_END##"));
		return getHtmlParser().getDocumentHTML(templateDocument, "utf-8");
	}
	
	/**
	 * 获取模板
	 * @param applicationName
	 * @param pageName
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param actionForm
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected HTMLDocument getTemplateDocument(String applicationName, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, ActionForm actionForm, HttpServletRequest request) throws ServiceException {
		return templateService.getTemplateHTMLDocument(applicationName, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/**
	 * @return the templateService
	 */
	public TemplateService getTemplateService() {
		return templateService;
	}

	/**
	 * @param templateService the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
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

	/**
	 * @return the pageDefineService
	 */
	public PageDefineService getPageDefineService() {
		return pageDefineService;
	}

	/**
	 * @param pageDefineService the pageDefineService to set
	 */
	public void setPageDefineService(
			PageDefineService pageDefineService) {
		this.pageDefineService = pageDefineService;
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