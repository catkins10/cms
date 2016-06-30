package com.yuanluesoft.fdi.customer.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface FdiCustomerService extends BusinessService {

	/**
	 * 保存或更新单位所属行业
	 * @param companyId
	 * @param industryIds
	 * @param industryNames
	 * @throws ServiceException
	 */
	public void saveOrUpdateIndustries(long companyId, String industryIds, String industryNames) throws ServiceException;
}