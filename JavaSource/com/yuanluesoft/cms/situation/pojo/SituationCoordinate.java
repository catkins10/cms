package com.yuanluesoft.cms.situation.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 民情回应:上报或协调其它单位(cms_situation_coordinate)
 * @author linchuan
 *
 */
public class SituationCoordinate extends Record {
	private long situationId; //民情ID
	private int isHigher; //是否上报
	private long unitId; //单位ID
	private String unitName; //单位名称
	private Timestamp coordinateTime; //协调时间
	private String coordinateReason; //协调原因
	private long coordinateUnitId; //协调单位ID
	private String coordinateUnitName; //协调单位名称
	
	/**
	 * @return the coordinateReason
	 */
	public String getCoordinateReason() {
		return coordinateReason;
	}
	/**
	 * @param coordinateReason the coordinateReason to set
	 */
	public void setCoordinateReason(String coordinateReason) {
		this.coordinateReason = coordinateReason;
	}
	/**
	 * @return the coordinateTime
	 */
	public Timestamp getCoordinateTime() {
		return coordinateTime;
	}
	/**
	 * @param coordinateTime the coordinateTime to set
	 */
	public void setCoordinateTime(Timestamp coordinateTime) {
		this.coordinateTime = coordinateTime;
	}
	/**
	 * @return the coordinateUnitId
	 */
	public long getCoordinateUnitId() {
		return coordinateUnitId;
	}
	/**
	 * @param coordinateUnitId the coordinateUnitId to set
	 */
	public void setCoordinateUnitId(long coordinateUnitId) {
		this.coordinateUnitId = coordinateUnitId;
	}
	/**
	 * @return the coordinateUnitName
	 */
	public String getCoordinateUnitName() {
		return coordinateUnitName;
	}
	/**
	 * @param coordinateUnitName the coordinateUnitName to set
	 */
	public void setCoordinateUnitName(String coordinateUnitName) {
		this.coordinateUnitName = coordinateUnitName;
	}
	/**
	 * @return the isHigher
	 */
	public int getIsHigher() {
		return isHigher;
	}
	/**
	 * @param isHigher the isHigher to set
	 */
	public void setIsHigher(int isHigher) {
		this.isHigher = isHigher;
	}
	/**
	 * @return the situationId
	 */
	public long getSituationId() {
		return situationId;
	}
	/**
	 * @param situationId the situationId to set
	 */
	public void setSituationId(long situationId) {
		this.situationId = situationId;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}