package com.yuanluesoft.jeaf.weather.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.weather.model.AirQuality;
import com.yuanluesoft.jeaf.weather.utils.WeatherUtils;

/**
 * 天气：精细预报(weather_hour_forecast)
 * @author linchuan
 *
 */
public class WeatherHourForecast extends Record {
	private long forecastId; //预报ID
	private Timestamp forecastTime; //时间
	private String describe; //天气概况
	private double temperature; //气温
	private String feelst; //体感温度
	private String feelstTip; //体感温度描述
	private String rain; //降水
	private String windDirection; //风向
	private String windPower; //风力
	private String windSpeed; //风速
	private String airpressure; //气压
	private String airpressureTip; //气压描述
	private String humidity; //湿度
	private String humidityTip; //湿度描述
	private String cloud; //云量
	private String visibility; //能见度
	private String airQuality; //空气质量

	/**
	 * 获取空气质量
	 * @return
	 */
	public AirQuality getAirQualityModel() {
		return WeatherUtils.getAirQuality(airQuality);
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
	 * @return the cloud
	 */
	public String getCloud() {
		return cloud;
	}
	/**
	 * @param cloud the cloud to set
	 */
	public void setCloud(String cloud) {
		this.cloud = cloud;
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
	 * @return the forecastId
	 */
	public long getForecastId() {
		return forecastId;
	}
	/**
	 * @param forecastId the forecastId to set
	 */
	public void setForecastId(long forecastId) {
		this.forecastId = forecastId;
	}
	/**
	 * @return the forecastTime
	 */
	public Timestamp getForecastTime() {
		return forecastTime;
	}
	/**
	 * @param forecastTime the forecastTime to set
	 */
	public void setForecastTime(Timestamp forecastTime) {
		this.forecastTime = forecastTime;
	}
	/**
	 * @return the humidity
	 */
	public String getHumidity() {
		return WeatherUtils.resetHumidity(humidity);
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(String humidity) {
		this.humidity = humidity;
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
	 * @return the rain
	 */
	public String getRain() {
		return WeatherUtils.resetRain(rain);
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
	 * @return the visibility
	 */
	public String getVisibility() {
		return visibility;
	}
	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
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
		return WeatherUtils.resetWindSpeed(windSpeed);
	}
	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
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
}