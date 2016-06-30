package com.yuanluesoft.land.declare.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author kanshiwei
 *
 */
public interface LandDeclareService extends BusinessService {

	/**
	 * 数据导入
	 * @param excelFilePath
	 * @throws ServiceException
	 */
	public void importData(String excelFilePath) throws ServiceException;
}
