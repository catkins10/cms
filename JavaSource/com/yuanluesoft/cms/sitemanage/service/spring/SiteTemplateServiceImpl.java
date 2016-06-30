package com.yuanluesoft.cms.sitemanage.service.spring;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplate;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateService;
import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 站点模板服务
 * @author linchuan
 *
 */
public class SiteTemplateServiceImpl extends TemplateServiceImpl implements SiteTemplateService {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof SiteTemplate) {
			SiteTemplate siteTemplate = (SiteTemplate)record;
			if(siteTemplate!=null) {
				if(siteTemplate.getColumnIds()!=null && siteTemplate.getColumnIds().startsWith(",") && siteTemplate.getColumnIds().endsWith(",")) {
					siteTemplate.setColumnIds(siteTemplate.getColumnIds().substring(1, siteTemplate.getColumnIds().length()-1));
				}
				if(siteTemplate.getColumnNames()!=null && siteTemplate.getColumnNames().startsWith(",") && siteTemplate.getColumnNames().endsWith(",")) {
					siteTemplate.setColumnNames(siteTemplate.getColumnNames().substring(1, siteTemplate.getColumnNames().length()-1));
				}
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof SiteTemplate) {
			resetTemplateColumn((SiteTemplate)record);
		}
		return super.save(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof SiteTemplate) {
			resetTemplateColumn((SiteTemplate)record);
		}
		return super.update(record);
	}
	
	/**
	 * 重置模板关联的栏目
	 * @param siteTemplate
	 */
	private void resetTemplateColumn(SiteTemplate siteTemplate) {
		if(siteTemplate.getColumnIds()!=null) {
			siteTemplate.setColumnIds(siteTemplate.getColumnIds().isEmpty() ? null : "," + siteTemplate.getColumnIds() + ",");
		}
		if(siteTemplate.getColumnNames()!=null) {
			siteTemplate.setColumnNames(siteTemplate.getColumnNames().isEmpty() ? null : "," + siteTemplate.getColumnNames() + ",");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateService#getSiteTemplateHTMLDocument(long, java.lang.String, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getSiteTemplateHTMLDocument(long siteId, String pageName, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		WebDirectory directory = (WebDirectory)getSiteService().getDirectory(siteId);
		if(directory==null) {
			throw new PageNotFoundException("The site or column is not exists.");
		}
		HTMLDocument templateDocument = null;
		String columnName = request==null ? null : request.getParameter("columnName");
		String cacheKey = siteId + "/" + pageName + (columnName==null ? "" : "/" + columnName) + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		try {
			templateDocument = (HTMLDocument)getCache().get(cacheKey); //从缓存中读取模板
		}
		catch(CacheException e) {
			Logger.exception(e);
		}
		if(templateDocument!=null) {
			return (HTMLDocument)templateDocument.cloneNode(true);
		}
		//没有缓存的模板,从数据库获取模板
		Template template = getSiteTemplate(directory, pageName, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		NormalTemplate normalTemplate = getNormalTemplateHTMLDocument("cms/sitemanage", "index".equals(pageName) && !"site".equals(directory.getDirectoryType()) ? "columnIndex" : pageName, themeType); //获取预设模板
		if(normalTemplate!=null && (template==null || normalTemplate.getThemeType()>template.getTheme().getType())) { //没有配置过模板、或者默认模板的主题类型更接近当前需要的主题类型
			templateDocument = normalTemplate.getHtmlDocument();
		}
		else if(template!=null) {
			templateDocument = getTemplateHTMLDocument(template.getId(), siteId, false, request); //获取模板
		}
		if(templateDocument==null) {
			return null;
		}
		//设置站点/栏目页面标题
		String title = templateDocument.getTitle();
		if(title==null || "".equals(title)) {
			title = columnName==null ? directory.getDirectoryName() : columnName;
		}
		else if(template!=null && (template.getPageName().equals("leafColumn") || template.getPageName().equals("branchColumn"))) { //子站/子栏目首页
			title = (columnName==null ? directory.getDirectoryName() : columnName) + " - " + title;
		}
		templateDocument.setTitle(title);
		try { //写入缓存
			getCache().put(cacheKey, templateDocument);
		}
		catch(CacheException e) {
			Logger.exception(e);
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getNormalTemplateForTemplate(java.lang.String, java.lang.String, int, com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected NormalTemplate getNormalTemplateForTemplate(String applicationName, String pageName, int themeType, Template template) throws ServiceException {
		if("index".equals(pageName)) {
			WebDirectory directory = (WebDirectory)getSiteService().getDirectory(template.getSiteId());
			if(!"site".equals(directory.getDirectoryType())) {
				NormalTemplate normalTemplate = super.getNormalTemplateForTemplate(applicationName, "columnIndex", themeType, template);
				if(normalTemplate!=null) {
					return normalTemplate;
				}
			}
		}
		return super.getNormalTemplateForTemplate(applicationName, pageName, themeType, template);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		if("subTemplate".equals(template.getPageName())) { //子模板
			return null; //总是没有同类模板
		}
		else if("branchColumn".equals(template.getPageName()) || "leafColumn".equals(template.getPageName())) { //子栏目模板
			SiteTemplate siteTemplate = (SiteTemplate)template;
			//获取同一个站点的模板列表
			String hql = "from SiteTemplate SiteTemplate" +
						 " where SiteTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
						 " and SiteTemplate.pageName='" + JdbcUtils.resetQuot(template.getPageName()) + "'" +
						 " and SiteTemplate.siteId=" + template.getSiteId() +
						 " and SiteTemplate.id!=" + template.getId() +
						 " and SiteTemplate.columnIds" + (siteTemplate.getColumnIds()==null || siteTemplate.getColumnIds().isEmpty() ? " is null" : "='" + (siteTemplate.getColumnIds().startsWith(",") ? siteTemplate.getColumnIds() : "," + siteTemplate.getColumnIds() + ",") + "'") +
						 " and SiteTemplate.matchByName=" + siteTemplate.getMatchByName();
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listCongenerTemplates(template, themeIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#rebuildStaticPage(com.yuanluesoft.cms.templatemanage.pojo.Template, com.yuanluesoft.cms.templatemanage.pojo.Template, long)
	 */
	public void rebuildStaticPage(Template currentTemplate, Template referenceTemplate, long siteId) throws ServiceException {
		if(!getSiteTemplateThemeService().isTypeOf(currentTemplate.getThemeId(), TemplateThemeService.THEME_TYPE_COMPUTER)) {
			return;
		}
		String applicationName = currentTemplate.getApplicationName();
		String pageName = currentTemplate.getPageName();
		long templateId = (referenceTemplate==null ? 0 : referenceTemplate.getId());
		boolean includeSubdirectory = true;
		if("branchColumn".equals(currentTemplate.getPageName()) || "leafColumn".equals(currentTemplate.getPageName())) { //子栏目首页模板
			pageName = "index"; //栏目首页
		}
		else if("columnRssSubscribe".equals(currentTemplate.getPageName())) { //子栏目RSS订阅模板
			pageName = "rssSubscribe"; //RSS订阅页面
		}
		else if("rssSubscribe".equals(currentTemplate.getPageName()) || "index".equals(currentTemplate.getPageName())) { //RSS订阅模、首页
			includeSubdirectory = false; //不更新子站点/栏目的页面
		}
		else if("photoSetArticle".equals(currentTemplate.getPageName()) || "videoSetArticle".equals(currentTemplate.getPageName())) { //图片集、视频集
			pageName = "article"; //文章
		}
		else if("subTemplate".equals(currentTemplate.getPageName())) { //子模板,注：只更新当前站点目录内的页面,故子模板不允许在当前目录以外使用
			if(referenceTemplate==null) { //没有关联模板
				referenceTemplate = getParentTemplate(currentTemplate, (TemplateTheme)getDatabaseService().findRecordById(TemplateTheme.class.getName(), currentTemplate.getThemeId())); //获取上级目录的同名子模板
				if(referenceTemplate==null) { //上级目录中没有同名子模板
					return; //不需要更新静态页面
				}
			}
			//不限制页面类型
			applicationName = null;
			pageName = null;
		}
		getStaticPageBuilder().rebuildPageForTemplate(applicationName, pageName, templateId, currentTemplate.getSiteId(), includeSubdirectory);
	}
	
	/**
	 * 获取模板记录
	 * @param siteId
	 * @param isSite 实在站点
	 * @param pageName
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected Template getSiteTemplate(final WebDirectory webDirectory, String pageName, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		long templateSiteId = webDirectory.getId();
		String extraHqlWhere = null;
		String columnName = request==null ? null : request.getParameter("columnName");
		if(columnName!=null && "index".equals(pageName)) { //指定栏目名称
			pageName = "leafColumn"; //修改页面名称为栏目(末级)
			extraHqlWhere = " SiteTemplate.columnNames like '%," + JdbcUtils.resetQuot(columnName) + ",%' and SiteTemplate.matchByName=1";
			Template template = retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, templateSiteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) {
				return template;
			}
			extraHqlWhere = "SiteTemplate.columnIds is null";
		}
		else if(!(webDirectory instanceof WebSite) && ("index".equals(pageName) || "rssSubscribe".equals(pageName))) { //不是站点, 获取首页、RSS订阅模板
			String originalPageName = pageName;
			int loop = themeType==TemplateThemeService.THEME_TYPE_COMPUTER ? 1 : 2;
			for(int i = 0; i < loop; i++) { //第一次获取为当前主题类型配置的模板,第二次允许获取全部类型
				//获取栏目自己的页面,先获取当前主题类型的模板
				Template indexTemplate = retrieveTemplate(SiteTemplate.class.getName(), null, null, null, true, "cms/sitemanage", originalPageName, webDirectory.getId(), themeId, themeType, i==0, pageWidth, flashSupport, temporaryOpeningFirst);
				if(indexTemplate!=null && i==0) {
					return indexTemplate;
				}
				//获取上级目录的子栏目模板
				templateSiteId = webDirectory.getParentDirectoryId();
				if("rssSubscribe".equals(originalPageName)) {
					pageName = "columnRssSubscribe"; //获取上级目录的子栏目RSS订阅模板
				}
				else if(getSiteService().hasChildDirectories(webDirectory.getId())) { //有子目录
					pageName = "branchColumn"; //修改页面名称为栏目(非末级)
				}
				else {
					pageName = "leafColumn"; //修改页面名称为栏目(末级)
				}
				if(pageName.equals("leafColumn") || pageName.equals("branchColumn")) { //获取首页模板
					//查找和为当前栏目配置的模板
					extraHqlWhere = "(SiteTemplate.columnIds like '%," + webDirectory.getId() + ",%' and SiteTemplate.matchByName=0)" +
									" or (SiteTemplate.columnNames like '%," + JdbcUtils.resetQuot(webDirectory.getDirectoryName()) + ",%' and SiteTemplate.matchByName=1)";
					Template template = retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, templateSiteId, themeId, themeType, i==0, pageWidth, flashSupport, temporaryOpeningFirst);
					if(template!=null) {
						return indexTemplate==null || indexTemplate.getTheme().getType() < template.getTheme().getType()  ? template : indexTemplate;
					}
					extraHqlWhere = "SiteTemplate.columnIds is null";
				}
				Template template = retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, templateSiteId, themeId, themeType, i==0, pageWidth, flashSupport, temporaryOpeningFirst);
				if(template!=null || i==loop-1) {
					return indexTemplate==null || indexTemplate.getTheme().getType() < template.getTheme().getType()  ? template : indexTemplate;
				}
			}
		}
		return retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, templateSiteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		if(template==null || template.getSiteId()==0) {
			return null;
		}
		//获取父站点/栏目ID
		WebDirectory directory = (WebDirectory)getSiteService().getDirectory(template.getSiteId());
		String pageName = template.getPageName();
		if("subTemplate".equals(template.getPageName())) { //子模板,按名称获取上级目录的子模板
			//查找上级目录的同名子模板
			String extraHqlWhere = "SiteTemplate.templateName='" + JdbcUtils.resetQuot(template.getTemplateName()) + "'";
			return retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, directory.getParentDirectoryId(), template.getThemeId(), theme.getType(), false, theme.getPageWidth(), theme.getFlashSupport()==1, false);
		}
		if(pageName.equals("leafColumn") || pageName.equals("branchColumn")) { //子栏目模板
			SiteTemplate siteTemplate = (SiteTemplate)template;
			String extraHqlWhere = "SiteTemplate.columnIds" + (siteTemplate.getColumnIds()==null || siteTemplate.getColumnIds().isEmpty() ? " is null" : "='" + siteTemplate.getColumnIds() + "'");
			return retrieveTemplate(SiteTemplate.class.getName(), null, extraHqlWhere, null, false, "cms/sitemanage", pageName, directory.getParentDirectoryId(), template.getThemeId(), theme.getType(), false, theme.getPageWidth(), theme.getFlashSupport()==1, false);
		}
		if(!(directory instanceof WebSite)) { //不是站点
			if("rssSubscribe".equals(pageName)) { //RSS订阅模板
				pageName = "columnRssSubscribe"; //获取上级目录的子栏目RSS订阅模板
			}
			else if("index".equals(pageName)) { //获取首页、RSS订阅模板
				if(getSiteService().hasChildDirectories(directory.getId())) { //有子目录
					pageName = "branchColumn"; //修改页面名称为栏目(非末级)
				}
				else {
					pageName = "leafColumn"; //修改页面名称为栏目(末级)
				}
			}
		}
		return getSiteTemplate((WebDirectory)getSiteService().getDirectory(directory.getParentDirectoryId()), pageName, theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false, null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateService#copySiteTemplate(long, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void copySiteTemplate(long toSiteId, long fromSiteId, SessionInfo sessionInfo) throws ServiceException {
		//获取模板
		String hql = "select Template" +
					 " from Template Template, WebDirectorySubjection WebDirectorySubjection" +
					 " where Template.siteId=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId=" + fromSiteId;
		List templates = getDatabaseService().findRecordsByHql(hql);
		if(templates==null || templates.isEmpty()) {
			return;
		}
		//获取新、旧站点及其下级目录的对应关系
		Map siteMapping = new HashMap();
		createSiteMapping(siteMapping, toSiteId, fromSiteId);
		//复制模板
		for(Iterator iterator = templates.iterator(); iterator.hasNext();) {
			Template template = (Template)iterator.next();
			Long newSiteId = (Long)siteMapping.get(new Long(template.getSiteId()));
			if(newSiteId==null) {
				continue;
			}
			long fromTemplateId = template.getId();
			try {
				template = (Template)template.clone();
			}
			catch (CloneNotSupportedException e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			template.setId(UUIDLongGenerator.generateId());
			template.setSiteId(newSiteId.longValue()); //站点/栏目ID
			template.setLastModified(DateTimeUtils.now()); //最后修改时间
			template.setLastModifierId(sessionInfo.getUserId()); //最后修改人ID
			template.setLastModifier(sessionInfo.getUserName()); //最后修改人
			getDatabaseService().saveRecord(template);
			//拷贝模板文件
			copyTemplate(template.getId(), fromTemplateId);
			//替换模板中出现的站点/栏目ID
			String templateFilePath = getTemplateDirectory(template.getId(), false) + TEMPLATE_FILE_NAME;
			String templateHtml = FileUtils.readStringFromFile(templateFilePath, "utf-8");
			if(templateHtml==null) {
				continue;
			}
			for(Iterator iteratorSite = siteMapping.keySet().iterator(); iteratorSite.hasNext();) {
				Long fromId = (Long)iteratorSite.next();
				if(fromId.longValue()==0) { //如果是根站点,不替换,避免误替换
					continue;
				}
				templateHtml = templateHtml.replaceAll("(?i)([\\D])" + fromId + "([\\D])", "$1" + siteMapping.get(fromId) + "$2");
			}
			//保存到目标目录
			FileUtils.saveStringToFile(templateFilePath, templateHtml, "utf-8", true);
		}
	}
	
	/**
	 * 递归函数:创建目录映射表
	 * @param siteMapping
	 * @param toDirectoryId
	 * @param fromDirectoryId
	 * @throws ServiceException
	 */
	private void createSiteMapping(Map siteMapping, long toDirectoryId, long fromDirectoryId) throws ServiceException {
		//加入映射表
		siteMapping.put(new Long(fromDirectoryId), new Long(toDirectoryId));
		List fromChildDiretories = siteService.listChildDirectories(fromDirectoryId, null, null, null, false, false, null, 0, 0);
		List toChildDiretories = siteService.listChildDirectories(toDirectoryId, null, null, null, false, false, null, 0, 0);
		if(fromChildDiretories==null || fromChildDiretories.isEmpty()) {
			return;
		}
		for(Iterator iterator = fromChildDiretories.iterator(); iterator.hasNext();) {
			WebDirectory fromDirectory = (WebDirectory)iterator.next();
			WebDirectory toDirectory = (WebDirectory)ListUtils.findObjectByProperty(toChildDiretories, "directoryName", fromDirectory.getDirectoryName());
			if(toDirectory!=null) {
				createSiteMapping(siteMapping, toDirectory.getId(), fromDirectory.getId()); //递归:处理下一级目录
			}
		}
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}