package com.yuanluesoft.jeaf.gps.provider.mapabc;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.provider.MapProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * MAPABC地名服务
 * @author linchuan
 *
 */
public class MapProviderImpl implements MapProvider {
	private String serviceKey = "947518d877fb275850a375d795be6a44c27ab526ef632290ee4f46327a7d0faac6c032378101477f"; //服务密钥
	private String placeNameServiceUrl = "http://search1.mapabc.com/sisserver";
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.PlaceNameService#getPlaceName(java.lang.String, java.lang.String)
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException {
		//请求解析的url
		String url = placeNameServiceUrl +
					 "?highLight=false" +
					 "&config=SPAS" +
					 "&ver=2.0" +
					 "&resType=xml" +
					 "&enc=utf-8" +
					 "&spatialXml=%3C?xml%20version=%221.0%22%20encoding=%22UTF-8%22?%3E%0D%0A" +
					 "%3Cspatial_request%20method=%22searchPoint%22%3E" +
					 "%3Cx%3E" + longitude +
					 "%3C/x%3E%3Cy%3E" + latitude +
					 "%3C/y%3E%3Cxs%3E%3C/xs%3E" +
					 "%3Cys%3E%3C/ys%3E" +
					 "%3CpoiNumber%3E10" +
					 "%3C/poiNumber%3E" +
					 "%3Crange%3E3000" +
					 "%3C/range%3E" +
					 "%3Cpattern%3E0" +
					 "%3C/pattern%3E" +
					 "%3CroadLevel%3E0" +
					 "%3C/roadLevel%3E" +
					 "%3Cexkey%3E" +
					 "%3C/exkey%3E" +
					 "%3C/spatial_request%3E" +
					 "&a_k=" + serviceKey +
					 "&ctx=1902286" +
					 "&a_nocache=615802291184";
		try {
			String content = HttpUtils.getHttpContent(url, null, true, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
			System.out.println("*********content:" + content);
			//返回值格式: <spatial_response type="SpatialBean" servername="APISever-01" versionname="1.0.7.3"><SpatialBean ver="1.0"><Province ver="1.0"><name>江西省</name><code>360000</code></Province><City ver="1.0"><name>南昌市</name><code>360100</code><telnum>0791</telnum></City><District ver="1.0"><name>新建县</name><code>360122</code><x>116.07</x><y>28.9736</y><bounds>115.534355163574;28.3385372161865;115.84407043457;28.768611907959</bounds></District><roadList type="list"/><poiList type="list"><poi><match>10</match><type>地名地址信息;行政地名;村庄级地名</type><id></id><citycode>0791</citycode><extid></extid><pguid>B031704GJY</pguid><timestamp></timestamp><name>五房村</name><srctype>basepoi</srctype><gridcode>4315379921</gridcode><address></address><tel></tel><icon></icon><url></url><buscode></buscode><distance>735</distance><direction>WestSouth</direction><xml></xml><en_type></en_type><en_name></en_name><en_address></en_address><x>115.992947</x><y>28.997585</y></poi><poi><match>10</match><type>地名地址信息;行政地名;村庄级地名</type><id></id><citycode>0791</citycode><extid></extid><pguid>B031704GKH</pguid><timestamp></timestamp><name>新增圩</name><srctype>basepoi</srctype><gridcode>4315470911</gridcode><address></address><tel></tel><icon></icon><url></url><buscode></buscode><distance>751</distance><direction>WestSouth</direction><xml></xml><en_type></en_type><en_name></en_name><en_address></en_address><x>115.994914</x><y>29.005109</y></poi></poiList><crossPoiList type="list"/></SpatialBean></spatial_response>
			Element rootElement = new XmlParser().parseXmlString(content).element("SpatialBean");
			String placeName = null;
			Element provinceElement = rootElement.element("Province");
			if(provinceElement!=null) {
				placeName = provinceElement.elementText("name");
			}
			Element cityElement = rootElement.element("City");
			if(cityElement!=null) {
				placeName = (placeName==null ? "" : placeName) + cityElement.elementText("name");
			}
			Element districtElement = rootElement.element("District");
			if(districtElement!=null) {
				placeName = (placeName==null ? "" : placeName) + districtElement.elementText("name");
			}
			return placeName;
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