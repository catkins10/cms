package com.yuanluesoft.jeaf.weather.spider.sina;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider;

/**
 * 从新浪网(weather.sina.com.cn)获取天气数据,http方式
 * @author linchuan
 *
 */
public class SinaWeatherSpider extends WeatherWebSpider {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getDescription()
	 */
	public String getDescription() {
		return "新浪天气";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getSiteName()
	 */
	public String getSiteName() {
		return "weather.sina.com.cn";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#getWeatherUrl(java.util.List, java.lang.String)
	 */
	protected Map getWeatherUrl(List weatherCaptureTasks, String areaName) throws Exception {
		//http://open.weather.sina.com.cn/api/location/getIndexSuggestion/顺昌
		//{"result":{"status":{"code":0,"msg":"success"},"data":[{"loc_code":"43-48-32-33-30-39-30-32","name":"","type":"1","url":"shunchang","chinese_name":"\u987a\u660c","parent_name":"\u798f\u5efa"}]}}
		String response = HttpUtils.getHttpContent("http://open.weather.sina.com.cn/api/location/getIndexSuggestion/" + URLEncoder.encode(areaName, "utf-8"), "utf-8", true, null, 20000).getResponseBody();
		int index = response.indexOf("\"url\":\"");
		if(index==-1) {
			return null;
		}
		index += "\"url\":\"".length();
		if(response.indexOf("\"url\":", index)!=-1) { //不只一个
			return null;
		}
		String url = response.substring(index, response.indexOf("\"", index));
		Map urls = new HashMap();
		CmsCaptureTask captureTask = (CmsCaptureTask)weatherCaptureTasks.iterator().next();
		urls.put(new Long(captureTask.getId()), "http://weather.sina.com.cn/" + url);
		return urls;
	}
}