package com.yuanluesoft.jeaf.weather.model;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.weather.pojo.WeatherForecast;
import com.yuanluesoft.jeaf.weather.pojo.WeatherLive;

/**
 * 天气
 * @author linchuan
 *
 */
public class Weather {
	private String area; //地区
	private WeatherLive weatherLive; //天气实况
	private List forecasts; //天气预报列表
	
	/**
	 * 获取天气预报更新时间
	 * @return
	 */
	public Timestamp getForecastUpdateTime() {
		return forecasts==null || forecasts.isEmpty() ? null : ((WeatherForecast)forecasts.get(0)).getUpdateTime();
	}
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the forecasts
	 */
	public List getForecasts() {
		return forecasts;
	}
	/**
	 * @param forecasts the forecasts to set
	 */
	public void setForecasts(List forecasts) {
		this.forecasts = forecasts;
	}
	/**
	 * @return the weatherLive
	 */
	public WeatherLive getWeatherLive() {
		return weatherLive;
	}
	/**
	 * @param weatherLive the weatherLive to set
	 */
	public void setWeatherLive(WeatherLive weatherLive) {
		this.weatherLive = weatherLive;
	}
}