package com.yuanluesoft.cms.complaint.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 诉求件:诉求类型配置(cms_complaint_type)
 * @author linchuan
 *
 */
public class ComplaintType extends Record {
	private String type; //类型
	private int workingDay; //指定工作日
	private long siteId; //绑定的站点ID
	
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
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the workingDay
	 */
	public int getWorkingDay() {
		return workingDay;
	}
	/**
	 * @param workingDay the workingDay to set
	 */
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}
}
