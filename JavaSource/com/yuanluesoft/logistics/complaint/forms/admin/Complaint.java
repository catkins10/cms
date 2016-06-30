package com.yuanluesoft.logistics.complaint.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Complaint extends PublicServiceAdminForm {
	private long compaintPersonId; //投诉人ID
	private String compaintPersonName; //投诉人
	private long supplyId; //被投诉货源ID
	private long vehicleSupplyId; //被投诉车源ID
	private String supplyDescription; //货源/车源描述
	private long userId; //被投诉公司/个人ID
	private String userName; //被投诉公司/个人
	private char deleteSupply = '0'; //处理结果：删除货源/车源
	private char addBlacklist = '0'; //处理结果：公司/个人加入黑名单

	private String sanctionResult; //处理结果
	private int supplyComplaintTimes; //货源/车源被投诉次数
	private int userComplaintTimes; //公司/个人被投诉次数
	
	/**
	 * @return the compaintPersonId
	 */
	public long getCompaintPersonId() {
		return compaintPersonId;
	}
	/**
	 * @param compaintPersonId the compaintPersonId to set
	 */
	public void setCompaintPersonId(long compaintPersonId) {
		this.compaintPersonId = compaintPersonId;
	}
	/**
	 * @return the compaintPersonName
	 */
	public String getCompaintPersonName() {
		return compaintPersonName;
	}
	/**
	 * @param compaintPersonName the compaintPersonName to set
	 */
	public void setCompaintPersonName(String compaintPersonName) {
		this.compaintPersonName = compaintPersonName;
	}
	/**
	 * @return the supplyId
	 */
	public long getSupplyId() {
		return supplyId;
	}
	/**
	 * @param supplyId the supplyId to set
	 */
	public void setSupplyId(long supplyId) {
		this.supplyId = supplyId;
	}
	/**
	 * @return the vehicleSupplyId
	 */
	public long getVehicleSupplyId() {
		return vehicleSupplyId;
	}
	/**
	 * @param vehicleSupplyId the vehicleSupplyId to set
	 */
	public void setVehicleSupplyId(long vehicleSupplyId) {
		this.vehicleSupplyId = vehicleSupplyId;
	}
	/**
	 * @return the addBlacklist
	 */
	public char getAddBlacklist() {
		return addBlacklist;
	}
	/**
	 * @param addBlacklist the addBlacklist to set
	 */
	public void setAddBlacklist(char addBlacklist) {
		this.addBlacklist = addBlacklist;
	}
	/**
	 * @return the deleteSupply
	 */
	public char getDeleteSupply() {
		return deleteSupply;
	}
	/**
	 * @param deleteSupply the deleteSupply to set
	 */
	public void setDeleteSupply(char deleteSupply) {
		this.deleteSupply = deleteSupply;
	}
	/**
	 * @return the supplyDescription
	 */
	public String getSupplyDescription() {
		return supplyDescription;
	}
	/**
	 * @param supplyDescription the supplyDescription to set
	 */
	public void setSupplyDescription(String supplyDescription) {
		this.supplyDescription = supplyDescription;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the supplyComplaintTimes
	 */
	public int getSupplyComplaintTimes() {
		return supplyComplaintTimes;
	}
	/**
	 * @param supplyComplaintTimes the supplyComplaintTimes to set
	 */
	public void setSupplyComplaintTimes(int supplyComplaintTimes) {
		this.supplyComplaintTimes = supplyComplaintTimes;
	}
	/**
	 * @return the userComplaintTimes
	 */
	public int getUserComplaintTimes() {
		return userComplaintTimes;
	}
	/**
	 * @param userComplaintTimes the userComplaintTimes to set
	 */
	public void setUserComplaintTimes(int userComplaintTimes) {
		this.userComplaintTimes = userComplaintTimes;
	}
	/**
	 * @return the sanctionResult
	 */
	public String getSanctionResult() {
		return sanctionResult;
	}
	/**
	 * @param sanctionResult the sanctionResult to set
	 */
	public void setSanctionResult(String sanctionResult) {
		this.sanctionResult = sanctionResult;
	}
}