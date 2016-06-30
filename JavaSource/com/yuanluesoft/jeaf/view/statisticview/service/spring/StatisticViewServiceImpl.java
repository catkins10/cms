/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.view.statisticview.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.jeaf.view.statisticview.model.Column;
import com.yuanluesoft.jeaf.view.statisticview.model.GroupField;
import com.yuanluesoft.jeaf.view.statisticview.model.Statistic;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticColumn;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;

/**
 * 
 * @author linchuan
 *
 */
public class StatisticViewServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		generateHqlGroupBy((StatisticView)view);
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}
	
	/**
	 * 设置hqlGroupBy
	 * @param statisticView
	 */
	private void generateHqlGroupBy(StatisticView statisticView) {
		if(statisticView.isHideDetail()) { //隐藏明细
			String pojoClassName = statisticView.getPojoClassName().substring(statisticView.getPojoClassName().lastIndexOf(".") + 1);
			String hqlGroupBy = "";
			Statistic statistic = (Statistic)statisticView.getStatistics().get(0);
			for(Iterator iterator=statistic.getGroupFields().iterator(); iterator.hasNext();) { //分组字段比较
				GroupField groupField = (GroupField)iterator.next();
				hqlGroupBy += (hqlGroupBy.equals("") ? "" : ",") + (groupField.getField()==null ? (groupField.getName().indexOf('.')==-1 ? pojoClassName + "." : "") + groupField.getName() : groupField.getField());
			}
			statisticView.setGroupBy(hqlGroupBy);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		StatisticView statisticView = (StatisticView)view;
		generateHqlGroupBy(statisticView);
		String hqlWhere = generateHqlWhere(view, currentCategories, searchConditionList, request, sessionInfo);
		//设置需要延迟加载的对象
		List lazyLoadProperties = listLazyLoadProperties(view);
		//设置hqlSelect
		Statistic statistic = null;
		String hqlSelect = null;
		List simpleColumns = new ArrayList();
		String pojoClassName = statisticView.getPojoClassName().substring(statisticView.getPojoClassName().lastIndexOf(".") + 1);
		if(statisticView.isHideDetail()) { //隐藏明细
			statistic = (Statistic)statisticView.getStatistics().get(0);
			for(Iterator iterator = statistic.getStatisticColumns().iterator(); iterator.hasNext();) {
				StatisticColumn statisticColumn = (StatisticColumn)iterator.next();
				Column column = (Column)ListUtils.findObjectByProperty(statisticView.getColumns(), "name", statisticColumn.getName());
				if(column==null) {
					continue;
				}
				if(column.getGroupValues()==null  && statisticColumn.getWhereExtend()==null) {
					simpleColumns.add(column);
					hqlSelect = (hqlSelect==null ? "" : hqlSelect + ",") + statisticColumn.getFunction();
				}
			}
			if(hqlSelect!=null) {
				hqlSelect = "max(" + pojoClassName + ".id)," + hqlSelect;
			}
		}
		String hqlJoin = generateHqlJoin(view, view.getJoin(), hqlSelect, hqlWhere, view.getOrderBy(), view.getGroupBy());
	
		//获取记录,为校验是否最后一页,多取一条记录
		List records = getDatabaseService().findRecordsByFilter(view.getPojoClassName(), hqlSelect, hqlJoin, hqlWhere, view.getGroupBy(), view.getOrderBy(), view.getFilter(), lazyLoadProperties, beginRow, view.getPageRows() + 1, sessionInfo);
		if(records==null ||records.isEmpty()) {
			return null;
		}
		
		//判断是否最后一页
		boolean lastPage = records.size()<=statisticView.getPageRows();
		if(!lastPage) { //不是最后一页,删除最后一条超额获取的记录
			records.remove(records.size() - 1);
		}
		
		//填充统计数据
		if(statisticView.isHideDetail()) {
			if(hqlSelect==null) {
				for(Iterator iterator = records.iterator(); iterator.hasNext();) {
					Record record = (Record)iterator.next();
					fillStatisticData(statisticView, statistic, record, hqlJoin, hqlWhere, request, sessionInfo, true);
					record.setExtendPropertyValue("statisticFieldNames", ListUtils.join(statistic.getGroupFields(), "name", ",", false)); //统计字段
				}
			}
			else {
				String ids = null;
				for(Iterator iterator = records.iterator(); iterator.hasNext();) {
					Object[] values = (Object[])iterator.next();
					ids = (ids==null ? "" : ids + ",") + values[0];
				}
				String hql = "from " + pojoClassName + " " + pojoClassName +
							 " where " + pojoClassName + ".id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")";
				List dataList = ListUtils.sortByProperty(getDatabaseService().findRecordsByHql(hql), "id", ids);
				for(int i=records.size() - 1; i>=0; i--) {
					Object[] values = (Object[])records.get(i);
					Record record = (Record)dataList.get(i);
					for(int j=0; j<simpleColumns.size(); j++) {
						fillColumn((Column)simpleColumns.get(j), record, values[j+1]);
					}
				}
				records = dataList;
				for(Iterator iterator = records.iterator(); iterator.hasNext();) {
					Record record = (Record)iterator.next();
					fillStatisticData(statisticView, statistic, record, hqlJoin, hqlWhere, request, sessionInfo, false);
					record.setExtendPropertyValue("statisticFieldNames", ListUtils.join(statistic.getGroupFields(), "name", ",", false)); //统计字段
				}
			}
		}
		
		//处理分组列
		List groupedColumnList = new ArrayList();
		for(Iterator iterator = statisticView.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			if(column.getGroupBy()==null) {
				continue;
			}
			//获取分组值列表
			List groupValues = getDatabaseService().findRecordsByFilter(view.getPojoClassName(), pojoClassName + "." + column.getGroupBy(), hqlJoin, hqlWhere, column.getGroupBy(), null, view.getFilter(), null, 0, 0, sessionInfo);
			if(groupValues!=null && !groupValues.isEmpty()) {
				column.setGroupValues(groupValues);
				statisticView.setHeadRowCount(2);
				groupedColumnList.add(column);
			}
		}
		if(statisticView.isHideDetail() || groupedColumnList.isEmpty()) {
			groupedColumnList = null;
		}
		
		//添加统计行
		int[] rowIndexes = initRowIndexes(statisticView, beginRow, hqlWhere, hqlJoin, (Record)records.get(0), request, sessionInfo); //初始化行号
		Record previousRecord = null;
		for(int i=0; i<records.size(); i++) {
			Record record = (Record)records.get(i);
			int insertAt = i;
			fillGroupedColumn(groupedColumnList, record); //根据记录所对应的分组填充数据到分组值列表
			//比较是否属于同一分组
			for(int j=(statisticView.isHideDetail() ? 1 : 0); j<statisticView.getStatistics().size(); j++) {
				statistic = (Statistic)statisticView.getStatistics().get(j);
				if(statistic.isStatisticAll()) { //总计,不检查
					continue;
				}
				boolean same = previousRecord!=null;
				for(Iterator iteratorField = statistic.getGroupFields().iterator(); same && iteratorField.hasNext();) { //分组字段比较
					GroupField groupField = (GroupField)iteratorField.next();
					try {
						if(!PropertyUtils.getProperty(previousRecord, groupField.getName()).equals(PropertyUtils.getProperty(record, groupField.getName()))) {
							same = false;
						}
					}
					catch (Exception e) {
						continue;
					}
				}
				if(!same && (previousRecord!=null || rowIndexes[j]==0)) { //记录属于不同分组,获取统计数据
					records.add(insertAt, createStatisticData(statisticView, statistic, rowIndexes[j+1]++, record, hqlJoin, hqlWhere, request, sessionInfo));
					i++;
					for(int k=j; k>=0; k--) { //重置行号
						rowIndexes[k] = 0;
					}
				}
			}
			String detailTitle = statisticView.isHideDetail() ? ((Statistic)statisticView.getStatistics().get(0)).getTitle() : statisticView.getDetailTitle();
			record.setExtendPropertyValue("statisticTitleFormat", detailTitle); //统计标题格式
			record.setExtendPropertyValue("rowIndex", new Integer(rowIndexes[statisticView.isHideDetail() ? 1 : 0]++)); //行号
			previousRecord = record;
		}
		
		//最后一页,添加总计
		if(lastPage) {
			for(int j=(statisticView.isHideDetail() ? 1 : 0); j<statisticView.getStatistics().size(); j++) {
				statistic = (Statistic)statisticView.getStatistics().get(j);
				if(statistic.isStatisticAll()) { //总计
					records.add(createStatisticData(statisticView, statistic, 0, previousRecord, hqlJoin, hqlWhere, request, sessionInfo));
				}
			}
		}
		generateStatisticTitle(records, request); //生成统计标题
		return records;
	}
	
	/**
	 * 生成统计标题
	 * @param records
	 * @param request
	 * @throws ServiceException
	 */
	protected void generateStatisticTitle(List records, HttpServletRequest request) throws ServiceException {
		for(int i=0; i<records.size(); i++) {
			final Record record = (Record)records.get(i);
			String statisticTitle = StringUtils.fillParameters((String)record.getExtendPropertyValue("statisticTitleFormat"), false, false, false, "utf-8", record, request, new FillParametersCallback() {
				public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
					if("rowIndex".equals(parameterName)) {
						Integer rowIndex = (Integer)record.getExtendPropertyValue("rowIndex");
						return rowIndex==null ? "" : "" + (rowIndex.intValue() + 1);
					}
					else if("chineseRowIndex".equals(parameterName)) {
						Integer rowIndex = (Integer)record.getExtendPropertyValue("rowIndex");
						return rowIndex==null ? "" : StringUtils.getChineseNumber(rowIndex.intValue() + 1, false);
					}
					return null;
				}
			});
			if(statisticTitle!=null && !statisticTitle.isEmpty()) {
				record.setExtendPropertyValue("statisticTitle", statisticTitle); //统计标题
			}
		}
	}
	
	/**
	 * 初始化行号
	 * @param statisticView
	 * @param beginRow
	 * @param hqlWhere
	 * @param hqlJoin
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private int[] initRowIndexes(StatisticView statisticView, int beginRow, String hqlWhere, String hqlJoin, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		int[] rowIndexes = new int[statisticView.getStatistics().size() + 1]; //行号
		if(beginRow==0) { //第一页
			return rowIndexes;
		}
		String pojoClassName = statisticView.getPojoClassName().substring(statisticView.getPojoClassName().lastIndexOf(".") + 1);
		//初始化行号
		for(int i=statisticView.getStatistics().size()-1; i>=0; i--) {
			Statistic statistic = (Statistic)statisticView.getStatistics().get(i);
			if(statistic.isStatisticAll()) { //总计,不检查
				continue;
			}
			String group = null;
			String orderBy = null;
			String where = null;
			for(Iterator iteratorField = statistic.getGroupFields().iterator(); iteratorField.hasNext();) { //分组字段比较
				GroupField groupField = (GroupField)iteratorField.next();
				String fieldName = groupField.getField()==null ? (groupField.getName().indexOf('.')==-1 ? pojoClassName + "." : "") + groupField.getName() : groupField.getField();
				group = (group==null ? "" : group + ",") + fieldName;
				orderBy = (orderBy==null ? "" : orderBy + ",") + fieldName + (statisticView.getOrderBy()!=null && statisticView.getOrderBy().indexOf(fieldName + " DESC")!=-1 ? " DESC" : "");
				String value = getFieldValue(record, groupField.getName(), true);
				String extend = "(" + fieldName + (value==null ? " is null" : "=" + value) + ")";
				if(hqlWhere==null || hqlWhere.indexOf(extend)==-1) {
					where = (where==null ? "" : where + " and ") + extend;
				}
			}
			List records = getDatabaseService().findRecordsByFilter(statisticView.getPojoClassName(), group, hqlJoin, hqlWhere, group, orderBy, statisticView.getFilter(), null, 0, 0, sessionInfo);
			hqlWhere = (hqlWhere==null ? "" : "(" + hqlWhere + ") and ") + where;
			//获取序号
			for(int j=0; j<records.size(); j++) {
				Object[] values = (Object[])records.get(j);
				int k = statistic.getGroupFields().size()-1;
				try {
					for(; k>=0 && BeanUtils.equals(values[k], PropertyUtils.getProperty(record, ((GroupField)statistic.getGroupFields().get(k)).getName())); k--);
				}
				catch (Exception e) {
				
				} 
				//分组字段比较
				if(k<0) {
					rowIndexes[i+1] = j;
					break;
				}
			}
		}
		if(!statisticView.isHideDetail()) { //不隐藏明细
			//获取记录序号
			List records = getDatabaseService().findRecordsByFilter(statisticView.getPojoClassName(), pojoClassName + ".id", hqlJoin, hqlWhere, null, statisticView.getOrderBy(), statisticView.getFilter(), null, 0, 0, sessionInfo);
			int j=0;
			for(; j<records.size(); j++) {
				Object value = records.get(j);
				long id = (value instanceof Number ? (Number)value : (Number)((Object[])value)[0]).longValue();
				if(id==record.getId()) {
					break;
				}
			}
			rowIndexes[0] = Math.max(0, j);
		}
		for(int i=0; i<rowIndexes.length-1; i++) {
			if(rowIndexes[i]>0) {
				rowIndexes[i+1]++; //后一级行号加1
			}
		}
		return rowIndexes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		generateHqlGroupBy((StatisticView)view);
		return super.retrieveRecordCount(view, currentCategories, searchConditionList, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#resetViewColumns(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View)
	 */
	public void resetViewColumns(View view, String viewMode, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		StatisticView statisticView = (StatisticView)view;
		statisticView.setHeadColCount(-1);
		int headColCount = 0;
		for(Iterator iterator = statisticView.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			String columnType = column.getType();
			if(Column.COLUMN_TYPE_ROWNUM.equals(columnType)) { //统计视图,不显示序号列
				List normalStatistics = ListUtils.getSubListByProperty(statisticView.getStatistics(), "statisticAll", Boolean.FALSE); //获取非总计的统计
				if(normalStatistics!=null && normalStatistics.size()>(statisticView.isHideDetail() ? 1 : 0)) {
					iterator.remove();
				}
				else {
					headColCount++;
				}
			}
			else if(statisticView.getHeadColCount()==-1) {
				if("statistic".equals(columnType)) { //统计列
					//statisticView.setHeadColCount(headColCount);
					break;
				}
				headColCount++;
			}
		}
		statisticView.setHeadColCount(headColCount);
		super.resetViewColumns(view, viewMode, request, sessionInfo);
	}
	
	/**
	 * 将记录中的值根据分组自动填充到分组列
	 * @param groupedColumnList
	 * @param record
	 * @throws ServiceException
	 */
	private void fillGroupedColumn(List groupedColumnList, Record record) throws ServiceException {
		if(groupedColumnList==null) {
			return;
		}
		List valueList = new ArrayList();
		Object[] valueArray = new Object[2];
		valueList.add(valueArray);
		for(Iterator iterator = groupedColumnList.iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			try {
				valueArray[0] = PropertyUtils.getProperty(record, column.getGroupBy());
				valueArray[1] = PropertyUtils.getProperty(record, column.getSourceField());
			}
			catch (Exception e) {
				continue;
			}
			fillGroupedColumn(column, record, valueList);
		}
	}
	
	/**
	 * 创建统计结果记录
	 * @param statisticView
	 * @param statistic
	 * @param rowIndex
	 * @param record
	 * @param hqlJoin
	 * @param hqlWhere
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private Object createStatisticData(StatisticView statisticView, Statistic statistic, final int rowIndex, Record record, String hqlJoin, String hqlWhere, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//克隆记录
		Record result = null;
		try {
			result = (Record)record.clone();
		}
		catch(Exception ce) {
		
		}
		if(result==null) { //不能克隆
			try { //创建新对象
				result = (Record)record.getClass().newInstance();
				PropertyUtils.copyProperties(result, record);
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		//清空统计时需要清空的字段
		for(Iterator iterator = statisticView.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			if(column.isEmptyWhenStatistic()) {
				try {
					PropertyUtils.setProperty(result, column.getName(), null);
				}
				catch(Exception e) {
					
				}
				result.setExtendPropertyValue(column.getName(), null); //扩展属性
			}
		}
		result.setExtendPropertyValue("statisticTitleFormat", statistic.getTitle()); //统计标题格式
		result.setExtendPropertyValue("statisticFieldNames", ListUtils.join(statistic.getGroupFields(), "name", ",", false)); //统计字段
		result.setExtendPropertyValue("rowIndex", new Integer(rowIndex)); //行号
		//填充统计数据
		fillStatisticData(statisticView, statistic, result, hqlJoin, hqlWhere, request, sessionInfo, true);
		return result;
	}
	
	/**
	 * 填充统计数据
	 * @param statisticView
	 * @param statistic
	 * @param target
	 * @return
	 * @throws ServiceException
	 */
	private void fillStatisticData(StatisticView statisticView, Statistic statistic, Record target, String hqlJoin, String hqlWhere, HttpServletRequest request, SessionInfo sessionInfo, boolean fullFill) throws ServiceException {
		String pojoClassName = statisticView.getPojoClassName();
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		//统计
		if(statistic.getGroupFields()!=null) { //分组字段
			if(hqlWhere!=null) {
				hqlWhere = "(" + hqlWhere + ")";
			}
			for(Iterator iterator=statistic.getGroupFields().iterator(); iterator.hasNext();) {
				GroupField groupField = (GroupField)iterator.next();
				hqlWhere = (hqlWhere==null ? "" : hqlWhere + " and ") + (groupField.getField()==null ? pojoClassName + "." + groupField.getName() : groupField.getField());
				try {
					String value = getFieldValue(target, groupField.getName(), true);
					hqlWhere += (value==null ? " is null" : "=" + value);
				}
				catch (Exception e) {
					Logger.exception(e);
					throw new ServiceException();
				}
			}
		}
		//获取统计结果
		List simpleColumns = new ArrayList();
		String hqlSelectSimple = null;
		for(Iterator iterator=statistic.getStatisticColumns().iterator(); iterator.hasNext();) {
			StatisticColumn statisticColumn = (StatisticColumn)iterator.next();
			Column column = (Column)ListUtils.findObjectByProperty(statisticView.getColumns(), "name", statisticColumn.getName());
			String fieldGroupBy = column==null || column.getGroupValues()==null ? null : pojoClassName + "." + column.getGroupBy();
			String hqlSelect = (fieldGroupBy==null ? statisticColumn.getFunction() : fieldGroupBy + "," + statisticColumn.getFunction());
			String hqlWhereStatistic = null;
			if(statisticColumn.getWhereExtend()!=null) { //统计条件扩展
				String whereExtend = StringUtils.fillParameters(statisticColumn.getWhereExtend(), false, false, true, "utf-8", null, request, null);
				hqlWhereStatistic = (hqlWhere==null || hqlWhere.isEmpty() ? whereExtend : "(" + hqlWhere + ") and (" + whereExtend + ")");
			}
			else {
				if(column!=null && column.getGroupValues()==null && !fullFill) {
					continue;
				}
				if(column!=null && fieldGroupBy==null) {
					hqlSelectSimple = (hqlSelectSimple==null ? "" : hqlSelectSimple + ",") + statisticColumn.getFunction();
					simpleColumns.add(column);
					continue;
				}
				hqlWhereStatistic = hqlWhere;
			}
			//填充统计结果
			String join = generateHqlJoin(statisticView, hqlJoin, null, hqlWhereStatistic, null, null);
			List statisticResult = getDatabaseService().findRecordsByFilter(pojoClassName, hqlSelect, join, hqlWhereStatistic, fieldGroupBy, null, statisticView.getFilter(), null, 0, 0, sessionInfo);
			if(column==null) {
				target.setExtendPropertyValue(statisticColumn.getName(), statisticResult.get(0));
			}
			else if(fieldGroupBy==null) {
				fillColumn(column, target, statisticResult.get(0));
			}
			else { //根据值列表([0]分组,[1]值)填充分组数据
				fillGroupedColumn(column, target, statisticResult);
			}
		}
		if(hqlSelectSimple!=null) {
			List statisticResult = getDatabaseService().findRecordsByFilter(pojoClassName, hqlSelectSimple, hqlJoin, hqlWhere, null, null, statisticView.getFilter(), null, 0, 0, sessionInfo);
			if(simpleColumns.size()==1) {
				fillColumn((Column)simpleColumns.get(0), target, statisticResult.get(0));
			}
			else {
				Object[] values = (Object[])statisticResult.get(0);
				for(int j=0; j<simpleColumns.size(); j++) {
					fillColumn((Column)simpleColumns.get(j), target, values[j]);
				}
			}
		}
	}
	
	/**
	 * 填充列值
	 * @param column
	 * @param target
	 * @param value
	 * @throws ServiceException
	 */
	private void fillColumn(Column column, Record target, Object value) throws ServiceException {
		try {
			if(value==null) {
				if(PropertyUtils.getProperty(target, column.getName()) instanceof Number) {
					PropertyUtils.setProperty(target, column.getName(), new Integer(0));
				}
				else {
					PropertyUtils.setProperty(target, column.getName(), null);
				}
			}
			else {
				PropertyUtils.setProperty(target, column.getName(), value);
			}
		}
		catch (Exception e) {
			target.setExtendPropertyValue(column.getName(), value);
		}
	}
	
	/**
	 * 根据值列表([0]分组,[1]值)填充分组数据
	 * @param column
	 * @param target
	 * @param valueList
	 * @throws ServiceException
	 */
	private void fillGroupedColumn(Column column, Record target, List valueList) throws ServiceException {
		List columnValueList = new ArrayList();
		int groupSize = column.getGroupValues().size();
		for(int i=0; i<groupSize; i++) {
			columnValueList.add("");
		}
		for(Iterator iteratorResult = valueList.iterator(); iteratorResult.hasNext();) {
			Object[] objects = (Object[])iteratorResult.next();
			int i=0;
			for(; i<groupSize && !objects[0].equals(column.getGroupValues().get(i)); i++);
			if(i<groupSize) {
				columnValueList.set(i, objects[1]);
			}
		}
		try {
			PropertyUtils.setProperty(target, column.getName(), columnValueList);
		}
		catch (Exception e) {
			target.setExtendPropertyValue(column.getName(), columnValueList);
		}
	}
	
	/**
	 * 获取字段值,以字符串形式返回
	 * @param pojo
	 * @param fieldName
	 * @param addSingleQuote
	 * @return
	 * @throws Exception
	 */
	private String getFieldValue(Object pojo, String fieldName, boolean addSingleQuote) throws ServiceException {
		Object value = null;
		try {
			value = PropertyUtils.getProperty(pojo, fieldName);
		}
		catch (Exception e) {
			
		}
		if(value==null) {
			return null;
		}
		else if(value instanceof Date) {
			return "DATE(" + DateTimeUtils.formatDate((Date)value, null) + ")";
		}
		else if(value instanceof Timestamp) {
			return "TIMESTAMP(" + DateTimeUtils.formatTimestamp((Timestamp)value, null) + ")";
		}
		else if((value instanceof String) || (value instanceof Character)) {
			return (addSingleQuote ? "'" : "") + value + (addSingleQuote ? "'" : "");
		}
		else {
			return "" + value;
		}
	}
}