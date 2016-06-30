package com.yuanluesoft.logistics.vehicle.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.gps.model.Location;

/**
 * 车源:载货车辆(logistics_vehicle_free)
 * @author linchuan
 *
 */
public class LogisticsVehicleFree extends Record {
	private long supplyId; //车源ID
	private String plateNumber; //车辆牌号
	
	//扩展属性
	private Location location; //所在位置
	
	/**
	 * @return the plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}
	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	/**
	 * @return the supplyId
	 */
	public long getSupplyId() {
		return supplyId;
	}
	/**
	 * @param supplyId the supplyId to set
	 */
	public void setSupplyId(long supplyId) {
		this.supplyId = supplyId;
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
}