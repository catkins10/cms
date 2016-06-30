package com.yuanluesoft.job.apply.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface JobApplyService extends BusinessService {
	
	/**
	 * 发送面试邀请函邮件
	 * @param companyId
	 * @param email
	 * @param mailContent
	 * @throws ServiceException
	 */
	public void sendInvitationMail(long companyId, String email, String mailContent) throws ServiceException;

	/**
	 * 发送录用通知函邮件
	 * @param companyId
	 * @param email
	 * @param mailContent
	 * @throws ServiceException
	 */
	public void sendOfferMail(long companyId, String email, String mailContent) throws ServiceException;
	
	/**
	 * 加入收藏夹
	 * @param talentId
	 * @param jobId
	 * @throws ServiceException
	 */
	public void addFavorite(long talentId, long jobId) throws ServiceException;
	
	/**
	 * 取消收藏
	 * @param talentId
	 * @param jobId
	 * @throws ServiceException
	 */
	public void removeFavorite(long talentId, long jobId) throws ServiceException;
	
	/**
	 * 检查是否收藏过
	 * @param talentId
	 * @param jobId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isFavorite(long talentId, long jobId) throws ServiceException;
}