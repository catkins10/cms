package com.yuanluesoft.appraise.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;

import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.appraise.model.AppraiseResult;
import com.yuanluesoft.appraise.model.ParticipateUnit;
import com.yuanluesoft.appraise.pojo.AppraiseMarkStandard;
import com.yuanluesoft.appraise.pojo.AppraiseParticipateUnit;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseResultViewServiceImpl extends ViewServiceImpl {
	private OrgService orgService; //组织机构服务
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
		int year = RequestUtils.getParameterIntValue(request, "year");
		if(year==0) {
			return;
		}
		String category = RequestUtils.getParameterStringValue(request, "category");
		long areaId = ((Number)ids.get(0)).longValue();
		//获取参评单位
		List participateUnits = appraiseService.listParticipateUnits(areaId, year);
		if(category!=null && !category.isEmpty()) {
			participateUnits = ListUtils.getSubListByProperty(participateUnits, "category", category); 
		}
		if(participateUnits==null || participateUnits.isEmpty()) {
			return;
		}
		//获取评分标准
		List markStandards = getDatabaseService().findRecordsByHql("from AppraiseMarkStandard AppraiseMarkStandard");
		List allAppraiseResults = new ArrayList(); //存放所有的评议结果,避免因嵌套产生死循环
		for(int i=0; i<participateUnits.size(); i++) {
			ParticipateUnit participateUnit = (ParticipateUnit)participateUnits.get(i);
			participateUnits.set(i, getAppraiseResult(participateUnit, year, markStandards, allAppraiseResults));
		}
		Collections.sort(participateUnits, new Comparator() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object arg0, Object arg1) {
				AppraiseResult appraiseResult0 = (AppraiseResult)arg0;
				AppraiseResult appraiseResult1 = (AppraiseResult)arg1;
				return appraiseResult0.getScore()==appraiseResult1.getScore() ? 0 : (appraiseResult0.getScore()<appraiseResult1.getScore() ? 1 : -1);
			}
		});
		viewPackage.setRecords(participateUnits);
		viewPackage.setRecordCount(participateUnits.size());
	}
	
	/**
	 * 获取单位评议结果
	 * @param participateUnit
	 * @param year
	 * @param allAppraiseResults
	 * @return
	 * @throws ServiceException
	 */
	private AppraiseResult getAppraiseResult(ParticipateUnit participateUnit, int year, List markStandards, List allAppraiseResults) throws ServiceException {
		//从列表中获取
		AppraiseResult appraiseResult = (AppraiseResult)ListUtils.findObjectByProperty(allAppraiseResults, "unitId", new Long(participateUnit.getUnitId()));
		if(appraiseResult!=null) {
			return appraiseResult;
		}
		appraiseResult = new AppraiseResult();
		appraiseResult.setUnitId(participateUnit.getUnitId()); //单位ID
		appraiseResult.setUnitName(participateUnit.getUnitName()); //单位名称
		appraiseResult.setCategory(participateUnit.getCategory()); //分类
		allAppraiseResults.add(appraiseResult); //存放所有的评议结果,避免因嵌套产生死循环
		appraiseResult.setSecondaryScore(-1); //二级单位得分,-1表示没有二级单位或者二级单位不是参评单位
		appraiseResult.setSubordinateScore(-1); //下级单位得分,-1表示没有下级单位或者下级单位不是参评单位
		//获取评议记录
		String hql = "select UnitAppraise.yearScoreComprehensive, Appraise.taskId, Appraise.appraiserType, Appraise.created" +
					 " from UnitAppraise UnitAppraise, Appraise Appraise" +
					 " where UnitAppraise.appraiseId=Appraise.id" +
					 " and Appraise.appraiseYear=" + year + //评议年度
					 " and Appraise.isSpecial=0" + //不是专题评议
					 " and UnitAppraise.unitId=" + participateUnit.getUnitId(); //评议单位
		List unitAppraises = getDatabaseService().findRecordsByHql(hql);
		//按评议任务ID筛选出最后的评议
		Map lastUnitAppraises = new HashedMap();
		for(Iterator iterator = unitAppraises==null ? null : unitAppraises.iterator(); iterator!=null && iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			Object[] valuesInMap = (Object[])lastUnitAppraises.get(values[1]);
			if(valuesInMap==null || ((Timestamp)valuesInMap[3]).before((Timestamp)values[3])) {
				lastUnitAppraises.put(values[1], values);
			}
		}
		//统计服务对象评议得分、专家评议得分
		int recipientCount = 0;
		int expertCount = 0;
		for(Iterator iterator = lastUnitAppraises.entrySet()==null ? null : lastUnitAppraises.entrySet().iterator(); iterator!=null && iterator.hasNext();) {
			Entry entry = (Entry)iterator.next();
			Object[] values = (Object[])entry.getValue();
			if(AppraiserService.APPRAISER_TYPE_RECIPIENT==((Number)values[2]).intValue()) { //服务对象评议
				recipientCount++;
				appraiseResult.setRecipientScore(appraiseResult.getRecipientScore() + ((Number)values[0]).doubleValue());
			}
			else if(AppraiserService.APPRAISER_TYPE_BASIC==((Number)values[2]).intValue()) { //专家评议
				expertCount++;
				appraiseResult.setExpertScore(appraiseResult.getExpertScore() + ((Number)values[0]).doubleValue());
			}
		}
		if(recipientCount>0) {
			appraiseResult.setRecipientScore(Math.round(appraiseResult.getRecipientScore() / recipientCount * 100) / 100.0);
		}
		if(expertCount>0) {
			appraiseResult.setExpertScore(Math.round(appraiseResult.getExpertScore() / expertCount * 100) / 100.0);
		}
		//统计扣分
		hql = "select sum(AppraiseDeduct.deduct)" +
			  " from AppraiseDeduct AppraiseDeduct" +
			  " where AppraiseDeduct.unitId=" + participateUnit.getUnitId() + //评议单位
			  " and AppraiseDeduct.year=" + year;
		Number deduct = (Number)getDatabaseService().findRecordByHql(hql);
		appraiseResult.setDeduct(deduct==null ? 0 : deduct.doubleValue());
		//计算得分
		appraiseResult.setScore(computeScore(participateUnit, appraiseResult, markStandards));
		//获取单位
		Unit unit = (Unit)orgService.getOrg(participateUnit.getUnitId());
		if(unit!=null) {
			//统计二级单位得分
			appraiseResult.setSecondaryScore(computeSubordinateUnitScore(unit.getSecondaryUnitIds(), year, markStandards, allAppraiseResults));
			//统计下级单位得分
			appraiseResult.setSubordinateScore(computeSubordinateUnitScore(unit.getSubordinateUnitIds(), year, markStandards, allAppraiseResults));
		}
		//重新计算得分
		if(appraiseResult.getSecondaryScore()!=-1 || appraiseResult.getSubordinateScore()!=-1) {
			appraiseResult.setScore(computeScore(participateUnit, appraiseResult, markStandards));
		}
		if(appraiseResult.getSecondaryScore()==-1) {
			appraiseResult.setSecondaryScore(0);
		}
		if(appraiseResult.getSubordinateScore()==-1) {
			appraiseResult.setSubordinateScore(0);
		}
		return appraiseResult;
	}
	
	/**
	 * 计算得分
	 * @param appraiseResult
	 * @param markStandards
	 * @return
	 */
	private double computeScore(ParticipateUnit participateUnit, AppraiseResult appraiseResult, List markStandards) {
		//获取评分标准
		AppraiseMarkStandard generalStandard = null; //通用标准,没有指定单位分类
		AppraiseMarkStandard categoryStandard = null; //分类特定标准
		for(Iterator iterator = markStandards==null ? null : markStandards.iterator(); iterator!=null && iterator.hasNext();) {
			AppraiseMarkStandard standard = (AppraiseMarkStandard)iterator.next();
			if(("," + standard.getAreaIds() + ",").indexOf("," + participateUnit.getAreaId() + ",")==-1) {
				continue;
			}
			if(standard.getCategories()==null || standard.getCategories().isEmpty()) {
				generalStandard = standard;
			}
			else if(("," + standard.getCategories() + ",").indexOf("," + participateUnit.getCategory() + ",")!=-1) {
				categoryStandard = standard;
			}
		}
		if(categoryStandard==null) {
			categoryStandard = generalStandard;
		}
		if(categoryStandard==null) {
			return 0;
		}
		double unitRatio = 100;
		double score = 0;
		if(appraiseResult.getSecondaryScore()!=-1) { //二级单位
			unitRatio -= categoryStandard.getSecondaryRatio();
			score += appraiseResult.getSecondaryScore() * (categoryStandard.getSecondaryRatio()/100);
		}
		if(appraiseResult.getSubordinateScore()!=-1) { //下级单位
			unitRatio -= categoryStandard.getSubordinateRatio();
			score += appraiseResult.getSubordinateScore() * (categoryStandard.getSubordinateRatio()/100);
		}
		score += (appraiseResult.getRecipientScore() * (categoryStandard.getRecipientRatio()/100) + //服务对象评议
				  appraiseResult.getExpertScore() * (categoryStandard.getExpertRatio()/100)) * //专家评议
				  (unitRatio/100);
		score -= Math.min(categoryStandard.getDeductLimit(), appraiseResult.getDeduct());
		return Math.round(score * 100) / 100.0;
	}
	
	/**
	 * 按单位ID获取参评单位
	 * @param unitId
	 * @param year
	 * @return
	 * @throws ServiceException
	 */
	private ParticipateUnit getParticipateUnitByUnitId(long unitId, int year) throws ServiceException {
		String hql = "from AppraiseParticipateUnit AppraiseParticipateUnit" +
					 " where AppraiseParticipateUnit.year=" + year +
					 " and (AppraiseParticipateUnit.unitIds='" + unitId + "'" +
					 "  or AppraiseParticipateUnit.unitIds like '%," + unitId + ",%'" +
					 "  or AppraiseParticipateUnit.unitIds like '" + unitId + ",%'" +
					 "  or AppraiseParticipateUnit.unitIds like '%," + unitId + "%')";
		AppraiseParticipateUnit appraiseParticipateUnit = (AppraiseParticipateUnit)getDatabaseService().findRecordByHql(hql);
		if(appraiseParticipateUnit==null) {
			return null;
		}
		String[] unitIds = appraiseParticipateUnit.getUnitIds().split(",");
		String[] unitNames = appraiseParticipateUnit.getUnitNames().split(",");
		for(int i=0; i<unitIds.length; i++) {
			if(unitIds[i].equals("" + unitId)) {
				return new ParticipateUnit(appraiseParticipateUnit.getAreaId(), unitId, unitNames[i], appraiseParticipateUnit.getCategory());
			}
		}
		return null;
	}
	
	/**
	 * 计算下级单位/二级单位的得分
	 * @param unitIds
	 * @param year
	 * @param markStandards
	 * @param allAppraiseResults
	 * @return
	 * @throws ServiceException
	 */
	private double computeSubordinateUnitScore(String unitIds, int year, List markStandards, List allAppraiseResults) throws ServiceException {
		if(unitIds==null || unitIds.isEmpty()) {
			return -1;
		}
		String[] ids = unitIds.split(",");
		int participateUnitCount = 0;
		double score = 0;
		for(int i=0; i<ids.length; i++) {
			ParticipateUnit participate = getParticipateUnitByUnitId(Long.parseLong(ids[i]), year);
			if(participate!=null) {
				participateUnitCount++;
				score += getAppraiseResult(participate, year, markStandards, allAppraiseResults).getScore();
			}
		}
		return participateUnitCount==0 ? -1 : Math.round(score / participateUnitCount * 100) / 100.0;
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
}