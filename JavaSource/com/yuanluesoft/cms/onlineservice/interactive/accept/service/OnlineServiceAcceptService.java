package com.yuanluesoft.cms.onlineservice.interactive.accept.service;

import java.util.List;

import com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface OnlineServiceAcceptService extends OnlineserviceInteractiveService {
	
	/**
	 * 发送缺件通知
	 * @param missingReason
	 * @param acceptId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void sendMissingNotify(String missingReason, long acceptId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 清除缺件通知
	 * @param acceptId
	 * @throws ServiceException
	 */
	public void clearMissingNotifies(long acceptId) throws ServiceException;
	
	/**
	 * 从文件导入收件记录
	 * @param uploadFiles
	 * @param siteId
	 * @throws ServiceException
	 */
	public void importCases(List uploadFiles) throws ServiceException;
}