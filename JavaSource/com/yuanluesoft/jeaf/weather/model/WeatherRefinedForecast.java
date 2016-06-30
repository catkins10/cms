package com.yuanluesoft.jeaf.weather.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 天气预报抓取
 * @author linchuan
 *
 */
public class WeatherRefinedForecast {
	private List hourForecasts; //整点预报列表
	
	private Timestamp[] hourForecastTimes; //整点预报时间列表
	private Date[] hourForecastDates; //整点预报日期列表
	private Timestamp[] hourForecastDateTimes; //整点预报日期和时间列表
	private String[] hourForecastDescribes; //整点预报天气概况列表
	private double[] hourForecastTemperatures; //整点预报气温列表
	private String[] hourForecastFeelsts; //整点预报体感温度列表
	private String[] hourForecastFeelstTips; //整点预报体感温度描述列表
	private String[] hourForecastRains; //整点预报降水列表
	private String[] hourForecastWindDirections; //整点预报风向列表
	private String[] hourForecastWindPowers; //整点预报风力列表
	private String[] hourForecastWindSpeeds; //整点预报风速列表
	private String[] hourForecastAirpressures; //整点预报气压列表
	private String[] hourForecastAirpressureTips; //整点预报气压描述列表
	private String[] hourForecastHumidities; //整点预报湿度列表
	private String[] hourForecastHumidityTips; //整点预报湿度描述列表
	private String[] hourForecastClouds; //整点预报云量列表
	private String[] hourForecastVisibilities; //整点预报能见度列表
	private String[] hourForecastAirQualities; //整点预报空气质量列表
	
