package com.yuanluesoft.jeaf.gps.provider.maxmind;

import com.maxmind.geoip.LookupService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Coordinate;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.provider.GpsProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class GpsProviderImpl implements GpsProvider {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.service.GeoService#getLocation(java.lang.String)
	 */
	public Location getLocationByIP(String ip) throws ServiceException {
		try {
		    LookupService cl = new LookupService(Environment.getWebinfPath() + "jeaf/gps/maxmind/GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE );
	        com.maxmind.geoip.Location geoLocation = cl.getLocation(ip);
		    cl.close();
		    if(geoLocation==null) {
		    	return null;
		    }
		    Location location = new Location();
		    location.setCoordinate(new Coordinate(geoLocation.latitude, geoLocation.longitude)); //坐标,经纬度
		    //location.setPlaceName(placeName); //地名
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
}