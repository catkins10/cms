package com.yuanluesoft.jeaf.fingerprint.service;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;

/**
 * 
 * @author linchuan
 *
 */
public interface FingerprintService extends MemberService {
	
	/**
	 * 是否支持指纹认证
	 * @return
	 */
	public boolean isFingerprintSupport();
	
	/**
	 * 保存指纹模板
	 * @param personId
	 * @param personName
	 * @param fingerName
	 * @param templateData
	 * @throws ServiceException
	 */
	public void saveFingerprintTemplate(long personId, String personName, String fingerName, String templateData) throws ServiceException;
	
	/**
	 * 指纹校验,返回匹配的用户ID,如果没有则返回-1
	 * @param featureData 指纹样本数据
	 * @param ip 请求的IP
	 * @param perhapsPersonId 可能的用户ID,大于0时有效
	 * @return
	 * @throws ServiceException
	 */
	public long verifyFingerprint(String featureData, String ip, long perhapsPersonId) throws ServiceException;
	
	/**
	 * 删除用户指纹数据
	 * @param personId
	 * @throws ServiceException
	 */
	public void removeFingerprint(long personId) throws ServiceException;
}