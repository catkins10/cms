package com.yuanluesoft.logistics.vehicle.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;

/**
 * 
 * @author linchuan
 *
 */
public interface LogisticsVehicleService extends BusinessService {

	/**
	 * 按车牌号获取车辆
	 * @param plateNumber
	 * @return
	 * @throws ServiceException
	 */
	public LogisticsVehicle loadVehicle(String plateNumber) throws ServiceException;
	
	/**
	 * 按联系人号码获取车辆
	 * @param linkmanTel
	 * @return
	 * @throws ServiceException
	 */
	public LogisticsVehicle loadVehicleByLinkmanTel(String linkmanTel) throws ServiceException;
	
	/**
	 * 按随车联系人电话获取车辆
	 * @param linkmanTel
	 * @return
	 * @throws ServiceException
	 */
	public LogisticsVehicle updateVehicleStatus(String linkmanTel, boolean isEmpty) throws ServiceException;
	
	/**
	 * 更新车源
	 * @param vehicleSupplyId
	 * @param isNew
	 * @param freeVehicleNumbers
	 * @param departureAreaIds
	 * @param departureAreas
	 * @param destinationAreaIds
	 * @param destinationAreas
	 * @throws ServiceException
	 */
	public void updateLogisticsVehicleSupply(long vehicleSupplyId, boolean isNew, String freeVehicleNumbers, String departureAreaIds, String departureAreas, String destinationAreaIds, String destinationAreas) throws ServiceException;

	/**
	 * 更新车辆位置,以便用户查询,由定时器调用
	 *
	 */
	public void updateVechicleLocation();
}