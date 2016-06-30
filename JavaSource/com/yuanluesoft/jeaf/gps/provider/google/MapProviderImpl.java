package com.yuanluesoft.jeaf.gps.provider.google;

import java.net.URLEncoder;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.provider.MapProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * 谷歌地图服务
 * 根据经纬度反向解析地址，有时需要多尝试几次
 * 注意:(摘自：http://code.google.com/intl/zh-CN/apis/maps/faq.html
 * 提交的地址解析请求次数是否有限制？) 如果在 24 小时时段内收到来自一个 IP 地址超过 2500 个地址解析请求， 或从一个 IP
 * 地址提交的地址解析请求速率过快，Google 地图 API 编码器将用 620 状态代码开始响应。 如果地址解析器的使用仍然过多，则从该 IP
 * 地址对 Google 地图 API 地址解析器的访问可能被永久阻止。
 * @author linchuan
 *
 */
public class MapProviderImpl implements MapProvider {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getPlaceName(java.lang.String, java.lang.String)
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException {
		try {
			String url = "http://ditu.google.cn/maps/geo" +
						 "?output=xml" + //csv/xml/json
						 "&key=abcdef" + //密钥,可以随便写一个abcdef
						 "&q=" + latitude + "," + longitude;
			String httpContent = HttpUtils.getHttpContent(url, null, false, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
			if(Logger.isDebugEnabled()) {
				Logger.debug("Google PlaceNameService: get place name from " + url + ", response is " + httpContent);
			}
			Element xmlElement = new XmlParser().parseXmlString(httpContent);
			return xmlElement.element("Response").element("Placemark").elementText("address");
		}
		catch (Exception e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getCoordinateByPlaceName(java.lang.String)
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException {
		try {
			String url = "http://maps.google.com/maps/geo" +
						 "?q=" + URLEncoder.encode(placeName, "utf-8") +
						 "&output=xml" + //csv/xml/json
						 "&key=abc"; //密钥可以随便写一个key=abc
			String httpContent = HttpUtils.getHttpContent(url, null, false, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
			if(Logger.isDebugEnabled()) {
				Logger.debug("Google PlaceNameService: get place name from " + url + ", response is " + httpContent);
			}
			Element xmlElement = new XmlParser().parseXmlString(httpContent);
			String[] coordinates = xmlElement.element("Response").element("Placemark").element("Point").elementText("coordinates").split(",");
			return new Coordinate(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
		}
		catch (Exception e) {
			Logger.exception(e);
			return null;
		}
	}
}