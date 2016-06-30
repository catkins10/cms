/**
 * 
 */
package com.yuanluesoft.jeaf.weather.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPoolException;
import com.yuanluesoft.jeaf.weather.model.Weather;
import com.yuanluesoft.jeaf.weather.pojo.WeatherForecast;
import com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast;
import com.yuanluesoft.jeaf.weather.pojo.WeatherLive;
import com.yuanluesoft.jeaf.weather.service.WeatherService;
import com.yuanluesoft.jeaf.weather.spider.WeatherSpider;

/**
 * 天气服务
 * @author linchuan
 *
 */
public class WeatherServiceImpl extends BusinessServiceImpl implements WeatherService {
	private final int WAIT_WEATHER_SITE = 30000; //等待天气预报网站响应的最大时间
	private List registedAreas = new ArrayList(); //注册的地区列表
	
	private String webApplicationUrl;
	private boolean updateWeatherByArea = false; //是否要按区域来更新天气
	private Map smallIconMap; //小图标映射表
	private Map largeIconMap; //大图标映射表
	private int smallIconSize; //小图标的尺寸,低于smallIconSize的使用小图标
	private int largeIconSize; //大图标的尺寸
	private DistributionService distributionService; //分布式服务
	private List weatherSpiders; //天气预报数据服务列表
	private ExchangeClient exchangeClient; //数据交换服务
	
