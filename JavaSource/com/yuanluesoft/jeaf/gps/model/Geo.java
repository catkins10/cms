package com.yuanluesoft.jeaf.gps.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Geo implements Serializable, Cloneable {
	private String longitude; //经度坐标 
	private String latitude; //维度坐标 
	private String city; //所在城市 
	private String province; //所在省份 
	private String address; //所在的实际地址 
	private String more; //更多信息
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the more
	 */
	public String getMore() {
		return more;
	}
	/**
	 * @param more the more to set
	 */
	public void setMore(String more) {
		this.more = more;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
}