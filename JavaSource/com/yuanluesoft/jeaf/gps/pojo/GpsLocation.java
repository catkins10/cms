package com.yuanluesoft.jeaf.gps.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * GPS定位:当前位置(gps_location)
 * @author linchuan
 *
 */
public class GpsLocation extends Record {
	private long recordId; //主记录ID
	private String placeName; //位置
	
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
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
}