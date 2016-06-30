package com.yuanluesoft.jeaf.weather.model;

import java.sql.Date;


/**
 * 天气预报抓取
 * @author linchuan
 *
 */
public class WeatherForecast {
	private Date forecastDate; //预报时间
	private int forecastDay; //日期,1~31
	
	private String describe; //天气情况(全天)
	private double lowTemperature = Double.MIN_VALUE; //最低温度(全天)
	private double highTemperature = Double.MIN_VALUE; //最高温度(全天)
	private String windDirection; //风向(全天)
	private String windPower; //风力(全天)
	private String windSpeed; //风速(全天)
	private String airQuality; //空气质量(全天)
	
	private String dayDescribe; //天气情况(白天)
	private double dayTemperature = Double.MIN_VALUE; //温度(白天)
	private String dayWindDirection; //风向(白天)
	private String dayWindPower; //风力(白天)
	private String dayWindSpeed; //风速(白天)
	private String dayAirQuality; //空气质量(白天)
	
	private String nightDescribe; //天气情况(夜晚)
	private double nightTemperature = Double.MIN_VALUE; //温度(夜晚)
	private String nightWindDirection; //风向(夜晚)
	private String nightWindPower; //风力(夜晚)
	private String nightWindSpeed; //风速(夜晚)
	private String nightAirQuality; //空气质量(夜晚)
	
	/**
	 * @return the airQuality
	 */
	public String getAirQuality() {
		return airQuality;
	}

	/**
	 * @param airQuality the airQuality to set
	 */
	public void setAirQuality(String airQuality) {
		this.airQuality = airQuality;
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
		return dayWindSpeed;
	}

	/**
	 * @param dayWindSpeed the dayWindSpeed to set
	 */
	public void setDayWindSpeed(String dayWindSpeed) {
		this.dayWindSpeed = dayWindSpeed;
	}

	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}

	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}

	/**
	 * @return the highTemperature
	 */
	public double getHighTemperature() {
		return highTemperature;
	}

	/**
	 * @param highTemperature the highTemperature to set
	 */
	public void setHighTemperature(double highTemperature) {
		this.highTemperature = highTemperature;
	}

	/**
	 * @return the lowTemperature
	 */
	public double getLowTemperature() {
		return lowTemperature;
	}

	/**
	 * @param lowTemperature the lowTemperature to set
	 */
	public void setLowTemperature(double lowTemperature) {
		this.lowTemperature = lowTemperature;
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
		return nightWindSpeed;
	}

	/**
	 * @param nightWindSpeed the nightWindSpeed to set
	 */
	public void setNightWindSpeed(String nightWindSpeed) {
		this.nightWindSpeed = nightWindSpeed;
	}

	/**
	 * @return the windDirection
	 */
	public String getWindDirection() {
		return windDirection;
	}

	/**
	 * @param windDirection the windDirection to set
	 */
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * @return the windPower
	 */
	public String getWindPower() {
		return windPower;
	}

	/**
	 * @param windPower the windPower to set
	 */
	public void setWindPower(String windPower) {
		this.windPower = windPower;
	}

	/**
	 * @return the windSpeed
	 */
	public String getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
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
	 * @return the forecastDay
	 */
	public int getForecastDay() {
		return forecastDay;
	}

	/**
	 * @param forecastDay the forecastDay to set
	 */
	public void setForecastDay(int forecastDay) {
		this.forecastDay = forecastDay;
	}	
}