package com.yuanluesoft.jeaf.gps.service;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.model.PlaceName;

/**
 * 定位服务
 * @author linchuan
 *
 */
public interface GpsService {
	
	/**
	 * GPS定位
	 * @param gpsTerminalNumber
	 * @param placeNameRequired 地名是否必须
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocation(String gpsTerminalNumber, boolean placeNameRequired) throws ServiceException;
	
	/**
	 * 按IP获取位置信息
	 * @param ip
	 * @param placeNameRequired
	 * @param coordinateRequired
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocationByIP(String ip, boolean placeNameRequired, boolean coordinateRequired) throws ServiceException;
	

	/**
	 * 获取地名
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws ServiceException
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException;
	
	/**
	 * 获取经纬度
	 * @param placeName
	 * @return
	 * @throws ServiceException
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException;
	
	/**
	 * 解析地名
	 * @param placeNameText
	 * @return
	 */
	public PlaceName parsePlaceName(String placeNameText);
	
	/**
	 * 获取外部显示的地图供应商名称
	 * @return
	 */
	public String getMapProviderName();
}