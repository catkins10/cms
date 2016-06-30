package com.yuanluesoft.jeaf.gps.service.spring;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.model.PlaceName;
import com.yuanluesoft.jeaf.gps.pojo.GpsPlaceName;
import com.yuanluesoft.jeaf.gps.provider.GpsProvider;
import com.yuanluesoft.jeaf.gps.provider.MapProvider;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.Mutex;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 定位服务
 * @author linchuan
 *
 */
public class GpsServiceImpl implements GpsService {
	private DatabaseService databaseService; //数据库服务
	private List gpsProviders; //定位服务列表
	private List mapProviders; //地图服务列表
	private String mapProviderName; //外部显示的地图供应商名称
	
	private int maxThreads = 10; //最大并发数
	private Mutex mutex = new Mutex();
	
	/**
	 * GPS定位
	 * @param gpsTerminalNumber
	 * @param placeNameRequired 地名是否必须
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocation(String gpsTerminalNumber, boolean placeNameRequired) throws ServiceException {
		mutex.lock(maxThreads, 60000); //最长等待60s
		try {
			Location location = null;
			for(Iterator iterator = gpsProviders.iterator();location==null && iterator.hasNext();) {
				GpsProvider gpsProvider = (GpsProvider)iterator.next();
				try {
					location = gpsProvider.getLocation(gpsTerminalNumber);
					if(location!=null) {
						break;
					}
				}
				catch(Exception e) {
					
				}
			}
			//设置地名
			if(location!=null && placeNameRequired && (location.getPlaceName()==null || location.getPlaceName().isEmpty())) {
				location.setPlaceName(getPlaceName(location.getCoordinate().getLatitude(), location.getCoordinate().getLongitude()));
			}
			return location;
		}
		finally {
			mutex.unlock();
		}
	}
	
	/**
	 * 按IP获取位置信息
	 * @param ip
	 * @param placeNameRequired
	 * @param coordinateRequired
	 * @return
	 * @throws ServiceException
	 */
	public Location getLocationByIP(String ip, boolean placeNameRequired, boolean coordinateRequired) throws ServiceException {
		mutex.lock(maxThreads, 60000); //最长等待60s
		try {
			Location location = null;
			for(Iterator iterator = gpsProviders.iterator(); location==null && iterator.hasNext();) {
				GpsProvider gpsProvider = (GpsProvider)iterator.next();
				try {
					location = gpsProvider.getLocationByIP(ip);
					if(location!=null) {
						break;
					}
				}
				catch(Exception e) {
					
				}
			}
			if(location==null) {
				return null;
			}
			if(location.getPlaceName()!=null && location.getCoordinate()==null) { //地址已知、坐标未知
				if(coordinateRequired) {
					location.setCoordinate(getCoordinateByPlaceName(location.getPlaceName())); //获取坐标
				}
			}
			else if(placeNameRequired && location.getPlaceName()==null) { //地址必须获取、地址为空
				location.setPlaceName(getPlaceName(location.getCoordinate().getLatitude(), location.getCoordinate().getLongitude())); //获取地名
			}
			return location;
		}
		finally {
			mutex.unlock();
		}
	}
	
