package com.yuanluesoft.traffic.busline.service;

import java.sql.Date;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.traffic.busline.pojo.BusLine;

/**
 * 
 * @author linchuan
 *
 */
public interface BusLineService extends BusinessService {

	/**
	 * 按名称获取公交线路
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	public BusLine getBusLineByName(String name) throws ServiceException;
	
	/**
	 * 根据临时变更通知重设公交线路站点,并返回变更截止时间
	 * @param busLine
	 * @return
	 * @throws ServiceException
	 */
	public Date resetBusLineByInterimChange(BusLine busLine) throws ServiceException;
	
	/**
	 * 更新站点列表
	 * @param busLine
	 * @param downlinkStationNames
	 * @param uplinkStationNames
	 * @throws ServiceException
	 */
	public void updateBusLineStations(BusLine busLine, String downlinkStationNames, String uplinkStationNames) throws ServiceException;
}