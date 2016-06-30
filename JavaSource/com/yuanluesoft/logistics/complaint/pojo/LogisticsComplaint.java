package com.yuanluesoft.logistics.complaint.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 投诉(logistics_complaint)
 * @author linchuan
 *
 */
public class LogisticsComplaint extends PublicService {
	private long compaintPersonId; //投诉人ID
	private String compaintPersonName; //投诉人
	private long supplyId; //被投诉货源ID
	private long vehicleSupplyId; //被投诉车源ID
	private String supplyDescription; //货源/车源描述
	private long userId; //被投诉公司/个人ID
	private String userName; //被投诉公司/个人
	private char deleteSupply = '0'; //处理结果：删除货源/车源
	private char addBlacklist = '0'; //处理结果：公司/个人加入黑名单
	
	/**
	 * 处理结果
	 * @return
	 */
	public String getSanctionResult() {
		try {
			if(getWorkItems()!=null && !getWorkItems().isEmpty()) {
				return "在处理";
			}
		}
		catch(Exception e) {
			
		}
		if(deleteSupply!='1' && addBlacklist!='1') {
			return "不予处理";
		}
		String result = deleteSupply=='1' ? "已删除被投诉的" + (supplyId>0 ? "货源" : "车源") : null;
		if(addBlacklist=='1') {
			result = (result==null ? "" : result + "，") + "已将被投诉的公司(个人)加入黑名单";
		}
		return result;
	}
	
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
}