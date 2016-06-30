package com.yuanluesoft.logistics.supply.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface LogisticsSupplyService extends BusinessService {

	/**
	 * 更新货源的出发地点和目的地点
	 * @param vehicleSupplyId
	 * @param isNew
	 * @param departureAreaIds
	 * @param departureAreas
	 * @param destinationAreaIds
	 * @param destinationAreas
	 * @throws ServiceException
	 */
	public void updateLogisticsSupply(long vehicleSupplyId, boolean isNew, String departureAreaIds, String departureAreas, String destinationAreaIds, String destinationAreas) throws ServiceException;
}