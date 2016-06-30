/*
 * Created on 2005-11-9
 *
 */
package com.yuanluesoft.j2oa.document.service;

import com.yuanluesoft.j2oa.document.pojo.DocumentOption;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface DocumentService extends BusinessService {
	
	/**
	 * 获取公文选项配置
	 * @return
	 * @throws ServiceException
	 */
	public DocumentOption getDocumentOption() throws ServiceException;
	
	/**
	 * 生成办理单
	 * @param handlingTemplate
	 * @param record
	 * @return
	 * @throws ServiceException
	 */
	public String generateHandling(String handlingTemplate, Record record) throws ServiceException;
}