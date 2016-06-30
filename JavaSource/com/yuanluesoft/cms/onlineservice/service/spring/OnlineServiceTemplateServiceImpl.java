package com.yuanluesoft.cms.onlineservice.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceTemplate;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceTemplateService;
import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceTemplateServiceImpl extends TemplateServiceImpl implements OnlineServiceTemplateService {
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceTemplateService#getTemplateHTMLDocument(java.lang.String, long, java.lang.String, long, long, int, int, boolean, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long directoryId, String itemType, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		if(itemType!=null) {
			itemType = itemType.split(",")[0];
		}
		HTMLDocument templateDocument = null;
		//从缓存中读取模板
		String cahceKey = "onlineservice/" + pageName + "/" + directoryId + (itemType==null ? "" : "/" + itemType) + "/" + siteId + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		try {
			templateDocument = (HTMLDocument)getCache().get(cahceKey);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			//获取模板
			OnlineServiceTemplate template = getOnlineServiceTemplate(directoryId, itemType, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			NormalTemplate normalTemplate = null;
			if(itemType!=null && !itemType.isEmpty()) {
				normalTemplate = getNormalTemplateHTMLDocument("cms/onlineservice", pageName + "/" + itemType, themeType);
			}
			if(normalTemplate==null) {
				normalTemplate = getNormalTemplateHTMLDocument("cms/onlineservice", pageName, themeType);
			}
			if(normalTemplate!=null && (template==null || normalTemplate.getThemeType()>template.getTheme().getType())) { //没有配置过模板、或者默认模板的主题类型更接近当前需要的主题类型
				templateDocument = normalTemplate.getHtmlDocument();
			}
			else if(template!=null) {
				templateDocument = getTemplateHTMLDocument(template.getId(), siteId, false, request);
			}
			if(templateDocument==null) {
				return null;
			}
			//写入缓存
			try {
				getCache().put(cahceKey, templateDocument);
			}
			catch (CacheException e) {
				
			}
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getNormalTemplateForTemplate(java.lang.String, java.lang.String, int, com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected NormalTemplate getNormalTemplateForTemplate(String applicationName, String pageName, int themeTpe, Template template) throws ServiceException {
		OnlineServiceTemplate onlineServiceTemplate = (OnlineServiceTemplate)template;
		String itemType;
		if(onlineServiceTemplate.getItemTypes()!=null && !onlineServiceTemplate.getItemTypes().isEmpty() &&
		   (itemType=onlineServiceTemplate.getItemTypes().replace(",", "")).equals(onlineServiceTemplate.getItemTypes())) {
			NormalTemplate normalTemplate = super.getNormalTemplateForTemplate(applicationName, pageName + "/" + itemType, themeTpe, template);
			if(normalTemplate!=null) {
				return normalTemplate;
			}
		}
		return super.getNormalTemplateForTemplate(applicationName, pageName, themeTpe, template);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		OnlineServiceTemplate onlineServiceTemplate = (OnlineServiceTemplate)template;
		if(onlineServiceTemplate==null || onlineServiceTemplate.getDirectoryId()==0) { //不获取根目录的父模板
			return null;
		}
		String pageName = template.getPageName();
		if("directory".equals(template.getPageName())) { //目录页面
			pageName = onlineServiceDirectoryService.hasChildDirectories(onlineServiceTemplate.getDirectoryId()) ? "branchDirectory" : "leafDirectory";
		}
		//获取父目录ID
		String hql = "select OnlineServiceDirectory.parentDirectoryId from OnlineServiceDirectory OnlineServiceDirectory where OnlineServiceDirectory.id=" + onlineServiceTemplate.getDirectoryId();
		Number parentDirectoryId = (Number)getDatabaseService().findRecordByHql(hql);
		return getOnlineServiceTemplate((parentDirectoryId==null ? 0 : parentDirectoryId.longValue()), null, pageName, onlineServiceTemplate.getSiteId(), theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false);
	}
	
	/**
	 * 获取模板记录
	 * @param directoryId
	 * @param itemType
	 * @param pageName
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @return
	 * @throws ServiceException
	 */
	private OnlineServiceTemplate getOnlineServiceTemplate(long directoryId, String itemType, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst) throws ServiceException {
		//查找直接为当前目录配置的模板
		String extraHqlWhere = "OnlineServiceTemplate.directoryId=" + directoryId;
		OnlineServiceTemplate template = retrieveOnlineServiceTemplate(itemType, null, extraHqlWhere, null, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null) {
			return template;
		}
		if(directoryId>0) { //不是网上办事根目录,获取上级目录的模板
			String parentPageName = pageName;
			if(pageName.equals("directory")) {
				//检查是否末级目录
				parentPageName = onlineServiceDirectoryService.hasChildDirectories(directoryId) ? "branchDirectory" : "leafDirectory";
			}
			//获取配置在当前站点的上级目录模板
			String extraHqlJoin = ", OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection";
			extraHqlWhere = "OnlineServiceTemplate.directoryId=OnlineServiceDirectorySubjection.parentDirectoryId" +
						 	" and OnlineServiceDirectorySubjection.directoryId=" + directoryId +
						 	" and OnlineServiceDirectorySubjection.parentDirectoryId!=" + directoryId;
			String extraHqlOrderBy = "OnlineServiceDirectorySubjection.id";
			template = retrieveOnlineServiceTemplate(itemType, extraHqlJoin, extraHqlWhere, extraHqlOrderBy, parentPageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) {
				return template;
			}
		}
		if(siteId==0) { //已经是根站点
			return null;
		}
		//递归:获取上级站点的模板配置
		String hql = "select WebDirectory.parentDirectoryId from WebDirectory WebDirectory where WebDirectory.id=" + siteId;
		Number parentSiteId = (Number)getDatabaseService().findRecordByHql(hql);
		return getOnlineServiceTemplate(directoryId, itemType, pageName, (parentSiteId==null ? 0 : parentSiteId.longValue()), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
	}
	
	/**
	 * 获取网上办事模板
	 * @param itemType
	 * @param extraHqlJoin
	 * @param extraHqlWhere
	 * @param extraHqlOrderBy
	 * @param pageName
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @return
	 * @throws ServiceException
	 */
	protected OnlineServiceTemplate retrieveOnlineServiceTemplate(String itemType, String extraHqlJoin, String extraHqlWhere, String extraHqlOrderBy, String pageName, long siteId, long themeId, int themeType, int pageWidth, final boolean flashSupport, final boolean temporaryOpeningFirst) throws ServiceException {
		String where = "(" + extraHqlWhere + ")";
		if(itemType==null || itemType.isEmpty()) {
			where += " and OnlineServiceTemplate.itemTypes is null";
		}
		else {
			where += " and (OnlineServiceTemplate.itemTypes='" + JdbcUtils.resetQuot(itemType) + "'" +
					 " or OnlineServiceTemplate.itemTypes like '%," + JdbcUtils.resetQuot(itemType) + ",%'" +
					 " or OnlineServiceTemplate.itemTypes like '" + JdbcUtils.resetQuot(itemType) + ",%'" +
					 " or OnlineServiceTemplate.itemTypes like '%," + JdbcUtils.resetQuot(itemType) + "')";
		}
		OnlineServiceTemplate template = (OnlineServiceTemplate)retrieveTemplate(OnlineServiceTemplate.class.getName(), extraHqlJoin, where, extraHqlOrderBy, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null) {
			return template;
		}
		if(itemType!=null && !itemType.isEmpty()) { //指定了事项类型
			where = "(" + extraHqlWhere + ") and OnlineServiceTemplate.itemTypes is null"; //获取通用模板
			return (OnlineServiceTemplate)retrieveTemplate(OnlineServiceTemplate.class.getName(), extraHqlJoin, where, extraHqlOrderBy, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		OnlineServiceTemplate onlineServiceTemplate = (OnlineServiceTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from OnlineServiceTemplate OnlineServiceTemplate" +
					 " where OnlineServiceTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and OnlineServiceTemplate.applicationName='" + JdbcUtils.resetQuot(onlineServiceTemplate.getApplicationName()) + "'" +
					 " and OnlineServiceTemplate.pageName='" + JdbcUtils.resetQuot(onlineServiceTemplate.getPageName()) + "'" +
					 " and OnlineServiceTemplate.directoryId=" + onlineServiceTemplate.getDirectoryId() +
					 " and OnlineServiceTemplate.itemTypes" + (onlineServiceTemplate.getItemTypes()==null || onlineServiceTemplate.getItemTypes().isEmpty() ? " is null" : "='" + JdbcUtils.resetQuot(onlineServiceTemplate.getItemTypes()) + "'") +
					 " and OnlineServiceTemplate.siteId=" + onlineServiceTemplate.getSiteId() +
					 " and OnlineServiceTemplate.id!=" + onlineServiceTemplate.getId();
		return getDatabaseService().findRecordsByHql(hql);
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
		if("leafDirectory".equals(currentTemplate.getPageName()) || "branchDirectory".equals(currentTemplate.getPageName())) { //子目录模板
			pageName = "directory"; //目录页面
		}
		getStaticPageBuilder().rebuildPageForTemplate(applicationName, pageName, templateId, currentTemplate.getSiteId(), true);
	}

	/**
	 * @return the onlineServiceDirectoryService
	 */
	public OnlineServiceDirectoryService getOnlineServiceDirectoryService() {
		return onlineServiceDirectoryService;
	}

	/**
	 * @param onlineServiceDirectoryService the onlineServiceDirectoryService to set
	 */
	public void setOnlineServiceDirectoryService(
			OnlineServiceDirectoryService onlineServiceDirectoryService) {
		this.onlineServiceDirectoryService = onlineServiceDirectoryService;
	}
}