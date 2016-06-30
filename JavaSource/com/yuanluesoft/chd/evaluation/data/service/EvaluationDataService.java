/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.chd.evaluation.data.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationData;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 * 
 */
public interface EvaluationDataService extends BusinessService {
	
	/**
	 * 更新资料的隶属关系
	 * @param data
	 * @param isNew
	 * @param subjectionRuleIds
	 * @throws ServiceException
	 */
	public void updateDataSubjections(ChdEvaluationData data, boolean isNew, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 获取提交的资料列表
	 * @param directoryId
	 * @param year
	 * @param month
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listData(long directoryId, int year, int month, int max) throws ServiceException;
	
	/**
	 * 加载必备条件完成情况列表
	 * @param plantId
	 * @param year
	 * @return
	 * @throws ServiceException
	 */
	public List loadPrerequisitesDataList(long plantId, int year) throws ServiceException;
	
	/**
	 * 保存必备条件完成情况
	 * @param plantId
	 * @param year
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void savePrerequisitesDataList(long plantId, int year, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 加载指标完成情况列表
	 * @param directoryId
	 * @param year
	 * @param month
	 * @param toEdit
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List loadIndicatorDataList(long directoryId, int year, int month, boolean toEdit, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 保存指标完成情况
	 * @param directoryId
	 * @param year
	 * @param month
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void saveIndicatorDataList(long directoryId, int year, int month, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 生成待办事宜,由定时器调用
	 * @throws ServiceException
	 */
	public void createTodo() throws ServiceException;
	
	/**
	 * 清空资料上传待办事宜
	 * @param directoryId
	 * @throws ServiceException
	 */
	public void clearDataTodo(long directoryId) throws ServiceException;
	
	/**
	 * 清空自查待办事宜
	 * @param directoryId
	 * @param evalYear
	 * @param evalMonth
	 * @throws ServiceException
	 */
	public void clearSelfEvalTodo(long directoryId, int evalYear, int evalMonth) throws ServiceException;
}