	//私有属性
	private ThreadPool updateThreadPool = new ThreadPool(10, 0, 10000); //同时运行10个任务

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.WeatherService#retrieveWeathers(java.lang.String, int, boolean)
	 */
	public List retrieveWeathers(String areas, int forecastDays, boolean updateIfNecessary) throws ServiceException {
		areas = areas.replaceAll("[；，;、]", ",");
		//注册到地区列表,定时更新天气
		List areaList = ListUtils.generateList(areas, ",");
		for(Iterator iterator = areaList.iterator(); iterator.hasNext();) {
			String area = (String)iterator.next();
			if(registedAreas.indexOf(area)==-1) {
				if(registedAreas.size() >= 100) { //最多登记100个地区
					registedAreas.remove(0);
				}
				registedAreas.add(area);
			}
		}
        Timestamp beginTime = DateTimeUtils.now();
        Timestamp endTime = DateTimeUtils.add(beginTime, Calendar.DAY_OF_MONTH, forecastDays - 1);
        //获取天气实况
		String hql = "from WeatherLive WeatherLive" +
				   	 " where WeatherLive.area in ('" + JdbcUtils.resetQuot(areas).replaceAll(",", "','") + "')" +
				   	 " and WeatherLive.updateTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(beginTime, Calendar.HOUR_OF_DAY, -3), null) + ")" +
				   	 " order by WeatherLive.updateTime DESC";
		List weatherLives = getDatabaseService().findRecordsByHql(hql);
		//获取天气预报
		hql = "from WeatherForecast WeatherForecast" +
	   	 	  " where WeatherForecast.area in ('" + JdbcUtils.resetQuot(areas).replaceAll(",", "','") + "')" +
	   	 	  " and WeatherForecast.forecastDate>=DATE(" + DateTimeUtils.formatTimestamp(beginTime, "yyyy-MM-dd") + ")" +		
	   	 	  " and WeatherForecast.forecastDate<=DATE(" + DateTimeUtils.formatTimestamp(endTime, "yyyy-MM-dd") + ")" +
	   	 	  " order by WeatherForecast.forecastDate";
		List weatherForecasts = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("hourForecasts"));
		final List needUpdateAreas = new ArrayList(); //需要更新的地区
		List weathers = new ArrayList();
		for(Iterator iterator = areaList.iterator(); iterator.hasNext();) {
			String area = (String)iterator.next();
			Weather weather = new Weather();
			weather.setArea(area);
			weather.setWeatherLive((WeatherLive)ListUtils.findObjectByProperty(weatherLives, "area", area));
			weather.setForecasts(ListUtils.getSubListByProperty(weatherForecasts, "area", area));
			if(weather.getWeatherLive()==null || weather.getForecasts()==null || weather.getForecasts().isEmpty()) {
				needUpdateAreas.add(area);
			}
			else {
				weathers.add(weather);
			}
		}
		if(!needUpdateAreas.isEmpty()) {
			if(updateIfNecessary) {
				updateWeatherData(new ArrayList(weatherSpiders), needUpdateAreas, 0); //更新天气数据
				return retrieveWeathers(areas, forecastDays, false); //重新获取天气数据
			}
			else {
				try {
					updateThreadPool.execute(new Task() {
						public void process() {
							try {
								updateWeatherData(new ArrayList(weatherSpiders), needUpdateAreas, 0); //更新天气数据
							}
							catch (Exception e) {
								
							}
						}
					});
				}
				catch(ThreadPoolException e) {
					Logger.exception(e);
				}
			}
		}
		return weathers.isEmpty() ? null : weathers;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.WeatherService#getWeatherIconURL(java.lang.String, boolean, int)
	 */
	public Image getWeatherIcon(String weatherDescribe, boolean day, int iconSize) throws ServiceException {
		if(weatherDescribe==null) {
			return null;
		}
		String[] weathers = weatherDescribe.split("到");
		if(weathers.length==1) {
			weathers = weatherDescribe.replaceFirst("间", "转").split("转");
		}
		else if(!weathers[0].endsWith("雨") && !weathers[0].endsWith("雪")) {
			if(weathers[1].endsWith("雨")) {
				weathers[0] += "雨";
			}
			if(weathers[1].endsWith("雪")) {
				weathers[0] += "雪";
			}
		}
		String weather = weathers[day ? 0 : weathers.length - 1];
		//获取天气图标的URL
	 	Map iconMap = iconSize <= smallIconSize ? smallIconMap : largeIconMap;
		String icon = (String)iconMap.get((day ? "白天" : "夜晚") + ":" + weather);
		if(icon==null) {
			icon = (String)iconMap.get(weather);
		}
		if(icon==null) {
			icon = (String)iconMap.get("N/A");
		}
		Image image = new Image();
		image.setUrl(webApplicationUrl + "/jeaf/weather/icons/" + icon);
		image.setWidth(iconSize <= smallIconSize ? smallIconSize : largeIconSize);
		image.setHeight(image.getWidth());
		return image;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.WeatherService#updateWeather()
	 */
	public void updateWeather() throws ServiceException {
		if(!distributionService.isMasterServer(true)) { //不是主服务器,不需要更新天气数据
			return;
		}
		Logger.info("**************** update weather ****************");
		updateWeatherData(new ArrayList(weatherSpiders), registedAreas, WAIT_WEATHER_SITE); //更新注册区域的天气
	}
	
	/**
	 * 调用天气数据服务获取天气数据
	 * @param weatherDataServices
	 * @param area
	 * @param skipTimeoutDataService 如果获取数据时间超过这个值,则下一次获取数据时跳过这个方式
	 * @throws ServiceException
	 */
	private void updateWeatherData(List weatherSpiders, List areas, long skipTimeoutDataService) throws ServiceException {
		for(Iterator iteratorArea = areas.iterator(); iteratorArea.hasNext();) {
			String area = (String)iteratorArea.next();
			boolean liveUpdated = false; //实况是否更新
			boolean forecasetUpdated = false; //天气预报是否更新
			for(Iterator iterator = weatherSpiders.iterator(); iterator.hasNext() && (!liveUpdated || !forecasetUpdated);) {
				WeatherSpider weatherSpider = (WeatherSpider)iterator.next();
				long start = System.currentTimeMillis();
				if(Logger.isDebugEnabled()) {
					Logger.debug("WeatherDataService: update weather data of area " + area + " from " + weatherSpider.getSiteName() + ".");
				}
				Weather weather = null;
				try {
					weather = weatherSpider.getWeatherData(area);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				if(skipTimeoutDataService > 0 && System.currentTimeMillis() - start > skipTimeoutDataService) { //超时
					if(Logger.isDebugEnabled()) {
						Logger.debug("WeatherService: weather data service that site is " + weatherSpider.getSiteName() + " request timeout.");
					}
					iterator.remove();
				}
				if(weather==null) {
					continue;
				}
				weather.setArea(area);
				if(!liveUpdated) { //实况未更新
					saveWeatherLive(weather, weatherSpider.getDescription()); //保存天气实况
					liveUpdated = weather.getWeatherLive()!=null;
				}
				if(!forecasetUpdated) { //天气预报未更新
					saveWeatherForecasts(weather, weatherSpider.getDescription()); //保存天气预报
					forecasetUpdated = weather.getForecasts()!=null && !weather.getForecasts().isEmpty();
				}
			}
		}
	}
	
	/**
	 * 保存天气实况
	 * @param weather
	 * @param source
	 * @throws ServiceException
	 */
	private void saveWeatherLive(Weather weather, String source) throws ServiceException {
		if(weather.getWeatherLive()==null) {
			return;
		}
		WeatherLive weatherLive = weather.getWeatherLive();
		if(weatherLive.getTemperature()==Double.MIN_VALUE || //温度没有抓取到
		   weatherLive.getUpdateTime()==null || weatherLive.getUpdateTime().getTime()>System.currentTimeMillis()) { //没有更新时间,或者更新时间超出当前时间
			weather.setWeatherLive(null);
			return;
		}
		//检查更新的记录是否已经存在
		String hql = "from WeatherLive WeatherLive" +
					 " where WeatherLive.area='" + JdbcUtils.resetQuot(weather.getArea()) + "'" +
					 " order by WeatherLive.updateTime DESC";
		WeatherLive oldWeatherLive = (WeatherLive)getDatabaseService().findRecordByHql(hql);
		if(oldWeatherLive!=null) {
			if(!oldWeatherLive.getUpdateTime().before(DateTimeUtils.add(weatherLive.getUpdateTime(), Calendar.MINUTE, 20 * compareSource(source, oldWeatherLive.getSource())))) { //旧的预报时间 >= 当前预报时间 + 20分钟 * 优先级差
				return;
			}
			hql = "from WeatherLive WeatherLive" +
			 	  " where WeatherLive.area='" + JdbcUtils.resetQuot(weather.getArea()) + "'" +
			 	  " and WeatherLive.updateTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(weatherLive.getUpdateTime(), null) + ")";
			List oldWeatherLives = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = oldWeatherLives==null ? null : oldWeatherLives.iterator(); iterator!=null && iterator.hasNext();) {
				oldWeatherLive = (WeatherLive)iterator.next();
				getDatabaseService().deleteRecord(oldWeatherLive);
				if(exchangeClient!=null) {
					exchangeClient.synchDelete(oldWeatherLive, null, 2000); //同步删除
				}
			}
		}
		//保存新记录
		weatherLive.setId(UUIDLongGenerator.generateId());
		weatherLive.setArea(weather.getArea());
		weatherLive.setSource(source);
		if(weatherLive.getComfortTip()==null || weatherLive.getComfortTip().isEmpty()) {
			weatherLive.setComfortTip(getComfortTip(weatherLive.getComfort()));
		}
		getDatabaseService().saveRecord(weatherLive);
		//同步更新
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(weatherLive, null, 2000);
		}
	}
	
	/**
	 * 保存天气预报
	 * @param weather
	 * @param weatherLiveUpdateTime
	 * @param source
	 * @throws ServiceException
	 */
	private void saveWeatherForecasts(Weather weather, String source) throws ServiceException {
		for(Iterator iterator = weather.getForecasts()==null ? null : weather.getForecasts().iterator(); iterator!=null && iterator.hasNext();) {
			WeatherForecast forecast = (WeatherForecast)iterator.next();
			if(forecast.getDayTemperature()==Double.MIN_VALUE || forecast.getNightTemperature()==Double.MIN_VALUE) { //温度没有抓取到
				iterator.remove();
				continue;
			}
			//查找原有的天气预报
			String hql = "from WeatherForecast WeatherForecast" +
						 " where WeatherForecast.area='" + JdbcUtils.resetQuot(weather.getArea()) + "'" +
						 " and WeatherForecast.forecastDate=DATE(" + DateTimeUtils.formatDate(forecast.getForecastDate(), null) + ")";
			WeatherForecast oldForecast = (WeatherForecast)getDatabaseService().findRecordByHql(hql);
			if(oldForecast!=null) { //原来的预报已经存在
				Timestamp updateTime = forecast.getUpdateTime()==null ? DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -6) : forecast.getUpdateTime();
				if(oldForecast.getUpdateTime()!=null && !oldForecast.getUpdateTime().before(DateTimeUtils.add(updateTime, Calendar.HOUR_OF_DAY, 2 * compareSource(source, oldForecast.getSource())))) { //旧的更新时间 >= 当前更新时间 + 2小时 * 优先级差
					continue;
				}
				getDatabaseService().deleteRecord(oldForecast); //删除原来的预报记录
				if(exchangeClient!=null) {
					exchangeClient.synchDelete(oldForecast, null, 2000); //同步删除
				}
			}
			//保存天气预报
			forecast.setId(UUIDLongGenerator.generateId());
			forecast.setArea(weather.getArea());
			forecast.setSource(source);
			getDatabaseService().saveRecord(forecast);
			//保存整点预报
			for(Iterator iteratorHour = forecast.getHourForecasts()==null ? null : forecast.getHourForecasts().iterator(); iteratorHour!=null && iteratorHour.hasNext();) {
				WeatherHourForecast hourForecast = (WeatherHourForecast)iteratorHour.next();
				if(hourForecast.getForecastTime()==null || hourForecast.getTemperature()==Double.MIN_VALUE) {
					continue;
				}
				hourForecast.setId(UUIDLongGenerator.generateId());
				hourForecast.setForecastId(forecast.getId());
				getDatabaseService().saveRecord(hourForecast);
			}
			//同步更新
			if(exchangeClient!=null) {
				exchangeClient.synchUpdate(oldForecast, null, 2000);
			}
		}
	}
	
	/**
	 * 比较天气数据来源
	 * @param source0
	 * @param source1
	 * @return
	 */
	private int compareSource(String source0, String source1) {
		WeatherSpider spider0 = (WeatherSpider)ListUtils.findObjectByProperty(weatherSpiders, "description", source0);
		WeatherSpider spider1 = (WeatherSpider)ListUtils.findObjectByProperty(weatherSpiders, "description", source1);
		int index0 = spider0==null ? weatherSpiders.size() : weatherSpiders.indexOf(spider0);
		int index1 = spider1==null ? weatherSpiders.size() : weatherSpiders.indexOf(spider1);
		return index1 - index0;
	}
	
	/**
	 * 获取舒适度描述
	 * @return
	 */
	private String getComfortTip(String comfort) {
		if("4".equals(comfort)) {
			return "很热,极不适应";
		}
		else if("3".equals(comfort)) {
			return "热,很不舒适";
		}
		else if("2".equals(comfort)) {
			return "暖,不舒适";
		}
		else if("1".equals(comfort)) {
			return "温暖,较舒适";
		}
		else if("0".equals(comfort)) {
			return "舒适,最可接受";
		}
		else if("-1".equals(comfort)) {
			return "凉爽,较舒适";
		}
		else if("-2".equals(comfort)) {
			return "凉,不舒适";
		}
		else if("-3".equals(comfort)) {
			return "冷,很不舒适";
		}
		else if("-4".equals(comfort)) {
			return "很冷,极不适应";
		}
		return null;
	}

	/**
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}

	/**
	 * @return the webApplicationUrl
	 */
	public String getWebApplicationUrl() {
		return webApplicationUrl;
	}

	/**
	 * @param webApplicationUrl the webApplicationUrl to set
	 */
	public void setWebApplicationUrl(String webApplicationUrl) {
		this.webApplicationUrl = webApplicationUrl;
	}

	/**
	 * @return the updateWeatherByArea
	 */
	public boolean isUpdateWeatherByArea() {
		return updateWeatherByArea;
	}

	/**
	 * @param updateWeatherByArea the updateWeatherByArea to set
	 */
	public void setUpdateWeatherByArea(boolean updateWeatherByArea) {
		this.updateWeatherByArea = updateWeatherByArea;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}

	/**
	 * @return the largeIconMap
	 */
	public Map getLargeIconMap() {
		return largeIconMap;
	}

	/**
	 * @param largeIconMap the largeIconMap to set
	 */
	public void setLargeIconMap(Map largeIconMap) {
		this.largeIconMap = largeIconMap;
	}

	/**
	 * @return the smallIconMap
	 */
	public Map getSmallIconMap() {
		return smallIconMap;
	}

	/**
	 * @param smallIconMap the smallIconMap to set
	 */
	public void setSmallIconMap(Map smallIconMap) {
		this.smallIconMap = smallIconMap;
	}

	/**
	 * @return the smallIconSize
	 */
	public int getSmallIconSize() {
		return smallIconSize;
	}

	/**
	 * @param smallIconSize the smallIconSize to set
	 */
	public void setSmallIconSize(int smallIconSize) {
		this.smallIconSize = smallIconSize;
	}

	/**
	 * @return the weatherSpiders
	 */
	public List getWeatherSpiders() {
		return weatherSpiders;
	}

	/**
	 * @param weatherSpiders the weatherSpiders to set
	 */
	public void setWeatherSpiders(List weatherSpiders) {
		this.weatherSpiders = weatherSpiders;
	}

	/**
	 * @return the largeIconSize
	 */
	public int getLargeIconSize() {
		return largeIconSize;
	}

	/**
	 * @param largeIconSize the largeIconSize to set
	 */
	public void setLargeIconSize(int largeIconSize) {
		this.largeIconSize = largeIconSize;
	}
}