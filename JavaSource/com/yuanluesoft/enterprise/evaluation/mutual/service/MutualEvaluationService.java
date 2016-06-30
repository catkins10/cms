package com.yuanluesoft.enterprise.evaluation.mutual.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.evaluation.mutual.model.MutualEvaluationTotal;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluation;
import com.yuanluesoft.enterprise.evaluation.mutual.pojo.MutualEvaluationParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface MutualEvaluationService extends BusinessService {

	/**
	 * 获取参数配置
	 * @return
	 * @throws ServiceException
	 */
	public MutualEvaluationParameter loadMutualEvaluationParameter() throws ServiceException;
	
	/**
	 * 获取需要互评的用户(Person)列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listToEvaluatePersons(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建互评
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public MutualEvaluation createMutualEvaluation(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 提交互评
	 * @param evaluationId
	 * @param isNew
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public MutualEvaluation submitMutualEvaluation(long evaluationId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取互评统计
	 * @param personId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public MutualEvaluationTotal getMutualEvaluationTotal(long personId, int year, int month) throws ServiceException;
}