package com.yuanluesoft.enterprise.exam.learn.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 学习服务
 * @author linchuan
 *
 */
public interface LearnService extends BusinessService {

	/**
	 * 启动学习,返回用户已经学习的分钟数
	 * @param missionId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public int startupLearned(long missionId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 更新学习时间
	 * @param missionId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public void updateLearnTime(long missionId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取学习任务完成情况
	 * @param missionId
	 * @return
	 * @throws ServiceException
	 */
	public List listLearnMissionAccomplishs(long missionId) throws ServiceException;
}