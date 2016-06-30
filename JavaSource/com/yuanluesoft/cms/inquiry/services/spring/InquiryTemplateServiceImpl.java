package com.yuanluesoft.cms.inquiry.services.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.inquiry.pojo.InquiryTemplate;
import com.yuanluesoft.cms.inquiry.services.InquiryTemplateService;
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
public class InquiryTemplateServiceImpl extends TemplateServiceImpl implements InquiryTemplateService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.services.InquiryTemplateService#getTemplateHTMLDocument(java.lang.String, long, long, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long subjectId, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		String key = "inquiry/" + pageName + "/" + subjectId + "/" + siteId + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(key);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			//获取模板
			InquiryTemplate template = getInquiryTemplate(subjectId, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			NormalTemplate normalTemplate = getNormalTemplateHTMLDocument("cms/inquiry", pageName, themeType); //获取预设模板
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
				getCache().put(key, templateDocument);
			}
			catch (CacheException e) {
				
			}
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		InquiryTemplate inquiryTemplate = (InquiryTemplate)template;
		if(inquiryTemplate.getSubjectId()>0) { //指定了主题
			return getInquiryTemplate(0, inquiryTemplate.getPageName(), inquiryTemplate.getSiteId(), theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false);
		}
		//获取上级站点
		String hql = "select WebDirectory.parentDirectoryId from WebDirectory WebDirectory where WebDirectory.id=" + template.getSiteId();
		long parentDirectoryId = ((Number)getDatabaseService().findRecordByHql(hql)).longValue();
		return getInquiryTemplate(0, inquiryTemplate.getPageName(), parentDirectoryId, theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		InquiryTemplate inquiryTemplate = (InquiryTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from InquiryTemplate InquiryTemplate" +
					 " where InquiryTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and InquiryTemplate.applicationName='" + JdbcUtils.resetQuot(template.getApplicationName()) + "'" +
					 " and InquiryTemplate.pageName='" + JdbcUtils.resetQuot(inquiryTemplate.getPageName()) + "'" +
					 " and InquiryTemplate.siteId=" + inquiryTemplate.getSiteId() +
					 " and InquiryTemplate.subjectId=" + inquiryTemplate.getSubjectId() +
					 " and InquiryTemplate.id!=" + template.getId();
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 获取模板记录
	 * @param subjectId
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
	protected InquiryTemplate getInquiryTemplate(long subjectId, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst) throws ServiceException {
		if(subjectId>0) {
			//获取直接为主题的模板
			String extraHqlWhere = "InquiryTemplate.subjectId=" + subjectId;
			InquiryTemplate template = (InquiryTemplate)retrieveTemplate(InquiryTemplate.class.getName(), null, extraHqlWhere, null, false, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) {
				return template;
			}
		}
		//获取通用模板
		String extraHqlWhere = "InquiryTemplate.subjectId=0";
		return (InquiryTemplate)retrieveTemplate(InquiryTemplate.class.getName(), null, extraHqlWhere, null, false, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
	}
}