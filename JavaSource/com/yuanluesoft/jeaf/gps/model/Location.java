package com.yuanluesoft.jeaf.gps.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 位置
 * @author linchuan
 *
 */
public class Location implements Serializable, Cloneable {
	private Coordinate coordinate; //坐标,经纬度
	private String speed; //行驶速度
	private String angle; //方向角度
	private double altitude; //海拔高度
	private String placeName; //地名
	private Timestamp created; //查询定位的时间
	
	/**
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}
	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	/**
	 * @return the angle
	 */
	public String getAngle() {
		return angle;
	}
	/**
	 * @param angle the angle to set
	 */
	public void setAngle(String angle) {
		this.angle = angle;
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
	 * @return the speed
	 */
	public String getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}
	/**
	 * @param coordinate the coordinate to set
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
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
}