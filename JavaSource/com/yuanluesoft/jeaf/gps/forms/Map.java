package com.yuanluesoft.jeaf.gps.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Map extends ActionForm {
	private boolean selectCity; //是否选择城市
	private double latitude = -1; //纬度
	private double longitude = -1; //经度
	private String fieldName; //字段名称,选择时有效
	private String script; //选择后执行的脚本,选择时有效
	private boolean internal; //是否内部操作
	private boolean ipLocationDisable; //是否禁止按ip来定位,选择时有效
	
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
	 * @return the selectCity
	 */
	public boolean isSelectCity() {
		return selectCity;
	}
	/**
	 * @param selectCity the selectCity to set
	 */
	public void setSelectCity(boolean selectCity) {
		this.selectCity = selectCity;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	/**
	 * @return the internal
	 */
	public boolean isInternal() {
		return internal;
	}
	/**
	 * @param internal the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	/**
	 * @return the ipLocationDisable
	 */
	public boolean isIpLocationDisable() {
		return ipLocationDisable;
	}
	/**
	 * @param ipLocationDisable the ipLocationDisable to set
	 */
	public void setIpLocationDisable(boolean ipLocationDisable) {
		this.ipLocationDisable = ipLocationDisable;
	}
}