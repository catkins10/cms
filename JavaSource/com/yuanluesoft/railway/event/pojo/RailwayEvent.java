package com.yuanluesoft.railway.event.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 问题记录(railway_event)
 * @author linchuan
 *
 */
public class RailwayEvent extends Record {
	private long importId; //导入日志ID
	private String sn; //信息单号
	private String code; //问题代码
	private long personId; //责任人ID
	private String personName; //责任人
	private String checker; //检查人
	private Timestamp checkTime; //检查时间
	private String checkAddress; //检查地点
	private String eventLevel; //问题层级
	private String eventCategory; //问题类别
	private String description; //检查内容
	private double deduct; //扣款
	
	/**
	 * @return the checkAddress
	 */
	public String getCheckAddress() {
		return checkAddress;
	}
	/**
	 * @param checkAddress the checkAddress to set
	 */
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}
	/**
	 * @return the checker
	 */
	public String getChecker() {
		return checker;
	}
	/**
	 * @param checker the checker to set
	 */
	public void setChecker(String checker) {
		this.checker = checker;
	}
	/**
	 * @return the checkTime
	 */
	public Timestamp getCheckTime() {
		return checkTime;
	}
	/**
	 * @param checkTime the checkTime to set
	 */
	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the deduct
	 */
	public double getDeduct() {
		return deduct;
	}
	/**
	 * @param deduct the deduct to set
	 */
	public void setDeduct(double deduct) {
		this.deduct = deduct;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the eventCategory
	 */
	public String getEventCategory() {
		return eventCategory;
	}
	/**
	 * @param eventCategory the eventCategory to set
	 */
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	/**
	 * @return the eventLevel
	 */
	public String getEventLevel() {
		return eventLevel;
	}
	/**
	 * @param eventLevel the eventLevel to set
	 */
	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}
	/**
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
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
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
}