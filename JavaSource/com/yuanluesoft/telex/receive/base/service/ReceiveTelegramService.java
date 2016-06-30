package com.yuanluesoft.telex.receive.base.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;
import com.yuanluesoft.telex.receive.base.pojo.ReceiveTelegram;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignAgent;

/**
 * 
 * @author linchuan
 *
 */
public interface ReceiveTelegramService extends BusinessService {
	
	/**
	 * 获取接收到的电报
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public ReceiveTelegram getReceiveTelegram(long id) throws ServiceException;

	/**
	 * 添加签收人
	 * @param telegramId
	 * @param receiverIds
	 * @param receiverNames
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void appendSignReceivers(long telegramId, String receiverIds, String receiverNames, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除签收人
	 * @param telegramSignIds
	 * @param notSignOnly 仅未签收的
	 * @throws ServiceException
	 */
	public void deleteSignReceivers(String telegramSignIds, boolean notSignOnly) throws ServiceException;
	
	/**
	 * 设置签收人是否需要回退
	 * @param telegramSignIds
	 * @param need
	 * @throws ServiceException
	 */
	public void setReturnOption(String telegramSignIds, boolean needReturn) throws ServiceException;
	
	/**
	 * 保存签收代理人
	 * @param name
	 * @param orgId
	 * @param orgName
	 * @param certificateName
	 * @param certificateCode
	 * @param sex
	 * @param fingerprintTemplate
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public TelegramSignAgent saveSignAgent(String name, long orgId, String orgName, String certificateName, String certificateCode, char sex, String fingerprintTemplate, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取签收人信息
	 * @param personId
	 * @throws ServiceException
	 */
	public TelegramSignPerson getSignPerson(long personId) throws ServiceException;
	
	/**
	 * 获取待签收的电报(TelegramSign)列表
	 * @param signPerson
	 * @param telegramIds 从指定的电报中获取,null表示从所有电报中获取
	 * @return
	 * @throws ServiceException
	 */
	public List listToSignTelegrams(TelegramSignPerson signPerson, String telegramIds) throws ServiceException;
	
	/**
	 * 获取待清退的电报(TelegramSign)列表
	 * @param signPerson
	 * @param telegramIds 从指定的电报中获取,null表示从所有电报中获取
	 * @return
	 * @throws ServiceException
	 */
	public List listToReturnTelegrams(TelegramSignPerson signPerson, String telegramIds) throws ServiceException;
	
	/**
	 * 获取电报(TelegramSign)列表
	 * @param telegramIds
	 * @return
	 * @throws ServiceException
	 */
	public List listTelegrams(String telegramSignIds) throws ServiceException;
	
	/**
	 * 签收电报
	 * @param signPerson
	 * @param telegramIds
	 * @param sessionInfo
	 * @return 实际签收的电报列表
	 * @throws ServiceException
	 */
	public List signTelegrams(TelegramSignPerson signPerson, String telegramSignIds, Timestamp signTime, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 清退电报
	 * @param signPerson
	 * @param telegramIds
	 * @param sessionInfo
	 * @return 实际清退的电报列表
	 * @throws ServiceException
	 */
	public List returnTelegrams(TelegramSignPerson signPerson, String telegramSignIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出电报清单
	 * @param year
	 * @param month
	 * @param isCryptic
	 * @param response
	 * @throws ServiceException
	 */
	public void exportListing(int year, int month, boolean isCryptic, HttpServletResponse response) throws ServiceException;
}