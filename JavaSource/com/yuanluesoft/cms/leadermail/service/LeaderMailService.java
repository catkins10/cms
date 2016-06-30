package com.yuanluesoft.cms.leadermail.service;

import java.util.List;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMailDepartment;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface LeaderMailService extends PublicService {
	
	/**
	 * 按站点获取诉求件类型列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listTypes(long siteId) throws ServiceException;
	
	/**
	 * 按站点ID获取受理部门配置
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public LeaderMailDepartment getLeaderMailDepartment(long siteId) throws ServiceException;
	
	/**
	 * 发布时,是否总是公开信件的全部信息
	 * @return
	 */
	public boolean isAlwaysPublishAll();
}