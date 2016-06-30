/*
 * Created on 2005-4-8
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ColumnCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.model.SortColumnCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.parser.ViewCustomParser;
import com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustomizeServiceImpl implements ViewCustomizeService {
	private DatabaseService databaseService; //数据库服务
	private ViewCustomParser viewCustomParser; //视图定制解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService#customizeView(com.yuanluesoft.jeaf.view.model.View, long)
	 */
	public void customizeView(View view, long userId) throws ServiceException {
		com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom pojoViewCustom = findViewCustom(view.getApplicationName(), view.getName(), userId);
		if(pojoViewCustom==null) {
			return;
		}
		ViewCustom viewCustom;
		try {
			viewCustom = viewCustomParser.parseViewCustom(pojoViewCustom.getCustom());
		}
		catch(ParseException e) {
			Logger.exception(e);
			return;
		}
		//每页记录数
		if(viewCustom.getPageRows()>0) {
			view.setPageRows(viewCustom.getPageRows());
		}
		//更新列
		if(viewCustom.getColumns()!=null && !viewCustom.getColumns().isEmpty()) {
			List columns = new ArrayList();
			for(Iterator iterator = viewCustom.getColumns().iterator(); iterator.hasNext();) {
				ColumnCustom columnCustom = (ColumnCustom)iterator.next();
				Column viewColumn = null;
				//查找视图列定义
				for(Iterator iteratorColumn = view.getColumns().iterator(); iteratorColumn.hasNext();) {
					Column column = (Column)iteratorColumn.next();
					if(StringUtils.isEquals(column.getName(), columnCustom.getColumnName()) && StringUtils.isEquals(column.getTitle(), columnCustom.getColumnTitle())) {
						viewColumn = column;
						break;
					}
				}
				if(viewColumn==null) {
					if(columnCustom.getColumnName()==null || columnCustom.getColumnName().isEmpty()) { //不是字段
						continue;
					}
					if(view instanceof StatisticView) {
						viewColumn = new com.yuanluesoft.jeaf.view.statisticview.model.Column(columnCustom.getColumnName(), columnCustom.getColumnTitle(), Column.COLUMN_TYPE_FIELD);
					}
					else {
						viewColumn = new Column(columnCustom.getColumnName(), columnCustom.getColumnTitle(), Column.COLUMN_TYPE_FIELD);
					}
					viewColumn.setLink((Link)ListUtils.findObjectByProperty(view.getLinks(), "type", "recordLink"));
				}
				viewColumn.setWidth(columnCustom.getColumnWidth()); //宽度
				viewColumn.setAlign(columnCustom.getColumnAlign()); //对齐方式
				viewColumn.setDisplay(null); //清空显示过滤参数
				viewColumn.setDisplayExcept(null); //清空显示过滤参数
				columns.add(viewColumn);
			}
			view.setColumns(columns);
		}
		//设置排序列
		if(viewCustom.getSortColumns()!=null && !viewCustom.getSortColumns().isEmpty()) {
			String pojoClassName = view.getPojoClassName();
			pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
			String orderBy = null;
			for(Iterator iterator = viewCustom.getSortColumns().iterator(); iterator.hasNext();) {
				SortColumnCustom sortColumnCustom = (SortColumnCustom)iterator.next();
				orderBy = (orderBy==null ? "" : orderBy + ",") + (sortColumnCustom.getColumnName().indexOf('.')==-1 ? pojoClassName + "." : "") + sortColumnCustom.getColumnName() + (sortColumnCustom.isDesc() ? " DESC" : " ASC");
			}
			view.setOrderBy(orderBy);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService#loadViewCustom(com.yuanluesoft.jeaf.view.model.View, long)
	 */
	public ViewCustom loadViewCustom(View view, long userId) throws ServiceException {
		com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom pojoViewCustom = findViewCustom(view.getApplicationName(), view.getName(), userId);
		if(pojoViewCustom==null) { //没有定义过
			return initViewCustom(view);
		}
		ViewCustom viewCustom;
		try {
			viewCustom = viewCustomParser.parseViewCustom(pojoViewCustom.getCustom());
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		if(viewCustom.getColumns()==null || viewCustom.getColumns().isEmpty()) { //没有定义视图列
			viewCustom.setColumns(initViewCustom(view).getColumns());
			viewCustom.setPageRows(view.getPageRows());
		}
		return viewCustom;
	}
	
	/**
	 * 初始化视图定制
	 * @param view
	 * @return
	 */
	private ViewCustom initViewCustom(View view) {
		ViewCustom viewCustom = new ViewCustom();
		//解析每页记录数
		viewCustom.setPageRows(view.getPageRows());
		//解析列定义
		viewCustom.setColumns(new ArrayList());
		for(Iterator iterator=view.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			ColumnCustom columnCustom = new ColumnCustom();
			columnCustom.setColumnName(column.getName()); //列名称
			columnCustom.setColumnTitle(column.getTitle()); //列标题
			columnCustom.setColumnWidth(column.getWidth()); //列宽度
			columnCustom.setColumnAlign(column.getAlign()); //列对齐方式
			viewCustom.getColumns().add(columnCustom);
		}
		if(view.getOrderBy()!=null && !view.getOrderBy().isEmpty()) {
			String[] orderBy = view.getOrderBy().split(",");
			String pojoClassName = view.getPojoClassName();
			pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
			//解析排序列
			viewCustom.setSortColumns(new ArrayList());
			for(int i=0; i<orderBy.length; i++) {
				SortColumnCustom sortColumnCustom = new SortColumnCustom();
				String[] values = orderBy[i].trim().split(" ");
				sortColumnCustom.setColumnName(values[0].replaceFirst(pojoClassName + ".", "")); //列名称
				Field field = FieldUtils.getRecordField(view.getPojoClassName(), sortColumnCustom.getColumnName(), null);
				if(field!=null) {
					sortColumnCustom.setColumnTitle(field.getTitle()); //列标题
				}
				else {
					sortColumnCustom.setColumnName(values[0]);
					sortColumnCustom.setColumnTitle(values[0]); //列标题
				}
				sortColumnCustom.setDesc("DESC".equalsIgnoreCase(values[values.length-1])); //是否降序排列
				viewCustom.getSortColumns().add(sortColumnCustom);
			}
		}
		return viewCustom;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService#saveViewCustom(java.lang.String, java.lang.String, long, java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	public void saveViewCustom(String applicationName, String viewName, long userId, int pageRows, String[] columnNames, String[] columnTitles, String[] columnWidths, String[] columnAligns, String[] sortColumnNames, String[] sortColumnTitles, String[] sortColumnDirections) throws ServiceException {
		com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom pojoViewCustom = findViewCustom(applicationName, viewName, userId);
		ViewCustom viewCustom = new ViewCustom();
		viewCustom.setPageRows(pageRows); //每页记录数
		viewCustom.setColumns(new ArrayList()); //列
		for(int i=0; i<columnNames.length; i++) {
			ColumnCustom columnCustom = new ColumnCustom();
			columnCustom.setColumnName(columnNames[i]); //列名称
			columnCustom.setColumnTitle(columnTitles[i]); //列标题
			columnCustom.setColumnWidth(columnWidths[i]); //列宽度
			columnCustom.setColumnAlign(columnAligns[i]); //列对齐方式
			viewCustom.getColumns().add(columnCustom);
		}
		viewCustom.setSortColumns(new ArrayList()); //排序列
		for(int i=0; i<(sortColumnNames==null ? 0 : sortColumnNames.length); i++) {
			SortColumnCustom sortColumnCustom = new SortColumnCustom();
			sortColumnCustom.setColumnName(sortColumnNames[i]); //列名称
			sortColumnCustom.setColumnTitle(sortColumnTitles[i]); //列标题
			sortColumnCustom.setDesc("desc".equals(sortColumnDirections[i]));
			viewCustom.getSortColumns().add(sortColumnCustom);
		}
		String viewCustomText;
		try {
			viewCustomText = viewCustomParser.generateViewCustomXmlText(viewCustom); //转换为文本
		}
		catch (ParseException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		if(pojoViewCustom!=null) {
			pojoViewCustom.setCustom(viewCustomText);
			databaseService.updateRecord(pojoViewCustom);
		}
		else {
			pojoViewCustom = new com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom();
			pojoViewCustom.setId(UUIDLongGenerator.generateId());
			pojoViewCustom.setApplicationName(applicationName);
			pojoViewCustom.setViewName(viewName);
			pojoViewCustom.setUserId(userId);
			pojoViewCustom.setCustom(viewCustomText);
			databaseService.saveRecord(pojoViewCustom);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.custom.service.CustomService#deleteViewCustom(com.yuanluesoft.erp.custom.model.viewcustom.ViewCustom, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.erp.core.util.SessionInfo)
	 */
	public void deleteViewCustom(String applicationName, String viewName, long userId)	throws ServiceException {
		com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom pojoViewCustom = findViewCustom(applicationName, viewName, userId);
		if(pojoViewCustom!=null) {
			databaseService.deleteRecord(pojoViewCustom);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService#saveSortColumn(com.yuanluesoft.jeaf.view.model.View, java.lang.String, long)
	 */
	public void saveSortColumn(View view, String sortColumnName, long userId) throws ServiceException {
		if(sortColumnName==null || sortColumnName.isEmpty()) {
			return;
		}
		com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom pojoViewCustom = findViewCustom(view.getApplicationName(), view.getName(), userId);
		ViewCustom viewCustom;
		if(pojoViewCustom==null) {
			viewCustom = initViewCustom(view);
		}
		else {
			try {
				viewCustom = viewCustomParser.parseViewCustom(pojoViewCustom.getCustom());
			}
			catch(ParseException e) {
				Logger.exception(e);
				return;
			}
		}
		SortColumnCustom sortColumnCustom = (SortColumnCustom)ListUtils.findObjectByProperty(viewCustom.getSortColumns(), "columnName", sortColumnName);
		if(sortColumnCustom!=null) {
			if(viewCustom.getSortColumns().get(0)==sortColumnCustom) { //第一个排序列就是当前列
				sortColumnCustom.setDesc(!sortColumnCustom.isDesc()); //反序
			}
			else {
				viewCustom.getSortColumns().remove(sortColumnCustom);
				sortColumnCustom.setDesc(false);
				viewCustom.getSortColumns().add(0, sortColumnCustom);
			}
		}
		else {
			Field field = FieldUtils.getRecordField(view.getPojoClassName(), sortColumnName, null);
			if(field==null || !field.isPersistence()) { //字段不存在、或者不是数据库字段
				return;
			}
			sortColumnCustom = new SortColumnCustom();
			sortColumnCustom.setColumnTitle(field.getTitle()); //列标题
			sortColumnCustom.setColumnName(sortColumnName); //列名称
			sortColumnCustom.setDesc(false); //是否降序
			if(viewCustom.getSortColumns()==null) {
				viewCustom.setSortColumns(new ArrayList());
			}
			viewCustom.getSortColumns().add(0, sortColumnCustom);
		}
		if(pojoViewCustom!=null) { //原来就自定义过
			try {
				pojoViewCustom.setCustom(viewCustomParser.generateViewCustomXmlText(viewCustom)); //转换为文本
			}
			catch (ParseException e) {
				Logger.exception(e);
				return;
			}
			databaseService.updateRecord(pojoViewCustom);
		}
		else { //原来没有自定义过视图
			viewCustom.setPageRows(0); //删除记录数,view-config.xml修改后自动继承
			viewCustom.setColumns(null); //删除列配置,view-config.xml修改后自动继承
			pojoViewCustom = new com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom();
			pojoViewCustom.setId(UUIDLongGenerator.generateId());
			pojoViewCustom.setApplicationName(view.getApplicationName());
			pojoViewCustom.setViewName(view.getName());
			pojoViewCustom.setUserId(userId);
			try {
				pojoViewCustom.setCustom(viewCustomParser.generateViewCustomXmlText(viewCustom)); //转换为文本
			}
			catch (ParseException e) {
				Logger.exception(e);
				return;
			}
			databaseService.saveRecord(pojoViewCustom);
		}
	}

	/**
	 * 查找视图定制持久层对象
	 * @param applicationName
	 * @param moduleName
	 * @param viewName
	 * @param sessionInfo
	 * @return
	 */
	private com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom findViewCustom(String applicationName, String viewName, long userId) {
		return (com.yuanluesoft.jeaf.view.viewcustomize.pojo.ViewCustom)databaseService.findRecordByHql("from ViewCustom ViewCustom where ViewCustom.applicationName='" + JdbcUtils.resetQuot(applicationName) + "' and ViewCustom.viewName='" + JdbcUtils.resetQuot(viewName) + "' and userId=" + userId);
	}
	
	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	/**
	 * @return Returns the viewCustomParser.
	 */
	public ViewCustomParser getViewCustomParser() {
		return viewCustomParser;
	}
	/**
	 * @param viewCustomParser The viewCustomParser to set.
	 */
	public void setViewCustomParser(ViewCustomParser viewCustomParser) {
		this.viewCustomParser = viewCustomParser;
	}
}