package com.yuanluesoft.jeaf.gps.model;

import java.io.Serializable;

/**
 * 坐标
 * @author linchuan
 *
 */
public class Coordinate  implements Serializable, Cloneable {
	private double latitude; //纬度
	private double longitude; //经度
	
	public Coordinate(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}