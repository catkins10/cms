package com.yuanluesoft.bidding.project.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 项目数据导入服务
 * @author linchuan
 *
 */
public interface BiddingProjectImportService {
	
	/**
	 * 导入数据
	 * @param dateFilePath
	 * @throws ServiceException
	 */
	public void importData(String dateFilePath) throws ServiceException;
}