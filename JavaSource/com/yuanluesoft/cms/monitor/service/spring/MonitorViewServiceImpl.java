package com.yuanluesoft.cms.monitor.service.spring;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.jeaf.view.statisticview.model.Statistic;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticColumn;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorViewServiceImpl extends ViewServiceImpl {
	private OrgService orgService; //组织机构服务
	private ViewDefineService viewDefineService; //视图定义服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		//获取用户有管理权限的机构目录ID
		boolean unitConfig = view.getName().equals("unitConfig");
		List directoryIds = orgService.listDirectoryIds("root,category,area,unit", unitConfig ? "manager" : "manager,monitor", true, sessionInfo, 0, 0);
		String pojoClassName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf('.')+1);
		if(directoryIds==null || directoryIds.isEmpty()) {
			if(unitConfig) {
				throw new PrivilegeException();
			}
			view.addWhere(pojoClassName + ".id=-1"); //不输出任何数据
		}
		else if(directoryIds.indexOf(new Long(0))==-1) { //不含根目录
			view.setJoin(",OrgSubjection OrgSubjection");
			view.addWhere(pojoClassName + ".unitId=OrgSubjection.directoryId and OrgSubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(directoryIds, ",", false)) + ")");
		}
		
		//对话框参数处理
		String sourceViewName = RequestUtils.getParameterStringValue(request, "sourceViewName"); //源视图
		if(sourceViewName!=null) {
			StatisticView sourceView = (StatisticView)viewDefineService.getView(view.getApplicationName(), sourceViewName, sessionInfo);
			String sourceColumnName = RequestUtils.getParameterStringValue(request, "sourceColumnName"); //源列名称
			String unitId = RequestUtils.getParameterStringValue(request, "unitId"); //单位ID
			Date beginDate = RequestUtils.getParameterDateValue(request, "beginDate"); //开始日期
			Date endDate = RequestUtils.getParameterDateValue(request, "endDate"); //结束日期
			String statTimeFieldName = (String)sourceView.getExtendParameter("statTimeFieldName"); //统计时间字段
			if(statTimeFieldName!=null) {
				if(beginDate!=null) {
					view.addWhere(pojoClassName + "." + statTimeFieldName + ">=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")"); 
				}
				if(endDate!=null) {
					view.addWhere(pojoClassName + "." + statTimeFieldName + "<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")"); 
				}
			}
			Statistic statistic = (Statistic)sourceView.getStatistics().get(0);
			StatisticColumn sourceColumn = (StatisticColumn)ListUtils.findObjectByProperty(statistic.getStatisticColumns(), "name", sourceColumnName);
			if(sourceColumn.getWhereExtend()!=null) { //统计条件
				view.addWhere(sourceColumn.getWhereExtend());
			}
			if(unitId!=null) { //指定了单位ID
				view.addWhere(pojoClassName + ".unitId=" + unitId); 
			}
			//分类
			String categories = RequestUtils.getParameterStringValue(request, "categories");
			if(categories!=null && !categories.isEmpty()) {
				viewPackage.setCategories(categories);
			}
			//搜索条件
			String searchConditions = RequestUtils.getParameterStringValue(request, "searchConditions");
			if(searchConditions!=null && !searchConditions.isEmpty()) {
				viewPackage.setSearchConditions(searchConditions);
				viewPackage.setSearchMode(true);
			}
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
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
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}
}