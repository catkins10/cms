package com.yuanluesoft.bidding.enterprise.services;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 企业数据导入服务
 * @author linchuan
 *
 */
public interface EnterpriseImportService {
	
	/**
	 * 导入数据
	 * @param dateFilePath
	 * @throws ServiceException
	 */
	public void importData(String dateFilePath) throws ServiceException;
}
