package com.yuanluesoft.jeaf.gps.provider.fsti.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邮科GPS:手机定位类型(gps_fsti_mobile_type)
 * @author linchuan
 *
 */
public class GpsFstiMobileType extends Record {
	private String mobile; //手机号码
	private String type; //定位类型,BTS/基站定位, GPS/卫星定位, AGPS|辅助卫星定位, GPSONE/CDMA辅助卫星定位
	
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}