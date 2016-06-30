package com.yuanluesoft.bbs.usermanage.service;

import com.yuanluesoft.bbs.usermanage.model.BbsUser;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface BbsUserService extends BusinessService {
	public static final char USER_TYPE_SYSTEM = '1'; //系统用户
	public static final char USER_TYPE_MEMBER = '2'; //网上注册用户
	
	/**
	 * 获取论坛用户信息
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public com.yuanluesoft.bbs.usermanage.pojo.BbsUser getBbsUser(long personId) throws ServiceException;
	
	/**
	 * 获取论坛用户信息模型
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public BbsUser getBbsUserModel(long personId) throws ServiceException;
	
	/**
	 * 增加发帖数
	 * @param personId
	 * @param count 数量
	 * @throws ServiceException
	 */
	public void increasePost(long personId, int count) throws ServiceException;
	
	/**
	 * 增加回帖数
	 * @param personId
	 * @param count
	 * @throws ServiceException
	 */
	public void increaseReply(long personId, int count) throws ServiceException;
}
