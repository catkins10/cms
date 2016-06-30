package com.yuanluesoft.jeaf.gps.provider.qqwry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.pojo.GpsIPLocation;
import com.yuanluesoft.jeaf.gps.provider.GpsProvider;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class GpsProviderImpl implements GpsProvider {
	private DatabaseService databaseService; //数据库服务
	private DistributionService distributionService; //分布式服务
	
	public void init() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					updateIPLocation();
				}
				catch (Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		}, 20000);
	}

	/**
	 * 更新IP定位表
	 *
	 */
	private void updateIPLocation() throws Exception {
		if(!distributionService.isMasterServer(true)) { //不是主服务器,不需要执行更新
			return;
		}
		String hql = "select GpsIPLocation.created" +
					 " from GpsIPLocation GpsIPLocation" +
					 " where GpsIPLocation.source='qqwry'" +
					 " order by GpsIPLocation.created DESC";
		Timestamp lastCreated = (Timestamp)databaseService.findRecordByHql(hql); //最后更新时间
		File qqwryFile = new File(Environment.getWebinfPath() + "jeaf/gps/qqwry/qqwry.txt");
		if(lastCreated!=null && lastCreated.getTime()>=qqwryFile.lastModified()) {
			return;
		}
		FileInputStream input = null;
		InputStreamReader fileReader = null;
		BufferedReader reader = null;
		try {
			input = new FileInputStream(qqwryFile);
			input.skip(3); //跳过 EF BB BF
			fileReader = new InputStreamReader(input, "utf-8");
			reader = new BufferedReader(fileReader);
			String line;
			while((line=reader.readLine())!=null && !line.isEmpty()) {
				try {
					//解析IP定位记录,格式：1.2.2.0         1.2.2.255       中国  CZ88.NET
					String beginIP = formatIP(line.substring(0, 16).trim());
					String endIP = formatIP(line.substring(16, 32).trim());
					int index = line.indexOf(' ', 32);
					String placeName = line.substring(32, index).trim();
					String remark = line.substring(index + 1).trim();
					if(remark.length()>50) {
						remark = remark.substring(0, 50);
					}
					hql = "from GpsIPLocation GpsIPLocation" +
						  " where GpsIPLocation.beginIP='" + beginIP + "'" +
						  " and GpsIPLocation.endIP='" + endIP + "'";
					GpsIPLocation gpsIPLocation = (GpsIPLocation)databaseService.findRecordByHql(hql);
					if(gpsIPLocation!=null) { //记录已经存在
						if(!placeName.equals(gpsIPLocation.getPlaceName())) { //地址已改变
							gpsIPLocation.setPlaceName(placeName);
							gpsIPLocation.setLatitude(-1);
							gpsIPLocation.setLongitude(-1);
						}
						gpsIPLocation.setRemark(remark); //备注
						gpsIPLocation.setSource("qqwry"); //数据来源
						gpsIPLocation.setCreated(DateTimeUtils.now()); //创建时间
						databaseService.updateRecord(gpsIPLocation);
					}
					else {
						gpsIPLocation = new GpsIPLocation();
						gpsIPLocation.setId(UUIDLongGenerator.generateId()); //ID
						gpsIPLocation.setBeginIP(beginIP); //起始IP
						gpsIPLocation.setEndIP(endIP); //结束IP
						gpsIPLocation.setPlaceName(placeName); //地址
						gpsIPLocation.setLongitude(-1); //经度
						gpsIPLocation.setLatitude(-1); //纬度
						gpsIPLocation.setCreated(DateTimeUtils.now()); //创建时间
						gpsIPLocation.setSource("qqwry"); //数据来源
						gpsIPLocation.setRemark(remark); //备注,如：运营商
						databaseService.saveRecord(gpsIPLocation);
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				fileReader.close();
			}
			catch(Exception e) {
				
			}
			try {
				input.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 格式化IP
	 * @param ip
	 * @return
	 */
	private String formatIP(String ip) {
		boolean ipv6 = ip.indexOf('.')==-1;
		String[] values = ip.split(ipv6 ? ":" : "\\.");
		for(int i=0; i<values.length; i++) {
			ip = (i==0 ? "" : ip + (ipv6 ? ":" : ".")) + ("000".substring(0, 3 - values[i].length())) + values[i];
		}
		return ip;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.provider.GpsProvider#getLocation(java.lang.String)
	 */
	public Location getLocation(String gpsTerminalNumber) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.gps.provider.GpsProvider#getLocationByIP(java.lang.String)
	 */
	public Location getLocationByIP(String ip) throws ServiceException {
		ip = formatIP(ip);
		String hql = "from GpsIPLocation GpsIPLocation" +
					 " where GpsIPLocation.beginIP<='" + ip + "'" +
					 " and GpsIPLocation.endIP>='" + ip + "'" +
					 " order by GpsIPLocation.created DESC";
		GpsIPLocation gpsIPLocation = (GpsIPLocation)databaseService.findRecordByHql(hql);
		if(gpsIPLocation==null) {
			return null;
		}
		Location location = new Location();
		location.setPlaceName(gpsIPLocation.getPlaceName()); //地名
		location.setCreated(DateTimeUtils.now()); //查询定位的时间
		return location;
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
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}
}