	/**
	 * 获取地名
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws ServiceException
	 */
	public String getPlaceName(double latitude, double longitude) throws ServiceException {
		//从数据库中获取
		double latitudeShort = Math.round(latitude * 100000) / 100000.0;
		double longitudeShort = Math.round(longitude * 100000) / 100000.0;
		String hql = "from GpsPlaceName GpsPlaceName" +
					 " where GpsPlaceName.latitude=" + latitudeShort +
					 " and GpsPlaceName.longitude=" + longitudeShort;
		GpsPlaceName gpsPlaceName = (GpsPlaceName)databaseService.findRecordByHql(hql);
		if(gpsPlaceName!=null && gpsPlaceName.getLastModified().after(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MONTH, -6))) { //数据存在,且是最近半年内更新的
			return gpsPlaceName.getPlaceName();
		}
		mutex.lock(maxThreads, 60000); //最长等待60s
		try {
			//通过地图服务获取
			String placeName = null;
			for(Iterator iterator = mapProviders.iterator(); placeName==null && iterator.hasNext();) {
				MapProvider mapProvider = (MapProvider)iterator.next();
				try {
					placeName = mapProvider.getPlaceName(latitude, longitude);
					if(placeName!=null && !placeName.isEmpty()) {
						break;
					}
				}
				catch(Exception e) {
					
				}
			}
			if(placeName==null) {
				return null;
			}
			//写入数据库
			if(gpsPlaceName!=null) {
				gpsPlaceName.setPlaceName(placeName); //地名
				gpsPlaceName.setLastModified(DateTimeUtils.now()); //最后更新时间
				databaseService.updateRecord(gpsPlaceName);
			}
			else {
				gpsPlaceName = new GpsPlaceName();
				gpsPlaceName.setId(UUIDLongGenerator.generateId()); //ID
				gpsPlaceName.setLongitude(longitudeShort); //经度,精度5位,大约1.1米
				gpsPlaceName.setLatitude(latitudeShort); //纬度,精度5位,大约1.1米
				gpsPlaceName.setPlaceName(placeName); //地名
				gpsPlaceName.setLastModified(DateTimeUtils.now()); //最后更新时间
				databaseService.saveRecord(gpsPlaceName);
			}
			return placeName;
		}
		finally {
			mutex.unlock();
		}
	}
	
	/**
	 * 获取经纬度
	 * @param placeName
	 * @return
	 * @throws ServiceException
	 */
	public Coordinate getCoordinateByPlaceName(String placeName) throws ServiceException {
		//从数据库中获取
		String hql = "from GpsPlaceName GpsPlaceName" +
					 " where GpsPlaceName.placeName='" + JdbcUtils.resetQuot(placeName) + "'";
		GpsPlaceName gpsPlaceName = (GpsPlaceName)databaseService.findRecordByHql(hql);
		if(gpsPlaceName!=null && gpsPlaceName.getLastModified().after(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MONTH, -6))) { //数据存在,且是最近半年内更新的
			return new Coordinate(gpsPlaceName.getLatitude(), gpsPlaceName.getLongitude());
		}
		mutex.lock(maxThreads, 60000); //最长等待60s
		try {
			//通过地图服务获取
			Coordinate coordinate = null;
			for(Iterator iterator = mapProviders.iterator(); coordinate==null && iterator.hasNext();) {
				MapProvider mapProvider = (MapProvider)iterator.next();
				try {
					coordinate = mapProvider.getCoordinateByPlaceName(placeName);
					if(coordinate!=null) {
						break;
					}
				}
				catch(Exception e) {
					
				}
			}
			if(coordinate==null) {
				return null;
			}
			//写入数据库
			if(gpsPlaceName!=null) {
				gpsPlaceName.setLongitude(Math.round(coordinate.getLongitude() * 100000) / 100000.0); //经度,精度5位,大约1.1米
				gpsPlaceName.setLatitude(Math.round(coordinate.getLatitude() * 100000) / 100000.0); //纬度,精度5位,大约1.1米
				gpsPlaceName.setLastModified(DateTimeUtils.now()); //最后更新时间
				databaseService.updateRecord(gpsPlaceName);
			}
			else {
				gpsPlaceName = new GpsPlaceName();
				gpsPlaceName.setId(UUIDLongGenerator.generateId()); //ID
				gpsPlaceName.setLongitude(Math.round(coordinate.getLongitude() * 100000) / 100000.0); //经度,精度5位,大约1.1米
				gpsPlaceName.setLatitude(Math.round(coordinate.getLatitude() * 100000) / 100000.0); //纬度,精度5位,大约1.1米
				gpsPlaceName.setPlaceName(placeName); //地名
				gpsPlaceName.setLastModified(DateTimeUtils.now()); //最后更新时间
				databaseService.saveRecord(gpsPlaceName);
			}
			return coordinate;
		}
		finally {
			mutex.unlock();
		}
	}
	
	/**
	 * 解析地名
	 * @param placeNameText
	 * @return
	 */
	public PlaceName parsePlaceName(String fullName) {
		if(fullName==null || fullName.isEmpty()) {
			return null;
		}
		PlaceName placeName = new PlaceName();
		placeName.setFullName(fullName);
		fullName = fullName.replaceFirst("中国", "");
		//解析省、市、自治区
		int beginIndex = 0;
		int endIndex = parsePlaceName(fullName, new String[]{"省", "自治区", "上海市", "天津市", "北京市", "重庆市"}, beginIndex);
		if(endIndex!=-1) {
			placeName.setProvince(fullName.substring(beginIndex, endIndex)); //省、市、自治区
			beginIndex = endIndex;
			if(placeName.getProvince().endsWith("市")) {
				placeName.setCity(placeName.getProvince());
			}
		}
		//解析市
		endIndex = parsePlaceName(fullName, new String[]{"市", "自治州"}, beginIndex);
		if(endIndex!=-1) {
			placeName.setCity(fullName.substring(beginIndex, endIndex)); //市
			beginIndex = endIndex;
		}
		//解析县区
		endIndex = parsePlaceName(fullName, new String[]{"县", "区", "市"}, beginIndex);
		if(endIndex!=-1) {
			placeName.setDistrict(fullName.substring(beginIndex, endIndex)); //县
			placeName.setRoad(fullName.substring(endIndex)); //村、路、街道及号码,如金山大道6号
		}
		return placeName;
	}
	
	/**
	 * 解析出某一级别的地名
	 * @param placeNameText
	 * @param keywords
	 * @param beginIndex
	 * @return
	 */
	private int parsePlaceName(String placeNameText, String[] keywords, int beginIndex) {
		for(int i=0; i<keywords.length; i++) {
			int index = placeNameText.indexOf(keywords[i], beginIndex);
			if(index!=-1) {
				return index + keywords[i].length();
			}
		}
		return -1;
	}
	
	/**
	 * @return the gpsServices
	 */
	public List getGpsProviders() {
		return gpsProviders;
	}
	/**
	 * @param gpsServices the gpsServices to set
	 */
	public void setGpsProviders(List gpsServices) {
		this.gpsProviders = gpsServices;
	}

	/**
	 * @return the maxThreads
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * @param maxThreads the maxThreads to set
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
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
	 * @return the mapProviderName
	 */
	public String getMapProviderName() {
		return mapProviderName;
	}

	/**
	 * @param mapProviderName the mapProviderName to set
	 */
	public void setMapProviderName(String mapProviderName) {
		this.mapProviderName = mapProviderName;
	}

	/**
	 * @return the mapProviders
	 */
	public List getMapProviders() {
		return mapProviders;
	}

	/**
	 * @param mapProviders the mapProviders to set
	 */
	public void setMapProviders(List mapProviders) {
		this.mapProviders = mapProviders;
	}
}