package com.yuanluesoft.bidding.project.ask.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface BiddingProjectAskService extends BusinessService {
	
	/**
	 * 获取项目的问题列表
	 * @param projectId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectAsks(long projectId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 是否需要应答
	 * @return
	 */
	public boolean isNeedReply();
}
