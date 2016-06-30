package com.yuanluesoft.j2oa.info.service.spring;

import java.sql.Date;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.j2oa.info.model.IssueTotal;
import com.yuanluesoft.j2oa.info.pojo.InfoPoint;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
/**
 * @author linchuan
 *
 */
public class InfoStatisticViewServiceImpl extends ViewServiceImpl {
	private OrgService orgService; //组织机构服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewData(com.yuanluesoft.jeaf.view.model.ViewPackage, int, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void retrieveViewData(ViewPackage viewPackage, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		Date beginDate = RequestUtils.getParameterDateValue(request, "beginDate"); //开始时间
		if(beginDate==null) { //没有指定开始时间,不统计
			return;
		}
		Date endDate = RequestUtils.getParameterDateValue(request, "endDate"); //结束时间
		boolean showCategory = "true".equals(RequestUtils.getParameterStringValue(request, "showCategory")); //是否显示单位分类
		boolean hideEmpty = "true".equals(RequestUtils.getParameterStringValue(request, "hideEmpty")); //是否隐藏空记录
		long parentOrgId = RequestUtils.getParameterLongValue(request, "parentOrgId"); //父机构ID
		
		//获取单位ID列表
		String unitIds = null;
		if(parentOrgId>0) {
			unitIds = ListUtils.join(orgService.listAllChildDirectories(parentOrgId, "unit", false), "id", ",", false);
		}
		
		//获取评分和稿酬配置
		List points = getDatabaseService().findRecordsByHql("from InfoPoint InfoPoint");
		
		//统计采用数
		String hql = "select count(InfoFilter.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoFilter.magazineDefineId" +
					 " from InfoFilter InfoFilter left join InfoFilter.infoReceive InfoReceive" +
					 " where InfoFilter.isCombined=0" +
					 " and InfoFilter.status=" + InfoService.INFO_STATUS_ISSUE +
					 " and InfoFilter.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 (endDate==null ? "" : " and InfoFilter.issueTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
					 (unitIds==null || unitIds.isEmpty() ? "" : " and InfoReceive.fromUnitId in (" + unitIds + ")") +
					 " group by InfoReceive.fromUnitId, InfoFilter.isBrief, InfoFilter.magazineDefineId";
		List issueTotals = getDatabaseService().findRecordsByHql(hql);
		
		//统计上报情况
		hql = "select count(InfoSendHigher.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level" +
			  " from InfoSendHigher InfoSendHigher, InfoFilter InfoFilter left join InfoFilter.infoReceive InfoReceive" +
			  " where InfoSendHigher.infoId=InfoFilter.id" +
			  " and InfoSendHigher.sendTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  (endDate==null ? "" : " and InfoSendHigher.sendTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
			  (unitIds==null || unitIds.isEmpty() ? "" : " and InfoReceive.fromUnitId in (" + unitIds + ")") +
			  " group by InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level";
		List sendHigherTotals = getDatabaseService().findRecordsByHql(hql);
		
		//统计上级采用情况
		hql = "select count(InfoSendHigher.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level, InfoSendHigher.useMagazine" +
			  " from InfoSendHigher InfoSendHigher, InfoFilter InfoFilter left join InfoFilter.infoReceive InfoReceive" +
			  " where InfoSendHigher.infoId=InfoFilter.id" +
			  " and InfoSendHigher.useTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  (endDate==null ? "" : " and InfoSendHigher.useTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
			  (unitIds==null || unitIds.isEmpty() ? "" : " and InfoReceive.fromUnitId in (" + unitIds + ")") +
			  " group by InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level, InfoSendHigher.useMagazine";
		List higherIssueTotals = getDatabaseService().findRecordsByHql(hql);
		
