package com.yuanluesoft.appraise.appraiser.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 管理服务对象导入任务(appraiser_import_task)
 * @author linchuan
 *
 */
public class AppraiserImportTask extends Record {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private int taskYear; //年度
	private int taskMonth; //月份
	private Timestamp created; //创建时间
	private Set visitors; //访问者列表
	
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
	 * @return the taskMonth
	 */
	public int getTaskMonth() {
		return taskMonth;
	}
	/**
	 * @param taskMonth the taskMonth to set
	 */
	public void setTaskMonth(int taskMonth) {
		this.taskMonth = taskMonth;
	}
	/**
	 * @return the taskYear
	 */
	public int getTaskYear() {
		return taskYear;
	}
	/**
	 * @param taskYear the taskYear to set
	 */
	public void setTaskYear(int taskYear) {
		this.taskYear = taskYear;
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
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
}