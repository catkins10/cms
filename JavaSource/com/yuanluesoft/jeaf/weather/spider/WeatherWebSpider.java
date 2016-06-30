package com.yuanluesoft.jeaf.weather.spider;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.capture.model.CapturedField;
import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.model.CapturedRecordList;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.weather.model.Weather;
import com.yuanluesoft.jeaf.weather.model.WeatherCapture;
import com.yuanluesoft.jeaf.weather.model.WeatherForecast;
import com.yuanluesoft.jeaf.weather.model.WeatherHourForecast;
import com.yuanluesoft.jeaf.weather.model.WeatherRefinedForecast;
import com.yuanluesoft.jeaf.weather.pojo.WeatherLive;

/**
 * 天气预报数据服务: 网页抓取方式
 * @author linchuan
 *
 */
public abstract class WeatherWebSpider implements WeatherSpider {
	private CaptureService captureService; //网页抓取服务
	private List weatherCaptureTasks; //抓取规则设置
	private Cache cache; //缓存,存放天气预报URL
	
	/**
	 * 取天气URL,返回MAP KYE:抓取任务ID,值:天气URL
	 * @param captureUrl
	 * @param areaName
	 * @return
	 * @throws ServiceException
	 */
	protected abstract Map getWeatherUrl(List weatherCaptureTasks, String areaName) throws Exception;
	
	/**
	 * 重置天气实况记录
	 * @param weatherLive
	 */
	protected void resetWeatherLive(WeatherLive weatherLive) {
		
	}
	
	/**
	 * 重置天气预报记录
	 * @param weatherLive
	 */
	protected void resetWeatherForecast(com.yuanluesoft.jeaf.weather.pojo.WeatherForecast weatherForecast) {
		
	}
	
