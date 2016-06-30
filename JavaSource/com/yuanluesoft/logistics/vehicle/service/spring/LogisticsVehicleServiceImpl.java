package com.yuanluesoft.logistics.vehicle.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.gps.model.Location;
import com.yuanluesoft.jeaf.gps.pojo.GpsLocation;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicle;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleDeparture;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleDestination;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleFree;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class LogisticsVehicleServiceImpl extends BusinessServiceImpl implements LogisticsVehicleService {
	private GpsService gpsService; //GPS定位服务
	private PageService pageService; //页面服务
	private ThreadPool threadPool = new ThreadPool(10, 10, 20000); //创建线程池
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof LogisticsVehicle) {
			LogisticsVehicle vehicle = (LogisticsVehicle)record;
			if(vehicle.getLinkmanTel()!=null && vehicle.getLinkmanTel().isEmpty()) {
				vehicle.setLinkmanTel(null);
			}
		}
		record = super.save(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof LogisticsVehicle) {
			LogisticsVehicle vehicle = (LogisticsVehicle)record;
			if(vehicle.getLinkmanTel()!=null && vehicle.getLinkmanTel().isEmpty()) {
				vehicle.setLinkmanTel(null);
			}
		}
		record = super.update(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(com.yuanluesoft.jeaf.database.Record)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		if(record instanceof LogisticsVehicle) {
			String hql = "select LogisticsVehicle.id" +
						 " from LogisticsVehicle LogisticsVehicle" +
						 " where LogisticsVehicle.plateNumber='" + JdbcUtils.resetQuot(((LogisticsVehicle)record).getPlateNumber()) + "'" +
						 " and LogisticsVehicle.id!=" + record.getId();
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return ListUtils.generateList("车牌号为" + ((LogisticsVehicle)record).getPlateNumber() + "的车辆已经登记过");
			}
			hql = "select LogisticsVehicle.plateNumber" +
				  " from LogisticsVehicle LogisticsVehicle" +
				  " where LogisticsVehicle.linkmanTel='" + JdbcUtils.resetQuot(((LogisticsVehicle)record).getLinkmanTel()) + "'" +
				  " and LogisticsVehicle.id!=" + record.getId();
			String plateNumber = (String)getDatabaseService().findRecordByHql(hql);
			if(plateNumber!=null) {
				return ListUtils.generateList("随车联系人号码" + ((LogisticsVehicle)record).getLinkmanTel() + "已经在车辆" + plateNumber + "使用过");
			}
		}
		return super.validateDataIntegrity(record, isNew);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService#loadLogisticsVehicle(java.lang.String)
	 */
	public LogisticsVehicle loadVehicle(String plateNumber) throws ServiceException {
		String hql = "from LogisticsVehicle LogisticsVehicle where LogisticsVehicle.plateNumber='" + JdbcUtils.resetQuot(plateNumber) + "'";
		return (LogisticsVehicle)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService#loadVehicleByLinkmanTel(java.lang.String)
	 */
	public LogisticsVehicle loadVehicleByLinkmanTel(String linkmanTel) throws ServiceException {
		String hql = "from LogisticsVehicle LogisticsVehicle where LogisticsVehicle.linkmanTel='" + JdbcUtils.resetQuot(linkmanTel) + "'";
		LogisticsVehicle vehicle = (LogisticsVehicle)getDatabaseService().findRecordByHql(hql);
		if(vehicle==null) {
			return null;
		}
		//获取用户信息
		LogisticsUser logisticsUser = (LogisticsUser)getDatabaseService().findRecordById(LogisticsUser.class.getName(), vehicle.getUserId());
		if(logisticsUser==null || logisticsUser.getIsDeleted()=='1' || logisticsUser.getIsHalt()=='1') { //用户不存在或者已经停用
			return null;
		}
		return vehicle;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService#updateVehicleStatus(java.lang.String, boolean)
	 */
	public LogisticsVehicle updateVehicleStatus(String linkmanTel, boolean isEmpty) throws ServiceException {
		LogisticsVehicle vehicle = loadVehicleByLinkmanTel(linkmanTel);
		if(vehicle==null) {
			return null;
		}
		//更新"空车/重车"
		vehicle.setIsEmpty(isEmpty ? '1' : '0');
		getDatabaseService().updateRecord(vehicle);
		return vehicle;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService#updateLogisticsVehicleSupply(long, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateLogisticsVehicleSupply(long vehicleSupplyId, boolean isNew, String freeVehicleNumbers, String departureAreaIds, String departureAreas, String destinationAreaIds, String destinationAreas) throws ServiceException {
		if(departureAreaIds==null || departureAreas==null || destinationAreaIds==null || destinationAreas==null) {
			return;
		}
		if(!isNew) {
			getDatabaseService().deleteRecordsByHql("from LogisticsVehicleFree LogisticsVehicleFree where LogisticsVehicleFree.supplyId=" + vehicleSupplyId);
			getDatabaseService().deleteRecordsByHql("from LogisticsVehicleDeparture LogisticsVehicleDeparture where LogisticsVehicleDeparture.supplyId=" + vehicleSupplyId);
			getDatabaseService().deleteRecordsByHql("from LogisticsVehicleDestination LogisticsVehicleDestination where LogisticsVehicleDestination.supplyId=" + vehicleSupplyId);
		}
		//保存车牌号
		if(freeVehicleNumbers!=null && !freeVehicleNumbers.equals("")) {
			String[] values = freeVehicleNumbers.split(",");
			for(int i=0; i<values.length; i++) {
				LogisticsVehicleFree vehicleFree = new LogisticsVehicleFree();
				vehicleFree.setId(UUIDLongGenerator.generateId()); //ID
				vehicleFree.setSupplyId(vehicleSupplyId); //车源ID
				vehicleFree.setPlateNumber(values[i]); //车辆牌号
				getDatabaseService().saveRecord(vehicleFree);
			}
		}
		//保存出发地点
		if(departureAreaIds!=null && !departureAreaIds.equals("")) {
			String[] ids = departureAreaIds.split(",");
			String[] areas = departureAreas.split(",");
			for(int i=0; i<ids.length; i++) {
				LogisticsVehicleDeparture vehicleDeparture = new LogisticsVehicleDeparture();
				vehicleDeparture.setId(UUIDLongGenerator.generateId()); //ID
				vehicleDeparture.setSupplyId(vehicleSupplyId); //车源ID
				vehicleDeparture.setDepartureId(Long.parseLong(ids[i])); //出发地点ID
				vehicleDeparture.setDeparture(areas[i]); //出发地点
				getDatabaseService().saveRecord(vehicleDeparture);
			}
		}
		//保存目的地点
		if(destinationAreaIds!=null && !destinationAreaIds.equals("")) {
			String[] ids = destinationAreaIds.split(",");
			String[] areas = destinationAreas.split(",");
			for(int i=0; i<ids.length; i++) {
				LogisticsVehicleDestination vehicleDestination = new LogisticsVehicleDestination();
				vehicleDestination.setId(UUIDLongGenerator.generateId()); //ID
				vehicleDestination.setSupplyId(vehicleSupplyId); //车源ID
				vehicleDestination.setDestinationId(Long.parseLong(ids[i])); //目的地点ID
				vehicleDestination.setDestination(areas[i]); //目的地点
				getDatabaseService().saveRecord(vehicleDestination);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService#updateVechicleLocation()
	 */
	public void updateVechicleLocation() {
		String hql = "from LogisticsVehicle LogisticsVehicle" +
					 " where not LogisticsVehicle.linkmanTel is null" +
					 " order by LogisticsVehicle.id";
		for(int i=0; ; i+=10) {
			List vehicles = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("gpsLocation"), i, 10); //每次处理10个
			if(vehicles==null || vehicles.isEmpty()) {
				break;
			}
			for(Iterator iterator = vehicles.iterator(); iterator.hasNext();) {
				final LogisticsVehicle vehicle = (LogisticsVehicle)iterator.next();
				try {
					threadPool.execute(new Task() {
						public void process() {
							try {
								Location location = gpsService.getLocation(vehicle.getLinkmanTel(), true); //GPS定位
								if(location!=null) {
									//更新位置
									if(vehicle.getGpsLocation()==null || vehicle.getGpsLocation().isEmpty()) {
										GpsLocation gpsLocation = new GpsLocation();
										gpsLocation.setId(UUIDLongGenerator.generateId()); //ID
										gpsLocation.setRecordId(vehicle.getId()); //主记录ID
										gpsLocation.setPlaceName(location.getPlaceName()); //位置
										getDatabaseService().saveRecord(gpsLocation);
									}
									else {
										GpsLocation gpsLocation = (GpsLocation)vehicle.getGpsLocation().iterator().next();
										gpsLocation.setPlaceName(location.getPlaceName()); //位置
										getDatabaseService().updateRecord(gpsLocation);
									}
								}
							} 
							catch (Exception e) {
								Logger.exception(e);
							}
						}
					});
				}
				catch (Exception e) {
					Logger.exception(e);
					return;
				}
			}
			if(vehicles.size()<10) {
				break;
			}
		}
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

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}