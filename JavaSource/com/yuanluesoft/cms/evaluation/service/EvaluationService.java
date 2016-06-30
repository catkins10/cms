package com.yuanluesoft.cms.evaluation.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.evaluation.pojo.EvaluationMark;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface EvaluationService extends BusinessService {

	/**
	 * 获取待评测用户列表
	 * @param evaluationTopic
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listToEvaluateTargetPersons(EvaluationTopic evaluationTopic, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 获取已评测用户列表
	 * @param evaluationTopic
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listEvaluatedTargetPersons(EvaluationTopic evaluationTopic, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 加载测评记录
	 * @param evaluationTopicId
	 * @param targetPesonId
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public EvaluationMark loadEvaluationMark(long evaluationTopicId, long targetPersonId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 提交测评记录
	 * @param evaluationTopicId
	 * @param targetPesonId
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public EvaluationMark submitEvaluation(long evaluationTopicId, long targetPersonId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 拷贝测评主题
	 * @param evaluationTopicId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public EvaluationTopic copyEvaluationTopic(long evaluationTopicId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 测评汇总
	 * @param evaluationTopicId
	 * @return
	 * @throws ServiceException
	 */
	public List evaluationTotal(long evaluationTopicId) throws ServiceException;
}