package com.yuanluesoft.cms.monitor.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 单位配置:SQL配置(monitor_unit_sql)
 * @author linchuan
 *
 */
public class MonitorUnitSql extends Record {
	private long unitConfigId; //单位配置ID
	private String captureContent; //采集内容,如:政府信息、短信接收
	private String captureContentClass; //采集内容类名称
	private String captureSql; //SQL
	private Timestamp lastCaptureTime; //最后采集时间
	private MonitorUnitConfig unitConfig; //单位配置
	
	/**
	 * @return the captureContent
	 */
	public String getCaptureContent() {
		return captureContent;
	}
	/**
	 * @param captureContent the captureContent to set
	 */
	public void setCaptureContent(String captureContent) {
		this.captureContent = captureContent;
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
	 * @return the unitConfigId
	 */
	public long getUnitConfigId() {
		return unitConfigId;
	}
	/**
	 * @param unitConfigId the unitConfigId to set
	 */
	public void setUnitConfigId(long unitConfigId) {
		this.unitConfigId = unitConfigId;
	}
	/**
	 * @return the unitConfig
	 */
	public MonitorUnitConfig getUnitConfig() {
		return unitConfig;
	}
	/**
	 * @param unitConfig the unitConfig to set
	 */
	public void setUnitConfig(MonitorUnitConfig unitConfig) {
		this.unitConfig = unitConfig;
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
	 * @return the captureContentClass
	 */
	public String getCaptureContentClass() {
		return captureContentClass;
	}
	/**
	 * @param captureContentClass the captureContentClass to set
	 */
	public void setCaptureContentClass(String captureContentClass) {
		this.captureContentClass = captureContentClass;
	}
}