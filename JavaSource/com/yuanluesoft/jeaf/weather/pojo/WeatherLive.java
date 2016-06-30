package com.yuanluesoft.jeaf.weather.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.weather.model.AirQuality;
import com.yuanluesoft.jeaf.weather.utils.WeatherUtils;

/**
 * 天气：天气实况(weather_live)
 * @author linchuan
 *
 */
public class WeatherLive extends Record {
	private String area; //地区
	private Timestamp updateTime; //更新时间
	private String source; //来源,如：中央气象台
	private String describe; //天气概况,如：晴，中雨
	private double temperature; //气温
	private String feelst; //体感温度
	private String feelstTip; //体感温度描述
	private String airpressure; //气压
	private String airpressureTip; //气压描述
	private String rain; //降水
	private String windDirection; //风向
	private String windPower; //风力
	private String windSpeed; //风速
	private String humidity; //湿度
	private String humidityTip; //湿度描述
	private String comfort; //舒适度
	private String comfortTip; //舒适度描述
	private String airQuality; //空气质量
	
	/**
	 * 获取空气质量
	 * @return
	 */
	public AirQuality getAirQualityModel() {
		return WeatherUtils.getAirQuality(airQuality);
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
		return WeatherUtils.resetWindSpeed(windSpeed);
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