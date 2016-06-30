package com.yuanluesoft.logistics.usermanage.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;

/**
 * 
 * @author linchuan
 *
 */
public interface LogisticsUserService extends BusinessService, MemberService {
	
	/**
	 * 检查用户是否在黑名单中
	 * @param user
	 * @return
	 */
	public boolean inBlacklist(LogisticsUser user);

	/**
	 * 列入黑名单
	 * @param userId
	 * @param reason
	 * @throws ServiceException
	 */
	public void addToBlacklist(long userId, String reason) throws ServiceException;
	
	/**
	 * 从黑名单中删除
	 * @param userId
	 * @throws ServiceException
	 */
	public void removeFromBlacklist(long userId) throws ServiceException;
}