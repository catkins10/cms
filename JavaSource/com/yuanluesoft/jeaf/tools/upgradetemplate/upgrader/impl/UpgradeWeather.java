package com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.impl;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 更新天气预报
 * @author chuan
 *
 */
public class UpgradeWeather implements TemplateUpgrader {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#getCreateDate()
	 */
	public String getCreateDate() {
		return "2016-1-18";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#upgrade(com.yuanluesoft.cms.templatemanage.pojo.Template, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String upgrade(Template template, String templateHTML, HttpServletRequest request) throws Exception {
		if(templateHTML.indexOf("weather")==-1) {
			return templateHTML;
		}
		HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
		HTMLDocument templateDocument = htmlParser.parseHTMLString(templateHTML, "utf-8");
		boolean found = false;
		for(;;) {
			HTMLElement weatherElement = (HTMLElement)templateDocument.getElementById("weather");
			if(weatherElement==null) {
				break;
			}
			upgradeWeahter(weatherElement, htmlParser);
			found = true;
		}
		return found ? htmlParser.getDocumentHTML(templateDocument, "utf-8") : templateHTML;
	}
	
	/**
	 * 天气预报改用记录列表
	 * @param weatherElement
	 * @throws Exception
	 */
	private void upgradeWeahter(HTMLElement weatherElement, HTMLParser htmlParser) throws Exception {
		String urn = weatherElement.getAttribute("urn");
		//创建天气记录列表
		HTMLElement weathersElement = (HTMLElement)weatherElement.getOwnerDocument().createElement("a");
		htmlParser.setTextContent(weathersElement, "<天气>");
		weathersElement.setId("recordList");
		RecordList weathers = new RecordList();
		weathers.setRecordListName("weathers");
		weathers.setApplicationName("jeaf/weather");
		weathers.setRecordCount(20);
		weathers.setExtendProperties("areas=" + StringUtils.getPropertyValue(urn, "areas") + "&forecastDays=" + StringUtils.getPropertyValue(urn, "days"));
		weathers.setSeparatorMode(StringUtils.getPropertyValue(urn, "separatorMode"));
		weathers.setSeparatorImage(StringUtils.getPropertyValue(urn, "separatorImage"));
		weathers.setSeparatorHeight("8");
		String scrollMode = StringUtils.getPropertyValue(urn, "scrollMode");
		weathers.setScrollMode(scrollMode);
		if(scrollMode!=null && !"none".equals(scrollMode)) {
			weathers.setScrollSpeed(StringUtils.getPropertyIntValue(urn, "scrollSpeed", 0));
			weathers.setScrollAmount(StringUtils.getPropertyIntValue(urn, "scrollAmount", 0));
			weathers.setAreaWidth(StringUtils.getPropertyValue(urn, "scrollWidth"));
			weathers.setAreaHeight(StringUtils.getPropertyValue(urn, "scrollHeight"));
		}
		
		//创建天气预报记录列表
		HTMLElement forecastsElement = (HTMLElement)weatherElement.getOwnerDocument().createElement("a");
		htmlParser.setTextContent(forecastsElement, "<天气预报>");
		forecastsElement.setId("recordList");
		RecordList forecasts = new RecordList();
		forecasts.setRecordListName("forecasts");
		forecasts.setPrivateRecordList(true);
		forecasts.setRecordClassName("com.yuanluesoft.jeaf.weather.model.Weather");
		forecasts.setApplicationName("jeaf/weather");
		forecasts.setRecordCount(20);
		forecasts.setSeparatorMode(StringUtils.getPropertyValue(urn, "separatorMode"));
		forecasts.setSeparatorImage(StringUtils.getPropertyValue(urn, "separatorImage"));
		forecasts.setSeparatorHeight("8");
		
		//更新输出格式
		String recordFormat = StringUtils.getPropertyValue(urn, "format");
		recordFormat = recordFormat.replaceAll("&lt;日期:?([^&]*)&gt;", "<a id=\"field\" urn=\"name=forecastDate&amp;fieldFormat=$1\">&lt;预报日期&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;农历年&gt;", "<a id=\"field\" urn=\"name=forecastDate&amp;fieldFormat=&lt;农历年&gt;\">&lt;预报日期&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;农历月&gt;", "<a id=\"field\" urn=\"name=forecastDate&amp;fieldFormat=&lt;农历月&gt;\">&lt;预报日期&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;农历日&gt;", "<a id=\"field\" urn=\"name=forecastDate&amp;fieldFormat=&lt;农历日&gt;\">&lt;预报日期&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;生肖&gt;", "<a id=\"field\" urn=\"name=forecastDate&amp;fieldFormat=&lt;生肖&gt;\">&lt;预报日期&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;地名&gt;", "<a id=\"field\" urn=\"name=area\">&lt;地区&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;概况&gt;", "<a id=\"field\" urn=\"name=describe\">&lt;概况&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;温度1&gt;", "<a id=\"field\" urn=\"name=nightTemperature\">&lt;夜晚气温&gt;</a>℃");
		recordFormat = recordFormat.replaceAll("&lt;温度2&gt;", "<a id=\"field\" urn=\"name=dayTemperature\">&lt;白天气温&gt;</a>℃");
		recordFormat = recordFormat.replaceAll("&lt;风力&gt;", "<a id=\"field\" urn=\"name=windPower\">&lt;风力&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;风向&gt;", "<a id=\"field\" urn=\"name=windDirection\">&lt;风向&gt;</a>");
		recordFormat = recordFormat.replaceAll("&lt;图标(\\d?\\d?)\\*?(\\d?\\d?)&gt;", "<a id=\"field\" urn=\"name=dayWeatherIconUrl&amp;imageWidth=$1&amp;imageHeight=$2&amp;imageAlign=absmiddle&amp;\">&lt;白天天气图标&gt;</a>&nbsp;<a id=\"field\" urn=\"name=nightWeatherIconUrl&amp;imageWidth=$1&amp;imageHeight=$2&amp;imageAlign=absmiddle&amp;\">&lt;夜晚天气图标&gt;</a>");
		forecasts.setRecordFormat(recordFormat);
		forecastsElement.setAttribute("urn", RecordListUtils.gernerateRecordListProperties(forecasts));
		
		weathers.setRecordFormat(htmlParser.getElementHTML(forecastsElement, "utf-8"));
		weathersElement.setAttribute("urn", RecordListUtils.gernerateRecordListProperties(weathers));
		weatherElement.getParentNode().replaceChild(weathersElement, weatherElement);
	}
}