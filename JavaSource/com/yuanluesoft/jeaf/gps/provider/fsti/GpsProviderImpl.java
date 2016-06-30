package com.yuanluesoft.jeaf.gps.provider.fsti;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.provider.GpsProvider;
import com.yuanluesoft.jeaf.gps.provider.fsti.pojo.GpsFstiMobileType;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 福建邮科GPS服务
 * @author linchuan
 *
 */
public class GpsProviderImpl implements GpsProvider {
	private String serviceKey = "D22AA7283C619C60"; //服务密钥
	private String locationServiceUrl = "http://220.162.239.162:9001/lbsp/cmd/xml.api"; //邮科GPS定位接口网址
	private DatabaseService databaseService; //数据库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.GpsLocationService#getLocation(java.lang.String)
	 */
	public Location getLocation(String gpsTerminalNumber) throws ServiceException {
		//获取定位类型
		String hql = "from GpsFstiMobileType GpsFstiMobileType where GpsFstiMobileType.mobile='" + JdbcUtils.resetQuot(gpsTerminalNumber) + "'";
		GpsFstiMobileType mobileType = databaseService==null ? null : (GpsFstiMobileType)databaseService.findRecordByHql(hql);
		Location location = null;
		if(mobileType!=null) {
			if((location = getLocation(gpsTerminalNumber, mobileType.getType()))!=null) {
				return location;
			}
			try {
				Thread.sleep(1000); //等待1s后重试
			}
			catch (InterruptedException e) {
			
			}
		}
		for(int j=0; j<2; j++) { //重试2遍
			String[] types = {"BTS", "GPSONE", "GPS", "AGPS"}; //终端类型: BTS/基站定位, GPS/卫星定位, AGPS|辅助卫星定位, GPSONE/CDMA辅助卫星定位
			int i=0;
			for(; i<types.length; i++) {
				if((location = getLocation(gpsTerminalNumber, types[i]))!=null) {
					break;
				}
			}
			if(location==null) { //没有找到位置
				try {
					Thread.sleep(1000); //等待1s后重试
				}
				catch (InterruptedException e) {
				
				}
				continue;
			}
			//保存定位类型
			if(databaseService!=null) {
				if(mobileType==null) {
					mobileType = new GpsFstiMobileType();
					mobileType.setId(UUIDLongGenerator.generateId()); //ID
					mobileType.setMobile(gpsTerminalNumber); //手机号码
					mobileType.setType(types[i]); //定位类型,BTS/基站定位, GPS/卫星定位, AGPS|辅助卫星定位, GPSONE/CDMA辅助卫星定位
					databaseService.saveRecord(mobileType);
				}
				else if(!mobileType.getType().equals(types[i])) { //定位方式已经改变了
					mobileType.setType(types[i]); //定位类型,BTS/基站定位, GPS/卫星定位, AGPS|辅助卫星定位, GPSONE/CDMA辅助卫星定位
					databaseService.updateRecord(mobileType);
				}
			}
			return location;
		}
		return null;
	}
	
	/**
	 * 获取位置
	 * @param gpsTerminalNumber
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	private Location getLocation(String gpsTerminalNumber, String type) throws ServiceException {
		String url = locationServiceUrl +
					 "?key=" + serviceKey + //服务密钥
					 "&sim=" + gpsTerminalNumber + //手机号码
					 "&isp=CDMA" + //号码类型: CDMA/中国电信
					 "&ter=" + type + //终端类型: BTS/基站定位, GPS/卫星定位, AGPS|辅助卫星定位, GPSONE/CDMA辅助卫星定位
					 "&oem=G000" + //设备网关: G000/通用网关
					 "&crd=WGS" + //坐标类型: WGS/WGS84坐标, MARS/WGS84加密坐标
					 "&cmd=C001" + //指令编码: C000/链路检测, C001/定位指令
					 "&syn=SYNC"; //交互方式: SYNC/同步交互, ASYNC/异步交互
		try {
			String httpContent = HttpUtils.getHttpContent(url, null, true, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
			if(Logger.isDebugEnabled()) {
				Logger.debug("FSTI GpsLocationService: get location from " + url + ", response is " + httpContent);
			}
			Element xmlElement = new XmlParser().parseXmlString(httpContent);
			String lat = xmlElement.elementText("LAT");
			if(lat==null) {
				return null;
			}
			Location location = new Location();
			location.setCoordinate(new Coordinate(Double.parseDouble(lat), Double.parseDouble(xmlElement.elementText("LNG")))); //坐标
			location.setSpeed(xmlElement.elementText("SPD")); //行驶速度
			location.setAngle(xmlElement.elementText("ANG")); //方向角度
			location.setAltitude(Double.parseDouble(xmlElement.elementText("ALT"))); //海拔高度
			location.setCreated(DateTimeUtils.now()); //查询定位的时间
			return location;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.GpsService#getLocationByIP(java.lang.String)
	 */
	public Location getLocationByIP(String ip) throws ServiceException {
		throw new SecurityException("not implemented");
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

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the locationServiceUrl
	 */
	public String getLocationServiceUrl() {
		return locationServiceUrl;
	}

	/**
	 * @param locationServiceUrl the locationServiceUrl to set
	 */
	public void setLocationServiceUrl(String locationServiceUrl) {
		this.locationServiceUrl = locationServiceUrl;
	}
}