package com.yuanluesoft.jeaf.gps.provider.fsti;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.provider.MapProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * 福建邮科地图服务
 * @author linchuan
 *
 */
public class MapProviderImpl implements MapProvider {
	private String serviceKey = "D22AA7283C619C60"; //服务密钥
	private String placeNameServiceUrl = "http://220.162.239.162:9001/lbsp/sit/xml.api";
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getPlaceName(java.lang.String, java.lang.String)
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException {
		String url = placeNameServiceUrl +
					 "?key=" + serviceKey + //服务密钥
					 "&lat=" + latitude + //手机号码
					 "&lng=" + longitude; //号码类型: CDMA/中国电信
		try {
			String httpContent = HttpUtils.getHttpContent(url, null, false, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
			if(Logger.isDebugEnabled()) {
				Logger.debug("FSTI PlaceNameService: get place name from " + url + ", response is " + httpContent);
			}
			Element rootElement = new XmlParser().parseXmlString(httpContent);
			return rootElement.elementText("WAY"); //返回值: POS/地点名称(如：农业发展银行浦城支行), WAY/道路信息(如：福建省浦城县浦城县中心部), LEN/偏差距离
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getCoordinateByPlaceName(java.lang.String)
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException {
		throw new ServiceException("not implemented");
	}

	/**
	 * @return the placeNameServiceUrl
	 */
	public String getPlaceNameServiceUrl() {
		return placeNameServiceUrl;
	}

	/**
	 * @param placeNameServiceUrl the placeNameServiceUrl to set
	 */
	public void setPlaceNameServiceUrl(String placeNameServiceUrl) {
		this.placeNameServiceUrl = placeNameServiceUrl;
	}

	/**
	 * @return the serviceKey
	 */
	public String getServiceKey() {
		return serviceKey;
	}

	/**
	 * @param serviceKey the serviceKey to set
	 */
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
}