package com.yuanluesoft.jeaf.gps.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * IP定位(gps_ip_location)
 * @author linchuan
 *
 */
public class GpsIPLocation extends Record {
	private String beginIP; //起始IP
	private String endIP; //结束IP
	private String placeName; //地址
	private double longitude; //经度
	private double latitude; //纬度
	private Timestamp created; //创建时间
	private String source; //数据来源
	private String remark; //备注,如：运营商
	
	/**
	 * @return the beginIP
	 */
	public String getBeginIP() {
		return beginIP;
	}
	/**
	 * @param beginIP the beginIP to set
	 */
	public void setBeginIP(String beginIP) {
		this.beginIP = beginIP;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the endIP
	 */
	public String getEndIP() {
		return endIP;
	}
	/**
	 * @param endIP the endIP to set
	 */
	public void setEndIP(String endIP) {
		this.endIP = endIP;
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
	/**
	 * @return the placeName
	 */
	public String getPlaceName() {
		return placeName;
	}
	/**
	 * @param placeName the placeName to set
	 */
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
}