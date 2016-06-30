package com.yuanluesoft.cms.monitor.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 监察:采集日志(monitor_capture_log)
 * @author linchuan
 *
 */
public class MonitorCaptureLog extends Record {
	private long unitSqlId; //SQL配置ID
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String captureSql; //SQL
	private Timestamp captureTime; //最后采集时间
	private int isSuccess; //是否成功
	private String captureFailedReason; //采集失败原因
	
	/**
	 * @return the captureFailedReason
	 */
	public String getCaptureFailedReason() {
		return captureFailedReason;
	}
	/**
	 * @param captureFailedReason the captureFailedReason to set
	 */
	public void setCaptureFailedReason(String captureFailedReason) {
		this.captureFailedReason = captureFailedReason;
	}
	/**
	 * @return the captureSql
	 */
	public String getCaptureSql() {
		return captureSql;
	}
	/**
	 * @param captureSql the captureSql to set
	 */
	public void setCaptureSql(String captureSql) {
		this.captureSql = captureSql;
	}
	/**
	 * @return the captureTime
	 */
	public Timestamp getCaptureTime() {
		return captureTime;
	}
	/**
	 * @param captureTime the captureTime to set
	 */
	public void setCaptureTime(Timestamp captureTime) {
		this.captureTime = captureTime;
	}
	/**
	 * @return the isSuccess
	 */
	public int getIsSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
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
	/**
	 * @return the unitSqlId
	 */
	public long getUnitSqlId() {
		return unitSqlId;
	}
	/**
	 * @param unitSqlId the unitSqlId to set
	 */
	public void setUnitSqlId(long unitSqlId) {
		this.unitSqlId = unitSqlId;
	}
}