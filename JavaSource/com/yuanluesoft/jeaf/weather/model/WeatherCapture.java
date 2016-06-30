package com.yuanluesoft.jeaf.weather.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * 天气预报抓取
 * @author linchuan
 *
 */
public class WeatherCapture {
	private Timestamp updateTime; //更新时间
	private String describe; //实时天气情况
	private double temperature = Double.MIN_VALUE; //实时气温
	private String feelst; //实时体感温度
	private String feelstTip; //实时体感温度描述
	private String airpressure; //实时气压
	private String airpressureTip; //实时气压描述
	private String rain; //实时降水
	private String windDirection; //实时风向
	private String windPower; //实时风力
	private String windSpeed; //实时风速
	private String humidity; //实时湿度
	private String humidityTip; //实时湿度描述
	private String comfort; //实时舒适度
	private String comfortTip; //实时舒适度描述
	private String airQuality; //实时空气质量
	private Timestamp forecastUpdateTime; //天气预报更新时间
	private List forecasts; //天气预报列表
	private List refinedForecasts; //天气精细预报列表
	
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
	 * @return the comfort
	 */
	public String getComfort() {
		return comfort;
	}
	/**
	 * @param comfort the comfort to set
	 */
	public void setComfort(String comfort) {
		this.comfort = comfort;
	}
	/**
	 * @return the feelst
	 */
	public String getFeelst() {
		return feelst;
	}
	/**
	 * @param feelst the feelst to set
	 */
	public void setFeelst(String feelst) {
		this.feelst = feelst;
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
	 * @return the humidity
	 */
	public String getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	/**
	 * @return the rain
	 */
	public String getRain() {
		return rain;
	}
	/**
	 * @param rain the rain to set
	 */
	public void setRain(String rain) {
		this.rain = rain;
	}
	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
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
	 * @return the refinedForecasts
	 */
	public List getRefinedForecasts() {
		return refinedForecasts;
	}
	/**
	 * @param refinedForecasts the refinedForecasts to set
	 */
	public void setRefinedForecasts(List refinedForecasts) {
		this.refinedForecasts = refinedForecasts;
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
	 * @return the airpressure
	 */
	public String getAirpressure() {
		return airpressure;
	}
	/**
	 * @param airpressure the airpressure to set
	 */
	public void setAirpressure(String airpressure) {
		this.airpressure = airpressure;
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
	 * @return the airpressureTip
	 */
	public String getAirpressureTip() {
		return airpressureTip;
	}
	/**
	 * @param airpressureTip the airpressureTip to set
	 */
	public void setAirpressureTip(String airpressureTip) {
		this.airpressureTip = airpressureTip;
	}
	/**
	 * @return the comfortTip
	 */
	public String getComfortTip() {
		return comfortTip;
	}
	/**
	 * @param comfortTip the comfortTip to set
	 */
	public void setComfortTip(String comfortTip) {
		this.comfortTip = comfortTip;
	}
	/**
	 * @return the feelstTip
	 */
	public String getFeelstTip() {
		return feelstTip;
	}
	/**
	 * @param feelstTip the feelstTip to set
	 */
	public void setFeelstTip(String feelstTip) {
		this.feelstTip = feelstTip;
	}
	/**
	 * @return the humidityTip
	 */
	public String getHumidityTip() {
		return humidityTip;
	}
	/**
	 * @param humidityTip the humidityTip to set
	 */
	public void setHumidityTip(String humidityTip) {
		this.humidityTip = humidityTip;
	}
	/**
	 * @return the forecastUpdateTime
	 */
	public Timestamp getForecastUpdateTime() {
		return forecastUpdateTime;
	}
	/**
	 * @param forecastUpdateTime the forecastUpdateTime to set
	 */
	public void setForecastUpdateTime(Timestamp forecastUpdateTime) {
		this.forecastUpdateTime = forecastUpdateTime;
	}
}