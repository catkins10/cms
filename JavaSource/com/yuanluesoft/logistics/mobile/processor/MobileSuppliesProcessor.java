package com.yuanluesoft.logistics.mobile.processor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.PlaceName;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.logistics.mobile.forms.SupplyQuery;
import com.yuanluesoft.logistics.supply.processor.SuppliesProcessor;

/**
 * 手机端货源列表处理器
 * @author linchuan
 *
 */
public class MobileSuppliesProcessor extends SuppliesProcessor {
	private GpsService gpsService; //定位服务,解析地名用
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#generateSearchConditions(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List searchConditions = super.generateSearchConditions(view, recordListModel, webDirectory, parentSite, sitePage, request);
		//获取地名
		SupplyQuery supplyQuery = (SupplyQuery)request.getAttribute("supplyQuery");
		if(supplyQuery==null) {
			return searchConditions;
		}
		if(searchConditions==null) {
			searchConditions = new ArrayList();
		}
		String placeName = supplyQuery.getPlaceName();
		//按地名获取组织机构ID
		Org org = getAreaIdByPlaceName(placeName);
		if(org!=null) { //地区有在系统中注册
			String areaConditions = generateSearchConditionsByAreaId(org.getId(), 0);
			if(areaConditions!=null) {
				searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, areaConditions, null));
			}
		}
		else { //地区未在系统中注册
			//直接按地名查找
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, "departures.departure", "string", Condition.CONDITION_EXPRESSION_CONTAIN, placeName, null));
		}
		return searchConditions;
	}

	/**
	 * 按地名获取地区
	 * @param placeNameText
	 * @return
	 * @throws ServiceException
	 */
	protected Org getAreaIdByPlaceName(String placeNameText) throws ServiceException {
		if(placeNameText==null) {
			return null;
		}
		PlaceName placeName = gpsService.parsePlaceName(placeNameText);
		if(placeName!=null) {
			//按县查找地区
			if(placeName.getDistrict()!=null) {
				Org org = (Org)getOrgService().getDirectoryByName(0, placeName.getDistrict(), false);
				if(org!=null) {
					return org;
				}
			}
			//按市查找地区
			if(placeName.getCity()!=null) {
				Org org = (Org)getOrgService().getDirectoryByName(0, placeName.getCity(), false);
				if(org!=null) {
					return org;
				}
			}
			//按省查地区
			if(placeName.getProvince()!=null) {
				Org org = (Org)getOrgService().getDirectoryByName(0, placeName.getProvince(), false);
				if(org!=null) {
					return org;
				}
			}
		}
		String[] postfix = {"县", "市", "省", "自治区", "自治州"};
		for(int i=0; i<postfix.length; i++) {
			Org org = (Org)getOrgService().getDirectoryByName(0, placeNameText + postfix[i], false);
			if(org!=null) {
				return org;
			}
		}
		return null;
	}

	/**
	 * @return the gpsService
	 */
	public GpsService getGpsService() {
		return gpsService;
	}

	/**
	 * @param gpsService the gpsService to set
	 */
	public void setGpsService(GpsService gpsService) {
		this.gpsService = gpsService;
	}
}