package com.yuanluesoft.chd.evaluation.data.service.spring;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationData;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationDataSubjection;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationIndicatorData;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationPrerequisitesData;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationTodo;
import com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectoryPopedom;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlant;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantDetail;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantRule;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisites;
import com.yuanluesoft.chd.evaluation.selfeval.service.EvaluationSelfEvalService;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationDataServiceImpl extends BusinessServiceImpl implements EvaluationDataService {
	private EvaluationDirectoryService evaluationDirectoryService; //体系目录服务
	private RecordControlService recordControlService; //记录权限控制服务
	private EvaluationSelfEvalService evaluationSelfEvalService; //自查服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationFileService#updateDataSubjections(com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationData, boolean, java.lang.String)
	 */
	public void updateDataSubjections(ChdEvaluationData data, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		if(!isNew) {
			//检查隶属目录是否发生变化
			if(subjectionDirectoryIds.equals(ListUtils.join(data.getSubjections(), "directoryId", ",", false))) {
				return;
			}
			//删除旧的隶属关系
			for(Iterator iterator = data.getSubjections().iterator(); iterator.hasNext();) {
				ChdEvaluationDataSubjection subjection = (ChdEvaluationDataSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
			}
		}
		//保存新的隶属关系
		String[] ids = subjectionDirectoryIds.split(",");
		data.setSubjections(new LinkedHashSet());
		for(int i=0; i<ids.length; i++) {
			if(ListUtils.findObjectByProperty(data.getSubjections(), "directoryId", new Long(ids[i]))!=null) { //重复
				continue;
			}
			ChdEvaluationDataSubjection subjection = new ChdEvaluationDataSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setDataId(data.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			data.getSubjections().add(subjection);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#listData(long, int, int, int)
	 */
	public List listData(long directoryId, int year, int month, int max) throws ServiceException {
		Date date = null;
		try {
			date = DateTimeUtils.parseDate(year + "-" + month + "-1", null);
		}
		catch (ParseException e) {
			
		}
		String hqlFrom = "from ChdEvaluationData ChdEvaluationData left join ChdEvaluationData.subjections subjections";
		String hqlWhere = "ChdEvaluationData.created>=DATE(" + DateTimeUtils.formatDate(date, null) + ")" +
						  " and ChdEvaluationData.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(date, Calendar.MONTH, 1), null) + ")" +
						  " and subjections.directoryId=" + directoryId;
		String hqlOrderBy = "ChdEvaluationData.created";
		return getDatabaseService().findRecordsByHqlClause(ChdEvaluationData.class.getName(), null, hqlFrom, hqlWhere, hqlOrderBy, null, null, 0, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#loadPrerequisitesDataList(long, int)
	 */
	public List loadPrerequisitesDataList(long plantId, int year) throws ServiceException {
		//查找已提交的数据
		String hql = "from ChdEvaluationPrerequisitesData ChdEvaluationPrerequisitesData" +
					 " where ChdEvaluationPrerequisitesData.plantId=" + plantId +
					 " and ChdEvaluationPrerequisitesData.declareYear=" + year +
					 " order by ChdEvaluationPrerequisitesData.id";
		List dataList = getDatabaseService().findRecordsByHql(hql);
		if(dataList!=null && !dataList.isEmpty()) {
			return dataList;
		}
		//未提交,构造列表
		ChdEvaluationPlant plant = (ChdEvaluationPlant)evaluationDirectoryService.getDirectory(plantId); //获取企业
		//获取对应的发电企业类型
		hql = "from ChdEvaluationPrerequisites ChdEvaluationPrerequisites" +
			  " where ChdEvaluationPrerequisites.plantTypeId=(" +
			  "  select ChdEvaluationPlantType.id" +
			  "   from ChdEvaluationPlantType ChdEvaluationPlantType" +
			  "   where ChdEvaluationPlantType.directoryName='" + JdbcUtils.resetQuot(plant.getType()) + "'" +
			  "   and ChdEvaluationPlantType.parentDirectoryId=" + plant.getParentDirectoryId() +
			  " )" +
			  " order by ChdEvaluationPrerequisites.priority DESC, ChdEvaluationPrerequisites.created";
		dataList = getDatabaseService().findRecordsByHql(hql);
		if(dataList==null || dataList.isEmpty()) {
			return null;
		}
		for(int i=0; i<dataList.size(); i++) {
			ChdEvaluationPrerequisites prerequisites = (ChdEvaluationPrerequisites)dataList.get(i);
			ChdEvaluationPrerequisitesData data = new ChdEvaluationPrerequisitesData();
			data.setPrerequisitesId(prerequisites.getId());
			data.setPrerequisites(prerequisites.getPrerequisites());
			dataList.set(i, data);
		}
		return dataList;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#savePrerequisitesDataList(long, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void savePrerequisitesDataList(long plantId, int year, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List dataList = loadPrerequisitesDataList(plantId, year);
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		for(int i=0; i<dataList.size(); i++) {
			ChdEvaluationPrerequisitesData data = (ChdEvaluationPrerequisitesData)dataList.get(i);
			data.setResult(request.getParameter("result_" + data.getPrerequisitesId()));
			data.setRemark(request.getParameter("remark_" + data.getPrerequisitesId()));
			data.setCreated(DateTimeUtils.now()); //提交时间
			data.setCreatorId(sessionInfo.getUserId()); //提交人ID
			data.setCreator(sessionInfo.getUserName()); //提交人姓名
			if(data.getId()==0) { //新记录
				data.setId(UUIDLongGenerator.generateId());
				data.setPlantId(plantId); //发电企业ID
				data.setDeclareYear(year); //年度
				getDatabaseService().saveRecord(data);
			}
			else {
				getDatabaseService().updateRecord(data);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#loadIndicatorDataList(long, int, int, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List loadIndicatorDataList(long directoryId, int year, int month, boolean toEdit, SessionInfo sessionInfo) throws ServiceException {
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(directoryId);
		if("company".equals(evaluationDirectory.getDirectoryType())) { //公司不处理,避免数据量过大
			return null;
		}
		List indicatorList = new ArrayList();
		if("plantRule".equals(evaluationDirectory.getDirectoryType()) && //企业评价项目
		   (!evaluationDirectoryService.hasChildDirectories(directoryId)) && //已经是最后一级
		   ((ChdEvaluationPlantRule)evaluationDirectory).getIsIndicator()=='1') { //指标评价
			indicatorList.add(evaluationDirectory);
		}
		else {
			loadIndicatorList(indicatorList, directoryId, toEdit, sessionInfo);
		}
		year = year==0 ? DateTimeUtils.getYear(DateTimeUtils.date()) : year;
		month = month==0 ? DateTimeUtils.getMonth(DateTimeUtils.date()) + 1 : month;
		for(int i=0; i<indicatorList.size(); i++) {
			ChdEvaluationPlantRule indicator = (ChdEvaluationPlantRule)indicatorList.get(i);
			//获取已提交的数据
			String hql = "from ChdEvaluationIndicatorData ChdEvaluationIndicatorData" +
						 " where ChdEvaluationIndicatorData.indicatorId=" + indicator.getId() +
						 " and ChdEvaluationIndicatorData.dataYear=" + year +
						 " and ChdEvaluationIndicatorData.dataMonth" + (toEdit ? "=" : "<=") + month +
						 " order by ChdEvaluationIndicatorData.dataMonth DESC";
			ChdEvaluationIndicatorData indicatorData = (ChdEvaluationIndicatorData)getDatabaseService().findRecordByHql(hql);
			if(indicatorData==null) {
				indicatorData = new ChdEvaluationIndicatorData();
				indicatorData.setIndicatorId(indicator.getId()); //指标ID
				indicatorData.setIndicator(indicator.getDirectoryName()); //指标名称
			}
			indicatorList.set(i, indicatorData);
		}
		return indicatorList;
	}
	
	/**
	 * 递归:加载指标列表
	 * @param indicatorDataList
	 * @param directoryId
	 * @param toEdit
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void loadIndicatorList(List indicatorList, long directoryId, boolean toEdit, SessionInfo sessionInfo) throws ServiceException {
		List childDirectories = evaluationDirectoryService.listChildDirectories(directoryId, null, (toEdit ? "manager,leader,transactor" : "all"), null, false, false, sessionInfo, 0, 0);
		if(childDirectories==null || childDirectories.isEmpty()) {
			return;
		}
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)iterator.next();
			if(!"plantRule".equals(evaluationDirectory.getDirectoryType())) { //不是企业评价项目
				loadIndicatorList(indicatorList, evaluationDirectory.getId(), toEdit, sessionInfo); //递归
			}
			else if(((ChdEvaluationPlantRule)evaluationDirectory).getIsIndicator()!='1') { //不是指标评价
				continue;
			}
			else if(!evaluationDirectoryService.hasChildDirectories(evaluationDirectory.getId())) { //最后一级
				indicatorList.add(evaluationDirectory);
			}
			else {
				loadIndicatorList(indicatorList, evaluationDirectory.getId(), toEdit, sessionInfo); //递归
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#saveIndicatorDataList(long, int, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void saveIndicatorDataList(long directoryId, int year, int month, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List dataList = loadIndicatorDataList(directoryId, year, month, true, sessionInfo);
		if(dataList==null || dataList.isEmpty()) {
			return;
		}
		for(int i=0; i<dataList.size(); i++) {
			ChdEvaluationIndicatorData data = (ChdEvaluationIndicatorData)dataList.get(i);
			data.setData(request.getParameter("data_" + data.getIndicatorId()));
			data.setRemark(request.getParameter("remark_" + data.getIndicatorId()));
			data.setCreatorId(sessionInfo.getUserId()); //提交人ID
			data.setCreator(sessionInfo.getUserName()); //提交人姓名
			if(data.getId()==0) { //新记录
				data.setId(UUIDLongGenerator.generateId());
				data.setDataYear(year); //年度
				data.setDataMonth(month); //月份
				getDatabaseService().saveRecord(data);
			}
			else {
				getDatabaseService().updateRecord(data);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#createTodo()
	 */
	public void createTodo() throws ServiceException {
		Date today = DateTimeUtils.date();
		int year = DateTimeUtils.getYear(today);
		int month = DateTimeUtils.getMonth(today) + 1;
		String hql = "from ChdEvaluationPlantDetail ChdEvaluationPlantDetail" +
					 " where ChdEvaluationPlantDetail.dataCycle='{dataCycle}'" +
					 " and (select max(ChdEvaluationTodo.id)" + //没有创建过待办事宜
					 "		 from ChdEvaluationTodo ChdEvaluationTodo" +
					 "		 where ChdEvaluationTodo.directoryId=ChdEvaluationPlantDetail.id" +
					 "		 and ChdEvaluationTodo.taskType='0'" +
					 "		 and ChdEvaluationTodo.created>=DATE({beginDate})" +
					 "		 and ChdEvaluationTodo.created<DATE({endDate})) is null" +
					 " and (select max(ChdEvaluationData.id)" + //没有提交过资料
					 "		 from ChdEvaluationData ChdEvaluationData left join ChdEvaluationData.subjections ChdEvaluationDataSubjection" +
					 "		 where ChdEvaluationDataSubjection.directoryId=ChdEvaluationPlantDetail.id" +
					 "		 and ChdEvaluationData.created>=DATE({beginDate})" +
					 "		 and ChdEvaluationData.created<DATE({endDate})) is null" +
					 " and (select max(ChdEvaluationSelf.id)" + //没有提交过自查
					 "		 from ChdEvaluationSelf ChdEvaluationSelf left join ChdEvaluationSelf.subjections ChdEvaluationSelfSubjection" +
					 "		 where ChdEvaluationSelfSubjection.directoryId=ChdEvaluationPlantDetail.id" +
					 "		 and ChdEvaluationSelf.evalYear=" + year +
					 "		 and ChdEvaluationSelf.evalMonth=" + month + ") is null" +
					 " order by ChdEvaluationPlantDetail.id";
		for(char dataCycle='1'; dataCycle<='3'; dataCycle++) { //资料提交频率,不定期|0\0按周|1\0按月|2\0按年|3
			Date beginDate = null;
			Date endDate = null;
			if(dataCycle=='1') { //按周
				int weekDay = DateTimeUtils.getWeek(today);
				beginDate = DateTimeUtils.add(today, Calendar.DAY_OF_MONTH, -(weekDay==1 ? 6 : weekDay-2));
				endDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, 7);
			}
			else if(dataCycle=='2') { //按月
				beginDate = DateTimeUtils.set(today, Calendar.DAY_OF_MONTH, 1);
				endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
			}
			else if(dataCycle=='3') { //按年
				beginDate = DateTimeUtils.set(DateTimeUtils.set(today, Calendar.MONTH, 0), Calendar.DAY_OF_MONTH, 1);
				endDate = DateTimeUtils.add(beginDate, Calendar.YEAR, 1);
			}
			String newHql = hql.replaceFirst("\\{dataCycle\\}", "" + dataCycle)
							   .replaceAll("\\{beginDate\\}", DateTimeUtils.formatDate(beginDate, null))
							   .replaceAll("\\{endDate\\}", DateTimeUtils.formatDate(endDate, null));
			if(dataCycle=='3') { //按年
				newHql = newHql.replaceFirst("and ChdEvaluationSelf.evalMonth=" + month, "");
			}
			for(int i=0; ; i+=100) {
				List plantDetails = getDatabaseService().findRecordsByHql(newHql, ListUtils.generateList("directoryPopedoms"), i, 100);
				if(plantDetails==null || plantDetails.isEmpty()) {
					break;
				}
				for(Iterator iterator = plantDetails.iterator(); iterator.hasNext();) {
					ChdEvaluationPlantDetail plantDetail = (ChdEvaluationPlantDetail)iterator.next();
					createDataUrgency(plantDetail, beginDate, endDate); //创建资料上报催办
				}
				if(plantDetails.size()<100) {
					break;
				}
			}
		}
		
		Date beginDate = DateTimeUtils.set(today, Calendar.DAY_OF_MONTH, 1);
		Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		//自查
		hql = "from ChdEvaluationPlantDetail ChdEvaluationPlantDetail" +
			  " where (select max(ChdEvaluationTodo.id)" + //没有创建过待办事宜
			  "		 from ChdEvaluationTodo ChdEvaluationTodo" +
			  "		 where ChdEvaluationTodo.directoryId=ChdEvaluationPlantDetail.id" +
			  "		 and ChdEvaluationTodo.taskType='1'" +
			  "		 and ChdEvaluationTodo.created>=DATE({beginDate})" +
			  "		 and ChdEvaluationTodo.created<DATE({endDate})) is null" +
			  " and (ChdEvaluationPlantDetail.dataCycle='1'" + //按周
			  "		 or ChdEvaluationPlantDetail.dataCycle='2'" + //按月
			  "		 or (select max(ChdEvaluationData.id)" + //本月或者上月有提交过资料
			  "		 from ChdEvaluationData ChdEvaluationData left join ChdEvaluationData.subjections ChdEvaluationDataSubjection" +
			  "		 where ChdEvaluationDataSubjection.directoryId=ChdEvaluationPlantDetail.id" +
			  "		 and ChdEvaluationData.created>=DATE(" + DateTimeUtils.formatDate(evaluationSelfEvalService.isSelfEvalCurrentMonth() ? beginDate : DateTimeUtils.add(beginDate, Calendar.MONTH, -1), null) + ")" +
			  "		 and ChdEvaluationData.created<DATE(" + DateTimeUtils.formatDate(evaluationSelfEvalService.isSelfEvalCurrentMonth() ? endDate : beginDate, null) + ")) is not null)" +
			  " and (select max(ChdEvaluationSelf.id)" + //没有提交过自查
			  "		 from ChdEvaluationSelf ChdEvaluationSelf left join ChdEvaluationSelf.subjections ChdEvaluationSelfSubjection" +
			  "		 where ChdEvaluationSelfSubjection.directoryId=ChdEvaluationPlantDetail.id" +
			  "		 and ChdEvaluationSelf.evalYear=" + year +
			  "		 and ChdEvaluationSelf.evalMonth=" + (evaluationSelfEvalService.isSelfEvalCurrentMonth() ? month : month-1) + ") is null" +
			  " order by ChdEvaluationPlantDetail.parentDirectoryId";
		hql = hql.replaceAll("\\{beginDate\\}", DateTimeUtils.formatDate(beginDate, null))
			     .replaceAll("\\{endDate\\}", DateTimeUtils.formatDate(endDate, null));
		long currentParentDirectoryId = -1;
		String selfEvalEnd = null;
		String selfEvalUrgency = null;
		for(int i=0; ; i+=100) {
			List plantDetails = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("directoryPopedoms"), i, 100);
			if(plantDetails==null || plantDetails.isEmpty()) {
				break;
			}
			for(Iterator iterator = plantDetails.iterator(); iterator.hasNext();) {
				ChdEvaluationPlantDetail plantDetail = (ChdEvaluationPlantDetail)iterator.next();
				if(plantDetail.getParentDirectoryId()!=currentParentDirectoryId) {
					currentParentDirectoryId = plantDetail.getParentDirectoryId();
					String selfEvalHql = "select ChdEvaluationDirectory.selfEvalEnd, ChdEvaluationDirectory.selfEvalUrgency" +
										 " from ChdEvaluationDirectory ChdEvaluationDirectory left join ChdEvaluationDirectory.childSubjections ChdEvaluationDirectorySubjection" +
								  		 " where ChdEvaluationDirectorySubjection.directoryId=" + currentParentDirectoryId +
								  		 " and not ChdEvaluationDirectory.selfEvalEnd is null" +
								  		 " order by ChdEvaluationDirectorySubjection.id";
					Object[] values = (Object[])getDatabaseService().findRecordByHql(selfEvalHql);
					selfEvalEnd = values==null ? null : (String)values[0];
					selfEvalUrgency = values==null ? null : (String)values[1];
				}
				createSelfEvalUrgency(plantDetail, beginDate, endDate, selfEvalEnd, selfEvalUrgency); //创建资料上报催办
			}
			if(plantDetails.size()<100) {
				break;
			}
		}
	}
	
	/**
	 * 创建资料上报催办
	 * @param plantDetail
	 * @throws ServiceException
	 */
	private void createDataUrgency(ChdEvaluationPlantDetail plantDetail, Date beginDate, Date endDate) throws ServiceException {
		char dataCycle = plantDetail.getDataCycle(); //资料提交频率,不定期|0\0按周|1\0按月|2\0按年|3
		Date urgencyDate = null;
		String timeRange = null;
		if(dataCycle=='1') { //按周
			int dataCycleEnd = Math.min(StringUtils.parseInt(plantDetail.getDataCycleEnd(), 5), 7);
			int dataUrgency = dataCycleEnd - StringUtils.parseInt(plantDetail.getDataUrgency(), 0);
			urgencyDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, Math.min(dataCycleEnd - 1, Math.max(0, dataUrgency - 1)));
			timeRange = DateTimeUtils.getYear(beginDate) + "年" + DateTimeUtils.getWeekOfYear(beginDate) + "周";
		}
		else if(dataCycle=='2') { //按月
			int dataCycleEnd = Math.min(StringUtils.parseInt(plantDetail.getDataCycleEnd(), 31), DateTimeUtils.getDay(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, -1)));
			int dataUrgency = dataCycleEnd - StringUtils.parseInt(plantDetail.getDataUrgency(), 0);
			urgencyDate = DateTimeUtils.set(beginDate, Calendar.DAY_OF_MONTH, Math.min(dataCycleEnd, Math.max(1, dataUrgency)));
			timeRange = DateTimeUtils.getYear(beginDate) + "年" + DateTimeUtils.getMonth(beginDate) + "月";
		}
		else if(dataCycle=='3') { //按年
			try {
				urgencyDate = DateTimeUtils.parseDate(DateTimeUtils.getYear(beginDate) + "-" + plantDetail.getDataCycleEnd(), null);
				int dataUrgency = Math.max(0, StringUtils.parseInt(plantDetail.getDataUrgency(), 0));
				urgencyDate = DateTimeUtils.add(urgencyDate, Calendar.DAY_OF_MONTH, -dataUrgency);
			}
			catch (ParseException e) {
	
			}
			timeRange = DateTimeUtils.getYear(beginDate) + "年";
		}
		if(urgencyDate==null || urgencyDate.before(beginDate)) {
			urgencyDate = beginDate;
		}
		//System.out.println("****************createDataUrgency:" + plantDetail.getDataCycle() + "," + plantDetail.getDataCycleEnd() + "," + plantDetail.getDataUrgency() + "," + urgencyDate);
		if(urgencyDate.after(DateTimeUtils.date())) { //时间还未到
			return;
		}
		//创建待办事宜
		doCreateTodo(plantDetail, timeRange, true);
	}
	
	/**
	 * 创建自查催办
	 * @param plantDetail
	 * @throws ServiceException
	 */
	private void createSelfEvalUrgency(ChdEvaluationPlantDetail plantDetail, Date beginDate, Date endDate, String selfEvalEnd, String selfEvalUrgency) throws ServiceException {
		int endDay = Math.min(StringUtils.parseInt(selfEvalEnd, 31), DateTimeUtils.getDay(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, -1)));
		int urgencyDay = endDay - StringUtils.parseInt(selfEvalUrgency, 0);
		Date urgencyDate = DateTimeUtils.set(beginDate, Calendar.DAY_OF_MONTH, Math.min(endDay, Math.max(1, urgencyDay)));
		String timeRange = DateTimeUtils.getYear(beginDate) + "年" + DateTimeUtils.getMonth(beginDate) + "月";
		//System.out.println("****************createSelfEvalUrgency:" + selfEvalEnd + "," + selfEvalUrgency + "," + urgencyDate);
		if(urgencyDate.after(DateTimeUtils.date())) { //时间还未到
			return;
		}
		//创建待办事宜
		doCreateTodo(plantDetail, timeRange, false);

	}
	
	/**
	 * 创建待办事宜
	 * @param plantDetail
	 * @param timeRange
	 * @param dataUrgency
	 * @throws ServiceException
	 */
	private void doCreateTodo(ChdEvaluationPlantDetail plantDetail, String timeRange, boolean dataUrgency) throws ServiceException {
		ChdEvaluationTodo todo = new ChdEvaluationTodo();
		todo.setId(UUIDLongGenerator.generateId()); //ID
		todo.setTask(timeRange + (dataUrgency ? "资料提交" : "自查") + "(" + StringUtils.slice(plantDetail.getDirectoryName(), 180, "...") + "）"); //事项名称
		todo.setDirectoryId(plantDetail.getId()); //目录ID
		todo.setDirectorName(StringUtils.slice(plantDetail.getDirectoryName(), 180, "...")); //目录名称
		todo.setTaskType(dataUrgency ? '0' : '1'); //事项类型, 资料提交|0,自查|1
		todo.setCreated(DateTimeUtils.now()); //创建时间
		todo.setActionName(dataUrgency ? "提交资料" : "自查"); //操作名称
		if(dataUrgency) {
			todo.setActionLink(Environment.getContextPath() + "/chd/evaluation/data/data.shtml?act=create&directoryId=" + plantDetail.getId()); //操作链接
		}
		else {
			Date today = DateTimeUtils.date();
			int year = DateTimeUtils.getYear(today);
			int month = DateTimeUtils.getMonth(today) + 1;
			todo.setActionLink(Environment.getContextPath() + "/chd/evaluation/selfeval/selfEval.shtml?act=create&directoryId=" + plantDetail.getId() + "&evalYear=" + year + "&evalMonth=" + (evaluationSelfEvalService.isSelfEvalCurrentMonth() ? month : month-1)); //操作链接
		}
		todo.setActionLink(todo.getActionLink() + "&todoId={PARAMETER:id}");
		//private Set visitors; //访问者列表
		getDatabaseService().saveRecord(todo);
		for(Iterator iterator = plantDetail.getDirectoryPopedoms().iterator(); iterator.hasNext();) {
			ChdEvaluationDirectoryPopedom popedom = (ChdEvaluationDirectoryPopedom)iterator.next();
			if("transactor".equals(popedom.getPopedomName()) || "leader".equals(popedom.getPopedomName())) {
				recordControlService.appendVisitor(todo.getId(), ChdEvaluationTodo.class.getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#clearDataTodo(long)
	 */
	public void clearDataTodo(long directoryId) throws ServiceException {
		String hql = "from ChdEvaluationTodo ChdEvaluationTodo" +
					 " where ChdEvaluationTodo.directoryId=" + directoryId +
					 " and ChdEvaluationTodo.taskType='0'";
		getDatabaseService().deleteRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#clearSelfEvalTodo(long, int, int)
	 */
	public void clearSelfEvalTodo(long directoryId, int evalYear, int evalMonth) throws ServiceException {
		Date beginDate = null;
		try {
			beginDate = DateTimeUtils.parseDate(evalYear + "-" + evalMonth + "-1", null);
		}
		catch (ParseException e) {
			
		}
		Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		//删除自查事宜
		String hql = "from ChdEvaluationTodo ChdEvaluationTodo" +
					 " where ChdEvaluationTodo.directoryId=" + directoryId +
					 " and ChdEvaluationTodo.taskType='1'" +
					 " and ChdEvaluationTodo.created>=DATE(" + (DateTimeUtils.formatDate(evaluationSelfEvalService.isSelfEvalCurrentMonth() ? beginDate : endDate, null)) + ")" +
					 " and ChdEvaluationTodo.created<DATE(" + (DateTimeUtils.formatDate(evaluationSelfEvalService.isSelfEvalCurrentMonth() ? endDate : DateTimeUtils.add(endDate, Calendar.MONTH, 1), null)) + ")";
		getDatabaseService().deleteRecordsByHql(hql);
		
		//删除资料上传事宜
		ChdEvaluationPlantDetail plantDetail = (ChdEvaluationPlantDetail)evaluationDirectoryService.getDirectory(directoryId);
		if(plantDetail.getDataCycle()=='3') { //按年时,删除全年的待办事宜
			try {
				beginDate = DateTimeUtils.parseDate(evalYear + "-1-1", null);
			}
			catch (ParseException e) {
				
			}
			endDate = DateTimeUtils.add(beginDate, Calendar.YEAR, 1);
		}
		hql = "from ChdEvaluationTodo ChdEvaluationTodo" +
			  " where ChdEvaluationTodo.directoryId=" + directoryId +
			  " and ChdEvaluationTodo.taskType='0'" +
			  " and ChdEvaluationTodo.created>=DATE(" + (DateTimeUtils.formatDate(beginDate, null)) + ")" +
			  " and ChdEvaluationTodo.created<DATE(" + (DateTimeUtils.formatDate(endDate, null)) + ")";
		getDatabaseService().deleteRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService#clearTodo(long, boolean, boolean)
	 */
	public void clearTodo(long directoryId, boolean dataUrgency, boolean selfEvalUrgency) throws ServiceException {
		
	}

	/**
	 * @return the evaluationDirectoryService
	 */
	public EvaluationDirectoryService getEvaluationDirectoryService() {
		return evaluationDirectoryService;
	}

	/**
	 * @param evaluationDirectoryService the evaluationDirectoryService to set
	 */
	public void setEvaluationDirectoryService(
			EvaluationDirectoryService evaluationDirectoryService) {
		this.evaluationDirectoryService = evaluationDirectoryService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the evaluationSelfEvalService
	 */
	public EvaluationSelfEvalService getEvaluationSelfEvalService() {
		return evaluationSelfEvalService;
	}

	/**
	 * @param evaluationSelfEvalService the evaluationSelfEvalService to set
	 */
	public void setEvaluationSelfEvalService(
			EvaluationSelfEvalService evaluationSelfEvalService) {
		this.evaluationSelfEvalService = evaluationSelfEvalService;
	}
}