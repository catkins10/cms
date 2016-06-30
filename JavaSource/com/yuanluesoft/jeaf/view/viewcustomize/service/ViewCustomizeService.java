/*
 * Created on 2005-4-8
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.service;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public interface ViewCustomizeService {
	
	/**
	 * 根据视图定制信息重置视图
	 * @param view
	 * @param userId
	 * @throws ServiceException
	 */
	public void customizeView(View view, long userId) throws ServiceException;
	
	/**
	 * 加载视图定制信息
	 * @param view
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public ViewCustom loadViewCustom(View view, long userId) throws ServiceException;
	
	/**
	 * 保存视图定制信息
	 * @param applicationName
	 * @param viewName
	 * @param userId
	 * @param pageRows
	 * @param columnNames
	 * @param columnTitles
	 * @param columnWidths
	 * @param columnAligns
	 * @param sortColumnNames
	 * @param sortColumnTitles
	 * @param sortColumnDirections
	 * @throws ServiceException
	 */
	public void saveViewCustom(String applicationName, String viewName, long userId, int pageRows, String[] columnNames, String[] columnTitles, String[] columnWidths, String[] columnAligns, String[] sortColumnNames, String[] sortColumnTitles, String[] sortColumnDirections) throws ServiceException;
	
	/**
	 * 删除视图定制信息
	 * @param viewCustom
	 * @param applicationName
	 * @param moduleName
	 * @param viewName
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void deleteViewCustom(String applicationName, String viewName, long userId) throws ServiceException;
	
	/**
	 * 保存用户定义的排序列
	 * @param view
	 * @param sortColumnName
	 * @param userId
	 * @throws ServiceException
	 */
	public void saveSortColumn(View view, String sortColumnName, long userId) throws ServiceException;
}