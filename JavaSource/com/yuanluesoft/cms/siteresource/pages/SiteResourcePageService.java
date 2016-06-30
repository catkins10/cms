package com.yuanluesoft.cms.siteresource.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 站点文章页面服务
 * @author linchuan
 *
 */
public class SiteResourcePageService extends PageService {
	private SiteService siteService; //站点服务
	private DatabaseService databaseService; //数据库服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		SiteResource siteResource = (SiteResource)request.getAttribute("record");
		if(siteResource==null || siteResource.getStatus()>=SiteResourceService.RESOURCE_STATUS_DELETED) {
			throw new PageNotFoundException();
		}
		sitePage.setAttribute("record", siteResource);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		SiteResource siteResource = (SiteResource)sitePage.getAttribute("record");
		//获取所在站点
		WebSite parentSite = siteService.getParentSite(siteId);
		//文章所属站点:当文章不属于当前站点时,如果站点配置是使用站点的文章模板时,则当前站点,否则是文章所在栏目
		long articleColumnId = parentSite.getUseSiteTemplate()=='1' ? parentSite.getId() : ((SiteResourceSubjection)siteResource.getSubjections().iterator().next()).getSiteId();
		for(Iterator iterator = siteResource.getSubjections().iterator(); iterator.hasNext();) {
			SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
			long articleParentSiteId = siteService.getParentSite(subjection.getSiteId()).getId(); //获取所在栏目的站点ID
			if(parentSite.getId()==articleParentSiteId) {
				articleColumnId = subjection.getSiteId();
				break;
			}
		}
		request.setAttribute("pageColumnId", new Long(articleColumnId)); //页面隶属ID
		HTMLDocument template = null;
		if(siteResource.getResourcePhotos()!=null && !siteResource.getResourcePhotos().isEmpty()) {
			template = ((SiteTemplateService)getTemplateService()).getSiteTemplateHTMLDocument(articleColumnId, "photoSetArticle" + (pageName.endsWith("Hint") ? "Hint" : ""), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		}
		else if(siteResource.getResourceVideos()!=null && !siteResource.getResourceVideos().isEmpty()) {
			template = ((SiteTemplateService)getTemplateService()).getSiteTemplateHTMLDocument(articleColumnId, "videoSetArticle" + (pageName.endsWith("Hint") ? "Hint" : ""), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		}
		return template!=null ? template : ((SiteTemplateService)getTemplateService()).getSiteTemplateHTMLDocument(articleColumnId, pageName, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		SiteResource siteResource = (SiteResource)sitePage.getAttribute("record");
		//设置标题
		String title = template.getTitle();
		template.setTitle(siteResource.getSubject() + (title==null || title.equals("") ? "" : " - " + title));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getPagingRecord(java.lang.Object, boolean, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected List getPagingRecords(Object currentRecord, boolean nextRecord, long siteId) throws ServiceException {
		if(!(currentRecord instanceof SiteResource)) {
			return null;
		}
		SiteResource siteResource = (SiteResource)currentRecord;
		if(siteResource.getIssueTime()==null) {
			return null;
		}
		List records = new ArrayList();
		for(Iterator iterator = siteResource.getSubjections().iterator(); iterator.hasNext();) {
			SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
			if(siteId>=0 && subjection.getSiteId()!=siteId && siteService.getParentSite(subjection.getSiteId()).getId()!=siteId) { //检查文章所在栏目是否属于当前站点
				continue;
			}
			//获取当前栏目下的前一篇或后一篇文章
			String sort = (nextRecord ? " DESC" : "");
			String hql = "select SiteResource" +
						 " from SiteResource SiteResource left join SiteResource.subjections SiteResourceSubjection" +
						 " where SiteResourceSubjection.siteId=" + subjection.getSiteId() +
						 " and SiteResource.type=" + siteResource.getType() +
						 " and SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
						 " and SiteResource.id!=" + siteResource.getId() +
						 " and (SiteResource.issueTime" + (nextRecord ? "<" : ">") + "TIMESTAMP(" + DateTimeUtils.formatTimestamp(siteResource.getIssueTime(), null) + ")" +
						 " or (SiteResource.issueTime=TIMESTAMP(" + DateTimeUtils.formatTimestamp(siteResource.getIssueTime(), null) + ")" +
						 " and SiteResource.id" + (nextRecord ? "<=" : ">=") + siteResource.getId() + "))" +
						 " order by SiteResource.issueTime" + sort + ", SiteResource.id" + sort;
			SiteResource resource = (SiteResource)databaseService.findRecordByHql(hql, ListUtils.generateList("subjections"));
			if(resource!=null) {
				records.add(resource);
			}
			if(siteId>=0) { //指定了站点,只需要获取一篇
				break;
			}
		}
		return records.isEmpty() ? null : records;
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

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}