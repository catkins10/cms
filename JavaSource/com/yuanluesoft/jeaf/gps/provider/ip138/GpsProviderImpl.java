package com.yuanluesoft.jeaf.gps.provider.ip138;

import java.util.Calendar;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.pojo.GpsIPLocation;
import com.yuanluesoft.jeaf.gps.provider.GpsProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class GpsProviderImpl implements GpsProvider {
	private String serverUrl = "http://ip138.com/";
	private DatabaseService databaseService; //数据库服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.GeoService#getLocation(java.lang.String)
	 */
	public Location getLocationByIP(String ip) throws ServiceException {
		try {
			String hql = "from GpsIPLocation GpsIPLocation" +
						 " where GpsIPLocation.beginIP='" + ip + "'" +
						 " and GpsIPLocation.source='ip138'";
			GpsIPLocation gpsIPLocation = (GpsIPLocation)databaseService.findRecordByHql(hql);
			if(gpsIPLocation==null || gpsIPLocation.getCreated().before(DateTimeUtils.add(DateTimeUtils.now(), Calendar.DAY_OF_MONTH, -10))) { //记不存在、或者是10天月前保存的
				String url = serverUrl + "ips138.asp?ip=" + ip + "&action=2";
				if(Logger.isTraceEnabled()) {
					Logger.trace("Ip138GpsProvider: get location by ip from " + url);
				}
				String placeName = HttpUtils.getHttpContent(url, null, true, null, 60000, 3, 1000).getResponseBody(); //重试3次,间隔1秒
				int beginIndex = placeName.indexOf("本站主数据：");
				if(beginIndex==-1) {
					return null;
				}
				beginIndex += "本站主数据：".length();
				int endIndex = placeName.indexOf(' ', beginIndex);
				placeName = placeName.substring(beginIndex, endIndex);
				if("保留地址".equals(placeName)) {
					return null;
				}
				if(gpsIPLocation!=null) {
					//更新记录
					gpsIPLocation.setPlaceName(placeName);
					gpsIPLocation.setCreated(DateTimeUtils.now());
					databaseService.updateRecord(gpsIPLocation);
				}
				else {
					//保存到数据库
					gpsIPLocation = new GpsIPLocation();
					gpsIPLocation.setId(UUIDLongGenerator.generateId());
					gpsIPLocation.setBeginIP(ip);
					gpsIPLocation.setEndIP(ip);
					gpsIPLocation.setPlaceName(placeName);
					gpsIPLocation.setCreated(DateTimeUtils.now());
					gpsIPLocation.setSource("ip138");
					databaseService.saveRecord(gpsIPLocation);
				}
			}
		    Location location = new Location();
		    location.setPlaceName(gpsIPLocation.getPlaceName()); //地名
			location.setCreated(DateTimeUtils.now()); //查询定位的时间
		    return location;
		}
		catch(Exception e) {
		    Logger.exception(e);
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.GpsService#getLocation(java.lang.String)
	 */
	public Location getLocation(String gpsTerminalNumber) throws ServiceException {
		throw new SecurityException("not implemented");
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
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
}