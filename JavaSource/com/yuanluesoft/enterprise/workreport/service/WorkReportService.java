package com.yuanluesoft.enterprise.workreport.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 考核服务
 * @author linchuan
 *
 */
public interface WorkReportService extends BusinessService {

	/**
	 * 获取项目组汇报列表
	 * @param teamId
	 * @param sessionInfo
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectTeamWorkReports(long teamId, SessionInfo sessionInfo, int offset, int limit) throws ServiceException;
}