		//统计领导批示
		hql = "select count(InfoInstruct.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoInstruct.level" +
			  " from InfoInstruct InfoInstruct, InfoFilter InfoFilter left join InfoFilter.infoReceive InfoReceive" +
			  " where InfoInstruct.infoId=InfoFilter.id" +
			  " and InfoInstruct.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
			  (endDate==null ? "" : " and InfoInstruct.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
			  (unitIds==null || unitIds.isEmpty() ? "" : " and InfoReceive.fromUnitId in (" + unitIds + ")") +
			  " group by InfoReceive.fromUnitId, InfoFilter.isBrief, InfoInstruct.level";
		List instructTotals = getDatabaseService().findRecordsByHql(hql);

		List records = new ArrayList();
		//获取分类
		List categories = showCategory ? orgService.listChildDirectories(parentOrgId, "category", null, null, true, false, sessionInfo, 0, 0) : null;
		if(categories==null || categories.isEmpty()) { //不显示分类,或者没有分类
			List totals = total(viewPackage, parentOrgId, "", points, issueTotals, sendHigherTotals, higherIssueTotals, instructTotals, hideEmpty, records);
			ListUtils.addAll(records, totals);
			records.add(groupTotal(0, "总计", totals));
		}
		else { //分类统计
			List categoryTotals = categoriesTotal(viewPackage, categories, false, points, issueTotals, sendHigherTotals, higherIssueTotals, instructTotals, hideEmpty, records, sessionInfo);
			records.add(groupTotal(0, "总计", categoryTotals));
		}
		viewPackage.setRecords(records);
		viewPackage.setRecordCount(records==null ? 0 : records.size());
	}
	
