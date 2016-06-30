package com.yuanluesoft.logistics.supply.processor;

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
public class SuppliesProcessor extends RecordListProcessor {
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
		String areaConditions = generateSearchConditionsByAreaId(departureAreaId, destinationAreaId);
		if(areaConditions!=null) {
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, areaConditions, null));
		}
		return searchConditions;
	}
	
	/**
	 * 按地区ID生成查询条件
	 * @param departureAreaId 出发地点ID
	 * @param destinationAreaId 目的地点ID
	 * @return
	 * @throws ServiceException
	 */
	protected String generateSearchConditionsByAreaId(long departureAreaId, long destinationAreaId) throws ServiceException {
		String conditions = null;
		if(departureAreaId>0) { //指定了出发地点
			String areaIds = orgService.getAllChildDirectoryIds("" + departureAreaId, "area");
			conditions = "departures.departureId in (" + JdbcUtils.validateInClauseNumbers(areaIds) + ")";
		}
		if(destinationAreaId>0) { //指定了目的地点
			String areaIds = orgService.getAllChildDirectoryIds("" + destinationAreaId, "area");
			conditions = (conditions==null ? "" : "(" + conditions + ") and ") + "destinations.destinationId in (" + JdbcUtils.validateInClauseNumbers(areaIds) + ")";
		}
		return conditions;
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