	/**
	 * 重置整点预报记录
	 * @param weatherLive
	 */
	protected void resetWeatherHourForecast(com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast weatherHourForecast) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getWeatherData(java.lang.String)
	 */
	public Weather getWeatherData(String areaName) throws ServiceException {
		//获取URL
		Map urls = null;
		try {
			urls = (Map)cache.get(this.hashCode() + "/" + areaName); //从缓存获取
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		if(urls==null) {
			try {
				urls = getWeatherUrl(weatherCaptureTasks, areaName);
				cache.put(this.hashCode() + "/" + areaName, urls);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		Timestamp captureTime = DateTimeUtils.now();
		WeatherCapture weatherCapture = null;
		for(Iterator iterator = weatherCaptureTasks.iterator(); iterator.hasNext();) {
			CmsCaptureTask captureTask = (CmsCaptureTask)iterator.next();
			String weatherUrl = urls==null ? null : (String)urls.get(new Long(captureTask.getId()));
			if(weatherUrl==null) {
				return null;
			}
			//抓取页面
			CapturedRecordList capturedRecordList = captureService.captureListPage(captureTask, weatherUrl, 0);
			if(capturedRecordList==null || capturedRecordList.getRecords()==null || capturedRecordList.getRecords().isEmpty()) {
				return null;
			}
			CapturedRecord capturedRecord = (CapturedRecord)capturedRecordList.getRecords().get(0);
			if(weatherCapture==null) {
				weatherCapture = (WeatherCapture)capturedRecord.getRecord();
				continue;
			}
			//合并抓取到的数据
			for(Iterator iteratorField = capturedRecord.getFields()==null ? null : capturedRecord.getFields().iterator(); iteratorField!=null && iteratorField.hasNext();) {
				CapturedField capturedField = (CapturedField)iteratorField.next();
				try {
					PropertyUtils.setProperty(weatherCapture, capturedField.getFieldName(), PropertyUtils.getProperty(capturedRecord.getRecord(), capturedField.getFieldName()));
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		Weather weather = new Weather();
		//天气实况
		if(weatherCapture.getTemperature()!=Double.MIN_VALUE) { //气温有效
			weather.setWeatherLive(new WeatherLive());
			BeanUtils.copyProperties(weatherCapture, weather.getWeatherLive());
			resetWeatherLive(weather.getWeatherLive());
		}
		//获取小时预报列表
		List hourForecasts = listHourForecasts(weatherCapture, captureTime);
		//设置天气预报列表
		Date today = DateTimeUtils.date();
		Date lastForecastDate = null;
		for(Iterator iterator = weatherCapture.getForecasts()==null ? null : weatherCapture.getForecasts().iterator(); iterator!=null && iterator.hasNext();) {
			WeatherForecast capturedForecast = (WeatherForecast)iterator.next();
			com.yuanluesoft.jeaf.weather.pojo.WeatherForecast forecast = new com.yuanluesoft.jeaf.weather.pojo.WeatherForecast();
			forecast.setUpdateTime(weatherCapture.getForecastUpdateTime()); //更新时间
			//预报日期
			if(capturedForecast.getForecastDate()!=null) {
				capturedForecast.setForecastDay(DateTimeUtils.getDay(capturedForecast.getForecastDate()));
			}
			if(capturedForecast.getForecastDay() < 1 || capturedForecast.getForecastDay() > 31) {
				continue;
			}
			if(capturedForecast.getForecastDay() >= DateTimeUtils.getDay(today)) {
				forecast.setForecastDate(DateTimeUtils.set(today, Calendar.DAY_OF_MONTH, capturedForecast.getForecastDay()));
			}
			else if(capturedForecast.getForecastDay() > weatherCapture.getForecasts().size()) {
				continue;
			}
			else {
				forecast.setForecastDate(DateTimeUtils.add(DateTimeUtils.set(today, Calendar.DAY_OF_MONTH, capturedForecast.getForecastDay()), Calendar.MONTH, 1)); //加一个月
			}
			if(lastForecastDate!=null && !forecast.getForecastDate().equals(DateTimeUtils.add(lastForecastDate, Calendar.DAY_OF_MONTH, 1))) { //时间不连续
				weather.setForecasts(null);
				break;
			}
			lastForecastDate = forecast.getForecastDate();
        	//气温
			if(capturedForecast.getLowTemperature()!=Double.MIN_VALUE && capturedForecast.getLowTemperature()!=capturedForecast.getHighTemperature()) { //全天
				forecast.setDayTemperature(Math.max(capturedForecast.getLowTemperature(), capturedForecast.getHighTemperature()));
				forecast.setNightTemperature(Math.min(capturedForecast.getLowTemperature(), capturedForecast.getHighTemperature()));
			}
			else if(capturedForecast.getDayTemperature()==Float.MIN_VALUE || capturedForecast.getNightTemperature()==Float.MIN_VALUE) {
				continue;
			}
			else {
				forecast.setDayTemperature(Math.max(capturedForecast.getDayTemperature(), capturedForecast.getNightTemperature()));
				forecast.setNightTemperature(Math.min(capturedForecast.getDayTemperature(), capturedForecast.getNightTemperature()));
			}
			setForecastProperty(forecast, "describe", capturedForecast, "describe"); //天气概况
			if(forecast.getDayDescribe()==null || forecast.getDayDescribe().isEmpty() || forecast.getNightDescribe()==null || forecast.getNightDescribe().isEmpty()) {
				continue;
			}
			setForecastProperty(forecast, "windDirection", capturedForecast, "windDirection"); //风向
			setForecastProperty(forecast, "windPower", capturedForecast, "windPower"); //风力
			setForecastProperty(forecast, "windSpeed", capturedForecast, "windSpeed"); //风速
			setForecastProperty(forecast, "airQuality", capturedForecast, "airQuality"); //空气质量
			//设置小时预报列表
			forecast.setHourForecasts(new LinkedHashSet());
			for(Iterator iteratorHour = hourForecasts==null ? null : hourForecasts.iterator(); iteratorHour!=null && iteratorHour.hasNext();) {
				com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast hourForecast = (com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast)iteratorHour.next();
				if(DateTimeUtils.getYear(hourForecast.getForecastTime())==DateTimeUtils.getYear(forecast.getForecastDate()) &&
				   DateTimeUtils.getMonth(hourForecast.getForecastTime())==DateTimeUtils.getMonth(forecast.getForecastDate()) &&
				   DateTimeUtils.getDay(hourForecast.getForecastTime())==DateTimeUtils.getDay(forecast.getForecastDate())) {
					resetWeatherHourForecast(hourForecast);
					if(hourForecast.getTemperature()!=Double.MIN_VALUE) {
						forecast.getHourForecasts().add(hourForecast);
					}
				}
			}
			//添加到天气预报列表
			if(weather.getForecasts()==null) {
				weather.setForecasts(new ArrayList());
			}
			resetWeatherForecast(forecast);
			weather.getForecasts().add(forecast);
		}
		return weather;
	}
	
	/**
	 * 设置天气预报属性
	 * @param forecast
	 * @param propertyName
	 * @param capturedForecast
	 * @param capturedPropertyName
	 */
	private void setForecastProperty(com.yuanluesoft.jeaf.weather.pojo.WeatherForecast forecast, String propertyName, WeatherForecast capturedForecast, String capturedPropertyName) {
    	try {
    		String upperPropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    		String upperCapturedPropertyName = capturedPropertyName.substring(0, 1).toUpperCase() + capturedPropertyName.substring(1);
    		String value = (String)PropertyUtils.getProperty(capturedForecast, capturedPropertyName);
	    	if(value!=null && !value.isEmpty()) { //全天
	    		String[] values = value.split("转");
	    		PropertyUtils.setProperty(forecast, "day" + upperPropertyName, values[0]);
	    		PropertyUtils.setProperty(forecast, "night" + upperPropertyName, values[values.length - 1]);
	    		return;
	    	}
			String dayValue = (String)PropertyUtils.getProperty(capturedForecast, "day" + upperCapturedPropertyName);
			String nightValue = (String)PropertyUtils.getProperty(capturedForecast, "night" + upperCapturedPropertyName);
	    	if(dayValue!=null && !dayValue.isEmpty()) { //白天数据不为空
	    		PropertyUtils.setProperty(forecast, "day" + upperPropertyName, dayValue);
			}
			if(nightValue!=null && !nightValue.isEmpty()) { //夜晚数据不为空
				PropertyUtils.setProperty(forecast, "night" + upperPropertyName, nightValue);
			}
    	}
    	catch(Exception e) {
    		Logger.exception(e);
    	}
	}
	
	/**
	 * 获取小时预报列表
	 * @param weatherCapture
	 * @param captureTime
	 * @return
	 */
	private List listHourForecasts(WeatherCapture weatherCapture, Timestamp captureTime) {
		if(weatherCapture.getForecastUpdateTime()!=null) {
			captureTime = weatherCapture.getForecastUpdateTime();
		}
		else if(weatherCapture.getUpdateTime()!=null) {
			captureTime = weatherCapture.getUpdateTime();
		}
		captureTime = DateTimeUtils.set(captureTime, Calendar.SECOND, 0);
		captureTime = DateTimeUtils.set(captureTime, Calendar.MILLISECOND, 0);
		List hourForecasts = new ArrayList();
		for(Iterator iterator = weatherCapture.getRefinedForecasts()==null ? null : weatherCapture.getRefinedForecasts().iterator(); iterator!=null && iterator.hasNext();) {
			WeatherRefinedForecast refinedForecast = (WeatherRefinedForecast)iterator.next();
			if(((refinedForecast.getHourForecastTimes()!=null && refinedForecast.getHourForecastTimes().length > 0) ||
			   (refinedForecast.getHourForecastDateTimes()!=null && refinedForecast.getHourForecastDateTimes().length > 0)) &&
			   refinedForecast.getHourForecastTemperatures()!=null && refinedForecast.getHourForecastTemperatures().length > 0) {
				refinedForecast.setHourForecasts(new ArrayList());
				for(int i = 0; i < refinedForecast.getHourForecastTemperatures().length; i++) {
					WeatherHourForecast hourForecast = new WeatherHourForecast();
					if(refinedForecast.getHourForecastDateTimes()!=null && refinedForecast.getHourForecastDateTimes().length>i) {
						hourForecast.setForecastDateTime(refinedForecast.getHourForecastDateTimes()[i]);
					}
					else if(refinedForecast.getHourForecastDates()!=null && refinedForecast.getHourForecastDates().length>i) {
						hourForecast.setForecastDate(refinedForecast.getHourForecastDates()[i]);
					}
					if(refinedForecast.getHourForecastTimes()!=null && refinedForecast.getHourForecastTimes().length>0) {
						hourForecast.setForecastTime(refinedForecast.getHourForecastTimes().length > i ? refinedForecast.getHourForecastTimes()[i] : DateTimeUtils.add(refinedForecast.getHourForecastTimes()[0], Calendar.HOUR_OF_DAY, i)); //时间
					}
					hourForecast.setTemperature(refinedForecast.getHourForecastTemperatures()[i]); //气温
					hourForecast.setDescribe(refinedForecast.getHourForecastDescribes()!=null && refinedForecast.getHourForecastDescribes().length > i ? refinedForecast.getHourForecastDescribes()[i] : null); //天气概况
					hourForecast.setFeelst(refinedForecast.getHourForecastFeelsts()!=null && refinedForecast.getHourForecastFeelsts().length > i ? refinedForecast.getHourForecastFeelsts()[i] : null); //体感温度
					hourForecast.setFeelstTip(refinedForecast.getHourForecastFeelstTips()!=null && refinedForecast.getHourForecastFeelstTips().length > i ? refinedForecast.getHourForecastFeelstTips()[i] : null); //体感温度描述
					hourForecast.setRain(refinedForecast.getHourForecastRains()!=null && refinedForecast.getHourForecastRains().length > i ? refinedForecast.getHourForecastRains()[i] : null); //降水
					hourForecast.setWindDirection(refinedForecast.getHourForecastWindDirections()!=null && refinedForecast.getHourForecastWindDirections().length > i ? refinedForecast.getHourForecastWindDirections()[i] : null); //风向
					hourForecast.setWindPower(refinedForecast.getHourForecastWindPowers()!=null && refinedForecast.getHourForecastWindPowers().length > i ? refinedForecast.getHourForecastWindPowers()[i] : null); //风力
					hourForecast.setWindSpeed(refinedForecast.getHourForecastWindSpeeds()!=null && refinedForecast.getHourForecastWindSpeeds().length > i ? refinedForecast.getHourForecastWindSpeeds()[i] : null); //风速
					hourForecast.setAirpressure(refinedForecast.getHourForecastAirpressures()!=null && refinedForecast.getHourForecastAirpressures().length > i ? refinedForecast.getHourForecastAirpressures()[i] : null); //气压
					hourForecast.setAirpressureTip(refinedForecast.getHourForecastAirpressureTips()!=null && refinedForecast.getHourForecastAirpressureTips().length > i ? refinedForecast.getHourForecastAirpressureTips()[i] : null); //气压描述
					hourForecast.setHumidity(refinedForecast.getHourForecastHumidities()!=null && refinedForecast.getHourForecastHumidities().length > i ? refinedForecast.getHourForecastHumidities()[i] : null); //湿度
					hourForecast.setHumidityTip(refinedForecast.getHourForecastHumidityTips()!=null && refinedForecast.getHourForecastHumidityTips().length > i ? refinedForecast.getHourForecastHumidityTips()[i] : null); //湿度描述
					hourForecast.setCloud(refinedForecast.getHourForecastClouds()!=null && refinedForecast.getHourForecastClouds().length > i ? refinedForecast.getHourForecastClouds()[i] : null); //云量
					hourForecast.setVisibility(refinedForecast.getHourForecastVisibilities()!=null && refinedForecast.getHourForecastVisibilities().length > i ? refinedForecast.getHourForecastVisibilities()[i] : null); //能见度
					hourForecast.setAirQuality(refinedForecast.getHourForecastAirQualities()!=null && refinedForecast.getHourForecastAirQualities().length > i ? refinedForecast.getHourForecastAirQualities()[i] : null); //空气质量
					refinedForecast.getHourForecasts().add(hourForecast);
				}
			}
			if(refinedForecast.getHourForecasts()==null || refinedForecast.getHourForecasts().isEmpty()) {
				continue;
			}
			Timestamp forecastTime = new Timestamp(captureTime.getTime());
			if(weatherCapture.getRefinedForecasts().size()==1) {
				WeatherHourForecast capturedHourForecast = (WeatherHourForecast)refinedForecast.getHourForecasts().get(0);
				if(capturedHourForecast.getForecastDateTime()!=null) {
					forecastTime = new Timestamp(capturedHourForecast.getForecastDateTime().getTime());
				}
				else if(capturedHourForecast.getForecastDate()!=null) {
					forecastTime = new Timestamp(capturedHourForecast.getForecastDate().getTime());
				}
				else if(capturedHourForecast.getForecastTime()!=null && DateTimeUtils.getHour(forecastTime) - DateTimeUtils.getHour(capturedHourForecast.getForecastTime()) > 10 ) {
					forecastTime = DateTimeUtils.add(forecastTime, Calendar.DAY_OF_MONTH, 1);
				}
			}
			int lastHour = -1;
			for(Iterator iteratorHour = refinedForecast.getHourForecasts().iterator(); iteratorHour.hasNext();) {
				WeatherHourForecast capturedHourForecast = (WeatherHourForecast)iteratorHour.next();
				if(capturedHourForecast.getForecastTime()==null && capturedHourForecast.getForecastDateTime()==null) {
					break;
				}
				int hour = DateTimeUtils.getHour(capturedHourForecast.getForecastTime()==null ? capturedHourForecast.getForecastDateTime() : capturedHourForecast.getForecastTime());
				int minute = DateTimeUtils.getMinute(capturedHourForecast.getForecastTime()==null ? capturedHourForecast.getForecastDateTime() : capturedHourForecast.getForecastTime());
				if(hour < lastHour) {
					forecastTime = DateTimeUtils.add(forecastTime, Calendar.DAY_OF_MONTH, 1);
				}
				lastHour = hour;
				forecastTime = DateTimeUtils.set(forecastTime, Calendar.HOUR_OF_DAY, hour);
				forecastTime = DateTimeUtils.set(forecastTime, Calendar.MINUTE, minute);
				if(ListUtils.findObjectByProperty(hourForecasts, "forecastTime", forecastTime)!=null) {
					continue;
				}
				com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast hourForecast = new com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast();
				BeanUtils.copyProperties(capturedHourForecast, hourForecast);
				hourForecast.setForecastTime(forecastTime);
				hourForecasts.add(hourForecast);
			}
			captureTime = DateTimeUtils.add(captureTime, Calendar.DAY_OF_MONTH, 1);
		}
		return hourForecasts;
	}

	/**
	 * @return the captureService
	 */
	public CaptureService getCaptureService() {
		return captureService;
	}

	/**
	 * @param captureService the captureService to set
	 */
	public void setCaptureService(CaptureService captureService) {
		this.captureService = captureService;
	}

	/**
	 * @param serializedWeatherCaptureTasks the serializedWeatherCaptureTasks to set
	 */
	public void setSerializedWeatherCaptureTasks(List serializedWeatherCaptureTasks) {
		weatherCaptureTasks = new ArrayList();
		for(Iterator iterator = serializedWeatherCaptureTasks==null ? null : serializedWeatherCaptureTasks.iterator(); iterator!=null && iterator.hasNext();) {
			String serializedWeatherCaptureTask = (String)iterator.next();
			try {
				weatherCaptureTasks.add((CmsCaptureTask)ObjectSerializer.deserialize(new Base64Decoder().decode(serializedWeatherCaptureTask)));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
}