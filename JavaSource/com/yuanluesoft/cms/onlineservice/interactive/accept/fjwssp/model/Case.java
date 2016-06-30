package com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 行政审批项目
 * @author linchuan
 *
 */
public class Case implements Serializable, Cloneable {
	private String receiverDept;//受理部门
	private String projectName;//受理事项
	private String projectID;//受理申报号
	private String serviceName;//审批事项名称
	private String serviceCode;//审批事项编码
	private String applyName;//申报人
	private Timestamp createTime;//申报日期
	private Date promiseEndDay;//承诺办结日期
	private String handleState;//办件状态

	/**
	 * @return the applyName
	 */
	public String getApplyName() {
		return applyName;
	}
	/**
	 * @param applyName the applyName to set
	 */
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the handleState
	 */
	public String getHandleState() {
		return handleState;
	}
	/**
	 * @param handleState the handleState to set
	 */
	public void setHandleState(String handleState) {
		this.handleState = handleState;
	}
	/**
	 * @return the projectID
	 */
	public String getProjectID() {
		return projectID;
	}
	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the promiseEndDay
	 */
	public Date getPromiseEndDay() {
		return promiseEndDay;
	}
	/**
	 * @param promiseEndDay the promiseEndDay to set
	 */
	public void setPromiseEndDay(Date promiseEndDay) {
		this.promiseEndDay = promiseEndDay;
	}
	/**
	 * @return the receiverDept
	 */
	public String getReceiverDept() {
		return receiverDept;
	}
	/**
	 * @param receiverDept the receiverDept to set
	 */
	public void setReceiverDept(String receiverDept) {
		this.receiverDept = receiverDept;
	}
	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}
	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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
}