package com.yuanluesoft.jeaf.gps.provider;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;

/**
 * GPS服务
 * @author linchuan
 *
 */
public interface GpsProvider {

	/**
	 * 根据GPS终端号码获取所在位置
	 * @param gpsTerminalNumber GPS终端号码,如：手机号码
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocation(String gpsTerminalNumber) throws ServiceException;
	
	/**
	 * 根据IP获取所在位置
	 * @param ip
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocationByIP(String ip) throws ServiceException;
}