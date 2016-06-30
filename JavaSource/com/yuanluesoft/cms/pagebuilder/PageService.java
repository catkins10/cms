package com.yuanluesoft.cms.pagebuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.config.ActionConfig;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLMetaElement;

import com.yuanluesoft.cms.pagebuilder.callback.GeneratePartPageCallback;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.exception.TemplateNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.service.SqlQueryService;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.client.ClientHeader;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class PageService {
	//页面服务常用的属性名称
	public final static String PAGE_ATTRIBUTE_RECORD = "record"; //记录
	public final static String PAGE_ATTRIBUTE_NEXT_RECORD = "nextRecord"; //上一个记录
	public final static String PAGE_ATTRIBUTE_PREVIOUS_RECORD = "previousRecord"; //下一个记录
	
	//页面选项
	public final static String PAGE_LOGGER_DISABLED = "LoggerDisabled"; //是否禁止访问日志记录
	public final static String PAGE_ADVERT_DISABLED = "AdvertDisabled"; //是否禁止加载广告
	public final static String PAGE_CORRECTION_DISABLED = "CorrectionDisabled"; //是否禁止纠错
	public final static String PAGE_TEMPLATE_MAP = "PageTemplates"; //预设模板
	
	private TemplateService templateService; //模板服务
	private PageDefineService pageDefineService; //站点应用服务
	private PageBuilder pageBuilder; //页面生成器
	private StaticPageBuilder staticPageBuilder; //静态页面生成器
	private BusinessDefineService businessDefineService = null; //业务逻辑定义服务
	
	/**
	 * 获取模板
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param themeId 指定的主题ID,0表示不指定
	 * @param themeType 主题类型,TemplateThemeService.THEME_TYPE_COMPUTER/TemplateThemeService.THEME_TYPE_SMART_PHONE/TemplateThemeService.THEME_TYPE_WAP
	 * @param pageWidth 页面宽度,智能手机有效
	 * @param flashSupport 是否支持flash,iphone不支持
	 * @param temporaryOpeningFirst
	 * @param request
	 * @param editMode
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		return templateService.getTemplateHTMLDocument(applicationName, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}
	
	/**
	 * 重置模板
	 * @param template
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param request
	 * @param response
	 * @param editMode
	 * @throws ServiceException
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		//检查子类是否有继承指定的方法
		Method resetPageLinkMethod = null; //重置页面链接的方法
		Method resetRecordListMethod = null; //列表重置方法
		Method resetFieldElementMethod = null; //字段重置方法
		try {
			resetPageLinkMethod = this.getClass().getMethod("resetPageLink", new Class[]{HTMLAnchorElement.class, String.class, HTMLDocument.class, String.class, String.class, SitePage.class, long.class, HttpServletRequest.class});
			resetRecordListMethod = this.getClass().getMethod("resetRecordList", new Class[]{HTMLAnchorElement.class, String.class, HTMLDocument.class, String.class, String.class, SitePage.class, long.class, HttpServletRequest.class});
			resetFieldElementMethod = this.getClass().getMethod("resetFieldElement", new Class[]{HTMLAnchorElement.class, String.class, HTMLDocument.class, String.class, String.class, SitePage.class, long.class, HttpServletRequest.class});
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		if((resetPageLinkMethod!=null && !resetPageLinkMethod.getDeclaringClass().equals(PageService.class)) ||
		   (resetRecordListMethod!=null && !resetRecordListMethod.getDeclaringClass().equals(PageService.class)) ||
		   (resetFieldElementMethod!=null && !resetFieldElementMethod.getDeclaringClass().equals(PageService.class))) {
			//重置页面链接
			NodeList links = template.getElementsByTagName("a");
			for(int i=links.getLength()-1; i>=0; i--) {
				HTMLAnchorElement a = (HTMLAnchorElement)links.item(i);
				if("recordList".equals(a.getId())) { //记录列表
					resetRecordList(a, StringUtils.getPropertyValue(a.getAttribute("urn"), "recordListName"), template, applicationName, pageName, sitePage, siteId, request);
				}
				else if("field".equals(a.getId())) { //字段
					resetFieldElement(a, StringUtils.getPropertyValue(a.getAttribute("urn"), "name"), template, applicationName, pageName, sitePage, siteId, request);
				}
				else if("pageLink".equals(a.getId())) { //页面链接
					String urn  = a.getAttribute("urn");
					String linkName = StringUtils.getPropertyValue(urn, "linkTitle");
					if(linkName==null || linkName.isEmpty()) {
						linkName = urn;
					}
					resetPageLink(a, linkName, template, applicationName, pageName, sitePage, siteId, request);
				}
			}
		}
	}
	
	/**
	 * 重置页面链接
	 * @param link
	 * @param linkName
	 * @param template
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		
	}
	
	/**
	 * 重置记录列表
	 * @param recordListElement
	 * @param recordListName
	 * @param template
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	public void resetRecordList(HTMLAnchorElement recordListElement, String recordListName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		
	}
	
	/**
	 * 重置字段元素
	 * @param fieldElement
	 * @param fieldName
	 * @param template
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	public void resetFieldElement(HTMLAnchorElement fieldElement, String fieldName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		
	}
	
	/**
	 * 获取上一个/下一个记录
	 * @param currentRecord
	 * @param nextRecord
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	protected List getPagingRecords(Object currentRecord, boolean nextRecord, long siteId) throws ServiceException {
		return null;
	}
	
	/**
	 * 数据变更后,重建静态页面
	 * @param object
	 * @param modifyAction
	 * @throws ServiceException
	 */
	public void rebuildStaticPageForModifiedObject(Serializable object, String modifyAction) throws ServiceException {
		//重建当前记录页面
		staticPageBuilder.rebuildPageForModifiedObject(object, modifyAction);
		//重建“上一篇”
		List pagingRecords = getPagingRecords(object, false, -1);
		for(Iterator iterator = pagingRecords==null ? null : pagingRecords.iterator(); iterator!=null && iterator.hasNext();) {
			Serializable previousObject = (Serializable)iterator.next();
			staticPageBuilder.rebuildPageForModifiedObject(previousObject, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		//重建“下一篇”
		pagingRecords = getPagingRecords(object, true, -1);
		for(Iterator iterator = pagingRecords==null ? null : pagingRecords.iterator(); iterator!=null && iterator.hasNext();) {
			Serializable nextObject = (Serializable)iterator.next();
			staticPageBuilder.rebuildPageForModifiedObject(nextObject, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
	}
	
	/**
	 * 设置页面属性
	 * @param applicationName
	 * @param pageName
	 * @param sitePage
	 * @param siteId
	 * @param request
	 * @param editMode
	 * @throws ServiceException
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		Object record = sitePage.getAttribute(PAGE_ATTRIBUTE_RECORD); //已经指定了记录
		if(record!=null) {
			return;
		}
		record = request.getAttribute(PAGE_ATTRIBUTE_RECORD); //action指定了记录
		if(record!=null) {
			sitePage.setAttribute(PAGE_ATTRIBUTE_RECORD, record);
			return;
		}
		//自动将struts form bean设置为record
		ActionConfig actionConfig = (ActionConfig)request.getAttribute("org.apache.struts.action.mapping.instance");
		if(actionConfig!=null && actionConfig.getName()!=null) {
			sitePage.setAttribute(PAGE_ATTRIBUTE_RECORD, request.getAttribute(actionConfig.getName()));
			return;
		}
		//按页面的记录类名称和id获取记录
		String id = RequestUtils.getParameterStringValue(request, "id");
		if(id!=null && sitePage.getRecordClassName()!=null) {
			if(businessDefineService==null) {
				businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
			}
			BusinessObject businessObject = businessDefineService.getBusinessObject(sitePage.getRecordClassName());
			//获取业务逻辑服务
			BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
			try {
				if(businessService instanceof SqlQueryService) { //SQL查询
					record = ((SqlQueryService)businessService).getRecord(sitePage.getRecordClassName(), id);
				}
				else {
					record = businessService.load(Class.forName(sitePage.getRecordClassName()), Long.parseLong(id));
				}
			} 
			catch (ClassNotFoundException e) {
				
			}
			if(record==null) {
				throw new PageNotFoundException();
			}
			sitePage.setAttribute(PAGE_ATTRIBUTE_RECORD, record);
		}
	}
	
	/**
	 * 输出页面
	 * @param applicationName
	 * @param pageName
	 * @param request
	 * @param response
	 * @param editMode
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void writePage(String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException, IOException {
		if("true".equals(request.getParameter("writePageElementAsJs"))) {
			writePageAsJs(applicationName, pageName, request, response);
			return;
		}
		//设置输出字符集为utf-8
		response.setCharacterEncoding("UTF-8");
		try {
			writePage(applicationName, pageName, request, response, response.getWriter(), editMode);
		}
		catch(Exception e) {
			response.setStatus(500);
		}
	}
	
	/**
	 * 按js输出页面
	 * @param applicationName
	 * @param pageName
	 * @param request
	 * @param response
	 * @throws ServiceException
	 * @throws IOException
	 */
	protected void writePageAsJs(String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId"); //获取站点ID
		//获取站点页面配置
		SitePage sitePage = getSitePage(applicationName, pageName, request);
		if(sitePage==null) {
			return;
		}
		try {
			setPageAttributes(applicationName, pageName, sitePage, siteId, request, false);
		}
		catch(PageNotFoundException e) {
			return;
		}
		//输出页面
		try {
			pageBuilder.writePageElementAsJs(sitePage, request, response);
		}
		catch(ServiceException se) {
			if("invalid request".equals(se.getMessage())) {
				return;
			}
			throw se;
		}
	}
	
	/**
	 * 输出页面
	 * @param applicationName
	 * @param pageName
	 * @param request
	 * @param response
	 * @param writer
	 * @param editMode
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void writePage(String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response, PrintWriter writer, boolean editMode) throws ServiceException, IOException {
		HTMLDocument template = null;
		long siteId = RequestUtils.getParameterLongValue(request, "siteId"); //获取站点ID
		boolean generateStaticPage = request.getParameter("staticPageId")!=null; //生成静态页面
		SitePage sitePage = null;
		RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
		int themeType = TemplateThemeService.THEME_TYPE_COMPUTER;
		long themeId = 0;
		try {
			//模板ID
			long templateId = RequestUtils.getParameterLongValue(request, "templateId");
			//获取站点页面配置
			sitePage = getSitePage(applicationName, pageName, request);
			if(sitePage==null) {
				return;
			}
			setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
			//获取模板
			if(templateId>0) { //按模板id获取模板
				template = templateService.getTemplateHTMLDocument(templateId, siteId, false, request);
			}
			else if("true".equals(request.getParameter("normalTemplate"))) { //获取默认模板
				NormalTemplate normalTemplate = templateService.getNormalTemplateHTMLDocument(applicationName, pageName, themeType);
				template = normalTemplate==null ? null : normalTemplate.getHtmlDocument();
			}
			else if(requestInfo.getPageWidth()==-1 && !pageName.equals("selectPhonePage")) { //智能手机,且页面宽度未定,且当前不是页面宽度选择页面
				//输出屏幕尺寸检查页面
				writer.write("<html>" +
							 "<body onload=\"location.replace('" +
							 				request.getContextPath() + "/cms/sitemanage/selectPhonePage.shtml" +
							 				"?siteId=" + siteId +
							 				"&redirect=" + URLEncoder.encode(RequestUtils.getRequestURL(request, true), "utf-8") +
							 				"&screenWidth=' + " +
							 				"(self.screen ? screen.width : (self.java ? java.awt.Toolkit.getDefaultToolkit().getScreenSize().width : 0)) + " +
							 				"'&screenHeight=' + " +
							 				"(self.screen ? screen.height : (self.java ? java.awt.Toolkit.getDefaultToolkit().getScreenSize().height : 0)))" +
											"\">" +
							 "</body>" +
							 "</html>");
				return;
			}
			else {
				//从预设模板获取
				Map pageTemplates = (Map)request.getAttribute(PAGE_TEMPLATE_MAP);
				if(pageTemplates!=null) {
					template = (HTMLDocument)pageTemplates.get(applicationName + "_" + pageName);
				}
				if(template==null) {
					if(requestInfo.isWapRequest()) { //WAP请求
						themeType = TemplateThemeService.THEME_TYPE_WAP;
					}
					else if(requestInfo.isClientRequest()) { //客户端请求
						themeType = TemplateThemeService.THEME_TYPE_CLIENT;
					}
					else if(requestInfo.isWechatRequest()) { //微信请求
						themeType = TemplateThemeService.THEME_TYPE_WECHAT;
					}
					else if(requestInfo.getPageWidth()>0 || pageName.equals("selectPhonePage")) { //指定了页面宽度
						themeType = TemplateThemeService.THEME_TYPE_SMART_PHONE; //智能手机
					}
					try {
						themeId = Long.parseLong(CookieUtils.getCookie(request, "THEMEID"));
					}
					catch(Exception e) {
						
					}
					template = getTemplate(applicationName, pageName, sitePage, siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), !generateStaticPage || sitePage.isRealtimeStaticPage(), request, editMode);
					String substitutePage;
					if(template==null && (substitutePage=(String)sitePage.getExtendedParameter("substitutePage"))!=null) { //模板找不到,且有替代页面
						template = getTemplate(applicationName, substitutePage, sitePage, siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), !generateStaticPage || sitePage.isRealtimeStaticPage(), request, editMode);
					}
				}
			}
			if(template==null) { //模板找不到
				throw new TemplateNotFoundException();
			}
			if(requestInfo.isClientRequest() && !"POST".equalsIgnoreCase(request.getMethod())) { //客户端请求,且不是提交
				SitePage hintPage;
				if(!"true".equals(request.getParameter("client.retrieveClientPage"))) { //获取数据
					HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
					String templatePath = htmlParser.getMeta(template, "cms.templatePath");
					//获取模板目录中最近的文件修改时间
					final long[] lastModified = new long[]{0};
					templatePath = RequestUtils.getFilePath(templatePath + "template.html"); //获取模版存放路径
					templatePath = new File(templatePath).getPath().replaceAll("\\\\", "/");
					FileUtils.fileSearch(templatePath.substring(0, templatePath.lastIndexOf('/')), null, new FileSearchCallback() {
						public void onFileFound(File file) {
							if(file.isFile() && file.lastModified()>lastModified[0]) {
								lastModified[0] = file.lastModified();
							}
						}
					});
					response.addHeader(ClientHeader.CLIENT_HEADER_TEMPLATE_MODIFIED, "" + lastModified[0]); //模板最后修改时间
					String webAppPath = new File(Environment.getWebAppPath()).getPath().replaceAll("\\\\", "/");
					int index;
					if(templatePath.startsWith(webAppPath)) {
						templatePath = templatePath.substring(webAppPath.length());
					}
					else if((index=templatePath.indexOf(':'))!=-1) {
						templatePath = templatePath.substring(index + 1);
					}
					if(templatePath.startsWith("/")) {
						templatePath = templatePath.substring(1);
					}
					response.addHeader(ClientHeader.CLIENT_HEADER_TEMPLATE_PATH, URLEncoder.encode(templatePath, "utf-8"));
					
					//设置缓存有效期
					String clientCacheMinutes = (String)sitePage.getExtendedParameter("clientCacheMinutes"); //获取客户端缓存分钟数
					if(clientCacheMinutes==null) { //没有指定缓存时间
						clientCacheMinutes = sitePage.isRecordPage() ? "30" : "5"; //记录页面缓存30分钟，其他页面5分钟
					}
					response.addHeader(ClientHeader.CLIENT_HEADER_CACHE_EXPIRE, "" + (Integer.parseInt(clientCacheMinutes) * 60000));
				}
				else if((hintPage = getSitePage(applicationName, pageName + "Hint", request))!=null) { //获取模版,且有提示页面
					//获取提示页面
					hintPage = (SitePage)sitePage.clone();
					hintPage.setName(pageName + "Hint");
					HTMLDocument hintTemplate = getTemplate(applicationName, pageName + "Hint", hintPage, siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), !generateStaticPage || sitePage.isRealtimeStaticPage(), request, editMode);
					if(hintTemplate!=null) {
						HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
						String hintPagePath = htmlParser.getMeta(hintTemplate, "cms.templatePath");
						if(hintPagePath!=null && !hintPagePath.equals(htmlParser.getMeta(template, "cms.templatePath"))) {
							htmlParser.appendMeta(template, "client.hintPagePath", hintPagePath);
						}
					}
				}
			}
			resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode); //重置模板
			//设置标题
			if(template!=null && sitePage.getTitle()!=null && (template.getTitle()==null || "".equals(template.getTitle()))) {
				int index = sitePage.getTitle().indexOf('(');
				template.setTitle(index==-1 ? sitePage.getTitle() : sitePage.getTitle().substring(0, index));
			}
			//检查模板中是否有上一篇或者下一篇的链接
			if(template.getElementById("nextRecordLink")!=null) {
				List pagingRecords = getPagingRecords(sitePage.getAttribute(PAGE_ATTRIBUTE_RECORD), true, siteId);
				sitePage.setAttribute(PAGE_ATTRIBUTE_NEXT_RECORD, pagingRecords==null || pagingRecords.isEmpty() ? null : pagingRecords.get(0));
			}
			if(template.getElementById("previousRecordLink")!=null) {
				List pagingRecords = getPagingRecords(sitePage.getAttribute(PAGE_ATTRIBUTE_RECORD), false, siteId);
				sitePage.setAttribute(PAGE_ATTRIBUTE_PREVIOUS_RECORD, pagingRecords==null || pagingRecords.isEmpty() ? null : pagingRecords.get(0));
			}
		}
		catch(Exception e) {
			if(e instanceof PageNotFoundException) {
				Logger.error("Page \"" + RequestUtils.getRequestURL(request, true) + "\" not found or has been deleted.");
			}
			else if(e instanceof TemplateNotFoundException) {
				if(Logger.isDebugEnabled()) {
					Logger.debug("Template is not exists, page is " + sitePage.getApplicationName() + "/" + sitePage.getName() + ", url is " + RequestUtils.getRequestURL(request, true));
				}
			}
			else {
				Logger.exception(e);
			}
			if(requestInfo.isClientRequest()) { //客户端请求
				throw new IOException(e);
			}
			if(pageName.equals("systemErrorPrompt")) { //已经系统错误提示页面
				writer.println("System error, please try later.");
				return;
			}
			String promptPageName = e instanceof PageNotFoundException ? "pageNotFoundPrompt" : "systemErrorPrompt"; //错误提示页面
			//获取错误提示页面模板
			sitePage = pageDefineService.getSitePage("cms/sitemanage", promptPageName);
			HTMLDocument promptTemplate = templateService.getTemplateHTMLDocument("cms/sitemanage", promptPageName, siteId, themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), true, request);
			if(generateStaticPage) { //生成静态页面
				//设置模板ID,模板配置后,重建静态页面
				HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
				htmlParser.removeMeta(promptTemplate, "cms.templateIds");
				String templateIds = null;
				if(template!=null) {
					NodeList metas = template.getElementsByTagName("meta");
					for(int i=(metas==null ? -1 : metas.getLength() - 1); i>=0; i--) {
						HTMLMetaElement meta = (HTMLMetaElement)metas.item(i);
						if("cms.templateIds".equals(meta.getName())) {
							templateIds = meta.getContent();
							break;
						}
					}
				}
				if(templateIds!=null && !templateIds.isEmpty()) {
					htmlParser.appendMeta(promptTemplate, "cms.templateIds", templateIds);
				}
			}
			template = promptTemplate;
		}
		if(generateStaticPage && "systemErrorPrompt".equals(sitePage.getName())) {
			HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
			htmlParser.appendMeta(template, "cms.pageError", "true");
		}
		//输出页面
		pageBuilder.outputHTMLDocument(buildHTMLDocument(template, siteId, sitePage, request, editMode), writer);
	}
	
	/**
	 * 输出页面
	 * @param templateDocument
	 * @param siteId
	 * @param sitePage
	 * @param request
	 * @param editMode
	 * @throws ServiceException
	 * @throws IOException
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		boolean appendLogger = !"false".equals(sitePage.getExtendedParameter("loggerSupport")) && !"true".equals(request.getAttribute(PAGE_LOGGER_DISABLED));
		boolean advertSupport = !"false".equals(sitePage.getExtendedParameter("advertSupport")) && !"true".equals(request.getAttribute(PAGE_ADVERT_DISABLED));
		boolean appendCorrection = !"true".equals(request.getAttribute(PAGE_CORRECTION_DISABLED));
		return pageBuilder.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode, appendLogger, appendCorrection, advertSupport);
	}
	
	/**
	 * 获取站点页面配置
	 * @param applicationName
	 * @param pageName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected SitePage getSitePage(String applicationName, String pageName, HttpServletRequest request) throws ServiceException {
		return pageDefineService.getSitePage(applicationName, pageName);
	}
	
	/**
	 * 获取页面链接
	 * @param templateDocument
	 * @param linkName
	 * @return
	 * @throws ServiceException
	 */
	protected HTMLAnchorElement getPageLink(HTMLDocument templateDocument, String linkName) throws ServiceException {
		NodeList links = templateDocument.getElementsByTagName("a");
		if(links==null || links.getLength()==0) {
			return null;
		}
		for(int i=links.getLength()-1; i>=0; i--) {
			HTMLAnchorElement a = (HTMLAnchorElement)links.item(i);
			if("pageLink".equals(a.getId()) && linkName.equals(a.getAttribute("urn"))) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * 生成局部页面
	 * @param applicationName
	 * @param pageName
	 * @param partName
	 * @param inneeHtml
	 * @param record
	 * @param siteId
	 * @param request
	 * @param generatePartPageCallback
	 * @return
	 * @throws ServiceException
	 */
	public String generatePartPage(String applicationName, String pageName, String partName, boolean innerHtml, Object record, long siteId, HttpServletRequest request, GeneratePartPageCallback generatePartPageCallback) throws ServiceException {
		//从缓存获取模板
		Cache templateCache = (Cache)Environment.getService("templateCache");
		HTMLElement partTemplate;
		String cacheKey = applicationName + "_" + pageName + "_" + partName;
		try {
			partTemplate = (HTMLElement)templateCache.get(cacheKey); //所有用户都使用相同的模板,避免消耗过多的资源
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		if(partTemplate==null) {
			//获取模板
			RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
			int themeType = requestInfo.isWapRequest() ? TemplateThemeService.THEME_TYPE_WAP : (requestInfo.getPageWidth()>0 ? TemplateThemeService.THEME_TYPE_SMART_PHONE : TemplateThemeService.THEME_TYPE_COMPUTER);
			long themeId = 0;
			try {
				themeId = Long.parseLong(CookieUtils.getCookie(request, "THEMEID"));
			}
			catch(Exception e) {
				
			}
			HTMLDocument template = templateService.getTemplateHTMLDocument(applicationName, pageName, RequestUtils.getParameterLongValue(request, "siteId"), themeId, themeType, requestInfo.getPageWidth(), requestInfo.isFlashSupport(), true, request);
			partTemplate = generatePartPageCallback.getPartTemplate(template);
			if(partTemplate==null) {
				throw new ServiceException();
			}
			if(!innerHtml) {
				//创建div,把局部模板复制到div中
				HTMLDivElement div = (HTMLDivElement)template.createElement("div");
				div.appendChild(partTemplate.cloneNode(true));
				partTemplate.getParentNode().replaceChild(div, partTemplate);
				partTemplate = div;
			}
			try {
				templateCache.put(cacheKey, partTemplate);
			}
			catch (CacheException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		partTemplate = (HTMLElement)partTemplate.cloneNode(true);
		//生成HTML
		SitePage sitePage = new SitePage();
		sitePage.setName(pageName);
		sitePage.setAttribute(PAGE_ATTRIBUTE_RECORD, record);
		pageBuilder.processPageElement(partTemplate, new WebDirectory(), new WebSite(), sitePage, RequestUtils.getRequestInfo(request), request);
		return ((HTMLParser)Environment.getService("htmlParser")).getElementInnerHTML(partTemplate, "utf-8");
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
	public void setPageDefineService(PageDefineService pageDefineService) {
		this.pageDefineService = pageDefineService;
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
	 * @return the staticPageBuilder
	 */
	public StaticPageBuilder getStaticPageBuilder() {
		return staticPageBuilder;
	}

	/**
	 * @param staticPageBuilder the staticPageBuilder to set
	 */
	public void setStaticPageBuilder(StaticPageBuilder staticPageBuilder) {
		this.staticPageBuilder = staticPageBuilder;
	}
}