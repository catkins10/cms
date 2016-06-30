package com.yuanluesoft.jeaf.weather.spider;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.weather.model.Weather;

/**
 * 天气预报数据服务,由系统定时调用,更新天气数据
 * @author linchuan
 *
 */
public interface WeatherSpider {
	
	/**
	 * 获取站点描述信息,如:中央气象台
	 * @return
	 */
	public String getDescription();

	/**
	 * 获取天气预报站点名称
	 * @return
	 */
	public String getSiteName();
	
	/**
	 * 根据地名获取天气数据
	 * @param areaName
	 * @return
	 * @throws ServiceException
	 */
	public Weather getWeatherData(String areaName) throws ServiceException;
}