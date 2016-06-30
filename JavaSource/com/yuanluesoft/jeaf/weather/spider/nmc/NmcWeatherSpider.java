package com.yuanluesoft.jeaf.weather.spider.nmc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast;
import com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider;

/**
 * 从中央气象台(www.nmc.cn)获取天气数据,http方式
 * @author linchuan
 *
 */
public class NmcWeatherSpider extends WeatherWebSpider {
	private String areaInfo = null;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getDescription()
	 */
	public String getDescription() {
		return "中央气象台";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.service.dataservice.WeatherDataService#getSiteName()
	 */
	public String getSiteName() {
		return "www.nmc.cn";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#getWeatherUrl(java.util.List, java.lang.String)
	 */
	protected Map getWeatherUrl(List weatherCaptureTasks, String areaName) throws Exception {
		if(areaInfo==null) {
			areaInfo = HttpUtils.getHttpContent("http://www.nmc.cn/static/site/nmc/themes/basic/js/common.js", "utf-8", true, null, 20000).getResponseBody();
			areaInfo = areaInfo.substring(0, areaInfo.indexOf("];") + 2);
		}
		int index = areaInfo.indexOf("'" + areaName + "','");
		if(index==-1) {
			return null;
		}
		//获取编号1
		int code1Index = areaInfo.lastIndexOf("['", index) + 2;
		String code1 = areaInfo.substring(code1Index, areaInfo.indexOf("'", code1Index));
		//获取省份代码
		int provinceIndex = areaInfo.lastIndexOf("['A", index) + 2;
		String provinceName = areaInfo.substring(provinceIndex, areaInfo.indexOf("'", provinceIndex));
		//获取编号2
		index += ("'" + areaName + "','").length();
		String code2 = areaInfo.substring(index, areaInfo.indexOf("'", index));
		///获取地区拼音
		index = areaInfo.indexOf("','", index) + 3;
		String areaSpell = areaInfo.substring(index, areaInfo.indexOf("']", index));
		//输出URL
		Map urls = new HashMap();
		for(Iterator iterator = weatherCaptureTasks.iterator(); iterator.hasNext();) {
			CmsCaptureTask captureTask = (CmsCaptureTask)iterator.next();
			if(captureTask.getCaptureURL().startsWith("http://www.nmc.cn/service/data/real/")) { //天气实况:http://www.nmc.cn/service/data/real/101130501.json
				urls.put(new Long(captureTask.getId()), "http://www.nmc.cn/service/data/real/" + code1 + ".json");
			}
			else if(captureTask.getCaptureURL().startsWith("http://www.nmc.cn/f/forecast/aqi")) { //空气质量:http://www.nmc.cn/f/forecast/aqi?stationcode=58847
				urls.put(new Long(captureTask.getId()), "http://www.nmc.cn/f/forecast/aqi?stationcode=" + code2);
			}
			else {//天气预报
				urls.put(new Long(captureTask.getId()), "http://www.nmc.cn/publish/forecast/" + provinceName + "/" + areaSpell + ".html");
			}
		}
		return urls;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.weather.spider.WeatherWebSpider#resetWeatherHourForecast(com.yuanluesoft.jeaf.weather.pojo.WeatherHourForecast)
	 */
	protected void resetWeatherHourForecast(WeatherHourForecast weatherHourForecast) {
		super.resetWeatherHourForecast(weatherHourForecast);
		if(weatherHourForecast.getDescribe()==null) {
			return;
		}
		String[][] describes = {{"0", "晴"}, {"1", "多云"}, {"2", "阴"}, {"3", "阵雨"}, {"4", "雷阵雨"}, {"5", "小雪"}, {"6", "阵雪"}, {"7", "小雨"}, {"8", "中雨"},
								{"9", "大雨"}, {"10", "暴雨"}, {"11", "大暴雨"}, {"12", "特大暴雨"}, {"13", "阵雪"}, {"14", "小雪"}, {"15", "中雪"}, {"16", "大雪"},
								{"17", "暴雪"}, {"18", "雾"}, {"19", "中雨"}, {"20", "沙尘暴"}, {"21", "小雨"}, {"22", "中雨"}, {"23", "大雨"}, {"24", "暴雨"},
								{"25", "大暴雨"}, {"26", "中雪"}, {"27", "大雪"}, {"28", "暴雪"}, {"29", "浮尘"}, {"30", "扬沙"}, {"31", "强沙尘暴"}, {"32", "中雨"},
								{"33", "中雪"}, {"36", "阵雨"}, {"53", null}};
		String imageName = weatherHourForecast.getDescribe().substring(weatherHourForecast.getDescribe().lastIndexOf('/') + 1);
		imageName = imageName.substring(0, imageName.indexOf('.'));
		for(int i = 0; i < describes.length; i++) {
			if(describes[i][0].equals(imageName)) {
				weatherHourForecast.setDescribe(describes[i][1]);
				break;
			}
		}
	}
}