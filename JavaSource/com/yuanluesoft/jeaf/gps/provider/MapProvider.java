package com.yuanluesoft.jeaf.gps.provider;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;

/**
 * 地图服务
 * @author linchuan
 *
 */
public interface MapProvider {

	/**
	 * 按经纬度获取地名
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws ServiceException
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException;
	
	/**
	 * 根据地址返回经纬度
	 * @param placeName
	 * @return
	 * @throws ServiceException
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException;
}