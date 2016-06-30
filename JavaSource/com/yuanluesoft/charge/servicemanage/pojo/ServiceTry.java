package com.yuanluesoft.charge.servicemanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 计费系统:服务试用记录(charge_service_try)
 * @author linchuan
 *
 */
public class ServiceTry extends Record {
	private String serviceItem; //服务项目名称,如：学习评测、英语学习器
	private long personId; //用户ID
	private String personName; //用户名
	private Timestamp beginTime; //第一次使用时间
	
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the serviceItem
	 */
	public String getServiceItem() {
		return serviceItem;
	}
	/**
	 * @param serviceItem the serviceItem to set
	 */
	public void setServiceItem(String serviceItem) {
		this.serviceItem = serviceItem;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
