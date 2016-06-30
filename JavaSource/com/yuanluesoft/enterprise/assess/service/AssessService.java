package com.yuanluesoft.enterprise.assess.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.assess.pojo.Assess;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 考核服务
 * @author linchuan
 *
 */
public interface AssessService extends BusinessService {

	/**
	 * 创建一个考核
	 * @param teamId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Assess createAssess(long teamId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 加载一个考核
	 * @param assessId
	 * @return
	 * @throws ServiceException
	 */
	public Assess loadAssess(long assessId) throws ServiceException;
	
	/**
	 *  保存一个考核
	 * @param assessId
	 * @param teamId
	 * @param workflowInstanceId
	 * @param isNewAssess
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void saveAssess(long assessId, long teamId, String workflowInstanceId, boolean isNewAssess, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取项目组绩效考核列表
	 * @param teamId
	 * @param sessionInfo
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectTeamAssesses(long teamId, SessionInfo sessionInfo, int offset, int limit) throws ServiceException;
}