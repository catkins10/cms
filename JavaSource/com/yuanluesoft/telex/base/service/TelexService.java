package com.yuanluesoft.telex.base.service;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface TelexService extends BusinessService {

	/**
	 * 获取下一个电报顺序号
	 * @param isSend
	 * @param isCryptic
	 * @param readOnly
	 * @return
	 * @throws ServiceException
	 */
	public int getNextSequence(boolean isSend, boolean isCryptic, boolean readOnly) throws ServiceException;
	
	/**
	 * 获取当前电报顺序号
	 * @param isSend
	 * @param isCryptic
	 * @return
	 * @throws ServiceException
	 */
	public int getCurrentSequence(boolean isSend, boolean isCryptic) throws ServiceException;
	
	/**
	 * 保存当前电报顺序号
	 * @param isSend
	 * @param isCryptic
	 * @param sn
	 * @throws ServiceException
	 */
	public void saveCurrentSequence(boolean isSend, boolean isCryptic, int sn) throws ServiceException;
	
	/**
	 * 获取电报密级列表
	 * @return
	 * @throws ServiceException
	 */
	public List listTelegramSecurityLevels() throws ServiceException;
	
	/**
	 * 获取电报等级列表
	 * @return
	 * @throws ServiceException
	 */
	public List listTelegramLevels() throws ServiceException;
	
	/**
	 * 获取电报分类列表
	 * @return
	 * @throws ServiceException
	 */
	public List listTelegramCategories() throws ServiceException;
	
	/**
	 * 保存电报密级列表
	 * @param securityLevels
	 * @throws ServiceException
	 */
	public void saveTelegramSecurityLevels(String securityLevels) throws ServiceException;
	
	/**
	 * 保存电报等级列表
	 * @param levels
	 * @throws ServiceException
	 */
	public void saveTelegramLevels(String levels) throws ServiceException;
	
	/**
	 * 保存电报分类列表
	 * @param categories
	 * @throws ServiceException
	 */
	public void saveTelegramCategories(String categories) throws ServiceException;
	
	/**
	 * 生成办理单
	 * @param telegram
	 * @param sheetid
	 * @return
	 * @throws ServiceException
	 */
	public String generateTransactionSheet(Object telegram, long sheetid) throws ServiceException;
	
	/**
	 * 保存意见填写人姓名
	 * @param personName
	 * @return
	 * @throws ServiceException
	 */
	public void saveOpinionPerson(String personName) throws ServiceException;
	
	/**
	 * 电报统计
	 * @param beginDate
	 * @param endDate
	 * @param telegramLevels
	 * @param securityLevels
	 * @return TotalBySecurityLevel列表
	 * @throws ServiceException
	 */
	public List totalTelegram(Date beginDate, Date endDate, String[] telegramLevels, String[] securityLevels, long fromUnitId) throws ServiceException;
}
