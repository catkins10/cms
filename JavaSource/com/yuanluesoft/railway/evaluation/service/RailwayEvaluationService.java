package com.yuanluesoft.railway.evaluation.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.railway.evaluation.model.RailwayEvaluation;
import com.yuanluesoft.railway.evaluation.pojo.RailwayEvaluationParameter;

/**
 * 
 * @author linchuan
 *
 */
public interface RailwayEvaluationService extends BusinessService {
	
	/**
	 * 是否考评本月
	 * @return
	 */
	public boolean isEvaluateCurrentMonth();

	/**
	 * 获取参数配置
	 * @return
	 * @throws ServiceException
	 */
	public RailwayEvaluationParameter loadRailwayEvaluationParameter() throws ServiceException;
	
	/**
	 * 获取综合评价考核结果
	 * @param postIds
	 * @param orgIds
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public List listRailwayEvaluations(String postIds, String orgIds, int year, int month) throws ServiceException;
	
	/**
	 * 获取个人综合评价考核结果
	 * @param personId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public RailwayEvaluation getPersonalRailwayEvaluation(long personId, int year, int month) throws ServiceException;
}