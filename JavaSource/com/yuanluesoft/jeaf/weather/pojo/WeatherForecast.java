package com.yuanluesoft.jeaf.weather.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.weather.model.AirQuality;
import com.yuanluesoft.jeaf.weather.utils.WeatherUtils;

/**
 * 天气：预报(weather_forecast)
 * @author linchuan
 *
 */
public class WeatherForecast extends Record {
	private String area; //地区
	private Timestamp updateTime; //更新时间
	private String source; //来源,如：中央气象台
	private Date forecastDate; //预报日期
	private String dayDescribe; //白天天气概况
	private String nightDescribe; //夜晚天气概况,如：晴，中雨
	private double dayTemperature; //白天气温
	private double nightTemperature; //夜晚气温
	private String dayWindDirection; //白天风向
	private String nightWindDirection; //夜晚风向
	private String dayWindPower; //白天风力
	private String nightWindPower; //夜晚风力
	private String dayWindSpeed; //白天风速
	private String nightWindSpeed; //夜晚风速
	private String dayAirQuality; //白天空气质量
	private String nightAirQuality; //夜晚空气质量
	private Set hourForecasts; //整点预报
	
	/**
	 * 获取全天天气概况
	 * @return
	 */
	public String getDescribe() {
		return dayDescribe.equals(nightDescribe) ? dayDescribe : dayDescribe + "转" + nightDescribe;
	}
	
	/**
	 * 获取全天风向
	 * @return
	 */
	public String getWindDirection() {
		if(dayWindDirection==null) {
			return nightWindDirection;
		}
		else if(nightWindDirection==null) {
			return dayWindDirection;
		}
		return dayWindDirection.equals(nightWindDirection) ? dayWindDirection : dayWindDirection + "转" + nightWindDirection;
	}
	
	/**
	 * 获取全天风力
	 * @return
	 */
	public String getWindPower() {
		if(dayWindPower==null) {
			return nightWindPower;
		}
		else if(nightWindPower==null) {
			return dayWindPower;
		}
		return dayWindPower.equals(nightWindPower) ? dayWindPower : dayWindPower + "转" + nightWindPower;
	}
	
	/**
	 * 获取全天空气质量
	 * @return
	 */
	public AirQuality getAirQualityModel() {
		return WeatherUtils.getAirQuality(dayAirQuality==null ? nightAirQuality : dayAirQuality);
	}

	/**
	 * 获取白天空气质量
	 * @return
	 */
	public AirQuality getDayAirQualityModel() {
		return WeatherUtils.getAirQuality(dayAirQuality);
	}
	
	/**
	 * 获取夜晚空气质量
	 * @return
	 */
	public AirQuality getNightAirQualityModel() {
		return WeatherUtils.getAirQuality(nightAirQuality);
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
	 * @return the dayAirQuality
	 */
	public String getDayAirQuality() {
		return dayAirQuality;
	}
	/**
	 * @param dayAirQuality the dayAirQuality to set
	 */
	public void setDayAirQuality(String dayAirQuality) {
		this.dayAirQuality = dayAirQuality;
	}
	/**
	 * @return the dayDescribe
	 */
	public String getDayDescribe() {
		return dayDescribe;
	}
	/**
	 * @param dayDescribe the dayDescribe to set
	 */
	public void setDayDescribe(String dayDescribe) {
		this.dayDescribe = dayDescribe;
	}
	/**
	 * @return the dayTemperature
	 */
	public double getDayTemperature() {
		return dayTemperature;
	}
	/**
	 * @param dayTemperature the dayTemperature to set
	 */
	public void setDayTemperature(double dayTemperature) {
		this.dayTemperature = dayTemperature;
	}
	/**
	 * @return the dayWindPower
	 */
	public String getDayWindPower() {
		return dayWindPower;
	}
	/**
	 * @param dayWindPower the dayWindPower to set
	 */
	public void setDayWindPower(String dayWindPower) {
		this.dayWindPower = dayWindPower;
	}
	/**
	 * @return the dayWindSpeed
	 */
	public String getDayWindSpeed() {
		return WeatherUtils.resetWindSpeed(dayWindSpeed);
	}
	/**
	 * @param dayWindSpeed the dayWindSpeed to set
	 */
	public void setDayWindSpeed(String dayWindSpeed) {
		this.dayWindSpeed = dayWindSpeed;
	}
	/**
	 * @return the forecastDate
	 */
	public Date getForecastDate() {
		return forecastDate;
	}
	/**
	 * @param forecastDate the forecastDate to set
	 */
	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}
	/**
	 * @return the nightAirQuality
	 */
	public String getNightAirQuality() {
		return nightAirQuality;
	}
	/**
	 * @param nightAirQuality the nightAirQuality to set
	 */
	public void setNightAirQuality(String nightAirQuality) {
		this.nightAirQuality = nightAirQuality;
	}
	/**
	 * @return the nightDescribe
	 */
	public String getNightDescribe() {
		return nightDescribe;
	}
	/**
	 * @param nightDescribe the nightDescribe to set
	 */
	public void setNightDescribe(String nightDescribe) {
		this.nightDescribe = nightDescribe;
	}
	/**
	 * @return the nightTemperature
	 */
	public double getNightTemperature() {
		return nightTemperature;
	}
	/**
	 * @param nightTemperature the nightTemperature to set
	 */
	public void setNightTemperature(double nightTemperature) {
		this.nightTemperature = nightTemperature;
	}
	/**
	 * @return the nightWindPower
	 */
	public String getNightWindPower() {
		return nightWindPower;
	}
	/**
	 * @param nightWindPower the nightWindPower to set
	 */
	public void setNightWindPower(String nightWindPower) {
		this.nightWindPower = nightWindPower;
	}
	/**
	 * @return the nightWindSpeed
	 */
	public String getNightWindSpeed() {
		return WeatherUtils.resetWindSpeed(nightWindSpeed);
	}
	/**
	 * @param nightWindSpeed the nightWindSpeed to set
	 */
	public void setNightWindSpeed(String nightWindSpeed) {
		this.nightWindSpeed = nightWindSpeed;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the hourForecasts
	 */
	public Set getHourForecasts() {
		return hourForecasts;
	}
	/**
	 * @param hourForecasts the hourForecasts to set
	 */
	public void setHourForecasts(Set hourForecasts) {
		this.hourForecasts = hourForecasts;
	}
	/**
	 * @return the dayWindDirection
	 */
	public String getDayWindDirection() {
		return dayWindDirection;
	}
	/**
	 * @param dayWindDirection the dayWindDirection to set
	 */
	public void setDayWindDirection(String dayWindDirection) {
		this.dayWindDirection = dayWindDirection;
	}
	/**
	 * @return the nightWindDirection
	 */
	public String getNightWindDirection() {
		return nightWindDirection;
	}
	/**
	 * @param nightWindDirection the nightWindDirection to set
	 */
	public void setNightWindDirection(String nightWindDirection) {
		this.nightWindDirection = nightWindDirection;
	}
}