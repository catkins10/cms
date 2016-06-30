package com.yuanluesoft.jeaf.weather.spider.tq121;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;

import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast;
import com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider;

/**
 * 从中国天气网(www.weather.com.cn)获取天气数据,http方式,可以获取县级天气
 * @author linchuan
 *
 */
public class Tq121WeatherSpider extends WeatherWebSpider {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getDescription()
	 */
	public String getDescription() {
		return "中国天气网";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getSiteName()
	 */
	public String getSiteName() {
		return "www.weather.com.cn";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#getWeatherUrl(java.util.List, java.lang.String)
	 */
	protected Map getWeatherUrl(List weatherCaptureTasks, String areaName) throws Exception {
		String weatherUrl = null;
		/*
		<form id="form1" name="form1" method="post" action="http://219.234.88.13/frame/url/1/">
		<input name="CityInfo" type="text" id="textfield" value="城市名,拼音,电话区号" size="23" onFocus="this.value=''"  />
		<input type="submit" name="button" id="button" value="查询" />
		</form>
		*/
		String response = HttpUtils.doPost("http://search.weather.com.cn/static/url.php", null, new NameValuePair[]{new NameValuePair("cityinfo", new String(areaName.getBytes("utf-8"),"iso-8859-1"))}, null).getResponseBody();
		if(response.startsWith("REDIRECT TO:")) {
			weatherUrl = response.substring("REDIRECT TO:".length());
		}
		else {
			int index = response.lastIndexOf("URL=");
			if(index==-1) {
				return null;
			}
			index += "URL=".length();
			weatherUrl = response.substring(index, response.lastIndexOf('\"'));
		}
		if(weatherUrl.indexOf("999999999")!=-1) {
			return null;
		}
		//输出URL
		Map urls = new HashMap();
		for(Iterator iterator = weatherCaptureTasks.iterator(); iterator.hasNext();) {
			CmsCaptureTask captureTask = (CmsCaptureTask)iterator.next();
			if(captureTask.getCaptureURL().startsWith("http://www.weather.com.cn/weather/")) { //天气预报
				urls.put(new Long(captureTask.getId()), weatherUrl);
			}
			else if(captureTask.getCaptureURL().startsWith("http://d1.weather.com.cn/sk_2d/")) { //天气实况
				urls.put(new Long(captureTask.getId()), "http://d1.weather.com.cn/sk_2d/" + weatherUrl.substring(weatherUrl.lastIndexOf('/') + 1, weatherUrl.lastIndexOf('.')) + ".html");
			}
		}
		return urls;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#resetWeatherHourForecast(com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast)
	 */
	protected void resetWeatherHourForecast(WeatherHourForecast weatherHourForecast) {
		super.resetWeatherHourForecast(weatherHourForecast);
		int index;
		if(weatherHourForecast.getWindPower()!=null && (index=weatherHourForecast.getWindPower().indexOf(','))!=-1) {
			weatherHourForecast.setWindPower(weatherHourForecast.getWindPower().substring(0, index));
		}
	}
}