	/**
	 * @return the hourForecastAirpressures
	 */
	public String[] getHourForecastAirpressures() {
		return hourForecastAirpressures;
	}
	/**
	 * @param hourForecastAirpressures the hourForecastAirpressures to set
	 */
	public void setHourForecastAirpressures(String[] hourForecastAirpressures) {
		this.hourForecastAirpressures = hourForecastAirpressures;
	}
	/**
	 * @return the hourForecastAirpressureTips
	 */
	public String[] getHourForecastAirpressureTips() {
		return hourForecastAirpressureTips;
	}
	/**
	 * @param hourForecastAirpressureTips the hourForecastAirpressureTips to set
	 */
	public void setHourForecastAirpressureTips(String[] hourForecastAirpressureTips) {
		this.hourForecastAirpressureTips = hourForecastAirpressureTips;
	}
	/**
	 * @return the hourForecastAirQualities
	 */
	public String[] getHourForecastAirQualities() {
		return hourForecastAirQualities;
	}
	/**
	 * @param hourForecastAirQualities the hourForecastAirQualities to set
	 */
	public void setHourForecastAirQualities(String[] hourForecastAirQualities) {
		this.hourForecastAirQualities = hourForecastAirQualities;
	}
	/**
	 * @return the hourForecastClouds
	 */
	public String[] getHourForecastClouds() {
		return hourForecastClouds;
	}
	/**
	 * @param hourForecastClouds the hourForecastClouds to set
	 */
	public void setHourForecastClouds(String[] hourForecastClouds) {
		this.hourForecastClouds = hourForecastClouds;
	}
	/**
	 * @return the hourForecastDescribes
	 */
	public String[] getHourForecastDescribes() {
		return hourForecastDescribes;
	}
	/**
	 * @param hourForecastDescribes the hourForecastDescribes to set
	 */
	public void setHourForecastDescribes(String[] hourForecastDescribes) {
		this.hourForecastDescribes = hourForecastDescribes;
	}
	/**
	 * @return the hourForecastFeelsts
	 */
	public String[] getHourForecastFeelsts() {
		return hourForecastFeelsts;
	}
	/**
	 * @param hourForecastFeelsts the hourForecastFeelsts to set
	 */
	public void setHourForecastFeelsts(String[] hourForecastFeelsts) {
		this.hourForecastFeelsts = hourForecastFeelsts;
	}
	/**
	 * @return the hourForecastFeelstTips
	 */
	public String[] getHourForecastFeelstTips() {
		return hourForecastFeelstTips;
	}
	/**
	 * @param hourForecastFeelstTips the hourForecastFeelstTips to set
	 */
	public void setHourForecastFeelstTips(String[] hourForecastFeelstTips) {
		this.hourForecastFeelstTips = hourForecastFeelstTips;
	}
	/**
	 * @return the hourForecastHumidities
	 */
	public String[] getHourForecastHumidities() {
		return hourForecastHumidities;
	}
	/**
	 * @param hourForecastHumidities the hourForecastHumidities to set
	 */
	public void setHourForecastHumidities(String[] hourForecastHumidities) {
		this.hourForecastHumidities = hourForecastHumidities;
	}
	/**
	 * @return the hourForecastHumidityTips
	 */
	public String[] getHourForecastHumidityTips() {
		return hourForecastHumidityTips;
	}
	/**
	 * @param hourForecastHumidityTips the hourForecastHumidityTips to set
	 */
	public void setHourForecastHumidityTips(String[] hourForecastHumidityTips) {
		this.hourForecastHumidityTips = hourForecastHumidityTips;
	}
	/**
	 * @return the hourForecastRains
	 */
	public String[] getHourForecastRains() {
		return hourForecastRains;
	}
	/**
	 * @param hourForecastRains the hourForecastRains to set
	 */
	public void setHourForecastRains(String[] hourForecastRains) {
		this.hourForecastRains = hourForecastRains;
	}
	/**
	 * @return the hourForecasts
	 */
	public List getHourForecasts() {
		return hourForecasts;
	}
	/**
	 * @param hourForecasts the hourForecasts to set
	 */
	public void setHourForecasts(List hourForecasts) {
		this.hourForecasts = hourForecasts;
	}
	/**
	 * @return the hourForecastTemperatures
	 */
	public double[] getHourForecastTemperatures() {
		return hourForecastTemperatures;
	}
	/**
	 * @param hourForecastTemperatures the hourForecastTemperatures to set
	 */
	public void setHourForecastTemperatures(double[] hourForecastTemperatures) {
		this.hourForecastTemperatures = hourForecastTemperatures;
	}
	/**
	 * @return the hourForecastTimes
	 */
	public Timestamp[] getHourForecastTimes() {
		return hourForecastTimes;
	}
	/**
	 * @param hourForecastTimes the hourForecastTimes to set
	 */
	public void setHourForecastTimes(Timestamp[] hourForecastTimes) {
		this.hourForecastTimes = hourForecastTimes;
	}
	/**
	 * @return the hourForecastVisibilities
	 */
	public String[] getHourForecastVisibilities() {
		return hourForecastVisibilities;
	}
	/**
	 * @param hourForecastVisibilities the hourForecastVisibilities to set
	 */
	public void setHourForecastVisibilities(String[] hourForecastVisibilities) {
		this.hourForecastVisibilities = hourForecastVisibilities;
	}
	/**
	 * @return the hourForecastWindDirections
	 */
	public String[] getHourForecastWindDirections() {
		return hourForecastWindDirections;
	}
	/**
	 * @param hourForecastWindDirections the hourForecastWindDirections to set
	 */
	public void setHourForecastWindDirections(String[] hourForecastWindDirections) {
		this.hourForecastWindDirections = hourForecastWindDirections;
	}
	/**
	 * @return the hourForecastWindPowers
	 */
	public String[] getHourForecastWindPowers() {
		return hourForecastWindPowers;
	}
	/**
	 * @param hourForecastWindPowers the hourForecastWindPowers to set
	 */
	public void setHourForecastWindPowers(String[] hourForecastWindPowers) {
		this.hourForecastWindPowers = hourForecastWindPowers;
	}
	/**
	 * @return the hourForecastWindSpeeds
	 */
	public String[] getHourForecastWindSpeeds() {
		return hourForecastWindSpeeds;
	}
	/**
	 * @param hourForecastWindSpeeds the hourForecastWindSpeeds to set
	 */
	public void setHourForecastWindSpeeds(String[] hourForecastWindSpeeds) {
		this.hourForecastWindSpeeds = hourForecastWindSpeeds;
	}
	/**
	 * @return the hourForecastDates
	 */
	public Date[] getHourForecastDates() {
		return hourForecastDates;
	}
	/**
	 * @param hourForecastDates the hourForecastDates to set
	 */
	public void setHourForecastDates(Date[] hourForecastDates) {
		this.hourForecastDates = hourForecastDates;
	}
	/**
	 * @return the hourForecastDateTimes
	 */
	public Timestamp[] getHourForecastDateTimes() {
		return hourForecastDateTimes;
	}
	/**
	 * @param hourForecastDateTimes the hourForecastDateTimes to set
	 */
	public void setHourForecastDateTimes(Timestamp[] hourForecastDateTimes) {
		this.hourForecastDateTimes = hourForecastDateTimes;
	}
}