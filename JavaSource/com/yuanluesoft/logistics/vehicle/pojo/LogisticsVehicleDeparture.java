package com.yuanluesoft.logistics.vehicle.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 车源:出发地点(logistics_vehicle_departure)
 * @author linchuan
 *
 */
public class LogisticsVehicleDeparture extends Record {
	private long supplyId; //车源ID
	private long departureId; //出发地点ID
	private String departure; //出发地点
	
	/**
	 * @return the departure
	 */
	public String getDeparture() {
		return departure;
	}
	/**
	 * @param departure the departure to set
	 */
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	/**
	 * @return the departureId
	 */
	public long getDepartureId() {
		return departureId;
	}
	/**
	 * @param departureId the departureId to set
	 */
	public void setDepartureId(long departureId) {
		this.departureId = departureId;
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
}