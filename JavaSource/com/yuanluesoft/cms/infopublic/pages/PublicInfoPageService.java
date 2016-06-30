package com.yuanluesoft.cms.infopublic.pages;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSubjection;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 政务信息页面服务
 * @author linchuan
 *
 */
public class PublicInfoPageService extends PageService {
	private PublicInfoService publicInfoService; //政务信息服务
	private DatabaseService databaseService; //DAO

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		PublicInfo publicInfo = (PublicInfo)sitePage.getAttribute("record");
		String title = template.getTitle();
		template.setTitle(publicInfo.getSubject() + (title==null || "".equals(title) ? "" : " - " + title));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取政务信息
		PublicInfo publicInfo = publicInfoService.getPublicInfo(RequestUtils.getParameterLongValue(request, "id"));
		if(publicInfo==null || publicInfo.getStatus()>=PublicInfoService.INFO_STATUS_DELETED) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", publicInfo);
		//设置所在目录
		if(publicInfo.getSubjections()==null || publicInfo.getSubjections().isEmpty()) {
			sitePage.setAttribute("directoryId", new Long(0));
		}
		else {
			sitePage.setAttribute("directoryId", new Long(((PublicInfoSubjection)publicInfo.getSubjections().iterator().next()).getDirectoryId()));
		}
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getPagingRecord(java.lang.Object, boolean, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected List getPagingRecords(Object currentRecord, boolean nextRecord, long siteId) throws ServiceException {
		if(!(currentRecord instanceof PublicInfo)) {
			return null;
		}
		PublicInfo publicInfo = (PublicInfo)currentRecord;
		//获取当前栏目下的前一篇或后一篇信息
		String sort = (nextRecord ? " DESC" : "");
		String hql = "select PublicInfo" +
					 " from PublicInfo PublicInfo left join PublicInfo.subjections PublicInfoSubjection" +
					 " where PublicInfoSubjection.directoryId=" + ((PublicInfoSubjection)publicInfo.getSubjections().iterator().next()).getDirectoryId() +
					 " and PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" +
					 " and PublicInfo.id!=" + publicInfo.getId();
		if(publicInfo.getGenerateDate()==null) {
			hql += " and PublicInfo.issueTime" + (nextRecord ? "<" : ">") + "TIMESTAMP(" + DateTimeUtils.formatTimestamp(publicInfo.getIssueTime(), null) + ")";
		}
		else {
			hql += " and (PublicInfo.generateDate" + (nextRecord ? "<" : ">") + "DATE(" + DateTimeUtils.formatDate(publicInfo.getGenerateDate(), null) + ")" +
				   " or (PublicInfo.generateDate=DATE(" + DateTimeUtils.formatDate(publicInfo.getGenerateDate(), null) + ")" +
				   " and PublicInfo.infoIndex" + (nextRecord ? "<=" : ">=") + "'" + JdbcUtils.resetQuot(publicInfo.getInfoIndex()) + "'))";
		}
		hql += " order by PublicInfo.generateDate" + sort + ", PublicInfo.infoIndex" + sort + ", PublicInfo.id" + sort;
		return databaseService.findRecordsByHql(hql, ListUtils.generateList("subjections"), 0, 1);
	}
	
	/**
	 * @return the publicInfoService
	 */
	public PublicInfoService getPublicInfoService() {
		return publicInfoService;
	}

	/**
	 * @param publicInfoService the publicInfoService to set
	 */
	public void setPublicInfoService(PublicInfoService publicInfoService) {
		this.publicInfoService = publicInfoService;
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