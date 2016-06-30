package com.yuanluesoft.cms.siteresource.service.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class SiteResourceViewServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		String status = RequestUtils.getParameterStringValue(request, "status"); //状态
		String unitName = RequestUtils.getParameterStringValue(request, "unitName"); //单位ID
		long orgId = RequestUtils.getParameterLongValue(request, "orgId"); //部门ID
		String statisticFieldNames = RequestUtils.getParameterStringValue(request, "statisticFieldNames"); //统计字段
		String siteIds = RequestUtils.getParameterStringValue(request, "siteIds"); //站点/栏目ID
		if(status!=null && !status.isEmpty()) {
			view.addWhere("5".equals(status) ? "SiteResource.status>='" + JdbcUtils.resetQuot(status) + "'" : "SiteResource.status='" + JdbcUtils.resetQuot(status) + "'");
		}
		if("unitName,orgId".equals(statisticFieldNames)) {
			view.addWhere("SiteResource.unitName='" + JdbcUtils.resetQuot(unitName) + "' and SiteResource.orgId=" + orgId);
		}
		else if("unitName".equals(statisticFieldNames) || "unitId".equals(statisticFieldNames)) {
			view.addWhere("SiteResource.unitName='" + JdbcUtils.resetQuot(unitName) + "'");
		}
		if(siteIds!=null && !siteIds.isEmpty()) {
			view.addJoin(", WebDirectorySubjection WebDirectorySubjection");
			String where = "subjections.siteId=WebDirectorySubjection.directoryId" +
						   " and WebDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(siteIds) + ")";
			view.addWhere(where);
		}
		//分类
		viewPackage.setCategories(RequestUtils.getParameterStringValue(request, "categories"));
		//搜索条件
		String searchConditions = RequestUtils.getParameterStringValue(request, "searchConditions");
		if(searchConditions!=null && !searchConditions.isEmpty()) {
			viewPackage.setSearchConditions(searchConditions);
			viewPackage.setSearchMode(true);
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}
}