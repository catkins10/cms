package com.yuanluesoft.jeaf.sensitive.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sensitive.pojo.SensitiveWord;

/**
 * 敏感词服务
 * @author linchuan
 *
 */
public interface SensitiveWordService extends BusinessService {

	/**
	 * 加载敏感词记录
	 * @return
	 * @throws ServiceException
	 */
	public SensitiveWord loadSensitiveWord() throws ServiceException;
	
	/**
	 * 获取预设的敏感词
	 * @return
	 */
	public String getPresettingWords();
	
	/**
	 * 查找敏感词,如果没有则返回null
	 * @param text
	 * @return
	 * @throws ServiceException
	 */
	public String findSensitiveWord(String text) throws ServiceException;
}