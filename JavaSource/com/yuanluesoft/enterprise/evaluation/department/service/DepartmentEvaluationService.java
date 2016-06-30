package com.yuanluesoft.enterprise.evaluation.department.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.evaluation.department.pojo.DepartmentEvaluation;
import com.yuanluesoft.enterprise.evaluation.department.pojo.DepartmentEvaluationParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface DepartmentEvaluationService extends BusinessService {

	/**
	 * 获取参数配置
	 * @return
	 * @throws ServiceException
	 */
	public DepartmentEvaluationParameter loadDepartmentEvaluationParameter() throws ServiceException;
	
	/**
	 * 获取需要考核的部门(Org)列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listToEvaluateDepartments(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建考核
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public DepartmentEvaluation createDepartmentEvaluation(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 提交考核
	 * @param evaluationId
	 * @param isNew
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public DepartmentEvaluation submitDepartmentEvaluation(long evaluationId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取部门考核结果,如果没有被考核,返回-1
	 * @param orgId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public double getDepartmentEvaluationResult(long orgId, int year, int month) throws ServiceException;
}