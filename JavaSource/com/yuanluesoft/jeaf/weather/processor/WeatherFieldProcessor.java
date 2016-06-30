package com.yuanluesoft.jeaf.weather.processor;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.weather.service.WeatherService;

/**
 * 
 * @author chuan
 *
 */
public class WeatherFieldProcessor extends RecordFieldProcessor {
	private WeatherService weatherService; //天气服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#getFieldValue(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if(!field.getType().equals("imageName")) {
			return super.getFieldValue(record, field, pageElement, webDirectory, parentSite, sitePage, request);
		}
		int index = field.getName().lastIndexOf('.'); 
		if(index!=-1) {
			try {
				record = PropertyUtils.getProperty(record, field.getName().substring(0, index));
			}
			catch(Exception e) {
				
			}
		}
		String urn = pageElement.getAttribute("urn");
		int imageWidth = StringUtils.getPropertyIntValue(urn, "imageWidth", 0);
		String weatherDescribe = null;
		try {
			weatherDescribe = (String)PropertyUtils.getProperty(record, (String)field.getParameter("describeField"));
		}
		catch(Exception e) {
			
		}
		boolean day = false;
		if("true".equals(field.getParameter("day"))) {
			day = true;
		}
		else if(!"true".equals(field.getParameter("night"))) {
			Timestamp time = null;
			try {
				time = (Timestamp)PropertyUtils.getProperty(record, (String)field.getParameter("timeField"));
			}
			catch(Exception e) {
				
			}
			day = time==null || (DateTimeUtils.getHour(time) >= 6 && DateTimeUtils.getHour(time) <= 18);
		}
		return weatherService.getWeatherIcon(weatherDescribe, day, imageWidth);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#writeImageField(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.jeaf.image.model.Image, java.lang.String, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, int)
	 */
	protected HTMLElement writeImageField(Object record, Field field, Image image, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		pageElement = super.writeImageField(record, field, image, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode);
		if(!"ie".equals(RequestUtils.getBrowserType(request)) || RequestUtils.getIEVersion(request)>=7) { //不是IE6
			return pageElement;
		}
		//IE6,因不支持透明PNG,所以使用滤镜
		HTMLElement fieldset = (HTMLElement)pageElement.getOwnerDocument().createElement("fieldset");
		fieldset.setTitle(pageElement.getTitle());
		fieldset.setAttribute("align", pageElement.getAttribute("align"));
		String style = "display:inline-block;" +
		   			   " border: #fff 0px none;" +
		   			   " width:" + pageElement.getAttribute("width") + "px;" +
					   " height:" + pageElement.getAttribute("height") + "px;" +
					   " filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='" + pageElement.getAttribute("src") + "');";
		fieldset.setAttribute("style", style);
		pageElement.getParentNode().replaceChild(fieldset, pageElement);
		return fieldset;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#writeTextField(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, java.lang.Object, java.lang.String, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, int)
	 */
	protected HTMLElement writeTextField(Object record, Field field, Object fieldValue, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		boolean customAreaEnable = field.getName().indexOf("area")!=-1 && "true".equals(sitePage.getAttribute("customAreaEnable"));
		if(customAreaEnable) { //允许自定义地区
			fieldUrl = "javascript:changeWeatherArea();";
		}
		pageElement = super.writeTextField(record, field, fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode);
		if(customAreaEnable) { //允许自定义地区
			HTMLImageElement img = (HTMLImageElement)pageElement.getOwnerDocument().createElement("img");
			img.setBorder("0");
			img.setAlign("absmiddle");
			img.setSrc(Environment.getContextPath() + "/jeaf/common/img/dropdown.gif");
			img.setClassName("waatherCustomImage");
			img.setAttribute("style", "margin-left: 2px");
			pageElement.appendChild(img);
		}
		return pageElement;
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
}