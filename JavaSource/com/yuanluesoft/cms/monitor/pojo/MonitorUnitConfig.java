package com.yuanluesoft.cms.monitor.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 单位配置(monitor_unit_config)
 * @author linchuan
 *
 */
public class MonitorUnitConfig extends Record {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String ip; //IP地址
	private String jdbcUrl; //JDBC URL
	private String dbUserName; //数据库用户名
	private String dbPassword; //数据库密码
	private String captureTime; //采集时间点,如果没有设置,设为最后一个单位的时间加5分钟
	private int captureDays = 1; //采集周期,以天为单位,默认1天
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private Set sqls; //SQL列表
	
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
	 * @return the sqls
	 */
	public Set getSqls() {
		return sqls;
	}
	/**
	 * @param sqls the sqls to set
	 */
	public void setSqls(Set sqls) {
		this.sqls = sqls;
	}
	/**
	 * @return the captureTime
	 */
	public String getCaptureTime() {
		return captureTime;
	}
	/**
	 * @param captureTime the captureTime to set
	 */
	public void setCaptureTime(String captureTime) {
		this.captureTime = captureTime;
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}
	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	/**
	 * @return the dbUserName
	 */
	public String getDbUserName() {
		return dbUserName;
	}
	/**
	 * @param dbUserName the dbUserName to set
	 */
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	/**
	 * @return the captureDays
	 */
	public int getCaptureDays() {
		return captureDays;
	}
	/**
	 * @param captureDays the captureDays to set
	 */
	public void setCaptureDays(int captureDays) {
		this.captureDays = captureDays;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}