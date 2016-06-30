package com.yuanluesoft.cms.siteresource.report.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.report.model.columnstat.ColumnStat;
import com.yuanluesoft.cms.siteresource.report.model.columnstat.UnitStat;
import com.yuanluesoft.cms.siteresource.report.model.ensurereport.EnsureReport;
import com.yuanluesoft.cms.siteresource.report.model.ensurereport.MonthStat;
import com.yuanluesoft.cms.siteresource.report.model.ensurereport.UnitCategory;
import com.yuanluesoft.cms.siteresource.report.pojo.EnsureColumnConfig;
import com.yuanluesoft.cms.siteresource.report.pojo.EnsureUnitCategory;
import com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SiteResourceStatServiceImpl implements SiteResourceStatService {
	private DatabaseService databaseService; //数据库服务
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService#createColumnStatReport(java.sql.Date, java.sql.Date, long, java.lang.String)
	 */
	public List createColumnStatReport(Date beginDate, Date endDate, long siteId, String unitIds) throws ServiceException {
		List unitStats = new ArrayList();
		String hql = "from Unit Unit" +
					 (unitIds==null || unitIds.isEmpty() ? "" : " where Unit.id in (" + JdbcUtils.validateInClauseNumbers(unitIds) + ")") +
					 " order by Unit.directoryName";
		List units = databaseService.findRecordsByHql(hql);
		for(Iterator iterator = units.iterator(); iterator.hasNext();) {
			//单位统计
			Unit unit = (Unit)iterator.next();
			UnitStat unitStat = new UnitStat();
			unitStat.setUnitId(unit.getId()); //单位ID
			unitStat.setUnitName(unit.getDirectoryName()); //单位名称
			unitStat.setTotal(issueStat(unit, siteId, beginDate, endDate)); //发布统计
			unitStat.setColumnStats(columnStat(unit, beginDate, endDate, siteId, 0)); //栏目统计
			unitStats.add(unitStat);
		}
		return unitStats;
	}
	
	/**
	 * 栏目统计
	 * @param unit
	 * @param beginDate
	 * @param endDate
	 * @param parentDirectoryId
	 * @param deep
	 * @return
	 * @throws ServiceException
	 */
	private List columnStat(Unit unit, Date beginDate, Date endDate, long parentDirectoryId, int deep) throws ServiceException {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUserId(unit.getId());
		sessionInfo.setDepartmentIds("" + unit.getId());
		List childColumns = siteService.listChildDirectories(parentDirectoryId, null, "editor", null, false, false, sessionInfo, 0, 0);
		if(childColumns==null || childColumns.isEmpty()) {
			return null;
		}
		List columnStats = new ArrayList();
		for(Iterator iterator = childColumns.iterator(); iterator.hasNext();) {
			WebDirectory webDirectory = (WebDirectory)iterator.next();
			ColumnStat columnStat = new ColumnStat();
			columnStat.setColumnId(webDirectory.getId()); //栏目ID
			columnStat.setColumnName(webDirectory.getDirectoryName()); //栏目名称
			columnStat.setTotal(issueStat(unit, webDirectory.getId(), beginDate, endDate)); //发布统计
			columnStat.setRowspan(1); //占用的表格行数
			if(deep<3) { //少于3级,子栏目统计
				List childColumnStats = columnStat(unit, beginDate, endDate, webDirectory.getId(), deep+1);
				if(childColumnStats!=null && !childColumnStats.isEmpty()) {
					int rowspan = 0;
					for(Iterator iteratorChild = childColumnStats.iterator(); iteratorChild.hasNext();) {
						ColumnStat childColumnStat = (ColumnStat)iteratorChild.next();
						rowspan += childColumnStat.getRowspan();
					}
					columnStat.setRowspan(rowspan);
				}
				columnStat.setColumnStats(childColumnStats);
			}
			columnStats.add(columnStat);
		}
		return columnStats;
	}
	
	/**
	 * 发布统计
	 * @param unit
	 * @param webDirectoryId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private int issueStat(Unit unit, long webDirectoryId, Date beginDate, Date endDate) {
		String hql = "select count(SiteResource.id)" +
					 " from SiteResource SiteResource" +
					 (webDirectoryId==0 ? "" : ", SiteResourceSubjection SiteResourceSubjection, WebDirectorySubjection WebDirectorySubjection") +
					 " where SiteResource.unitName='" + JdbcUtils.resetQuot(unit.getDirectoryName()) + "'" +
					 " and SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
					 (beginDate==null ? "" : " and SiteResource.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
					 (endDate==null ? "" : " and SiteResource.issueTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")");
		if(webDirectoryId>0) {
			hql += " and SiteResourceSubjection.resourceId=SiteResource.id" +
				   " and WebDirectorySubjection.directoryId=SiteResourceSubjection.siteId" +
				   " and WebDirectorySubjection.parentDirectoryId=" + webDirectoryId;
		}
		Number total = (Number)databaseService.findRecordByHql(hql);
		return total==null ? 0 : total.intValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService#listEnsureColumnConfigs(long)
	 */
	public List listEnsureColumnConfigs(long siteId) throws ServiceException {
		String hql = "from EnsureColumnConfig EnsureColumnConfig" +
					 " where EnsureColumnConfig.siteId=" + siteId +
					 " order by EnsureColumnConfig.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService#listEnsureUnitCategories(long)
	 */
	public List listEnsureUnitCategories(long siteId) throws ServiceException {
		String hql = "from EnsureUnitCategory EnsureUnitCategory" +
					 " where EnsureUnitCategory.siteId=" + siteId +
					 " order by EnsureUnitCategory.priority DESC, EnsureUnitCategory.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.report.service.SiteResourceStatService#writeEnsureReport(long, java.sql.Date, java.sql.Date)
	 */
	public EnsureReport writeEnsureReport(long siteId, Date beginDate, Date endDate) throws ServiceException {
		EnsureReport ensureReport = new EnsureReport();
		List ensureColumnConfigs = listEnsureColumnConfigs(siteId); //栏目配置
		List ensureUnitCategories = listEnsureUnitCategories(siteId); //单位分类
		if(ensureColumnConfigs==null || ensureColumnConfigs.isEmpty() || ensureUnitCategories==null || ensureUnitCategories.isEmpty()) {
			return ensureReport;
		}
		//转换为UnitCategory模型
		ensureReport.setUnitCategories(new ArrayList());
		for(Iterator iterator = ensureUnitCategories.iterator(); iterator.hasNext();) {
			EnsureUnitCategory ensureUnitCategory = (EnsureUnitCategory)iterator.next();
			UnitCategory unitCategory = new UnitCategory();
			unitCategory.setCategory(ensureUnitCategory.getCategory());
			unitCategory.setUnitNames(ensureUnitCategory.getUnitNames());
			retrieveClusterUnitNames(unitCategory); //检查是否集群单位
			unitCategory.setUnitStats(new ArrayList());
			ensureReport.getUnitCategories().add(unitCategory);
		}
		//设置月份列表
		ensureReport.setMonths(new ArrayList());
		for(Date monthBegin = DateTimeUtils.set(beginDate, Calendar.DAY_OF_MONTH, 1); !monthBegin.after(endDate); monthBegin=DateTimeUtils.add(monthBegin, Calendar.MONTH, 1)) {
			ensureReport.getMonths().add(new Integer(DateTimeUtils.getMonth(monthBegin) + 1));
		}
		//开始统计
		for(Iterator iterator = ensureReport.getUnitCategories().iterator(); iterator.hasNext();) {
			UnitCategory unitCategory = (UnitCategory)iterator.next();
			Map averages = new HashMap(); //各类平均数
			String[] unitNames = unitCategory.getUnitNames().split(",");
			for(int i=0; i<unitNames.length; i++) {
				unitCategory.getUnitStats().add(unitStat(unitNames[i], siteId, ensureColumnConfigs, unitCategory, averages, beginDate, endDate));
			}
		}
		return ensureReport;
	}
	
	/**
	 * 单位统计
	 * @param unitName
	 * @param siteId
	 * @param ensureColumnConfigs
	 * @param unitCategory
	 * @param averages
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	private com.yuanluesoft.cms.siteresource.report.model.ensurereport.UnitStat unitStat(String unitName, long siteId, List ensureColumnConfigs, UnitCategory unitCategory, Map averages, Date beginDate, Date endDate) throws ServiceException {
		String usedColumnIds = "," + ListUtils.join(ensureColumnConfigs, "columnIds", ",", false) + ","; //已经被单独制定的栏目
		usedColumnIds = usedColumnIds.replaceAll(",0,", ",").replaceAll(",1,", ",").replaceAll(",2,", ",");
		usedColumnIds = usedColumnIds.equals(",") ? "" : usedColumnIds.substring(1, usedColumnIds.length()-1);
		com.yuanluesoft.cms.siteresource.report.model.ensurereport.UnitStat unitStat = new com.yuanluesoft.cms.siteresource.report.model.ensurereport.UnitStat();
		unitStat.setMonthStats(new ArrayList());
		unitStat.setUnitName(unitName);
		for(Date monthBegin = DateTimeUtils.set(beginDate, Calendar.DAY_OF_MONTH, 1); !monthBegin.after(endDate); monthBegin=DateTimeUtils.add(monthBegin, Calendar.MONTH, 1)) {
			Date monthEnd = DateTimeUtils.add(monthBegin, Calendar.MONTH, 1);
			MonthStat monthStat = new MonthStat(); //月统计
			monthStat.setIssueStat(issueTotal(siteId + "", siteId, usedColumnIds, unitName, monthBegin, monthEnd, false, false)); //总的发布数
			monthStat.setColumnStats(new ArrayList());
			unitStat.getMonthStats().add(monthStat);
			//计算分数
			double score = 0;
			for(Iterator iterator = ensureColumnConfigs.iterator(); iterator.hasNext();) {
				EnsureColumnConfig ensureColumnConfig = (EnsureColumnConfig)iterator.next();
				com.yuanluesoft.cms.siteresource.report.model.ensurereport.ColumnStat columnStat = new com.yuanluesoft.cms.siteresource.report.model.ensurereport.ColumnStat();
				columnStat.setEnsureColumnConfig(ensureColumnConfig);
				monthStat.getColumnStats().add(columnStat);
				if(ensureColumnConfig.getMode()==1) { //计分方式,0/按信息量,1/按维护栏目数
					//获取应维护栏目ID
					List ensureColumns = listEnsureColumns(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitName);
					if(ensureColumns!=null && !ensureColumns.isEmpty()) {
						//获取发布过信息的栏目
						int issueColumnTotal = issueColumnTotal(ListUtils.join(ensureColumns, ",", false), unitName, monthBegin, monthEnd);
						columnStat.setScore((issueColumnTotal+0.0) / ensureColumns.size() * ensureColumnConfig.getScore()); //单位本部分维护的栏目数÷本部分应保障栏目数量
						columnStat.setEnsureColumnNumber(ensureColumns.size()); //应保障栏目数
						columnStat.setIssueColumnNumber(issueColumnTotal); //已维护栏目数
						score += columnStat.getScore(); 
					}
				}
				else if(ensureColumnConfig.getCaptureScore()<=0 || ("," + unitCategory.getClusterUnitNames() + ",").indexOf("," + unitName + ",")!=-1) { //没有指定抓取分数,或者当前单位是集群单位
					double average = getAverage(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, (ensureColumnConfig.getCaptureScore()<=0 ? unitCategory.getUnitNames() : unitCategory.getClusterUnitNames()), averages, monthBegin, monthEnd, false, false);
					if(average>0) {
						int issueTotal = issueTotal(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitName, monthBegin, monthEnd, false, false);
						columnStat.setIssueStat(issueTotal); //发布统计,按信息量计分时有效
						columnStat.setAverage(average); //同类单位平均值,按信息量计分时有效
						columnStat.setScore(Math.min(ensureColumnConfig.getScore(), ((issueTotal+0.0)/average) * ensureColumnConfig.getScore()));
						score += columnStat.getScore();
					}
				}
				else { //指定抓取分数
					//计算手工报送
					double manualAverage = getAverage(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitCategory.getNotClusterUnitNames(), averages, monthBegin, monthEnd, true, false);
					double columnScore = 0;
					if(manualAverage>0) {
						int manualTotal = issueTotal(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitName, monthBegin, monthEnd, true, false);
						columnScore = ((manualTotal+0.0)/manualAverage) * (ensureColumnConfig.getScore()-ensureColumnConfig.getCaptureScore());
						columnStat.setIssueStat(manualTotal); //发布统计,按信息量计分时有效
						columnStat.setAverage(manualAverage); //同类单位平均值,按信息量计分时有效
					}
					//计算抓取
					double captureAverage = getAverage(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitCategory.getNotClusterUnitNames(), averages, monthBegin, monthEnd, false, true);
					if(captureAverage>0) {
						int captureTotal = issueTotal(ensureColumnConfig.getColumnIds(), siteId, usedColumnIds, unitName, monthBegin, monthEnd, false, true);
						columnScore += ((captureTotal+0.0)/captureAverage) * ensureColumnConfig.getCaptureScore();
						columnStat.setCaptureStat(captureTotal); //抓取统计,按信息量计分时有效
						columnStat.setCaptureAverage(captureAverage); //同类单位抓取平均值,按信息量计分时有效
					}
					columnStat.setScore(Math.min(ensureColumnConfig.getScore(), columnScore));
					score += columnStat.getScore();
				}
				if(Logger.isDebugEnabled()) {
					Logger.debug("EnsureReport: columnStat, UnitName:" + unitName +
								   ", ColumnNames:" + columnStat.getEnsureColumnConfig().getColumnNames() +
								   ", Mode:" + columnStat.getEnsureColumnConfig().getMode() +
								   ", EnsureColumnNumber:" + columnStat.getEnsureColumnNumber() +
								   ", IssueColumnNumber:" + columnStat.getIssueColumnNumber() +
								   ", IssueStat:" + columnStat.getIssueStat() +
								   ", Average:" + columnStat.getAverage() +
								   ", CaptureStat:" + columnStat.getCaptureStat() +
								   ", CaptureAverage:" + columnStat.getCaptureAverage() +
								   ", Score:" + columnStat.getScore());
				}
			}
			monthStat.setScore((int)score);
			unitStat.setIssueStat(unitStat.getIssueStat() + monthStat.getIssueStat()); //单位总的发布量
			unitStat.setScore(unitStat.getScore() + monthStat.getScore()); //单位总分
		}
		return unitStat;
	}
	
	/**
	 * 获取平均数
	 * @param columnIds
	 * @param unitNames
	 * @param averages
	 * @param beginDate
	 * @param endDate
	 * @param manualOnly
	 * @param captureOnly
	 * @return
	 * @throws ServiceException
	 */
	private double getAverage(String columnIds, long siteId, String usedColumnIds, String unitNames, Map averages, Date beginDate, Date endDate, boolean manualOnly, boolean captureOnly) throws ServiceException {
		if(unitNames==null) {
			return 0;
		}
		String key = columnIds + "_" + unitNames + "_" + DateTimeUtils.formatDate(beginDate, null) + "_" + manualOnly + "_" + captureOnly;
		Double average = (Double)averages.get(key);
		if(average!=null) {
			return average.doubleValue();
		}
		int total = issueTotal(columnIds, siteId, usedColumnIds, unitNames, beginDate, endDate, manualOnly, captureOnly);
		average = new Double((total+0.0)/unitNames.split(",").length);
		averages.put(key, average);
		return average.doubleValue();
	}
	
	/**
	 * 发布统计
	 * @param columnIds
	 * @param siteId
	 * @param usedColumnIds 已经使用的栏目ID,parentColumnIds=="1"时,需要剔除usedColumnIds
	 * @param unitNames
	 * @param beginDate
	 * @param endDate
	 * @param manualOnly
	 * @param captureOnly
	 * @return
	 * @throws ServiceException
	 */
	private int issueTotal(String columnIds, long siteId, String usedColumnIds, String unitNames, Date beginDate, Date endDate, boolean manualOnly, boolean captureOnly) throws ServiceException {
		if(unitNames==null || columnIds.isEmpty()) {
			return 0;
		}
		if(columnIds.equals("2")) { //0/全部栏目,1/其他应保障栏目,2/政府信息公开
			//信息公开统计
			String hql = "select count(PublicInfo.id)" +
						 " from PublicInfo PublicInfo" +
						 " where PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" +
						 " and PublicInfo.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
						 " and PublicInfo.issueTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
						 " and PublicInfo.unitName in ('" + JdbcUtils.resetQuot(unitNames).replaceAll(",", "','") + "')";
			Number count = (Number)getDatabaseService().findRecordByHql(hql);
			return count==null ? 0 : count.intValue();
		}
		if(columnIds.equals("0")) { //0/全部栏目,1/其他应保障栏目,2/政府信息公开
			columnIds = "" + siteId;
		}
		else if(columnIds.equals("1")) { //0/全部栏目,1/其他应保障栏目,2/政府信息公开
			columnIds = ListUtils.join(listEnsureColumns(columnIds, siteId, usedColumnIds, unitNames), ",", false); //获取单位需要保障的栏目ID列表
			if(columnIds==null || columnIds.isEmpty()) {
				return 0;
			}
		}
		String hql = "select count(distinct SiteResource.id)" +
					 " from SiteResource SiteResource left join SiteResource.subjections SiteResourceSubjection, WebDirectorySubjection WebDirectorySubjection" +
					 " where SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
					 " and SiteResource.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and SiteResource.issueTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
					 " and SiteResourceSubjection.siteId=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(columnIds) + ")" +
					 " and SiteResource.unitName in ('" + JdbcUtils.resetQuot(unitNames).replaceAll(",", "','") + "')" +
					 (manualOnly ? " and (SiteResource.sourceRecordUrl is null or not SiteResource.sourceRecordUrl like '%://%')" : "") + //仅手工录入的信息
					 (captureOnly ? " and SiteResource.sourceRecordUrl like '%://%'" : ""); //仅抓取的信息
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : count.intValue();
	}
	
	/**
	 * 获取应维护的栏目ID列表
	 * @param columnIds
	 * @param siteId
	 * @param usedColumnIds 已经使用的栏目ID,columnIds=="1"时,需要剔除usedColumnIds
	 * @param unitNames
	 * @return
	 * @throws ServiceException
	 */
	private List listEnsureColumns(String columnIds, long siteId, String usedColumnIds, String unitNames) throws ServiceException {
		String hql = "select distinct WebDirectory.id" +
					 " from WebDirectory WebDirectory" +
					 " left join WebDirectory.subjections WebDirectorySubjection" +
					 " left join WebDirectory.directoryPopedoms WebDirectoryPopedom" +
					 " where WebDirectoryPopedom.userName in ('" + JdbcUtils.resetQuot(unitNames).replaceAll(",", "','") + "')" + //用户是栏目的管理员或者编辑
					 " and WebDirectoryPopedom.isInherit='0'" + //不是从上级目录继承
					 " and WebDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers((columnIds.equals("0") || columnIds.equals("1") ? "" + siteId : columnIds)) + ")";
		if(columnIds.equals("1") && usedColumnIds!=null && !usedColumnIds.isEmpty()) { //1/其他应保障栏目
			//剔除usedColumnIds
			hql += " and (select min(Subjection.id)" +
				   " 	   from WebDirectorySubjection Subjection" +
				   "	   where Subjection.directoryId=WebDirectory.id" +
				   "	   and Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(usedColumnIds) + ")) is null";
		}
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 统计在columnIds发布过信息的栏目数
	 * @param columnIds
	 * @param unitName
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	private int issueColumnTotal(String columnIds, String unitName, Date beginDate, Date endDate) throws ServiceException {
		if(columnIds==null || columnIds.isEmpty()) {
			return 0;
		}
		String hql = "select count(distinct WebDirectorySubjection.parentDirectoryId)" +
					 " from SiteResource SiteResource" +
					 " left join SiteResource.subjections SiteResourceSubjection, WebDirectorySubjection WebDirectorySubjection" +
					 " where SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
					 " and SiteResource.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and SiteResource.issueTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")" +
					 " and SiteResourceSubjection.siteId=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(columnIds) + ")" +
					 " and SiteResource.unitName='" + JdbcUtils.resetQuot(unitName) + "'";
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : count.intValue();
	}
	
	/**
	 * 检查是否集群单位,如果系统中有名称和单位名称匹配、且设置了域名的站点,则认为是集群单位
	 * @param ensureUnitCategories
	 * @return
	 * @throws ServiceException
	 */
	private void retrieveClusterUnitNames(UnitCategory unitCategory) throws ServiceException {
		String[] unitNames = unitCategory.getUnitNames().split(",");
		for(int i=0; i<unitNames.length; i++) {
			if(unitNames[i].indexOf("办公室")!=-1 || unitNames[i].indexOf("办公厅")!=-1 || unitNames[i].indexOf("府办")!=-1) {
				unitCategory.setClusterUnitNames((unitCategory.getClusterUnitNames()==null ? "" : unitCategory.getClusterUnitNames() + ",") + unitNames[i]);
				continue;
			}
			String hql = "select WebSite.id" +
						 " from WebSite WebSite" +
						 " where not WebSite.hostName is null" +
						 " and (WebSite.directoryName like '%" + JdbcUtils.resetQuot(unitNames[i]) + "%'" +
						 " or ' + unitNames[i] + ' like concat(concat('%', WebSite.directoryName), '%'))";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				unitCategory.setClusterUnitNames((unitCategory.getClusterUnitNames()==null ? "" : unitCategory.getClusterUnitNames() + ",") + unitNames[i]);
			}
			else {
				unitCategory.setNotClusterUnitNames((unitCategory.getNotClusterUnitNames()==null ? "" : unitCategory.getNotClusterUnitNames() + ",") + unitNames[i]);
			}
		}
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
}