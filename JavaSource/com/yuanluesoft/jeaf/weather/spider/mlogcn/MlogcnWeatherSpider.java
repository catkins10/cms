package com.yuanluesoft.jeaf.weather.spider.mlogcn;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast;
import com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider;

/**
 * 从象辑科技(www.mlogcn.com)获取天气数据,http方式
 * @author linchuan
 *
 */
public class MlogcnWeatherSpider extends WeatherWebSpider {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getDescription()
	 */
	public String getDescription() {
		return "象辑科技";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getSiteName()
	 */
	public String getSiteName() {
		return "www.mlogcn.com";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#getWeatherUrl(java.util.List, java.lang.String)
	 */
	protected Map getWeatherUrl(List weatherCaptureTasks, String areaName) throws Exception {
		//获取坐标,[{"province":"福建","city":"南平","county":"顺昌","areaid":"101230902","lon":117.8,"lat":26.8}]
		String response = HttpUtils.getHttpContent("http://dev.api.mlogcn.com:8000/api/weather/v1/area/serach?area=" + URLEncoder.encode(areaName, "utf-8"), "utf-8", true, null, 20000).getResponseBody();
		int index = response.indexOf("\"lon\":");
		if(index==-1) {
			return null;
		}
		index += "\"lon\":".length();
		int indexEnd = response.indexOf(",", index);
		double longitude = Double.parseDouble(response.substring(index, indexEnd)); //经度
		index = response.indexOf("\"lat\":", indexEnd);
		if(index==-1) {
			return null;
		}
		index += "\"lat\":".length();
		indexEnd = response.indexOf("}", index);
		double latitude = Double.parseDouble(response.substring(index, indexEnd)); //纬度
		//输出URL
		Map urls = new HashMap();
		for(Iterator iterator = weatherCaptureTasks.iterator(); iterator.hasNext();) {
			CmsCaptureTask captureTask = (CmsCaptureTask)iterator.next();
			index = captureTask.getCaptureURL().lastIndexOf("coor/") + "coor/".length();
			urls.put(new Long(captureTask.getId()), captureTask.getCaptureURL().substring(0, index) + longitude + "/" + latitude + ".json");
		}
		return urls;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#resetWeatherHourForecast(com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast)
	 */
	protected void resetWeatherHourForecast(WeatherHourForecast weatherHourForecast) {
		super.resetWeatherHourForecast(weatherHourForecast);
		if(weatherHourForecast.getTemperature()==Double.MIN_VALUE) { //没有温度值,使用体感温度
			weatherHourForecast.setTemperature(Math.round(StringUtils.parseDouble(weatherHourForecast.getFeelst(), 0)));
		}
	}
}