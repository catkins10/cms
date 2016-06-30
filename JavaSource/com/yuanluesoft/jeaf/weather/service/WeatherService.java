package com.yuanluesoft.jeaf.weather.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;

/**
 * 
 * @author linchuan
 *
 */
public interface WeatherService extends BusinessService {

	/**
	 * 更新天气,更新范围包括系统注册过的地区,以及用户自定义的地区,多服务器时必须将更新的时间错开,避免冲突
	 * @throws ServiceException
	 */
	public void updateWeather() throws ServiceException;
	
	/**
	 * 获取指定地区的天气列表
	 * @param areas 地区列表
	 * @param days 预报天数
	 * @param updateIfNecessary 是否在需要的时候更新数据
	 * @return
	 * @throws ServiceException
	 */
	public List retrieveWeathers(String areas, int forecastDays, boolean updateIfNecessary)throws ServiceException;
	
	/**
	 * 获取天气图标
	 * @param weatherDescribe
	 * @param day
	 * @param iconSize
	 * @return
	 * @throws ServiceException
	 */
	public Image getWeatherIcon(String weatherDescribe, boolean day, int iconSize) throws ServiceException;
}