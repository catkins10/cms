package com.yuanluesoft.traffic.busline.forms.admin;

import com.yuanluesoft.traffic.busline.pojo.BusLineStation;

/**
 * 
 * @author lmiky
 *
 */
public class Station extends BusLine {
	private BusLineStation station = new BusLineStation();

	/**
	 * @return the station
	 */
	public BusLineStation getStation() {
		return station;
	}

	/**
	 * @param station the station to set
	 */
	public void setStation(BusLineStation station) {
		this.station = station;
	}
}