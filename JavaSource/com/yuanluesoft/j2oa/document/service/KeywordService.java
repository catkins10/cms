/*
 * Created on 2005-9-16
 *
 */
package com.yuanluesoft.j2oa.document.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface KeywordService extends BusinessService {
	
	/**
	 * 解析文本中的主题词
	 * @param text
	 * @return
	 * @throws ServiceException
	 */
	public String parseKeyword(String text) throws ServiceException;
}
