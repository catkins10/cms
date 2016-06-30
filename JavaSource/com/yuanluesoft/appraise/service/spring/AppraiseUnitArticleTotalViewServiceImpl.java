package com.yuanluesoft.appraise.service.spring;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.model.AppraiseUnitArticleTotal;
import com.yuanluesoft.appraise.model.ParticipateUnit;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseUnitArticleTotalViewServiceImpl extends ViewServiceImpl {
	private OrgService orgService; //组织机构服务
	private SiteService siteService; //站点服务
	private SiteResourceService siteResourceService; //站点资源服务
	private AppraiseService appraiseService; //评议服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewData(com.yuanluesoft.jeaf.view.model.ViewPackage, int, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void retrieveViewData(ViewPackage viewPackage, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取用户有管理权限的地区
		List ids = orgService.listDirectoryIds("area", "appraiseManager", true, sessionInfo, 0, 1);
		if(ids==null || ids.isEmpty()) {
			return;
		}
		Date beginDate = RequestUtils.getParameterDateValue(request, "beginDate");
		if(beginDate==null) {
			return;
		}
		Date endDate = RequestUtils.getParameterDateValue(request, "endDate");
		String category = RequestUtils.getParameterStringValue(request, "category");
		long areaId = ((Number)ids.get(0)).longValue();
		//获取参评单位
		List participateUnits = appraiseService.listParticipateUnits(areaId, DateTimeUtils.getYear(beginDate));
		if(category!=null && !category.isEmpty()) {
			participateUnits = ListUtils.getSubListByProperty(participateUnits, "category", category); 
		}
		if(participateUnits==null || participateUnits.isEmpty()) {
			return;
		}
		for(int i=0; i<participateUnits.size(); i++) {
			ParticipateUnit participateUnit = (ParticipateUnit)participateUnits.get(i);
			AppraiseUnitArticleTotal articleTotal = new AppraiseUnitArticleTotal();
			articleTotal.setUnitId(participateUnit.getUnitId()); //单位ID
			articleTotal.setUnitName(participateUnit.getUnitName()); //单位名称
			articleTotal.setCategory(participateUnit.getCategory()); //分类
			participateUnits.set(i, articleTotal);
			//按单位名称查找栏目
			WebDirectory unitWebDirectory = (WebDirectory)siteService.getDirectoryByName(0, articleTotal.getUnitName(), false);
			if(unitWebDirectory==null) {
				continue;
			}
			//统计各个栏目的发布情况
			for(Iterator iterator = viewPackage.getView().getColumns().iterator(); iterator.hasNext();) {
				Column column = (Column)iterator.next();
				if(column.getName()==null || !column.getName().startsWith("issueTotal_")) {
					continue;
				}
				String columnName = column.getName().substring("issueTotal_".length());
				WebDirectory webDirectory = (WebDirectory)siteService.getDirectoryByName(unitWebDirectory.getId(), columnName, false);
				if(webDirectory==null) {
					continue;
				}
				//统计上报情况
				articleTotal.setCreateTotal(articleTotal.getCreateTotal() + siteResourceService.countSiteResources(webDirectory.getId(), 'a', beginDate, endDate)); //上报数量
				//统计发布情况
				int issueTotal = siteResourceService.countSiteResources(webDirectory.getId(), SiteResourceService.RESOURCE_STATUS_ISSUE, beginDate, endDate);
				articleTotal.setExtendPropertyValue(column.getName(), new Integer(issueTotal));
				articleTotal.setIssueTotal(articleTotal.getIssueTotal() + issueTotal); //采用数量
			}
		}
		Collections.sort(participateUnits, new Comparator() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object arg0, Object arg1) {
				AppraiseUnitArticleTotal articleTotal0 = (AppraiseUnitArticleTotal)arg0;
				AppraiseUnitArticleTotal articleTotal1 = (AppraiseUnitArticleTotal)arg1;
				return articleTotal0.getIssueTotal()==articleTotal1.getIssueTotal() ? 0 : (articleTotal0.getIssueTotal()<articleTotal1.getIssueTotal() ? 1 : -1);
			}
		});
		viewPackage.setRecords(participateUnits);
		viewPackage.setRecordCount(participateUnits.size());
	}

	/**
	 * @return the appraiseService
	 */
	public AppraiseService getAppraiseService() {
		return appraiseService;
	}

	/**
	 * @param appraiseService the appraiseService to set
	 */
	public void setAppraiseService(AppraiseService appraiseService) {
		this.appraiseService = appraiseService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
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
	 * @return the siteResourceService
	 */
	public SiteResourceService getSiteResourceService() {
		return siteResourceService;
	}

	/**
	 * @param siteResourceService the siteResourceService to set
	 */
	public void setSiteResourceService(SiteResourceService siteResourceService) {
		this.siteResourceService = siteResourceService;
	}	
}