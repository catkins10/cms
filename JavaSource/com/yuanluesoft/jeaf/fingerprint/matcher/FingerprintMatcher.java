package com.yuanluesoft.jeaf.fingerprint.matcher;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.fingerprint.pojo.FingerprintTemplate;

/**
 * 
 * @author linchuan
 *
 */
public interface FingerprintMatcher {
	
	/**
	 * 匹配指纹,返回匹配的指纹模板
	 * @param featureData 采样数据
	 * @param templates 模板列表
	 * @return
	 * @throws ServiceException
	 */
	public FingerprintTemplate match(String featureData, List templates) throws ServiceException;
}