	/**
	 * 分类统计
	 * @param viewPackage
	 * @param categories
	 * @param secondCategory
	 * @param points
	 * @param issueTotals
	 * @param sendHigherTotals
	 * @param higherIssueTotals
	 * @param instructTotals
	 * @param hideEmpty
	 * @param allTotals
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private List categoriesTotal(ViewPackage viewPackage, List categories, boolean secondCategory, List points, List issueTotals, List sendHigherTotals, List higherIssueTotals, List instructTotals, boolean hideEmpty, List allTotals, SessionInfo sessionInfo) throws ServiceException {
		List categoryTotals = new ArrayList();
		for(Iterator iterator = categories.iterator(); iterator.hasNext();) {
			Org category = (Org)iterator.next();
			//获取第二级分类
			List childCategories = secondCategory ? null : orgService.listChildDirectories(category.getId(), "category", null, null, true, false, sessionInfo, 0, 0);
			if(childCategories!=null && !childCategories.isEmpty()) { //有子类
				int index = allTotals.size();
				List childCategoryTotals = categoriesTotal(viewPackage, childCategories, true, points, issueTotals, sendHigherTotals, higherIssueTotals, instructTotals, hideEmpty, allTotals, sessionInfo);
				if(!childCategoryTotals.isEmpty()) {
					IssueTotal categoryTotal = groupTotal(category.getId(), StringUtils.getChineseNumber(categoryTotals.size() + 1, false) + "、" + category.getDirectoryName(), childCategoryTotals);
					categoryTotals.add(categoryTotal);
					allTotals.add(index, categoryTotal);
				}
				continue;
			}
			List totals = total(viewPackage, category.getId(), secondCategory ? "　　" : "　", points, issueTotals, sendHigherTotals, higherIssueTotals, instructTotals, hideEmpty, allTotals);
			if(totals!=null) {
				String num = StringUtils.getChineseNumber(categoryTotals.size() + 1, false);
				//分类合计
				IssueTotal categoryTotal = groupTotal(category.getId(), (secondCategory ? "　(" + num + ") " : num + "、") + category.getDirectoryName(), totals);
				categoryTotals.add(categoryTotal);
				allTotals.add(categoryTotal);
				ListUtils.addAll(allTotals, totals);
			}
		}
		return categoryTotals;
	}
	
	/**
	 * 统计
	 * @param viewPackage
	 * @param parentOrgId
	 * @param prefix
	 * @param points
	 * @param issueTotals
	 * @param sendHigherTotals
	 * @param higherIssueTotals
	 * @param instructTotals
	 * @param hideEmpty
	 * @param allTotals
	 * @return
	 * @throws ServiceException
	 */
	private List total(final ViewPackage viewPackage, long parentOrgId, String prefix, List points, List issueTotals, List sendHigherTotals, List higherIssueTotals, List instructTotals, boolean hideEmpty, List allTotals) throws ServiceException {
		//获取单位列表
		List units = orgService.listAllChildDirectories(parentOrgId, "unit", false);
		List totals = new ArrayList();
		for(Iterator iterator = units==null ? null : units.iterator(); iterator!=null && iterator.hasNext();) {
			Org unit = (Org)iterator.next();
			if(ListUtils.findObjectByProperty(allTotals, "id", new Long(unit.getId()))!=null) { //已经统计过,避免重复计算
				continue;
			}
			IssueTotal unitTotal = new IssueTotal();
			unitTotal.setId(unit.getId());
			unitTotal.setUnitName(unit.getDirectoryName());
			//统计采用数
			issueTotal(issueTotals, unit.getId(), unitTotal, points);
			//统计上报情况
			sendHigherTotal(sendHigherTotals, unit.getId(), unitTotal, points);
			//统计上级采用情况
			higherIssueTotal(higherIssueTotals, unit.getId(), unitTotal, points);
			//统计领导批示
			instructTotal(instructTotals, unit.getId(), unitTotal, points);
			if(!hideEmpty || !isEmptyTotal(unitTotal)) {
				totals.add(unitTotal);
			}
		}
		//排序
		final String propertyName = viewPackage.getSortColumn()==null ? "point" : viewPackage.getSortColumn();
		final boolean descendingSort = viewPackage.getSortColumn()==null || viewPackage.isDescendingSort();
		Collections.sort(totals, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				IssueTotal issueTotal0 = (IssueTotal)arg0;
				IssueTotal issueTotal1 = (IssueTotal)arg1;
				Number number0 = null, number1 = null;
				try {
					number0 = (Number)PropertyUtils.getProperty(issueTotal0, propertyName);
					number1 = (Number)PropertyUtils.getProperty(issueTotal1, propertyName);
				}
				catch(Exception e) {
					
				}
				if(number0==null) {
					number0 = new Double(issueTotal0.getPoint());
					number1 = new Double(issueTotal1.getPoint());
				}
				if(number0.doubleValue()==number1.doubleValue()) {
					return Collator.getInstance(Locale.CHINA).compare(issueTotal0.getUnitName(), issueTotal1.getUnitName());
				}
				else if(number0.doubleValue()>number1.doubleValue()) {
					 return descendingSort ? -1 : 1;
				}
				else {
					return descendingSort ? 1 : -1;
				}
			}
		});
		for(int i=0; i<totals.size(); i++) {
			IssueTotal issueTotal = (IssueTotal)totals.get(i);
			issueTotal.setUnitName(prefix + (i+1) + "、" + issueTotal.getUnitName());
		}
		return totals.isEmpty() ? null : totals;
	}
	
	/**
	 * 采用数统计
	 * @param issueTotals
	 * @param unitId
	 * @param unitTotal
	 * @param points
	 */
	private void issueTotal(List issueTotals, long unitId, IssueTotal unitTotal, List points) {
		for(Iterator iteratorTotal = issueTotals==null ? null : issueTotals.iterator(); iteratorTotal!=null && iteratorTotal.hasNext();) {
			Object[] values = (Object[])iteratorTotal.next(); //count(InfoFilter.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoFilter.magazineDefineId
			if(((Number)values[1]).longValue()!=unitId) {
				continue;
			}
			int count = ((Number)values[0]).intValue();
			unitTotal.setIssue(unitTotal.getIssue() + count); //采用数
			if(((Number)values[2]).intValue()==1) { //简讯
				unitTotal.setBriefIssue(unitTotal.getBriefIssue() + count); //简讯采用数
			}
			//统计各刊物采用情况
			Number magazineIssue = (Number)unitTotal.getExtendPropertyValue("issue_" + values[3]);
			unitTotal.setExtendPropertyValue("issue_" + values[3], new Integer((magazineIssue==null ? 0 : magazineIssue.intValue()) + count));
			
			//计算积分和稿酬
			InfoPoint point = null;
			InfoPoint magazinePoint = null;
			for(Iterator iteratorPoint = points==null ? null : points.iterator(); iteratorPoint!=null && iteratorPoint.hasNext();) {
				InfoPoint infoPoint = (InfoPoint)iteratorPoint.next();
				if(infoPoint.getItem()!=0) { //得分项目,0/采用,1/领导批示,2/报县(区)办,3/县(区)办采用,4/县(区)领导批示,5/报市办,6/市办采用,7/市领导批示,8/报省办,9/省办采用,10/省领导批示,11/报国办,12/国办采用,13/国办领导批示
					continue;
				}
				if(infoPoint.getMagazineIds()==null || infoPoint.getMagazineIds().isEmpty()) {
					point = infoPoint;
				}
				else if(("," + infoPoint.getMagazineIds() + ",").indexOf("," + values[3] + ",")!=-1) {
					magazinePoint = infoPoint;
				}
			}
			pointTotal(unitTotal, magazinePoint==null ? point : magazinePoint, count, ((Number)values[2]).intValue()==1); 
		}
	}
	
	/**
	 * 报送统计
	 * @param sendHigherTotals
	 * @param unitId
	 * @param unitTotal
	 * @param points
	 */
	private void sendHigherTotal(List sendHigherTotals, long unitId, IssueTotal unitTotal, List points) {
		for(Iterator iteratorTotal = sendHigherTotals==null ? null : sendHigherTotals.iterator(); iteratorTotal!=null && iteratorTotal.hasNext();) {
			Object[] values = (Object[])iteratorTotal.next(); //count(InfoSendHigher.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level
			if(((Number)values[1]).longValue()!=unitId) {
				continue;
			}
			int count = ((Number)values[0]).intValue();
			int level = ((Number)values[3]).intValue(); //0/乡镇,1/县,2/市,3/省,4/国办
			if(level==1) {
				unitTotal.setSendCounty(unitTotal.getSendCounty() + count); //上报县办数量
			}
			else if(level==2) {
				unitTotal.setSendCity(unitTotal.getSendCity() + count); //上报市办数量
			}
			else if(level==3) {
				unitTotal.setSendProvincial(unitTotal.getSendProvincial() + count); //上报省办数量
			}
			else if(level==4) {
				unitTotal.setSendCountry(unitTotal.getSendCountry() + count); //上报国办数量
			}
			//得分项目,0/采用,1/领导批示,2/报县(区)办,3/县(区)办采用,4/县(区)领导批示,5/报市办,6/市办采用,7/市领导批示,8/报省办,9/省办采用,10/省领导批示,11/报国办,12/国办采用,13/国办领导批示
			InfoPoint point = (InfoPoint)ListUtils.findObjectByProperty(points, "item", new Integer(level*3-1));
			pointTotal(unitTotal, point, count, ((Number)values[2]).intValue()==1);
		}
	}
	
	/**
	 * 上级采用统计
	 * @param higherIssueTotals
	 * @param unitId
	 * @param unitTotal
	 * @param points
	 */
	private void higherIssueTotal(List higherIssueTotals, long unitId, IssueTotal unitTotal, List points) {
		for(Iterator iteratorTotal = higherIssueTotals==null ? null : higherIssueTotals.iterator(); iteratorTotal!=null && iteratorTotal.hasNext();) {
			Object[] values = (Object[])iteratorTotal.next(); //count(InfoSendHigher.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoSendHigher.level, InfoSendHigher.useMagazine
			if(((Number)values[1]).longValue()!=unitId) {
				continue;
			}
			int count = ((Number)values[0]).intValue();
			int level = ((Number)values[3]).intValue(); //0/乡镇,1/县,2/市,3/省,4/国办
			if(level==1) {
				unitTotal.setCountyIssue(unitTotal.getCountyIssue() + count); //县办采用数量
			}
			else if(level==2) {
				unitTotal.setCityIssue(unitTotal.getCityIssue() + count); //市办采用数量
			}
			else if(level==3) {
				unitTotal.setProvincialIssue(unitTotal.getProvincialIssue() + count); //省办采用数量
			}
			else if(level==4) {
				unitTotal.setCountryIssue(unitTotal.getCountryIssue() + count); //国办采用数量
			}
			InfoPoint point = null;
			InfoPoint magazinePoint = null;
			for(Iterator iteratorPoint = points==null ? null : points.iterator(); iteratorPoint!=null && iteratorPoint.hasNext();) {
				InfoPoint infoPoint = (InfoPoint)iteratorPoint.next();
				if(infoPoint.getItem()!=level*3) { //得分项目,0/采用,1/领导批示,2/报县(区)办,3/县(区)办采用,4/县(区)领导批示,5/报市办,6/市办采用,7/市领导批示,8/报省办,9/省办采用,10/省领导批示,11/报国办,12/国办采用,13/国办领导批示
					continue;
				}
				if(infoPoint.getMagazineNames()==null || infoPoint.getMagazineNames().isEmpty()) {
					point = infoPoint;
				}
				else if(values[4]!=null && !((String)values[4]).isEmpty() && ("," + infoPoint.getMagazineNames() + ",").indexOf("," + values[4] + ",")!=-1) {
					magazinePoint = infoPoint;
				}
			}
			pointTotal(unitTotal, magazinePoint==null ? point : magazinePoint, count, ((Number)values[2]).intValue()==1);
		}
	}
	
	/**
	 * 批示统计
	 * @param instructTotals
	 * @param unitId
	 * @param unitTotal
	 * @param points
	 */
	private void instructTotal(List instructTotals, long unitId, IssueTotal unitTotal, List points) {
		for(Iterator iteratorTotal = instructTotals==null ? null : instructTotals.iterator(); iteratorTotal!=null && iteratorTotal.hasNext();) {
			Object[] values = (Object[])iteratorTotal.next(); //count(InfoInstruct.id), InfoReceive.fromUnitId, InfoFilter.isBrief, InfoInstruct.level
			if(((Number)values[1]).longValue()!=unitId) {
				continue;
			}
			int count = ((Number)values[0]).intValue();
			int level = ((Number)values[3]).intValue(); //0/乡镇,1/县,2/市,3/省,4/国办
			if(level==1) {
				unitTotal.setCountyInstruct(unitTotal.getCountyInstruct() + count); //县领导批示数量
			}
			else if(level==2) {
				unitTotal.setCityInstruct(unitTotal.getCityInstruct() + count); //市领导批示数量
			}
			else if(level==3) {
				unitTotal.setProvincialInstruct(unitTotal.getProvincialInstruct() + count); //省领导批示数量
			}
			else if(level==4) {
				unitTotal.setCountryInstruct(unitTotal.getCountryInstruct() + count); //国家领导批示数量
			}
			//得分项目,0/采用,1/领导批示,2/报县(区)办,3/县(区)办采用,4/县(区)领导批示,5/报市办,6/市办采用,7/市领导批示,8/报省办,9/省办采用,10/省领导批示,11/报国办,12/国办采用,13/国办领导批示
			InfoPoint point = (InfoPoint)ListUtils.findObjectByProperty(points, "item", new Integer(level*3+1));
			pointTotal(unitTotal, point, count, ((Number)values[2]).intValue()==1);
		}
	}
	
	/**
	 * 计算积分和稿酬
	 * @param unitTotal
	 * @param point
	 * @param count
	 * @param isBrief
	 */
	private void pointTotal(IssueTotal unitTotal, InfoPoint point, int count, boolean isBrief) {
		if(point!=null) {
			unitTotal.setPoint(unitTotal.getPoint() + (isBrief ? point.getBriefPoint() : point.getPoint()) * count);
			unitTotal.setRemuneration(unitTotal.getRemuneration() + (isBrief ? point.getBriefRemuneration() : point.getRemuneration()) * count);
		}
	}
	
	/**
	 * 汇总
	 * @param orgId
	 * @param orgName
	 * @param totals
	 * @return
	 * @throws ServiceException
	 */
	private IssueTotal groupTotal(long orgId, String orgName, List totals) throws ServiceException {
		IssueTotal issueTotal = new IssueTotal();
		issueTotal.setId(orgId);
		issueTotal.setUnitName(orgName);
		for(Iterator iterator = totals==null ? null : totals.iterator(); iterator!=null && iterator.hasNext();) {
			IssueTotal total = (IssueTotal)iterator.next();
			issueTotal.setPoint(issueTotal.getPoint() + total.getPoint()); //积分
			issueTotal.setRemuneration(issueTotal.getRemuneration() + total.getRemuneration()); //稿酬
			issueTotal.setIssue(issueTotal.getIssue() + total.getIssue()); //采用数
			issueTotal.setBriefIssue(issueTotal.getBriefIssue() + total.getBriefIssue()); //简讯采用数
			issueTotal.setSendCounty(issueTotal.getSendCounty() + total.getSendCounty()); //上报县办数量
			issueTotal.setSendCity(issueTotal.getSendCity() + total.getSendCity()); //上报市办数量
			issueTotal.setSendProvincial(issueTotal.getSendProvincial() + total.getSendProvincial()); //上报省办数量
			issueTotal.setSendCountry(issueTotal.getSendCountry() + total.getSendCountry()); //上报国办数量
			issueTotal.setCountyIssue(issueTotal.getCountyIssue() + total.getCountyIssue()); //县办采用数量
			issueTotal.setCityIssue(issueTotal.getCityIssue() + total.getCityIssue()); //市办采用数量
			issueTotal.setProvincialIssue(issueTotal.getProvincialIssue() + total.getProvincialIssue()); //省办采用数量
			issueTotal.setCountryIssue(issueTotal.getCountryIssue() + total.getCountryIssue()); //国办采用数量
			issueTotal.setCountyInstruct(issueTotal.getCountyInstruct() + total.getCountyInstruct()); //县领导批示数量
			issueTotal.setCityInstruct(issueTotal.getCityInstruct() + total.getCityInstruct()); //市领导批示数量
			issueTotal.setProvincialInstruct(issueTotal.getProvincialInstruct() + total.getProvincialInstruct()); //省领导批示数量
			issueTotal.setCountryInstruct(issueTotal.getCountryInstruct() + total.getCountryInstruct()); //国家领导批示数量
			Set extendPropertyNames = total.getExtendPropertyNames();
			for(Iterator iteratorProperty = extendPropertyNames==null ? null : extendPropertyNames.iterator(); iteratorProperty!=null && iteratorProperty.hasNext();) {
				String propertyName = (String)iteratorProperty.next();
				Number value = (Number)issueTotal.getExtendPropertyValue(propertyName);
				issueTotal.setExtendPropertyValue(propertyName, new Integer((value==null ? 0 : value.intValue()) + ((Number)total.getExtendPropertyValue(propertyName)).intValue()));
			}
		}
		return issueTotal;
	}

	/**
	 * 是否空记录
	 * @param issueTotal
	 * @return
	 */
	public boolean isEmptyTotal(IssueTotal issueTotal) {
		return issueTotal.getPoint()==0 && //积分
			   issueTotal.getRemuneration()==0 && //稿酬
			   issueTotal.getIssue()==0 && //采用数
			   issueTotal.getBriefIssue()==0 && //简讯采用数
			   issueTotal.getSendCounty()==0 && //上报县办数量
			   issueTotal.getSendCity()==0 && //上报市办数量
			   issueTotal.getSendProvincial()==0 && //上报省办数量
			   issueTotal.getSendCountry()==0 && //上报国办数量
			   issueTotal.getCountyIssue()==0 && //县办采用数量
			   issueTotal.getCityIssue()==0 && //市办采用数量
			   issueTotal.getProvincialIssue()==0 && //省办采用数量
			   issueTotal.getCountryIssue()==0 && //国办采用数量
			   issueTotal.getCountyInstruct()==0 && //县领导批示数量
			   issueTotal.getCityInstruct()==0 && //市领导批示数量
			   issueTotal.getProvincialInstruct()==0 && //省领导批示数量
			   issueTotal.getCountryInstruct()==0; //国家领导批示数量
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#resetViewColumns(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewColumns(View view, String viewMode, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		super.resetViewColumns(view, viewMode, request, sessionInfo);
		if(View.VIEW_DISPLAY_MODE_CUSTOMIZE.equals(viewMode)) {
			String hql = "select InfoMagazineDefine.id, InfoMagazineDefine.name from InfoMagazineDefine InfoMagazineDefine order by InfoMagazineDefine.name";
			List defines = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = defines==null ? null : defines.iterator(); iterator!=null && iterator.hasNext();) {
				Object[] values = (Object[])iterator.next();
				view.getColumns().add(new Column("issue_" + values[0], "" + values[1], Column.COLUMN_TYPE_FIELD));
			}
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