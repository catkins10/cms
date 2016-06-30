package com.yuanluesoft.jeaf.usermanage.member.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface MemberService extends BusinessService, com.yuanluesoft.jeaf.membermanage.service.MemberService {
	
	/**
	 * 解密口令
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public String decryptPassword(long userId, String password) throws ServiceException;
	
	/**
	 * 检查用户名是否已经被使用,除检查用户表外,还要检查注册用户表
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException;
	
	/**
	 * 按用户名获取用户
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	public Member getMemberByLoginName(String loginName) throws ServiceException;
	
	/**
	 * 按ID获取注册用户
	 * @param memberId
	 * @return
	 * @throws ServiceException
	 */
	public Member getMemberById(long memberId) throws ServiceException;
		
	/**
	 * 获取注册用户总数
	 * @return
	 * @throws ServiceException
	 */
	public int totalMember() throws ServiceException;
	
	/**
	 * 获取最后注册的用户
	 * @return
	 * @throws ServiceException
	 */
	public Member getLastMember() throws ServiceException;
}