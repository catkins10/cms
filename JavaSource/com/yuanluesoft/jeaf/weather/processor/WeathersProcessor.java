package com.yuanluesoft.jeaf.weather.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.model.PlaceName;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.weather.service.WeatherService;

/**
 * 页面元素处理器：天气列表
 * @author linchuan
 *
 */
public class WeathersProcessor extends RecordListProcessor {
	private WeatherService weatherService; //天气服务
	private GpsService gpsService; //GPS服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecords(java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.NodeList, int, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.util.model.RequestInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void writeRecords(List records, View view, RecordList recordListModel, NodeList recordFormatNodes, int pageIndex, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if("true".equals(sitePage.getAttribute("customAreaEnable"))) { //允许自定义地区
			HTMLScriptElement scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
			scriptElement.setLang("javascript");
			scriptElement.setSrc(Environment.getContextPath() + "/jeaf/weather/js/weather.js");
			pageElement.getParentNode().insertBefore(scriptElement, pageElement);
			String weatherArea = (String)sitePage.getAttribute("weatherArea");
			if(weatherArea!=null) {
				scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
				scriptElement.setLang("javascript");
				scriptElement.appendChild(pageElement.getOwnerDocument().createTextNode("setWeatherArea('" + weatherArea + "')"));
				pageElement.getParentNode().insertBefore(scriptElement, pageElement);
			}
		}
		super.writeRecords(records, view, recordListModel, recordFormatNodes, pageIndex, pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String areas = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "areas");
		int forecastDays = StringUtils.getPropertyIntValue(recordListModel.getExtendProperties(), "forecastDays", 1);
		if(areas!=null && !areas.isEmpty()) {
			return new RecordListData(weatherService.retrieveWeathers(areas, forecastDays, false));
		}
		sitePage.setAttribute("customAreaEnable", "true"); //允许自定义地区
		String weatherArea = CookieUtils.getCookie(request, "weatherArea");
		if(weatherArea!=null) {
			try {
				return new RecordListData(weatherService.retrieveWeathers(URLDecoder.decode(weatherArea, "utf-8"), forecastDays, false));
			}
			catch(UnsupportedEncodingException e) {
				
			}
		}
		//根据IP地址获取地区
		Location location = gpsService.getLocationByIP(request.getRemoteAddr(), true, false);
		List cities = new ArrayList();
		if(location!=null && location.getPlaceName()!=null && !location.getPlaceName().isEmpty()) {
			PlaceName placeName = gpsService.parsePlaceName(location.getPlaceName());
			if(placeName.getDistrict()!=null && !placeName.getDistrict().endsWith("区")) { //县、区
				if(placeName.getDistrict().length()>2 && (placeName.getDistrict().endsWith("市") || placeName.getDistrict().endsWith("县"))) {
					placeName.setDistrict(placeName.getDistrict().substring(0, placeName.getDistrict().length() - 1));
				}
				cities.add(placeName.getDistrict());
			}
			if(placeName.getCity()!=null) { //市
				if(placeName.getCity().length()>4 && placeName.getCity().endsWith("自治州")) {
					placeName.setCity(placeName.getCity().substring(0, placeName.getCity().length() - 3));
				}
				else if(placeName.getCity().length()>2 && placeName.getCity().endsWith("市")) {
					placeName.setCity(placeName.getCity().substring(0, placeName.getCity().length() - 1));
				}
				cities.add(placeName.getCity());
			}
		}
		cities.add("福州");
		for(Iterator iterator = cities.iterator(); iterator.hasNext();) {
			String city = (String)iterator.next();
			List waethers = weatherService.retrieveWeathers(city, forecastDays, true);
			if(waethers!=null && !waethers.isEmpty()) {
				sitePage.setAttribute("weatherArea", city); //地区
				return new RecordListData(waethers);
			}
		}
		return null;
	}

	/**
	 * @return the weatherService
	 */
	public WeatherService getWeatherService() {
		return weatherService;
	}

	/**
	 * @param weatherService the weatherService to set
	 */
	public void setWeatherService(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	/**
	 * @return the gpsService
	 */
	public GpsService getGpsService() {
		return gpsService;
	}

	/**
	 * @param gpsService the gpsService to set
	 */
	public void setGpsService(GpsService gpsService) {
		this.gpsService = gpsService;
	}
}