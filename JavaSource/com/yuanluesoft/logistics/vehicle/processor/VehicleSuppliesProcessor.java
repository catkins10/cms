package com.yuanluesoft.logistics.vehicle.processor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleSuppliesProcessor extends RecordListProcessor {
	private OrgService orgService; //组织机构服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#generateSearchConditions(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List searchConditions = super.generateSearchConditions(view, recordListModel, webDirectory, parentSite, sitePage, request);
		if(searchConditions==null) {
			searchConditions = new ArrayList();
		}
		long departureAreaId = RequestUtils.getParameterLongValue(request, "departureAreaId"); //出发地点ID
		long destinationAreaId = RequestUtils.getParameterLongValue(request, "destinationAreaId"); //目的地点ID
		if(departureAreaId>0) { //指定了出发地点
			String areaIds = orgService.getAllChildDirectoryIds("" + departureAreaId, "area");
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, "departures.departureId in (" + JdbcUtils.validateInClauseNumbers(areaIds) + ")", null));
		}
		if(destinationAreaId>0) { //指定了目的地点
			String areaIds = orgService.getAllChildDirectoryIds("" + destinationAreaId, "area");
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, "destinations.destinationId in (" + JdbcUtils.validateInClauseNumbers(areaIds) + ")", null));
		}
		return searchConditions;
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
}