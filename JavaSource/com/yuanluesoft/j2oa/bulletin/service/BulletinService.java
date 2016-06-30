package com.yuanluesoft.j2oa.bulletin.service;

import com.yuanluesoft.j2oa.bulletin.pojo.Bulletin;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface BulletinService extends BusinessService {

	/**
	 * 
	 * @param bulletin
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issueBulletin(Bulletin bulletin, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param bulletin
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void unissueBulletin(Bulletin bulletin, SessionInfo sessionInfo) throws ServiceException;
}