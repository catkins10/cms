package com.yuanluesoft.cms.monitor.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorRecord extends Record {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String recordId; //记录ID
	private Timestamp captureTime; //采集时间
	private Timestamp lastCaptureTime; //最后采集时间
	private int timeoutCheckDisabled; //禁止超时检查,首次采集时不需要超时检查
	private double processDays; //办理用时
	private int timeoutLevel; //超时等级
	private double timeoutDays; //超时天数
	
	/**
	 * 获取监察状态
	 * @return
	 */
	public String getMonitorStatus() {
		return timeoutLevel==0 ? "正常" : "<font style=\"color:#ff0000\">超时" + StringUtils.format(new Double(timeoutDays), "#.#", "") + "天</font>";
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
	 * @return the recordId
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
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
	 * @return the timeoutLevel
	 */
	public int getTimeoutLevel() {
		return timeoutLevel;
	}
	/**
	 * @param timeoutLevel the timeoutLevel to set
	 */
	public void setTimeoutLevel(int timeoutLevel) {
		this.timeoutLevel = timeoutLevel;
	}

	/**
	 * @return the processDays
	 */
	public double getProcessDays() {
		return processDays;
	}

	/**
	 * @param processDays the processDays to set
	 */
	public void setProcessDays(double processDays) {
		this.processDays = processDays;
	}

	/**
	 * @return the lastCaptureTime
	 */
	public Timestamp getLastCaptureTime() {
		return lastCaptureTime;
	}

	/**
	 * @param lastCaptureTime the lastCaptureTime to set
	 */
	public void setLastCaptureTime(Timestamp lastCaptureTime) {
		this.lastCaptureTime = lastCaptureTime;
	}

	/**
	 * @return the timeoutCheckDisabled
	 */
	public int getTimeoutCheckDisabled() {
		return timeoutCheckDisabled;
	}

	/**
	 * @param timeoutCheckDisabled the timeoutCheckDisabled to set
	 */
	public void setTimeoutCheckDisabled(int timeoutCheckDisabled) {
		this.timeoutCheckDisabled = timeoutCheckDisabled;
	}

	/**
	 * @return the timeoutDays
	 */
	public double getTimeoutDays() {
		return timeoutDays;
	}

	/**
	 * @param timeoutDays the timeoutDays to set
	 */
	public void setTimeoutDays(double timeoutDays) {
		this.timeoutDays = timeoutDays;
	}
}