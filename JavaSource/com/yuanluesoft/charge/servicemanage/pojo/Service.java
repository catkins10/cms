package com.yuanluesoft.charge.servicemanage.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 计费:服务包(charge_service)
 * @author linchuan
 *
 */
public class Service extends Record {
	private String serviceName; //名称
	private char studentEnabled = '0'; //是否面向学生
	private char teacherEnabled = '0'; //是否面向教师
	private char genearchEnabled = '0'; //是否面向家长
	private char employeeEnabled = '0'; //是否面向普通用户
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private char deleted = '0'; //是否删除
	private char halt = '0'; //是否停用
	private String remark; //备注
	
	private Set serviceItems; //服务内容
	private Set servicePrices; //服务报价列表	
		
	/**
	 * 获取服务项目标题列表
	 * @return
	 */
	public String getItemTitles() {
		return ListUtils.join(serviceItems, "item", ",", false);
	}
	
	/**
	 * 获取服务标题,服务名称+用户类型
	 * @return
	 */
	public String getTitle() {
		String personTypes = null;
		if(studentEnabled=='1') {
			personTypes = "学生";
		}
		if(teacherEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "教师";
		}
		if(genearchEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "家长";
		}
		if(employeeEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "工作人员";
		}
		return serviceName + "(" + personTypes + ")";
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
	 * @return the deleted
	 */
	public char getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the employeeEnabled
	 */
	public char getEmployeeEnabled() {
		return employeeEnabled;
	}

	/**
	 * @param employeeEnabled the employeeEnabled to set
	 */
	public void setEmployeeEnabled(char employeeEnabled) {
		this.employeeEnabled = employeeEnabled;
	}

	/**
	 * @return the genearchEnabled
	 */
	public char getGenearchEnabled() {
		return genearchEnabled;
	}

	/**
	 * @param genearchEnabled the genearchEnabled to set
	 */
	public void setGenearchEnabled(char genearchEnabled) {
		this.genearchEnabled = genearchEnabled;
	}

	/**
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}

	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the serviceItems
	 */
	public Set getServiceItems() {
		return serviceItems;
	}

	/**
	 * @param serviceItems the serviceItems to set
	 */
	public void setServiceItems(Set serviceItems) {
		this.serviceItems = serviceItems;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the servicePrices
	 */
	public Set getServicePrices() {
		return servicePrices;
	}

	/**
	 * @param servicePrices the servicePrices to set
	 */
	public void setServicePrices(Set servicePrices) {
		this.servicePrices = servicePrices;
	}

	/**
	 * @return the studentEnabled
	 */
	public char getStudentEnabled() {
		return studentEnabled;
	}

	/**
	 * @param studentEnabled the studentEnabled to set
	 */
	public void setStudentEnabled(char studentEnabled) {
		this.studentEnabled = studentEnabled;
	}

	/**
	 * @return the teacherEnabled
	 */
	public char getTeacherEnabled() {
		return teacherEnabled;
	}

	/**
	 * @param teacherEnabled the teacherEnabled to set
	 */
	public void setTeacherEnabled(char teacherEnabled) {
		this.teacherEnabled = teacherEnabled;
	}
}
