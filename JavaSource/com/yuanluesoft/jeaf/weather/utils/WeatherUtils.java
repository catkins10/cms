package com.yuanluesoft.jeaf.weather.utils;

import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.weather.model.AirQuality;

/**
 * 
 * @author chuan
 *
 */
public class WeatherUtils {

	/**
	 * 获取空气质量
	 * @return
	 */
	public static AirQuality getAirQuality(String airQualityValue) {
		double qualityValue = StringUtils.parseDouble(airQualityValue, -1);
		if(qualityValue < 0) {
			return null;
		}
		else if(qualityValue<=50) {
			return new AirQuality(qualityValue, "优", "空气质量令人满意,基本无空气污染", "各类人群可正常活动", "绿色", "#32f43e");
		}
		else if(qualityValue<=100) {
			return new AirQuality(qualityValue, "良", "空气质量可接受,某些污染物对极少数敏感人群健康有较弱影响", "极少数敏感人群应减少户外活动", "黄色", "#e4f33e");
		}
		else if(qualityValue<=150) {
			return new AirQuality(qualityValue, "轻度污染", "易感人群有症状有轻度加剧,健康人群出现刺激症状", "老人、儿童、呼吸系统等疾病患者减少长时间、高强度的户外活动", "橙色", "#e19535");
		}
		else if(qualityValue<=200) {
			return new AirQuality(qualityValue, "中度污染", "进一步加剧易感人群症状,会对健康人群的呼吸系统有影响", "儿童、老人、呼吸系统等疾病患者及一般人群减少户外活动", "红色", "#ec0800");
		}
		else if(qualityValue<=300) {
			return new AirQuality(qualityValue, "重度污染", "心脏病和肺病患者症状加剧运动耐受力降低,健康人群出现症状", "儿童、老人、呼吸系统等疾病患者及一般人群停止或减少户外运动", "紫红色", "#950449");
		}
		else {
			return new AirQuality(qualityValue, "严重污染", "健康人群运动耐受力降低,有明显强烈症状,可能导致疾病", "儿童、老人、呼吸系统等疾病患者及一般人群停止户外活动", "褐红色", "#7b001f");
		}
	}
	
	/**
	 * 重置风速
	 * @param windSpeed
	 * @return
	 */
	public static String resetWindSpeed(String windSpeed) {
		return windSpeed==null || windSpeed.isEmpty() || windSpeed.indexOf("/")!=-1 || windSpeed.indexOf("每")!=-1 ? windSpeed : windSpeed + "米/秒";
	}

	/**
	 * 重置湿度
	 * @param windSpeed
	 * @return
	 */
	public static String resetHumidity(String humidity) {
		return humidity==null || humidity.isEmpty() || humidity.indexOf("%")!=-1 ? humidity : humidity + "%";
	}

	/**
	 * 重置降水
	 * @param windSpeed
	 * @return
	 */
	public static String resetRain(String rain) {
		return rain==null || rain.isEmpty() || rain.indexOf("水")!=-1 || rain.indexOf("m")!=-1 || rain.indexOf("M")!=-1 || rain.indexOf("米")!=-1 ? rain : rain + "mm";
	}
}