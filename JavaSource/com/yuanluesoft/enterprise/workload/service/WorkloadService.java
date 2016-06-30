package com.yuanluesoft.enterprise.workload.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.workload.pojo.WorkloadAssess;
import com.yuanluesoft.enterprise.workload.pojo.WorkloadAssessResult;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface WorkloadService extends BusinessService {
	
	/**
	 * 获取需要考核的用户(Person)列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listToAssessPersons(SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 创建一个考核
	 * @param teamId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public WorkloadAssess createAssess(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 提交考核结果
	 * @param assessId
	 * @param isNew
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public WorkloadAssess submitAssess(long assessId, boolean isNew, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取工作量考核结果
	 * @param personId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public WorkloadAssessResult getWorkloadAssessResult(long personId, int year, int month) throws ServiceException;
}