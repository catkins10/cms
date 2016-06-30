package com.yuanluesoft.bbs.templatemanage.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bbs.templatemanage.pojo.BbsTemplate;
import com.yuanluesoft.bbs.templatemanage.service.BbsTemplateService;
import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
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
public class BbsTemplateServiceImpl extends TemplateServiceImpl implements BbsTemplateService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.templatemanage.service.BbsTemplateService#getTemplateHTMLDocument(java.lang.String, long, long, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long directoryId, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		String cacheKey = "bbs/" + directoryId + "/" + siteId + "/" + pageName + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(cacheKey);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			//获取模板
			BbsTemplate	template = getBbsTemplate(directoryId, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			NormalTemplate normalTemplate = getNormalTemplateHTMLDocument("bbs", pageName, themeType); //获取预设模板
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
				getCache().put(cacheKey, templateDocument);
			}
			catch (CacheException e) {
				
			}
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		BbsTemplate bbsTemplate = (BbsTemplate)template;
		if(bbsTemplate==null || bbsTemplate.getDirectoryId()==0) {
			return null;
		}
		//获取父目录
		String hql = "select BbsDirectory.parentDirectoryId from BbsDirectory BbsDirectory where BbsDirectory.id=" + bbsTemplate.getDirectoryId();
		Number parentDirectoryId = (Number)getDatabaseService().findRecordByHql(hql);
		return getBbsTemplate((parentDirectoryId==null ? 0 : parentDirectoryId.longValue()), template.getPageName(), template.getSiteId(), theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		BbsTemplate bbsTemplate = (BbsTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from BbsTemplate BbsTemplate" +
					 " where BbsTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and BbsTemplate.applicationName='" + JdbcUtils.resetQuot(bbsTemplate.getApplicationName()) + "'" +
					 " and BbsTemplate.pageName='" + JdbcUtils.resetQuot(bbsTemplate.getPageName()) + "'" +
					 " and BbsTemplate.directoryId='" + bbsTemplate.getDirectoryId() + "'" +
					 " and BbsTemplate.siteId=" + bbsTemplate.getSiteId() +
					 " and BbsTemplate.id!=" + template.getId();
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 获取模板记录
	 * @param directoryId
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
	private BbsTemplate getBbsTemplate(long directoryId, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst) throws ServiceException {
		String extraHqlJoin = ", BbsDirectorySubjection BbsDirectorySubjection";
		String extraHqlWhere = "BbsTemplate.directoryId=BbsDirectorySubjection.parentDirectoryId" +
							   " and BbsDirectorySubjection.directoryId=" + directoryId;
		String extraHqlOrderBy = "BbsDirectorySubjection.id";
		BbsTemplate	template = (BbsTemplate)retrieveTemplate(BbsTemplate.class.getName(), extraHqlJoin, extraHqlWhere, extraHqlOrderBy, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null || siteId==0) {
			return template;
		}
		//递归:获取上级站点的模板配置
		String hql = "select WebDirectory.parentDirectoryId from WebDirectory WebDirectory where WebDirectory.id=" + siteId;
		Number parentSiteId = (Number)getDatabaseService().findRecordByHql(hql);
		return getBbsTemplate(directoryId, pageName, (parentSiteId==null ? 0 : parentSiteId.longValue()), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
	}
}