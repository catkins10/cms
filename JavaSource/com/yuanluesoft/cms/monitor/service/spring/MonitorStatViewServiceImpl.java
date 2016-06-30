package com.yuanluesoft.cms.monitor.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.monitor.model.MonitorStat;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorStatViewServiceImpl extends StatisticViewServiceImpl {
	private OrgService orgService; //组织机构服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
		viewPackage.setRecordCount(viewPackage.getRecords()==null ? 0 : viewPackage.getRecords().size());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(view.getName().equals("monitorStat")) { //监察统计汇总
			MonitorStat monitorStat = new MonitorStat();
			Date monthBegin = DateTimeUtils.getMonthBegin();
			Date lastMonthBegin = DateTimeUtils.getLastMonthBegin();
			Date nextMonthBegin = DateTimeUtils.getNextMonthEnd();
			//信息公开
			monitorStat.setLastMonthInfos(monitorTotal("MonitorPublicInfo", "issueTime", lastMonthBegin, monthBegin, false, false, sessionInfo)); //上月信息数
			monitorStat.setLastMonthOntimeInfos(monitorTotal("MonitorPublicInfo", "issueTime", lastMonthBegin, monthBegin, true, false, sessionInfo)); //上月正常信息数
			monitorStat.setLastMonthTimeoutInfos(monitorTotal("MonitorPublicInfo", "issueTime", lastMonthBegin, monthBegin, false, true, sessionInfo)); //上月超时信息数
			monitorStat.setThisMonthInfos(monitorTotal("MonitorPublicInfo", "issueTime", monthBegin, nextMonthBegin, false, false, sessionInfo)); //本月信息数
			monitorStat.setThisMonthOntimeInfos(monitorTotal("MonitorPublicInfo", "issueTime", monthBegin, nextMonthBegin, true, false, sessionInfo)); //本月正常信息数
			monitorStat.setThisMonthTimeoutInfos(monitorTotal("MonitorPublicInfo", "issueTime", monthBegin, nextMonthBegin, false, true, sessionInfo)); //本月超时信息数
			//党务公开
			monitorStat.setLastMonthPartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", lastMonthBegin, monthBegin, false, false, sessionInfo)); //上月党务信息数
			monitorStat.setLastMonthOntimePartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", lastMonthBegin, monthBegin, true, false, sessionInfo)); //上月正常党务信息数
			monitorStat.setLastMonthTimeoutPartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", lastMonthBegin, monthBegin, false, true, sessionInfo)); //上月超时党务信息数
			monitorStat.setThisMonthPartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", monthBegin, nextMonthBegin, false, false, sessionInfo)); //本月党务信息数
			monitorStat.setThisMonthOntimePartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", monthBegin, nextMonthBegin, true, false, sessionInfo)); //本月正常党务信息数
			monitorStat.setThisMonthTimeoutPartyInfos(monitorTotal("MonitorPartyInfo", "issueTime", monthBegin, nextMonthBegin, false, true, sessionInfo)); //本月超时党务信息数
			//短信发送
			monitorStat.setLastMonthSendSms(monitorTotal("MonitorSmsSend", "sendTime", lastMonthBegin, monthBegin, false, false, sessionInfo)); //上月发送短信数
			monitorStat.setThisMonthSendSms(monitorTotal("MonitorSmsSend", "sendTime", monthBegin, nextMonthBegin, false, false, sessionInfo)); //本月发送短信数
			return ListUtils.generateList(monitorStat);
		}
		List orgIds  = null;
		long parentOrgId = RequestUtils.getParameterLongValue(request, "parentOrgId");
		if(parentOrgId==0) { //未指定目录ID
			orgIds = orgService.listDirectoryIds("root,category,area,unit", "manager,monitor", true, sessionInfo, 0, 0); //获取用户有管理权限或者监察权限的目录
		}
		else if(orgService.checkPopedom(parentOrgId, "manager,monitor", sessionInfo)) { //指定了目录ID,检查目录权限
			orgIds = ListUtils.generateList(new Long(parentOrgId));
		}
		if(orgIds==null || orgIds.isEmpty()) {
			return null;
		}
		List orgs = null; 
		if(orgIds.indexOf(new Long(0))!=-1) { //包含根目录
			orgs = orgService.listChildDirectories(0, "category,area,unit", null, null, false, false, sessionInfo, 0, 0); //获取下级目录
			orgs.add(0, orgService.getOrg(0));
		}
		else if(orgIds.size()>1) { //多个目录
			orgs = orgService.listDirectories(ListUtils.join(orgIds, ",", false));
		}
		else { //只有一个目录
			parentOrgId = ((Long)orgIds.get(0)).longValue();
			orgs = orgService.listChildDirectories(parentOrgId, "category,area,unit", null, null, false, false, sessionInfo, 0, 0); //获取下级目录
			if(orgs==null) {
				orgs = new ArrayList();
			}
			orgs.add(0, orgService.getDirectory(parentOrgId)); //获取父目录自己
		}
		view.setPageRows(1000); //获取全部记录
		String unitIdFieldName = (String)view.getExtendParameter("unitIdFieldName");
		if(unitIdFieldName==null) {
			unitIdFieldName = "unitId";
		}
		String unitNameFieldName = (String)view.getExtendParameter("unitNameFieldName");
		if(unitNameFieldName==null) {
			unitNameFieldName = "unitName";
		}
		String pojoName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf('.') + 1);
		view.addWhere(pojoName + "." + unitIdFieldName + " in (" + ListUtils.join(orgs, "id", ",", false) + ")");
		String statTimeFieldName = (String)view.getExtendParameter("statTimeFieldName"); //统计时间字段
		if(statTimeFieldName!=null) {
			Date beginDate = (Date)view.getExtendParameter("beginDate"); //开始时间
			Date endDate = (Date)view.getExtendParameter("endDate"); //结束时间
			if(beginDate!=null) {
				view.addWhere(pojoName + "." + statTimeFieldName + ">=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")");
			}
			if(beginDate!=null) {
				view.addWhere(pojoName + "." + statTimeFieldName + "<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")");
			}
		}
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		for(int i=0; i<orgs.size(); i++) {
			Org org = (Org)orgs.get(i);
			Object stat = ListUtils.findObjectByProperty(records, unitIdFieldName, new Long(org.getId()));
			if(stat==null) {
				try {
					stat = Class.forName(view.getPojoClassName()).newInstance();
				}
				catch(Exception e) {
					
				}
			}
			try {
				PropertyUtils.setProperty(stat, unitIdFieldName, new Long(org.getId()));
				PropertyUtils.setProperty(stat, unitNameFieldName, org.getDirectoryName());
			}
			catch(Exception e) {
				
			}
			orgs.set(i, stat);
		}
		return orgs;
	}
	
	/**
	 * 信息监察统计
	 * @param pojoName
	 * @param timeFieldName
	 * @param beginDate
	 * @param dateEnd
	 * @param ontime
	 * @param timeout
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private int monitorTotal(String pojoName, String timeFieldName, Date beginDate, Date dateEnd, boolean ontime, boolean timeout, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select count(distinct " + pojoName + ".id)" +
					 " from " + pojoName + " " + pojoName +
					 " where " + pojoName + "." + timeFieldName + ">=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and " + pojoName + "." + timeFieldName + "<=DATE(" + DateTimeUtils.formatDate(dateEnd, null) + ")" +
					 (ontime ? " and " + pojoName + ".timeoutLevel=0" : "") +
					 (timeout ? " and " + pojoName + ".timeoutLevel>0" : "") +
					 " and " + pojoName + ".unitId=" + sessionInfo.getUnitId();
		try {
			Number total = (Number)getDatabaseService().findRecordByHql(hql);
			return total==null ? 0 : total.intValue();
		}
		catch(Exception e) {
			return 0;
		}
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