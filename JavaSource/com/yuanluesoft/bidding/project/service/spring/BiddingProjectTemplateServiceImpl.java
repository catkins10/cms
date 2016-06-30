package com.yuanluesoft.bidding.project.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTemplate;
import com.yuanluesoft.bidding.project.service.BiddingProjectTemplateService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
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
public class BiddingProjectTemplateServiceImpl extends TemplateServiceImpl implements BiddingProjectTemplateService {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectTemplateService#getTemplateHTMLDocument(java.lang.String, com.yuanluesoft.bidding.project.pojo.BiddingProject, long, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, BiddingProject project, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		String key = "bidding/project/" + pageName + "/" + project.getProjectCategory() + "/" + project.getProjectProcedure() + "/" + project.getCity() + "/" + project.getBiddingMode() + "/" + siteId + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1: 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(key);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			//获取模板
			BiddingProjectTemplate template = getProjectTemplate(project.getProjectCategory(), project.getProjectProcedure(), project.getCity(), project.getBiddingMode(), pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) {
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
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		return null; //不获取父模板
	}

	/**
	 * 获取模板
	 * @param projectCategories
	 * @param projectProcedures
	 * @param projectCities
	 * @param projectBiddingModes
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
	private BiddingProjectTemplate getProjectTemplate(String projectCategories, String projectProcedures, String projectCities, String projectBiddingModes, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst) throws ServiceException {
		//查找当前站点和项目匹配的模板
		String extraHqlWhere = "BiddingProjectTemplate.categories like '%," + JdbcUtils.resetQuot(projectCategories) + ",%'" +
							   " and BiddingProjectTemplate.procedures like '%," + JdbcUtils.resetQuot(projectProcedures) + ",%'" +
							   " and BiddingProjectTemplate.cities like '%," + JdbcUtils.resetQuot(projectCities) + ",%'" +
							   " and BiddingProjectTemplate.biddingModes like '%," + JdbcUtils.resetQuot(projectBiddingModes) + ",%'";
		BiddingProjectTemplate template = (BiddingProjectTemplate)retrieveTemplate(BiddingProjectTemplate.class.getName(), null, extraHqlWhere, null, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null) {
			return template;
		}
		//查找当前站点的模板,未设置作用范围的模板
		extraHqlWhere = generateCondition("BiddingProjectTemplate.categories", null) +
				 		" and " + generateCondition("BiddingProjectTemplate.procedures", null) +
				 		" and " + generateCondition("BiddingProjectTemplate.cities", null) +
				 		" and " + generateCondition("BiddingProjectTemplate.biddingModes", null);
		template = (BiddingProjectTemplate)retrieveTemplate(BiddingProjectTemplate.class.getName(), null, extraHqlWhere, null, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null || siteId==0) {
			return template;
		}
		//递归:获取上级站点的模板配置
		WebDirectory parentSite = (WebDirectory)siteService.getParentDirectory(siteId, "site"); //获取父站点
		return getProjectTemplate(projectCategories, projectProcedures, projectCities, projectBiddingModes, pageName, (parentSite==null ? 0 : parentSite.getId()), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		BiddingProjectTemplate projectTemplate = (BiddingProjectTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from BiddingProjectTemplate BiddingProjectTemplate" +
					 " where BiddingProjectTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and BiddingProjectTemplate.applicationName='" + JdbcUtils.resetQuot(projectTemplate.getApplicationName()) + "'" +
					 " and BiddingProjectTemplate.pageName='" + JdbcUtils.resetQuot(projectTemplate.getPageName()) + "'" +
					 " and " + generateCondition("BiddingProjectTemplate.categories", projectTemplate.getProcedures()) +
					 " and " + generateCondition("BiddingProjectTemplate.procedures", projectTemplate.getCategories()) +
					 " and " + generateCondition("BiddingProjectTemplate.cities", projectTemplate.getCities()) +
					 " and " + generateCondition("BiddingProjectTemplate.biddingModes", projectTemplate.getBiddingModes()) +
					 " and BiddingProjectTemplate.siteId=" + projectTemplate.getSiteId() +
					 " and BiddingProjectTemplate.id!=" + template.getId();
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 生成查询条件
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	private String generateCondition(String fieldName, String fieldValue) {
		return fieldValue==null || fieldValue.isEmpty() ? fieldName + " is null" : fieldName + "='" + fieldValue + "'";
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
