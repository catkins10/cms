/*
 * Created on 2007-7-7
 *
 */
package com.yuanluesoft.cms.templatemanage.service.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLTitleElement;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteSubPage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteTemplateView;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.model.SynchSetDefaultTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.CssFile;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.model.StyleDefine;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.ZipUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateServiceImpl extends BusinessServiceImpl implements TemplateService {
	private Cache cache; //模板缓存
	protected final String TEMPLATE_FILE_NAME = "template.html"; //模板文件名称
	private String templatePath = Environment.getWebAppPath() + "cms/templates/"; //模板路径
	private String templateUrl = "/cms/templates/"; //模板路径
	private FileDownloadService fileDownloadService; //文件传输服务
	private HTMLParser htmlParser; //网页解析
	
	private StaticPageBuilder staticPageBuilder; //静态页面服务
	private ExchangeClient exchangeClient; //数据交换服务
	private AttachmentService templateAttachmentService; //模板附件服务
	private SiteTemplateThemeService siteTemplateThemeService; //站点模板主题服务
	private PageDefineService pageDefineService; //页面定义服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(!(record instanceof Template)) {
			return super.save(record);
		}
		Template template = (Template)record;
		List congenerTemplates = listCongenerTemplates(template, "" + template.getThemeId()); //同类模板
		if(congenerTemplates==null || congenerTemplates.isEmpty()) {
			template.setIsSelected('1');
		}
		//从缓存清除模板
		try {
			getCache().clear();
		}
		catch (CacheException e) {
			
		}
		super.save(record);
		//重建静态页面
		if(template.getIsSelected()=='1') { //没有有同类模板,那么当前模板是第一个模板
			rebuildStaticPageForFirstTemplate(template);
		}
		//同步更新
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return template;
	}
	
	/**
	 * 为第一个模板重建静态页面
	 * @param template
	 * @throws ServiceException
	 */
	private void rebuildStaticPageForFirstTemplate(Template template) throws ServiceException {
		if(siteTemplateThemeService==null) {
			return;
		}
		SiteTemplateTheme theme = (SiteTemplateTheme)load(SiteTemplateTheme.class, template.getThemeId());
		if(theme==null || theme.getType()!=TemplateThemeService.THEME_TYPE_COMPUTER) {
			return;
		}
		if(ListUtils.findObjectByProperty(theme.getUsages(), "isDefault", new Integer(1))!=null) { //是某个站点的默认主题
			rebuildStaticPage(template, null, template.getSiteId()); //更新使用父模板的页面
			return;
		}
		if(ListUtils.findObjectByProperty(theme.getUsages(), "temporaryOpening", new Integer(1))!=null) { //是某个站点临时启用的主题
			//检查页面是否需要实时重建静态页面
			SitePage sitePage = pageDefineService.getSitePage(template.getApplicationName(), template.getPageName());
			if(sitePage==null || sitePage.isRealtimeStaticPage()) {
				rebuildStaticPage(template, null, template.getSiteId()); //更新使用父模板的页面
				return;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof Template)) {
			return super.update(record);
		}
		Template template = (Template)record;
		//从缓存清除模板
		try {
			getCache().clear();
		}
		catch (CacheException e) {
			
		}
		//更新记录
		super.update(record);
		if(template.getIsSelected()=='1') { //当前模板是默认模板
			//更新静态页面
			rebuildStaticPage(template, template, template.getSiteId());
		}
		//同步更新
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return template;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(!(record instanceof Template)) {
			return;
		}
		Template template = (Template)record;
		//删除模板配置文件
		FileUtils.deleteDirectory(getTemplateDirectory(template.getId(), false));
		//从缓存清除模板
		try {
			getCache().clear();
		}
		catch (CacheException e) {
			
		}
		if(template.getIsSelected()=='1') { //当前模板是默认模板
			//获取同类模板，找到第一个作为默认模板
			List congenerTemplates = listCongenerTemplates(template, "" + template.getThemeId()); //同类模板
			if(congenerTemplates!=null && !congenerTemplates.isEmpty()) {
				Template congenerTemplate = (Template)congenerTemplates.get(0);
				congenerTemplate.setIsSelected('1');
				getDatabaseService().updateRecord(congenerTemplate);
			}
			//更新静态页面
			rebuildStaticPage(template, template, template.getSiteId());
		}
		//同步删除
		if(exchangeClient!=null) {
			exchangeClient.synchDelete(record, null, 2000);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#setDefaultTemplate(long)
	 */
	public void setDefaultTemplate(long templateId) throws ServiceException {
		Template currentTemplate = (Template)getDatabaseService().findRecordById(Template.class.getName(), templateId);
		List currentDefaultTemplates = new ArrayList(); //现在的默认模板
		List congenerTemplates = listCongenerTemplates(currentTemplate, "" + currentTemplate.getThemeId()); //获取同类的模板列表
		if(congenerTemplates!=null && !congenerTemplates.isEmpty()) {
			for(Iterator iterator = congenerTemplates.iterator(); iterator.hasNext();) {
				Template template = (Template)iterator.next();
				if(template.getId()==templateId) { //当前模板,不处理
					continue;
				}
				if(template.getIsSelected()=='1') { //现在的默认模板
					currentDefaultTemplates.add(template);
					template.setIsSelected('0');
					getDatabaseService().updateRecord(template);
				}
			}
		}
		currentTemplate.setIsSelected('1');
		getDatabaseService().updateRecord(currentTemplate);
		//从缓存清除模板
		try {
			getCache().clear();
		}
		catch (CacheException e) {
			
		}
		//更新静态页面
		if(currentDefaultTemplates.isEmpty()) {
			rebuildStaticPage(currentTemplate, null, currentTemplate.getSiteId());
		}
		else {
			for(Iterator iterator = currentDefaultTemplates.iterator(); iterator.hasNext();) {
				Template currentDefaultTemplate = (Template)iterator.next();
				rebuildStaticPage(currentTemplate, currentDefaultTemplate, currentTemplate.getSiteId());
			}
		}
	
		//同步设置默认模板
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(new SynchSetDefaultTemplate(templateId), null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchUpdate(com.yuanluesoft.jeaf.database.Record)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		if(object instanceof SynchSetDefaultTemplate) {
			SynchSetDefaultTemplate synchSetDafaultTemplate = (SynchSetDefaultTemplate)object;
			setDefaultTemplate(synchSetDafaultTemplate.getTemplateId());
			return;
		}
		super.synchUpdate(object, senderName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#uploadTemplate(long)
	 */
	public void uploadTemplate(long templateId) throws ServiceException {
		//获取模板文件
		List templateFiles = templateAttachmentService.list("cms/templatemanage", "templateUpload", templateId, false, 1, null);
		if(templateFiles==null || templateFiles.isEmpty()) {
			return;
		}
		String temporaryDirectory = FileUtils.createDirectory(Environment.getWebinfPath() + "temp/" + UUIDLongGenerator.generateId());
		try {
			//解压缩页面文件
			ZipUtils.unZip(((Attachment)templateFiles.get(0)).getFilePath(), temporaryDirectory);
			
			//查找目录中的网页文件
			File directory = new File(temporaryDirectory);
			File[] files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					name = name.toLowerCase();
					return name.endsWith(".html") || name.endsWith(".htm");
				}
			});
			String templateFileName = null;
			if(files!=null && files.length>0) {
				for(int i=0; i<files.length; i++) {
					String name = files[i].getName().toLowerCase();
					if(name.indexOf("tmplate")!=-1 || name.indexOf("模板")!=-1) {
						templateFileName = files[i].getPath();
						break;
					}
				}
				if(templateFileName==null) {
					templateFileName = files[0].getPath();
				}
			}
			if(templateFileName!=null) { //保存成名为TEMPLATE_FILE_NAME的模板文件
				String templateDirectory = getTemplateDirectory(templateId, true);
				//删除目录中原有的文件
				FileUtils.deleteFilesInDirectory(templateDirectory);
				//整理模板文件
				saveHtmlPageAs(templateFileName, templateDirectory + TEMPLATE_FILE_NAME, temporaryDirectory, templateDirectory, templateUrl + templateId + "/");
			}
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		finally {
			//删除上传目录
			FileUtils.deleteDirectory(temporaryDirectory);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#saveTemplateHTML(long, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void saveTemplateHTML(long templateId, String html, HttpServletRequest request) throws ServiceException {
		OutputStreamWriter fileWriter = null;
		FileOutputStream out = null;
		try {
			//清除页面中被IE自动加入的服务器地址
			html = StringUtils.removeServerPath(html, request);
			HTMLDocument templateDocument = htmlParser.parseHTMLString(html, "utf-8");
			//在head中加入屏蔽脚本错误提示的js引用
			htmlParser.appendDisableErrorAlertScript(templateDocument);
			String templateFilePath = getTemplateDirectory(templateId, true) + TEMPLATE_FILE_NAME;
			if(FileUtils.isExists(templateFilePath)) { //原来已经有模板
				//保存原来的模板到backup目录
				FileUtils.copyFile(templateFilePath, FileUtils.createDirectory(getTemplateDirectory(templateId, false) + "backup/") + TEMPLATE_FILE_NAME, true, false);
			}
			htmlParser.removeMeta(templateDocument, "cms.templateIds");
			htmlParser.removeMeta(templateDocument, "cms.templatePath");
			HTMLElement css = (HTMLElement)templateDocument.getElementById("clientCss");
			if(css!=null) {
				css.getParentNode().removeChild(css);
			}
			out = new FileOutputStream(templateFilePath);
			out.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
			fileWriter = new OutputStreamWriter(out, "utf-8");
			htmlParser.writeHTMLDocument(templateDocument, fileWriter, "utf-8");
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				fileWriter.close();
			}
			catch (Exception e) {
				
			}
			try {
				out.close();
			}
			catch (Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#restoreTemplateHTML(long)
	 */
	public void restoreTemplateHTML(long templateId) throws ServiceException {
		String backupTemplateFilePath = getTemplateDirectory(templateId, false) + "backup/" + TEMPLATE_FILE_NAME;
		if(!FileUtils.isExists(backupTemplateFilePath)) { //备份模板存在
			return;
		}
		//保存现在的模板到backup目录
		FileUtils.copyFile(getTemplateDirectory(templateId, false) + TEMPLATE_FILE_NAME, backupTemplateFilePath + ".1", true, false);
		//把备份模板拷贝到模板目录
		FileUtils.renameFile(backupTemplateFilePath, getTemplateDirectory(templateId, false) + TEMPLATE_FILE_NAME, true, false);
		FileUtils.renameFile(backupTemplateFilePath + ".1", backupTemplateFilePath, true, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#copyTemplate(long, long)
	 */
	public void copyTemplate(long toTemplateId, long fromTemplateId) throws ServiceException {
		//源目录
		String fromTemplateDirectory = getTemplateDirectory(fromTemplateId, false);
		if(!FileUtils.isExists(fromTemplateDirectory + TEMPLATE_FILE_NAME)) {
			return;
		}
		//目标目录
		String toTemplateDirectory = getTemplateDirectory(toTemplateId, true);
		//删除目录中原有的文件
		FileUtils.deleteFilesInDirectory(toTemplateDirectory);
		//拷贝除backup外的其他目录到export目录
		File[] files = new File(fromTemplateDirectory).listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].getName().equals("backup")) {
				continue;
			}
			if(files[i].getName().equals("css")) { //css目录
				String cssPath = FileUtils.createDirectory(toTemplateDirectory + files[i].getName());
				File[] cssFiles = files[i].listFiles();
				for(int j=0; j<cssFiles.length; j++) {
					//读取css文件,并修改图片、附件路径为相对目录
					String css = FileUtils.readStringFromFile(cssFiles[j].getAbsolutePath(), "UTF-8").replaceAll("/" + fromTemplateId + "/", "/" + toTemplateId + "/");
					//写入导出目录
					FileUtils.saveStringToFile(cssPath + cssFiles[j].getName(), css, "utf-8", true);
				}
			}
			else if(files[i].isDirectory()) {
				FileUtils.copyDirectory(files[i].getAbsolutePath(), FileUtils.createDirectory(toTemplateDirectory + files[i].getName()), true, true);
			}
		}
		//读取模板文件,并修改图片、附件路径为相对目录
		String templateHtml = FileUtils.readStringFromFile(fromTemplateDirectory + TEMPLATE_FILE_NAME, "UTF-8").replaceAll("/" + fromTemplateId + "/", "/" + toTemplateId + "/");
		//保存到目标目录
		FileUtils.saveStringToFile(toTemplateDirectory + TEMPLATE_FILE_NAME, templateHtml, "utf-8", true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#loadNormalTemplate(java.lang.String, java.lang.String, long)
	 */
	public void loadNormalTemplate(String applicationName, String pageName, long templateId) throws ServiceException {
		Template template = getTemplate(templateId);
		//获取主题
		TemplateTheme theme = (TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), template.getThemeId());
		//获取上级站点/栏目的页面模板
		Template parentTemplate = getParentTemplate(template, theme);
		TemplateTheme parentTemplateTheme;
		//获取系统预置的模版
		NormalTemplate normalTemplate = getNormalTemplateForTemplate(applicationName, pageName, theme==null ? TemplateThemeService.THEME_TYPE_COMPUTER : theme.getType(), template);
		if(parentTemplate!=null && parentTemplate.getId()!=template.getId() && 
		  (normalTemplate==null ||
		   (parentTemplateTheme=(TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), parentTemplate.getThemeId()))==null ||
		   normalTemplate.getThemeType()<=parentTemplateTheme.getType())) {
			copyTemplate(templateId, parentTemplate.getId());
			return;
		}
		//没有系统预置的模板
		if(normalTemplate==null) {
			return;
		}
		String templateDirectory = getTemplateDirectory(templateId, true);
		//删除目录中原有的文件
		FileUtils.deleteFilesInDirectory(templateDirectory);
		//拷贝目录
		File[] files = new File(normalTemplate.getTemplatePath()).listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				FileUtils.copyDirectory(files[i].getAbsolutePath(), FileUtils.createDirectory(templateDirectory + files[i].getName()), true, true);
			}
		}
		//拷贝模板文件
		String templateHtml = FileUtils.readStringFromFile(normalTemplate.getTemplatePath() + "template.html", "UTF-8")
									   .replaceAll("\\x7bCONTEXTPATH\\x7d/" + normalTemplate.getTemplatePath().substring(Environment.getWebAppPath().length()), templateUrl + templateId + "/")
									   .replaceAll("\\x7bCONTEXTPATH\\x7d", Environment.getContextPath());
		//解析模板页面
		HTMLDocument htmlDocument = htmlParser.parseHTMLString(templateHtml, "utf-8");
		//复制不在模板目录中的样式表文件
		NodeList links = htmlDocument.getElementsByTagName("link");
		for(int i=links==null ? -1 : links.getLength()-1; i>=0; i--) {
			HTMLLinkElement linkElement = (HTMLLinkElement)links.item(i);
			if(!"text/css".equalsIgnoreCase(linkElement.getType()) || //不是样式表
			   linkElement.getHref()==null || //没有指定URL
			   linkElement.getHref().indexOf("" + templateId)!=-1) { //已经在模板目录中
				continue;
			}
			//读取css文件
			String cssText = loadCssText(linkElement.getHref());
			String cssFileName = linkElement.getHref().substring(linkElement.getHref().lastIndexOf("/") + 1);
			//写到模板的css目录
			FileUtils.saveStringToFile(FileUtils.createDirectory(templateDirectory + "css") + cssFileName, cssText, "utf-8", true);
			//修改href
			linkElement.setHref(templateUrl + templateId + "/css/" + cssFileName);
		}
		OutputStreamWriter fileWriter = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(templateDirectory + TEMPLATE_FILE_NAME);
			out.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
			fileWriter = new OutputStreamWriter(out, "utf-8"); 
			htmlParser.writeHTMLDocument(htmlDocument, fileWriter, "utf-8");
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				fileWriter.close();
			}
			catch (Exception e) {
				
			}
			try {
				out.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getNormalFormHTML(java.lang.String, java.lang.String, int)
	 */
	public String getNormalFormHTML(String applicationName, String formName, int themeType) throws ServiceException {
		String path = getNormalTemplatePath(applicationName, formName, false, true, new int[]{themeType});
		if(path==null) {
			return null;
		}
		return FileUtils.readStringFromFile(path + "/template.html", "utf-8").replaceAll("\\{CONTEXTPATH\\}", Environment.getContextPath());
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getSubPageHTML(java.lang.String, java.lang.String, int)
	 */
	public String getSubPageHTML(String applicationName, String subPageName, int themeType) throws ServiceException {
		String path = getNormalTemplatePath(applicationName, subPageName, true, false, new int[]{themeType});
		if(path==null) {
			return null;
		}
		return FileUtils.readStringFromFile(path + "/template.html", "utf-8").replaceAll("\\{CONTEXTPATH\\}", Environment.getContextPath());
	}

	/**
	 * 获取预置模板路径
	 * @param applicationName
	 * @param pageName
	 * @param isForm
	 * @param themeType
	 * @return
	 */
	private String getNormalTemplatePath(String applicationName, String pageName, boolean isSubPage, boolean isForm, int[] themeTypes) {
		String[] directoryNames = {"", "phone", "wap", "client", "wechat"};
		String normalTemplatePath = Environment.getWebAppPath() + applicationName.toLowerCase() + "/template/";
		for(int i=themeTypes[0]; i>=0; i--) {
			String file = normalTemplatePath + 
						 (i==0 ? "" : directoryNames[i] + "/") + (isSubPage ? "subpage/" : (isForm ? "form/" : "")) +
						 pageName.toLowerCase() + "/";
			if(FileUtils.isExists(file + "template.html")) {
				themeTypes[0] = i;
				return file;
			}
			if(i==TemplateThemeService.THEME_TYPE_CLIENT) { //客户端,跳过WAP模板
				i--;
			}
			else if(i==TemplateThemeService.THEME_TYPE_WECHAT) { //微信,跳过客户端、WAP模板
				i -= 2;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#loadCssFile(java.lang.String)
	 */
	public CssFile loadCssFile(String cssUrl) throws ServiceException {
		if(cssUrl.indexOf(templateUrl)==-1) {
			return null;
		}
		return (CssFile)getDatabaseService().findRecordByHql("from CssFile CssFile where CssFile.cssUrl='" + JdbcUtils.resetQuot(cssUrl) + "'");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#loadCssText(java.lang.String)
	 */
	public String loadCssText(String cssUrl) throws ServiceException {
		if(cssUrl==null || cssUrl.isEmpty()) {
			return null;
		}
		int index = cssUrl.indexOf(templateUrl);
		if(index!=-1) { //在模板目录,自定义CSS
			//读取css文件
			return FileUtils.readStringFromFile(templatePath + cssUrl.substring(index + templateUrl.length()), "UTF-8");
		}
		else { //不在模板目录,系统预置的CSS
			//读取css文件
			String cssText = FileUtils.readStringFromFile(Environment.getWebAppPath() + cssUrl.substring(Environment.getContextPath().length()), "UTF-8");
			//转换引用的路径为绝对路径
			String cssPath = cssUrl.substring(0, cssUrl.lastIndexOf('/') + 1);
			cssText = cssText.replaceAll("url\\x28", "url(" + cssPath);
			return cssText;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#saveCssFile(long, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public void saveCssFile(long id, String cssName, String cssUrl, String fromCssFile, long siteId, String cssText) throws ServiceException {
		CssFile cssFile = new CssFile();
		cssFile.setId(id); //ID
		cssFile.setCssName(cssName); //名称
		cssFile.setCssUrl(cssUrl); //URL
		cssFile.setFromCssFile(fromCssFile); //源文件
		cssFile.setSiteId(siteId); //隶属站点ID
		
		String cssFileName = cssFile.getFromCssFile().substring(cssFile.getFromCssFile().lastIndexOf('/') + 1);
		if(cssFile.getFromCssFile().equals(cssFile.getCssUrl())) { //二者相同,新文件
			cssFile.setCssUrl(templateUrl + cssFile.getId() + "/" + cssFileName);
			getDatabaseService().saveRecord(cssFile);
		}
		else { //更新
			getDatabaseService().updateRecord(cssFile);
		}
		//保存css文件
		FileUtils.saveStringToFile(FileUtils.createDirectory(templatePath + cssFile.getId() + "/") + cssFileName, cssText, "utf-8", true);
		if(exchangeClient!=null) {
			//同步更新
			exchangeClient.synchUpdate(cssFile, null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#deleteCssFile(long)
	 */
	public void deleteCssFile(long cssFileId) throws ServiceException {
		CssFile cssFile = (CssFile)getDatabaseService().findRecordById(CssFile.class.getName(), cssFileId);
		if(cssFile==null) {
			return;
		}
		getDatabaseService().deleteRecord(cssFile);
		//删除目录
		FileUtils.deleteDirectory(templatePath + cssFile.getId());
		if(exchangeClient!=null) {
			//同步删除
			exchangeClient.synchDelete(cssFile, null, 2000);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getTemplate(long)
	 */
	public Template getTemplate(long templateId) throws ServiceException {
		return (Template)getDatabaseService().findRecordById(Template.class.getName(), templateId);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getTemplateHTMLDocument(java.lang.String, java.lang.String, long, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String applicationName, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		String cacheKey = applicationName + "/" + pageName + "/" + siteId + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		try {
			templateDocument = (HTMLDocument)getCache().get(cacheKey); //从缓存中读取模板
		}
		catch(CacheException e) {
			Logger.exception(e);
		}
		if(templateDocument!=null) {
			return (HTMLDocument)templateDocument.cloneNode(true);
		}
		//获取模板记录
		Template template = retrieveTemplate(Template.class.getName(), null, null, null, false, applicationName, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		NormalTemplate normalTemplate = getNormalTemplateHTMLDocument(applicationName, pageName, themeType);
		if(normalTemplate!=null && (template==null || normalTemplate.getThemeType()>template.getTheme().getType())) { //没有配置过模板、或者默认模板的主题类型更接近当前需要的主题类型
			templateDocument = normalTemplate.getHtmlDocument();
		}
		else if(template!=null) {
			templateDocument = getTemplateHTMLDocument(template.getId(), siteId, false, request);
		}
		if(templateDocument==null) {
			return null;
		}
		try {//写入缓存
			getCache().put(cacheKey, templateDocument);
		}
		catch(CacheException e) {
			Logger.exception(e);
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/**
	 * 获取模板记录
	 * @param templateClassName
	 * @param extraHqlJoin
	 * @param extraHqlWhere
	 * @param extraHqlOrderBy
	 * @param currentSiteOnly
	 * @param selectedOnly
	 * @param applicationName
	 * @param pageName
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param themeTypeOnly
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @return
	 * @throws ServiceException
	 */
	protected Template retrieveTemplate(String templateClassName, String extraHqlJoin, String extraHqlWhere, String extraHqlOrderBy, boolean currentSiteOnly, String applicationName, String pageName, long siteId, long themeId, int themeType, boolean themeTypeOnly, int pageWidth, final boolean flashSupport, final boolean temporaryOpeningFirst) throws ServiceException {
		if(themeId>0) { //指定了主题
			List templates = retrieveTemplatesByThemeIds(templateClassName, extraHqlJoin, extraHqlWhere, extraHqlOrderBy, currentSiteOnly, applicationName, pageName, siteId, themeId + "");
			if(templates!=null && !templates.isEmpty()) {
				Template template = (Template)templates.get(0);
				template.setTheme((TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), themeId)); //设置主题
				return template;
			}
		}
		//获取当前站点/栏目使用的主题所在站点
		int[] themeTypes = null;
		if(themeTypeOnly || themeType==TemplateThemeService.THEME_TYPE_COMPUTER) {
			themeTypes = new int[] {themeType};
		}
		else if(themeType==TemplateThemeService.THEME_TYPE_SMART_PHONE) {
			themeTypes = new int[] {TemplateThemeService.THEME_TYPE_COMPUTER, TemplateThemeService.THEME_TYPE_SMART_PHONE};
		}
		else if(themeType==TemplateThemeService.THEME_TYPE_WAP || themeType==TemplateThemeService.THEME_TYPE_CLIENT || themeType==TemplateThemeService.THEME_TYPE_WECHAT) {
			themeTypes = new int[] {TemplateThemeService.THEME_TYPE_COMPUTER, TemplateThemeService.THEME_TYPE_SMART_PHONE, themeType};
		}
		List themes = siteTemplateThemeService.listSiteThemes(siteId, themeTypes, -1, -1);
		if(themes==null || themes.isEmpty()) {
			return null;
		}
		//对主题排序,主题类型降序->临时启用降序->默认主题降序->页面宽度升序->是否支持flash降序
		Collections.sort(themes, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				SiteTemplateTheme theme0 = (SiteTemplateTheme)arg0;
				SiteTemplateTheme theme1 = (SiteTemplateTheme)arg1;
				//主题类型降序
				if(theme0.getType()<theme1.getType()) {
					return 1;
				}
				if(theme0.getType()>theme1.getType()) {
					return -1;
				}
				if(temporaryOpeningFirst) { //临时启用的优先,临时启用降序
					if(theme0.isTemporaryOpening() && !theme1.isTemporaryOpening()) {
						return -1;
					}
					if(!theme0.isTemporaryOpening() && theme1.isTemporaryOpening()) {
						return 1;
					}
				}
				//默认主题降序
				if(theme0.isDefaultTheme() && !theme1.isDefaultTheme()) {
					return -1;
				}
				if(!theme0.isDefaultTheme() && theme1.isDefaultTheme()) {
					return 1;
				}
				//页面宽度升序
				if(theme0.getPageWidth()>theme1.getPageWidth()) {
					return 1;
				}
				if(theme0.getPageWidth()>theme1.getPageWidth()) {
					return -1;
				}
				if(flashSupport) { //是否支持flash降序
					if(theme0.getFlashSupport()<theme1.getFlashSupport()) {
						return 1;
					}
					if(theme0.getFlashSupport()>theme1.getFlashSupport()) {
						return -1;
					}
				}
				return 0;
			}
		});
		String themeIds = ListUtils.join(themes, "id", ",", false);
		List templates = retrieveTemplatesByThemeIds(templateClassName, extraHqlJoin, extraHqlWhere, extraHqlOrderBy, currentSiteOnly, applicationName, pageName, siteId, themeIds);
		if(templates==null || templates.isEmpty()) {
			return null;
		}
		if(pageWidth<0) {
			pageWidth = 0;
		}
		//按模板主题排序
		templates = ListUtils.sortByProperty(templates, "themeId", themeIds);
		//获取页面宽度相同的模板
		Template computerTemplate = null;
		Template targetTemplate = null;
		int templateWidth = Integer.MAX_VALUE;
		for(Iterator iterator = templates.iterator(); iterator.hasNext();) {
			Template template = (Template)iterator.next();
			SiteTemplateTheme theme = (SiteTemplateTheme)ListUtils.findObjectByProperty(themes, "id", new Long(template.getThemeId()));
			template.setTheme(theme); //设置主题
			if(themeType==TemplateThemeService.THEME_TYPE_WAP || themeType==TemplateThemeService.THEME_TYPE_WECHAT || theme.getPageWidth()==pageWidth) { //WAP,或者宽度一致且模板类型一致
				return template;
			}
			if(computerTemplate==null && theme.getType()==TemplateThemeService.THEME_TYPE_COMPUTER) { //电脑模板
				computerTemplate = template;
			}
			if(pageWidth==0) {
				continue;
			}
			if(targetTemplate!=null && theme.getType()<themeType) {
				break;
			}
			if(Math.abs(theme.getPageWidth() - pageWidth) < Math.abs(templateWidth - pageWidth)) { //宽度差距比之前的小
				targetTemplate = template;
				templateWidth = theme.getPageWidth();
			}
		}
		if(targetTemplate!=null) {
			return targetTemplate;
		}
		if(computerTemplate!=null) { //电脑模板
			return computerTemplate;
		}
		return (Template)templates.iterator().next();
	}
	
	/**
	 * 按主题ID获取模板
	 * @param templateClassName
	 * @param extraHqlJoin
	 * @param extraHqlWhere
	 * @param extraHqlOrderBy
	 * @param currentSiteOnly
	 * @param applicationName
	 * @param pageName
	 * @param siteId
	 * @param themeIds
	 * @return
	 * @throws ServiceException
	 */
	private List retrieveTemplatesByThemeIds(String templateClassName, String extraHqlJoin, String extraHqlWhere, String extraHqlOrderBy, boolean currentSiteOnly, String applicationName, String pageName, long siteId, String themeIds) throws ServiceException {
		String className = templateClassName.substring(templateClassName.lastIndexOf('.') + 1);
		//生成from子句
		String hqlFrom = className + " " + className;
		if(extraHqlJoin!=null) {
			hqlFrom += extraHqlJoin;
		}
		if(!currentSiteOnly) { //允许使用上级站点/栏目的模板
			hqlFrom += ", WebDirectorySubjection WebDirectorySubjection";
		}
		//生成where子句
		String hqlWhere = className + ".themeId in (" + themeIds + ")" +
						  (applicationName==null ? "" : " and " + className + ".applicationName='" + JdbcUtils.resetQuot(applicationName) + "'") +
						  " and " + className + ".pageName='" + JdbcUtils.resetQuot(pageName) + "'" +
						  " and " + className + ".isSelected='1'";
		if(extraHqlWhere!=null) {
			hqlWhere += " and (" + extraHqlWhere + ")";
		}
		if(currentSiteOnly) { //不允许使用上级站点/栏目的模板
			hqlWhere += " and " + className + ".siteId=" + siteId;
		}
		else {
			hqlWhere += " and " + className + ".siteId=WebDirectorySubjection.parentDirectoryId" +
		     			" and WebDirectorySubjection.directoryId=" + siteId;
		}
		//生成order by子句
		String hqlOrderBy = extraHqlOrderBy;
		if(!currentSiteOnly) { //允许使用上级站点/栏目的模板
			hqlOrderBy = (hqlOrderBy==null ? "" : hqlOrderBy + ", ") + "WebDirectorySubjection.id";
		}
		
		//组合hql语句,并获取模板
		String hql = "select " + className +
					 " from " + hqlFrom +
					 " where " + hqlWhere +
					 (hqlOrderBy==null ? "" : " order by " + hqlOrderBy);
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 获取上级模板,默认按应用名称+页面名称查找上级站点/栏目的模板
	 * @param template
	 * @param theme
	 * @return
	 * @throws ServiceException
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		if(template==null || template.getSiteId()==0) {
			return null;
		}
		//获取父站点/栏目ID
		String hql = "select WebDirectory.parentDirectoryId from WebDirectory WebDirectory where WebDirectory.id=" + template.getSiteId();
		long parentDirectoryId = ((Number)getDatabaseService().findRecordByHql(hql)).longValue();
		template = retrieveTemplate(template.getClass().getName(), null, null, null, false, template.getApplicationName(), template.getPageName(), parentDirectoryId, template.getThemeId(), theme.getType(), false, theme.getPageWidth(), theme.getFlashSupport()==1, false);
		return template;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getNormalTemplateHTMLDocument(java.lang.String, java.lang.String)
	 */
	public NormalTemplate getNormalTemplateHTMLDocument(String applicationName, String pageName, int themeType) throws ServiceException {
		//查找应用的jsp文件目录
		int[] themeTypes = {themeType};
		String normalTemplatePath = getNormalTemplatePath(applicationName, pageName, false, false, themeTypes);
		if(normalTemplatePath==null) {
			return null;
		}
		HTMLDocument templateDocument = getTemplateHTMLDocument(Environment.getWebApplicationUrl() + "/" + normalTemplatePath.substring(Environment.getWebAppPath().length()));
		return new NormalTemplate(templateDocument, normalTemplatePath, themeTypes[0]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getTemplateHTMLDocument(java.lang.String)
	 */
	public HTMLDocument getTemplateHTMLDocument(String templatePath) throws ServiceException {
		String filePath = RequestUtils.getFilePath(templatePath + "template.html");
		String templateHtml = FileUtils.readStringFromFile(filePath, "UTF-8").replaceAll("\\x7bCONTEXTPATH\\x7d", Environment.getContextPath());
		HTMLDocument templateDocument = getHtmlParser().parseHTMLString(templateHtml, "utf-8");
		getHtmlParser().appendMeta(templateDocument, "cms.templatePath", templatePath);
		return templateDocument;
	}
	
	/**
	 * 为指定的模板加载预置的模板
	 * @param applicationName
	 * @param pageName
	 * @param themeTpe
	 * @param template
	 * @return
	 * @throws ServiceException
	 */
	protected NormalTemplate getNormalTemplateForTemplate(String applicationName, String pageName, int themeTpe, Template template) throws ServiceException {
		return getNormalTemplateHTMLDocument(applicationName, pageName, themeTpe);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#listSubTemplates(long, long)
	 */
	public List listSubTemplates(long siteId, long themeId) throws ServiceException {
		String hql = "from Template Template" +
					 " where Template.siteId=" + siteId +
					 " and Template.themeId=" + themeId +
					 " and Template.applicationName='cms/sitemanage'" +
					 " and Template.pageName='subTemplate'" +
					 " order by Template.templateName";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#clearCachedTemplate()
	 */
	public void clearCachedTemplate() throws ServiceException {
		//从缓存清除模板
		try {
			getCache().clear();
		}
		catch (CacheException e) {
			
		}
	}

	/**
	 * 获取同类的模板
	 * @param template
	 * @param themeIds
	 * @return
	 * @throws ServiceException
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		//获取同一个站点的模板列表
		String hql = "from Template Template" +
					 " where Template.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and Template.applicationName='" + JdbcUtils.resetQuot(template.getApplicationName()) + "'" +
					 " and Template.pageName='" + JdbcUtils.resetQuot(template.getPageName()) + "'" +
					 " and Template.siteId=" + template.getSiteId() +
					 " and Template.id!=" + template.getId();
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getInitializeTemplateHTMLDocument(long, long, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getInitializeTemplateHTMLDocument(long themeId, long siteId, boolean editMode, HttpServletRequest request) throws ServiceException {
		String html = "<html>\r\n" +
					  "<body></body>\r\n" +
					  "</html>";
		HTMLDocument templateDocument = htmlParser.parseHTMLString(html, "utf-8");
		if(editMode) {
			retrieveClientTemplateHTMLDocument(templateDocument, themeId, siteId, request);
		}
		return templateDocument;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#getTemplateHTMLDocument(long, long, boolean)
	 */
	public HTMLDocument getTemplateHTMLDocument(long templateId, long siteId, boolean editMode, HttpServletRequest request) throws ServiceException {
		List usedTemplateIds = new ArrayList();
		HTMLDocument templateDocument = getTemplateHTMLDocument(templateId, siteId, editMode, new ArrayList(), usedTemplateIds, request);
		if(templateDocument!=null) {
			htmlParser.removeMeta(templateDocument, "cms.templateIds");
			htmlParser.appendMeta(templateDocument, "cms.templateIds", ListUtils.join(usedTemplateIds, ",", false));
			if(editMode) {
				Template template = getTemplate(templateId);
				retrieveClientTemplateHTMLDocument(templateDocument, template.getThemeId(), template.getSiteId(), request);
			}
		}
		return templateDocument;
	}
	
	/**
	 * 重置客户端模版
	 * @param templateDocument
	 * @param themeId
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	private void retrieveClientTemplateHTMLDocument(HTMLDocument templateDocument, long themeId, long siteId, HttpServletRequest request) throws ServiceException {
		TemplateTheme theme = (TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), themeId);
		if(theme==null || theme.getType()!=TemplateThemeService.THEME_TYPE_CLIENT) { //不是客户端模版
			return;
		}
		if(templateDocument.getDoctype()==null) {
			DocumentType doctype = templateDocument.getImplementation().createDocumentType("html", null, null);
			templateDocument.appendChild(doctype);
		}
		HTMLElement css = (HTMLElement)templateDocument.getElementById("clientCss");
		if(css!=null) {
			css.getParentNode().removeChild(css);
		}
		//获取客户端主页模版
		HTMLDocument mainPageTemplate = getTemplateHTMLDocument("jeaf/client", "main", siteId, 0, theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false, request);
		if(mainPageTemplate==null) {
			return;
		}
		//获取样式配置
		List styleDefines = htmlParser.parseStyleDefines(htmlParser.getMeta(mainPageTemplate, "styleDefineMeta"));
		if(styleDefines!=null && !styleDefines.isEmpty()) {
			StyleDefine styleDefine = (StyleDefine)styleDefines.get(0);
			HTMLLinkElement link = htmlParser.appendCssFile(templateDocument, styleDefine.getCssUrl() + "?req=" + Math.random(), false);
			link.setId("clientCss");
		}
	}
	
	/**
	 * 获取模板
	 * @param templateId
	 * @param siteId
	 * @param editMode
	 * @param circularCheck 循环引用检查
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private HTMLDocument getTemplateHTMLDocument(long templateId, long siteId, boolean editMode, List circularCheck, List usedTemplateIds, HttpServletRequest request) throws ServiceException {
		//读取模板
		HTMLDocument templateDocument = null;
		try {
			String templateFilePath = getTemplateDirectory(templateId, false) + TEMPLATE_FILE_NAME;
			templateDocument = getHtmlParser().parseHTMLFile(templateFilePath);
			if(!editMode && templateDocument!=null && usedTemplateIds.isEmpty()) {
				getHtmlParser().removeMeta(templateDocument, "cms.templatePath");
				getHtmlParser().appendMeta(templateDocument, "cms.templatePath", templateUrl + templateId + "/");
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			return null;
		}
		usedTemplateIds.add("" + templateId);
		HTMLHeadElement head = htmlParser.getHTMLHeader(templateDocument, true);
		//从头部删除所有的"subTemplateHeadElement_"元素
		NodeList nodes = head.getChildNodes();
		if(nodes!=null && nodes.getLength()>0) {
			for(int i=nodes.getLength()-1; i>=0; i--) {
				Node node = nodes.item(i);
				String id = (node instanceof HTMLElement ? ((HTMLElement)node).getId() : null);
				if(id!=null && id.startsWith("subTemplateHeadElement_")) {
					head.removeChild(node);
				}
			}
		}
		//处理子模板DIV
		NodeList divs = templateDocument.getElementsByTagName("div");
		if(divs==null || divs.getLength()==0) {
			return templateDocument;
		}
		circularCheck.add(new Long(templateId));
		for(int i=divs.getLength()-1; i>=0; i--) {
			HTMLDivElement subTemplateElement = (HTMLDivElement)divs.item(i);
			if(!"subTemplate".equals(subTemplateElement.getId())) {
				continue;
			}
			//解析子模板属性
			String title = subTemplateElement.getTitle();
			String subTemplateName = StringUtils.getPropertyValue(title, "子模板");
			long subTemplateId = StringUtils.getPropertyLongValue(title, "ID", -1);
			boolean isInheritDisabled = "true".equals(StringUtils.getPropertyValue(title, "禁止继承"));
			if(!editMode && siteId>=0 && !isInheritDisabled) { //需要继承站点或栏目里面的同名子模板
				//查找当前站点或栏目里的同名子模板
				String hql = "select Template.id" +
					  " from Template Template, WebDirectorySubjection WebDirectorySubjection" +
					  " where Template.siteId=WebDirectorySubjection.parentDirectoryId" +
					  " and WebDirectorySubjection.directoryId=" + siteId +
					  " and Template.themeId=(select Template.themeId from Template Template where Template.id=" + templateId + ")" +
					  " and Template.templateName='" + subTemplateName + "'" +
					  " and Template.applicationName='cms/sitemanage'" +
					  " and Template.pageName='subTemplate'" +
					  " order by WebDirectorySubjection.id";
				Number id = (Number)getDatabaseService().findRecordByHql(hql);
				if(id!=null) {
					subTemplateId = id.longValue();
				}
			}
			//获取子模板
			HTMLDocument subTemplateDocument = null;
			if(circularCheck.indexOf(new Long(subTemplateId))==-1) { //检查是否循环引用
				int size = circularCheck.size();
				subTemplateDocument = getTemplateHTMLDocument(subTemplateId, siteId, editMode, circularCheck, usedTemplateIds, request);
				circularCheck = circularCheck.subList(0, size);
			}
			if(subTemplateDocument==null) { //子模板不存在
				//删除子模板对象
				subTemplateElement.getParentNode().removeChild(subTemplateElement);
				continue;
			}
			//引入子模板head里面的link,style,script元素
			HTMLHeadElement subTemplateHead = htmlParser.getHTMLHeader(subTemplateDocument, false);
			NodeList subTemplateHeadChildNodes = (subTemplateHead==null ? null : subTemplateHead.getChildNodes());
    		if(subTemplateHeadChildNodes!=null && subTemplateHeadChildNodes.getLength()>0) {
    			//获取新元素的插入位置
    			Node insertBefore = null;
    			nodes = head.getChildNodes();
    			if(nodes!=null && nodes.getLength()>0) {
    				for(int j=0; j<nodes.getLength(); j++) {
    					if((nodes.item(j) instanceof HTMLElement)) { //查找第一个HTMLElement
    						HTMLElement element = (HTMLElement)nodes.item(j);
    						if(element instanceof HTMLTitleElement) { //在title之后插入
    							insertBefore = (j<nodes.getLength()-1 ? nodes.item(j+1) : null);
    						}
    						else {
    							insertBefore = element;
    						}
    						break;
    					}
    				}
    			}
    			//引入子模板HEAD元素
    			for(int j=0; j<subTemplateHeadChildNodes.getLength(); j++) {
    				if(!(subTemplateHeadChildNodes.item(j) instanceof HTMLElement)) {
    					continue;
    				}
    				HTMLElement htmlElement = (HTMLElement)subTemplateHeadChildNodes.item(j);
    				String tagName = htmlElement.getTagName().toLowerCase();
    				if(",script,link,style,".indexOf("," + tagName + ",")==-1) {
    					continue;
    				}
    				//检查是否已经引入过
    				NodeList currentNodes = head.getElementsByTagName(tagName);
    				if(currentNodes!=null && currentNodes.getLength()>0) {
    					String attributeName = null;
    					String attributeValue = null;
    					String textContent = null;
    					if("script".equals(tagName)) {
    						attributeName = "src";
    					}
    					else if("link".equals(tagName)) {
    						attributeName = "href";
    					}
    					if(attributeName!=null) {
    						attributeValue = htmlElement.getAttribute(attributeName);
    						if("".equals(attributeValue)) {
    							attributeValue = null;
    						}
    					}
    					if(attributeValue==null) { //没有设置属性值
    						textContent = htmlParser.getTextContent(htmlElement);
    						if(textContent==null || textContent.equals("")) { //没有设置属性值,也没有内容
    							continue; //不引入
    						}
    					}
    					boolean same = false;
    					for(int k=0; k<currentNodes.getLength(); k++) {
    						HTMLElement currentElement = (HTMLElement)currentNodes.item(k);
    						if(attributeValue!=null) { //按属性比较
    							if(attributeValue.equals(currentElement.getAttribute(attributeName))) {
        							same = true;
        							break;
        						}
        					}
    						else { //按内容比较
    							if(textContent.equals(htmlParser.getTextContent(currentElement))) {
        							same = true;
        							break;
        						}
    						}
    					}
    					if(same) {
    						continue;
    					}
    				}
    				//引入子模板元素
    				htmlElement =(HTMLElement)templateDocument.importNode(htmlElement, true);
    				if(editMode) { //编辑模式
    					htmlElement.setId("subTemplateHeadElement_" + templateId);
    				}
    				if(insertBefore==null) {
    					head.appendChild(htmlElement);
    				}
    				else {
    					head.insertBefore(htmlElement, insertBefore);
    				}
    			}
	    	}
			NodeList subNodes = subTemplateDocument.getBody().getChildNodes();
			if(!editMode) { //非编辑模式,保留子模板DIV
				if(subNodes!=null && subNodes.getLength()>0) { //添加子模板元素
					for(int j=0; j<subNodes.getLength(); j++) {
						subTemplateElement.getParentNode().insertBefore(subTemplateElement.getOwnerDocument().importNode(subNodes.item(j), true), subTemplateElement);
					}
				}
				//删除子模板对象
				subTemplateElement.getParentNode().removeChild(subTemplateElement);
			}
			else { //编辑模式,保留子模板DIV
				htmlParser.setTextContent(subTemplateElement, null); //清空子模板内容
				if(subNodes!=null && subNodes.getLength()>0) { //添加子模板元素
					for(int j=0; j<subNodes.getLength(); j++) {
						subTemplateElement.appendChild(subTemplateElement.getOwnerDocument().importNode(subNodes.item(j), true));
					}
				}
			}
		}
		return templateDocument;
	}
	
	/**
	 * 获取模板目录
	 * @param templateId
	 * @param mkdir
	 * @return
	 */
	protected String getTemplateDirectory(long templateId, boolean mkdir) {
		if(mkdir) {
			FileUtils.createDirectory(templatePath + templateId + "/");
		}
		return templatePath + templateId + "/";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#exportTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, long)
	 */
	public synchronized void exportTemplate(HttpServletRequest request, HttpServletResponse response, long templateId) throws ServiceException {
		//获取模板信息
		Template template = (Template)getDatabaseService().findRecordById(Template.class.getName(), templateId);
		if(template==null) {
			return;
		}
		String templateDirectory = getTemplateDirectory(templateId, false);
		String exportDirectory = null;
		String zipFileName = null;
		try {
			//新建导出目录
			exportDirectory = FileUtils.createDirectory(templateDirectory + "export");
			//拷贝除backup外的其他目录到export目录
			File[] files = new File(templateDirectory).listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].getName().equals("backup") || files[i].getName().equals("export")) {
					continue;
				}
				if(files[i].getName().equals("css")) { //css目录
					String cssPath = FileUtils.createDirectory(exportDirectory + files[i].getName());
					File[] cssFiles = files[i].listFiles();
					for(int j=0; j<cssFiles.length; j++) {
						//读取css文件,并修改图片、附件路径为相对目录
						String css = FileUtils.readStringFromFile(cssFiles[j].getAbsolutePath(), "UTF-8").replaceAll(templateUrl + templateId + "/", "../");
						//写入导出目录
						FileUtils.saveStringToFile(cssPath + cssFiles[j].getName(), css, "utf-8", true);
					}
				}
				else if(files[i].isDirectory()) {
					FileUtils.copyDirectory(files[i].getAbsolutePath(), FileUtils.createDirectory(exportDirectory + files[i].getName()), true, true);
				}
			}
			//读取模板文件,并修改图片、附件路径为相对目录
			String templateHtml = FileUtils.readStringFromFile(templateDirectory + TEMPLATE_FILE_NAME, "UTF-8").replaceAll(templateUrl + templateId + "/", "");
			templateHtml = templateHtml.replaceAll(".css\\?seq=[\\d]*\"", ".css\"");
			//保存到导出目录
			FileUtils.saveStringToFile(exportDirectory + TEMPLATE_FILE_NAME, templateHtml, "utf-8", true);
			//压缩
			zipFileName = templateDirectory + template.getTemplateName() + "_" + DateTimeUtils.formatDate(DateTimeUtils.date(), null) + ".zip";
			ZipUtils.zip(zipFileName, exportDirectory);
			//下载
			fileDownloadService.httpDownload(request, response, zipFileName, null, true, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			if(exportDirectory!=null) {
				FileUtils.deleteFile(zipFileName);
			}
			if(exportDirectory!=null) {
				FileUtils.deleteDirectory(exportDirectory);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#batchCopyTemplate(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void batchCopyTemplate(long sourceTemplateId, String targetPageNames, SessionInfo sessionInfo) throws ServiceException {
		if(targetPageNames==null || targetPageNames.isEmpty()) {
			return;
		}
		BusinessDefineService businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
		ViewDefineService viewDefineService = (ViewDefineService)Environment.getService("viewDefineService");
		PageDefineService pageDefineService = (PageDefineService)Environment.getService("pageDefineService");
		Template sourceTemplate = getTemplate(sourceTemplateId); //源模板
		TemplateTheme theme = (TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), sourceTemplate.getThemeId());
		SiteSubPage subPage;
		String[] pageNames = targetPageNames.split(",");
		boolean subPageEmbeded = true; //是否有引入子模板
		for(int i=0; i<pageNames.length; i++) {
			try {
				String[] values = pageNames[i].split("__");
				SitePage sitePage = pageDefineService.getSitePage(values[0], values[1]);
				SiteApplication siteApplication = pageDefineService.getSiteApplication(sitePage.getApplicationName());
				Template template;
				if(sitePage.getTemplateView()==null && siteApplication.getTemplateView()==null) { //没有配置模板视图
					template = new Template();
				}
				else {
					SiteTemplateView siteTemplateView = (sitePage.getTemplateView()==null ? siteApplication.getTemplateView() : sitePage.getTemplateView());
					View view = viewDefineService.getView((siteTemplateView.getViewApplication()==null ? siteApplication.getName() : siteTemplateView.getViewApplication()), siteTemplateView.getViewName(), sessionInfo);
					template = (Template)Class.forName(view.getPojoClassName()).newInstance();
				}
				//创建模板记录
				template.setId(UUIDLongGenerator.generateId()); //ID
				template.setTemplateName(sitePage.getTitle()); //模板名称
				template.setApplicationName(sitePage.getApplicationName()); //应用名称
				template.setPageName(sitePage.getName()); //页面名称
				template.setSiteId(sourceTemplate.getSiteId()); //站点/栏目ID
				template.setIsSelected('0'); //是否选中,是否选中
				template.setLastModified(DateTimeUtils.now()); //最后修改时间
				template.setLastModifierId(sessionInfo.getUserId()); //最后修改人ID
				template.setLastModifier(sessionInfo.getUserName()); //最后修改人姓名
				template.setThemeId(sourceTemplate.getThemeId()); //主题ID
				
				//拷贝模板HTML
				copyTemplate(template.getId(), sourceTemplateId);
				
				//如果有子页面,替换子页面
				if(subPageEmbeded && (subPage = (SiteSubPage)ListUtils.findObjectByProperty(sitePage.getSubPages(), "type", "template"))!=null) {
					//读取模板HTML
					String templateFilePath = getTemplateDirectory(template.getId(), false) + TEMPLATE_FILE_NAME;
					String templateHtml = FileUtils.readStringFromFile(templateFilePath, "utf-8");
					int beginIndex = templateHtml.lastIndexOf("<!-- subPage begin -->");
					if(beginIndex==-1) {
						subPageEmbeded = false;
					}
					else {
						beginIndex += "<!-- subPage begin -->".length();
						int endIndex = templateHtml.indexOf("<!-- subPage end -->", beginIndex);
						if(endIndex==-1) {
							subPageEmbeded = false;
						}
						else {
							//读取子页面HTML
							FileUtils.saveStringToFile(templateFilePath, templateHtml.substring(0, beginIndex) + getSubPageHTML(sitePage.getApplicationName(), subPage.getName(), theme.getType()) + templateHtml.substring(endIndex), "utf-8", true);
						}
					}
				}
				//保存模板记录
				BusinessObject businessObject = businessDefineService.getBusinessObject(template.getClass());
				BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null || businessObject.getBusinessServiceName().isEmpty() ? "businessService" : businessObject.getBusinessServiceName());
				businessService.save(template);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/**
	 * 保存HTML文件为另一个文件
	 * @param fromFileName
	 * @param toFileName
	 * @throws ServiceException
	 */
	private void saveHtmlPageAs(String pageFileName, String newPageFileName, String resourceDirectory, String templateDirectory, String templateBaseURL) throws ServiceException {
		//解析HTML文件
		HTMLDocument htmlDocument = htmlParser.parseHTMLFile(pageFileName);
		//遍历所有的对象
		updateHTMLElement(htmlDocument, resourceDirectory, templateDirectory, templateBaseURL, new HashMap());
		//保存HTML文件,保持原有的编码
		htmlParser.saveHTMLDocumentToFile(htmlDocument, newPageFileName, "UTF-8");
	}

	/**
	 * 更新HTML元素的属性
	 * @param element
	 * @param pageDirectory
	 * @param templateDirectory
	 * @param templateBaseURL
	 * @throws ServiceException
	 */
	private void updateHTMLElement(Node element, String resourceDirectory, String templateDirectory, String templateBaseURL, Map fileMap) throws ServiceException {
		String tagName = element.getNodeName().toUpperCase();
		if("IMG".equals(tagName)) { //图片
			moveResource(element, "src", resourceDirectory, "images", templateDirectory, templateBaseURL, fileMap);
		}
		else if("LINK".equals(tagName)) { //样式表
			Node attribute = element.getAttributes().getNamedItem("type");
			if(attribute!=null && "text/css".equals(attribute.getNodeValue())) {
				MoveResource moveResourceModel = moveResource(element, "href", resourceDirectory, "css", templateDirectory, templateBaseURL, fileMap);
				if(moveResourceModel!=null) { //移动CSS文件中的图片文件
					String cssText = FileUtils.readStringFromFile(moveResourceModel.getNewResourceFileName(), "UTF-8");
					if(cssText!=null) {
						String newCssText = updateCssText(cssText, moveResourceModel.getOldResourceFileName().substring(0, moveResourceModel.getOldResourceFileName().lastIndexOf('/') + 1), templateDirectory, templateBaseURL);
						FileUtils.saveStringToFile(moveResourceModel.getNewResourceFileName(), (newCssText==null ? cssText : newCssText), "UTF-8", true);
					}
				}
				((HTMLLinkElement)element).setCharset("UTF-8"); //设置字符集
			}
		}
		else if("SCRIPT".equals(tagName)) { //脚本
			HTMLScriptElement scriptElement = (HTMLScriptElement)element;
			if(scriptElement.getSrc()!=null) {
				MoveResource moveResourceModel = moveResource(element, "src", resourceDirectory, "js", templateDirectory, templateBaseURL, fileMap);
				if(moveResourceModel!=null) { //移动CSS文件中的图片文件
					String js = FileUtils.readStringFromFile(moveResourceModel.getNewResourceFileName(), "UTF-8");
					FileUtils.saveStringToFile(moveResourceModel.getNewResourceFileName(), js, "UTF-8", true);
				}
				scriptElement.setCharset("UTF-8");
			}
		}
		else if("INPUT".equals(tagName)) { //输入框,type="image"
			HTMLInputElement inputElement = (HTMLInputElement)element;
			if(inputElement.getSrc()!=null && inputElement.getType()!=null && inputElement.getType().toLowerCase().equals("image")) {
				moveResource(element, "src", resourceDirectory, "images", templateDirectory, templateBaseURL, fileMap);
			}
		}
		else if("IFRAME".equals(tagName) || "FRAME".equals(tagName)) { //帧
			MoveResource moveResourceModel = moveResource(element, "src", resourceDirectory, "pages", templateDirectory, templateBaseURL, fileMap);
			if(moveResourceModel!=null) { //更新被链接页面
				saveHtmlPageAs(moveResourceModel.getNewResourceFileName(), moveResourceModel.getNewResourceFileName(), moveResourceModel.getOldResourceFileName().substring(0, moveResourceModel.getOldResourceFileName().lastIndexOf('/') + 1), templateDirectory, templateBaseURL);
			}
		}
		else if("STYLE".equals(tagName)) { //直接写在页面上的样式表
			//更新样式表中的图片文件路径
			Node cssTextNode = element.getFirstChild();
			if(cssTextNode!=null) {
				String cssText = cssTextNode.getNodeValue();
				if((cssText=updateCssText(cssText, resourceDirectory, templateDirectory, templateBaseURL))!=null) {
					cssTextNode.setNodeValue(cssText);
				}
			}
		}
		else if("A".equals(tagName)) { //检查A,如果id="recordList",根据名称查找站点/栏目ID,更新站点/栏目ID列表,并初始化站点栏目格式
			//TODO
		}
		//其他,检查是否有样式,如果有更新引用到的图片路径
		Node attribute = (element.getAttributes()==null ? null : element.getAttributes().getNamedItem("style"));
		if(attribute!=null) {
			String cssText = attribute.getNodeValue();
			if((cssText=updateCssText(cssText, resourceDirectory, templateDirectory, templateBaseURL))!=null) {
				attribute.setNodeValue(cssText);
			}
		}
		//检查background属性
		if(element.getAttributes()!=null && element.getAttributes().getNamedItem("background")!=null) {
			moveResource(element, "background", resourceDirectory, "images", templateDirectory, templateBaseURL, fileMap);
		}
		//递归更新子元素
		Node childElement = element.getFirstChild();
		while(childElement!=null) {
			updateHTMLElement(childElement, resourceDirectory, templateDirectory, templateBaseURL, fileMap);
			childElement = childElement.getNextSibling();
		}
	}
	
	/**
	 * 移动引用到的文件,如果移动成功,返回移动后的文件路径
	 * @param element
	 * @param attributeName
	 * @param pageDirectory
	 * @param folderName
	 * @param templateDirectory
	 * @param templateBaseURL
	 * @return
	 */
	private MoveResource moveResource(Node element, String attributeName, String resourceDirectory, String folderName, String templateDirectory, String templateBaseURL, Map fileMap) {
		Node attribute = element.getAttributes().getNamedItem(attributeName);
		if(attribute==null) {
			return null;
		}
		String fileName = attribute.getNodeValue(); 
		if(fileName==null) {
			return null;
		}
		fileName = fileName.replaceAll("\\x5C", "/");
		String filePath = resourceDirectory + fileName;
		MoveResource moveResourceModel = new MoveResource();
		moveResourceModel.setOldResourceFileName(filePath);
		File oldFile = new File(filePath);
		if(oldFile.isDirectory()) {
			return null; //是目录,说明被链接文件为空
		}
		if(!oldFile.exists()) { //文件已经被移动
			//从历史记录里面获取被移动文件的URL
			filePath = (String)fileMap.get(new File(filePath).getPath());
			if(filePath!=null) {
				attribute.setNodeValue(filePath);
			}
			return null;
		}
		else {
			//移动文件
			filePath = FileUtils.moveFile(filePath, FileUtils.createDirectory(templateDirectory + folderName), false, true);
			filePath = filePath.replaceAll("\\x5C", "/");
			moveResourceModel.setNewResourceFileName(filePath);
			filePath = templateBaseURL + folderName + "/" + filePath.substring(filePath.lastIndexOf('/') + 1);
			fileMap.put(oldFile.getPath(), filePath); //添加新文件的URL到历史记录中
			attribute.setNodeValue(filePath);
			return moveResourceModel;
		}
	}
	
	/**
	 * 移动CSS中的图片文件
	 * @param templateId
	 * @param fromDirectory
	 * @param toDirectory
	 * @param cssText
	 * @return
	 */
	private String updateCssText(String cssText, String cssFileDirectory, String templateDirectory, String templateBaseURL) {
		String newCssText = "";
		int beginIndex = cssText.indexOf("url(");
		int endIndex;
		int prevEndIndex = 0;
		String quotation;
		String fileName;
		for(beginIndex = cssText.indexOf("url("); beginIndex>0; beginIndex = cssText.indexOf("url(", endIndex)) {
			quotation = cssText.charAt(beginIndex + 4) + "";
			if(!quotation.equals("'") && !quotation.equals("\"")) {
				quotation = "";
			}
			beginIndex += 4 + quotation.length();
			endIndex = cssText.indexOf(quotation + ")", beginIndex);
			if(endIndex - beginIndex>200) {
				return null;
			}
			fileName = cssText.substring(beginIndex, endIndex).replaceAll("\\x2520", " ").replaceAll("\\x5C", "/");
			endIndex += 1 + quotation.length();
			//移动文件
			String filePath = cssFileDirectory + fileName;
			filePath = FileUtils.moveFile(filePath, FileUtils.createDirectory(templateDirectory + "images"), false, true);
			if(filePath==null) {
				if(fileName.indexOf(':')!=-1) {
					continue;
				}
				fileName = fileName.substring(fileName.lastIndexOf('/') + 1).replaceAll(" ", "%20");
			}
			else {
				fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1).replaceAll(" ", "%20");
			}
			newCssText += cssText.substring(prevEndIndex, beginIndex);
			newCssText += templateBaseURL + "images/" + fileName;
			newCssText += quotation + ")";
			prevEndIndex = endIndex;
		}
		return (prevEndIndex==0 ? null : newCssText + cssText.substring(prevEndIndex));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateService#rebuildStaticPage(com.yuanluesoft.cms.templatemanage.pojo.Template, com.yuanluesoft.cms.templatemanage.pojo.Template, long)
	 */
	public void rebuildStaticPage(Template currentTemplate, Template referenceTemplate, long siteId) throws ServiceException {
		if(!siteTemplateThemeService.isTypeOf(currentTemplate.getThemeId(), TemplateThemeService.THEME_TYPE_COMPUTER)) {
			return;
		}
		staticPageBuilder.rebuildPageForTemplate(currentTemplate.getApplicationName(), currentTemplate.getPageName(), (referenceTemplate==null ? 0 : referenceTemplate.getId()), siteId, true);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("linkOpenModes".equals(itemsName)) {
			List items = new ArrayList();
			items.add(new String[]{"新窗口", "newWindow"});
			//if(TemplateThemeService.THEME_TYPE_CLIENT==RequestUtils.getParameterIntValue(request, "themeType")) {
			//	items.add(new String[]{"记录窗口", "recordWindow"});
			//}
			items.add(new String[]{"本窗口", "currentWindow"});
			items.add(new String[]{"对话框", "dialog"});
			return items;
		}
		else if("viewRecordLinks".equals(itemsName)) { //视图记录链接
			try {
				View view = (View)PropertyUtils.getProperty(bean, "view");
				return ListUtils.generatePropertyList(ListUtils.getSubListByProperty(view.getLinks(), "type", "recordLink"), "title");
			} 
			catch (Exception e) {
				return null;
			}
		}
		else if("recordLinks".equals(itemsName)) { //记录链接
			try {
				View view = pageDefineService.getRecordList(request.getParameter("applicationName"), request.getParameter("recordListName"), "true".equals(request.getParameter("privateRecordList")), request.getParameter("recordClassName"), sessionInfo);
				return ListUtils.generatePropertyList(ListUtils.getSubListByProperty(view.getLinks(), "type", "recordLink"), "title");
			} 
			catch (Exception e) {
				return null;
			}
		}
		else if("embedViews".equals(itemsName)) { //内嵌记录列表
			try {
				View view = (View)PropertyUtils.getProperty(bean, "view");
				List items = new ArrayList();
				String embedViews = (String)view.getExtendParameter("embedViews");
				//获取内嵌的视图
		    	if(embedViews!=null) {
		    		ViewDefineService viewDefineService = (ViewDefineService)Environment.getService("viewDefineService");
		    		String[] names = embedViews.split(",");
		    		for(int i=0; i<names.length; i++) {
		    			String[] viewParameters = names[i].split("\\x7c");
		    			items.add(viewDefineService.getView(viewParameters.length==1 ? view.getApplicationName() : viewParameters[0], viewParameters[viewParameters.length-1], sessionInfo));
		    		}
		    	}
		    	//插入私有记录列表
		    	List componentsFields = FieldUtils.listRecordFields(view.getPojoClassName(), "components,attachment,video,image", null, null, "none", true, false, false, false, 0);
		    	for(Iterator iterator = componentsFields==null ? null : componentsFields.iterator(); iterator!=null && iterator.hasNext();) {
		    		Field field = (Field)iterator.next();
		    		if(!"false".equals(field.getParameter("recordList"))) { //允许作为记录列表
		    			View privateRecordList = pageDefineService.getRecordList(null, field.getName(), true, view.getPojoClassName(), sessionInfo);
		    			privateRecordList.setExtendParameter("private", "true");
		    			privateRecordList.setExtendParameter("recordClassName", view.getPojoClassName());
		    			items.add(privateRecordList);
		    		}
		    	}
		    	for(int i=0; i<items.size(); i++) {
		    		View embedView = (View)items.get(i);
		    		String privateRecordList = (String)embedView.getExtendParameter("private");
		    		items.set(i, new String[]{embedView.getTitle(), "{applicationName: '" + embedView.getApplicationName() + "'," +
		    					 									" recordListName: '" + embedView.getName() + "'," +
		    					 									" recordListTitle: '" + embedView.getTitle() + "'" +
		    					 									("true".equals(privateRecordList) ? ", privateRecordList: 'true', recordClassName: '" + embedView.getExtendParameter("recordClassName") + "'" : "") + "}"
		    					 			 });
		    	}
		    	return items;
			}
			catch (Exception e) {
				return null;
			}
		}
		else if("fields".equals(itemsName) || "formFields".equals(itemsName)) { //字段列表
			List items = new ArrayList();
			String fieldName;
			if("formFields".equals(itemsName)) { //表单字段
				//获取表单定义
				FormDefineService formDefineService = (FormDefineService)Environment.getService("formDefineService");
				Form formDefine = formDefineService.loadFormDefine(request.getParameter("applicationName"), request.getParameter("formName"));
				items = FieldUtils.listFormFields(formDefine, null, null, null, null, false, false, false, false, 0);
				FieldUtils.sortFieldsByInputMode(items, "readonly,hidden,none,password");
			}
			else if((fieldName = request.getParameter("fieldName"))!=null) { //插入指定的字段
	    		items.add(new Field(fieldName, request.getParameter("fieldTitle"), request.getParameter("fieldType"), null, null, false, true));
	    	}
	    	else {
	    		String applicationName = request.getParameter("applicationName");
	    		String recordListName = request.getParameter("recordListName");
	    		String pageName = request.getParameter("pageName");
		    	if(recordListName!=null) { //按记录列表获取字段列表
		    		items = pageDefineService.listRecordListFields(applicationName, recordListName, "true".equals(request.getParameter("privateRecordList")), request.getParameter("recordClassName"));
		    	}
		    	else if(pageName!=null) {
		    		items = pageDefineService.listPageFields(applicationName, pageName);
				}
		    	FieldUtils.sortFieldsByInputMode(items, "hidden,none,password");
	    	}
			if(items==null || items.isEmpty()) {
				return null;
			}
			for(int i=0; i<items.size(); i++) {
	    		Field field = (Field)items.get(i);
	    		String itemsText = FieldUtils.getSelectItemsText(field, null, request);
	    		String checkboxLabel = (String)field.getParameter("label");
	    		String checkboxValue = (String)field.getParameter("value");
	    		String templateExtendURL = (String)field.getParameter("templateExtendURL");
	   		    items.set(i, new String[]{field.getTitle(), "{name: '" + field.getName() + "'," +
	    													" title: '" + field.getTitle() + "'," +
	    					 								" type: '" + field.getType() + "'," +
	    					 								" inputMode: '" + field.getInputMode() + "'" +
	    					 								(checkboxLabel==null ? "" : ", checkboxLabel: '" + checkboxLabel + "'") +
	    					 								(checkboxValue==null ? "" : ", checkboxValue: '" + checkboxValue + "'") +
	    					 								(itemsText==null ? "" : ", itemsText: '" + itemsText.replaceAll("\\\\0", "\\\\\\\\0").replaceAll("'", "\\\\\\\\'") + "'") +
	    					 								(templateExtendURL==null ? "" : ", templateExtendURL: '" + Environment.getContextPath() + templateExtendURL + "'") + "}"});
	    	}
	    	return items;
		}
		else if("formButtons".equals(itemsName)) { //表单按钮列表
			//获取表单定义
			FormDefineService formDefineService = (FormDefineService)Environment.getService("formDefineService");
			Form formDefine = formDefineService.loadFormDefine(request.getParameter("applicationName"), request.getParameter("formName"));
			return ListUtils.generatePropertyList(formDefine.getActions(), "title");
		}
		else if("cssFiles".equals(itemsName)) { //css文件列表
			String normalCssFile = (String)request.getAttribute("normalCssFile");
			if(normalCssFile==null || normalCssFile.isEmpty()) {
				return null;
			}
			//根据模板主题类型,检查是否有以“[模板主题类型名称,如:client,phone]”开头的css
			int themeType = RequestUtils.getParameterIntValue(request, "themeType");
	    	String[] normalCssFiles = normalCssFile.split(",");
	    	if(normalCssFiles.length>0) {
	    		String[] themeTypeNames = {"", "phone", "wap", "client", "wechat"};
	    		normalCssFile = null;
	    		for(int i=themeType; i>0 && normalCssFile==null; i--) {
	    			for(int j=normalCssFiles.length-1; j>0 && normalCssFile==null; j--) {
	    				if(normalCssFiles[j].startsWith("[" + themeTypeNames[i] + "]")) {
	    					normalCssFile = normalCssFiles[j].substring(themeTypeNames[i].length() + 2);
	    				}
	    			}
	    			if(i==TemplateThemeService.THEME_TYPE_CLIENT) { //客户端,跳过WAP模板
	    				i--;
	    			}
	    			else if(i==TemplateThemeService.THEME_TYPE_WECHAT) { //微信,跳过客户端、WAP模板
	    				i -= 2;
	    			}
	    		}
	    		if(normalCssFile==null) {
	    			normalCssFile = normalCssFiles[0];
	    		}
	    	}
	    	normalCssFile = Environment.getContextPath() + normalCssFile;
			//获取自定义的CSS文件
			List cssFiles = getDatabaseService().findRecordsByHql("select CssFile.cssName, CssFile.cssUrl from CssFile CssFile where CssFile.fromCssFile='" + JdbcUtils.resetQuot(normalCssFile) + "' order by CssFile.cssName"); // and CssFile.siteId=" + siteId + "
			if(cssFiles==null) {
				cssFiles = new ArrayList();
			}
			//添加默认的CSS
			cssFiles.add(0, new String[]{"默认CSS", normalCssFile});
			return cssFiles;
		}
		else if("subPages".equals(itemsName)) { //子页面列表
			SitePage sitePage = pageDefineService.getSitePage(request.getParameter("applicationName"), request.getParameter("pageName"));
			List items = new ArrayList();
			for(Iterator iterator = sitePage.getSubPages()==null ? null : sitePage.getSubPages().iterator(); iterator!=null && iterator.hasNext();) {
				SiteSubPage subPage = (SiteSubPage)iterator.next();
				items.add(new String[]{subPage.getTitle(), subPage.getName()});
			}
			return items.isEmpty() ? null : items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * 移动资源
	 * @author linchuan
	 */
	protected class MoveResource {
		private String oldResourceFileName;
		private String newResourceFileName;
		
		/**
		 * @return Returns the newResourceFileName.
		 */
		public String getNewResourceFileName() {
			return newResourceFileName;
		}
		/**
		 * @param newResourceFileName The newResourceFileName to set.
		 */
		public void setNewResourceFileName(String newResourceFileName) {
			this.newResourceFileName = newResourceFileName;
		}
		/**
		 * @return Returns the oldResourceFileName.
		 */
		public String getOldResourceFileName() {
			return oldResourceFileName;
		}
		/**
		 * @param oldResourceFileName The oldResourceFileName to set.
		 */
		public void setOldResourceFileName(String oldResourceFileName) {
			this.oldResourceFileName = oldResourceFileName;
		}
	}
	
	/**
	 * @return Returns the templatePath.
	 */
	public String getTemplatePath() {
		return templatePath;
	}
	/**
	 * @return Returns the templateUrl.
	 */
	public String getTemplateUrl() {
		return templateUrl;
	}
	/**
	 * @param templateUrl The templateUrl to set.
	 */
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	/**
	 * @param templatePath The templatePath to set.
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = FileUtils.createDirectory(templatePath); //创建模板目录
	}
	/**
	 * @return Returns the htmlParser.
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}
	/**
	 * @param htmlParser The htmlParser to set.
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}

	/**
	 * @return the templateAttachmentService
	 */
	public AttachmentService getTemplateAttachmentService() {
		return templateAttachmentService;
	}

	/**
	 * @param templateAttachmentService the templateAttachmentService to set
	 */
	public void setTemplateAttachmentService(
			AttachmentService templateAttachmentService) {
		this.templateAttachmentService = templateAttachmentService;
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

	/**
	 * @return the siteTemplateThemeService
	 */
	public SiteTemplateThemeService getSiteTemplateThemeService() {
		return siteTemplateThemeService;
	}

	/**
	 * @param siteTemplateThemeService the siteTemplateThemeService to set
	 */
	public void setSiteTemplateThemeService(
			SiteTemplateThemeService siteTemplateThemeService) {
		this.siteTemplateThemeService = siteTemplateThemeService;
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
}