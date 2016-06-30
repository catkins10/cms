package com.yuanluesoft.fet.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:参加活动目标任务
 * @author linchuan
 *
 */
public class FetProjectTarget extends Record {
	private String fairName; //活动名称
	private long fairId; //活动ID
	private String fairNumber; //届别
	private String county; //区县
	private int signTarget; //总签约目标
	private int contractTarget; //合同项目目标
	private int orderTarget; //意向项目目标
	
	/**
	 * @return the contractTarget
	 */
	public int getContractTarget() {
		return contractTarget;
	}
	/**
	 * @param contractTarget the contractTarget to set
	 */
	public void setContractTarget(int contractTarget) {
		this.contractTarget = contractTarget;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the fairNumber
	 */
	public String getFairNumber() {
		return fairNumber;
	}
	/**
	 * @param fairNumber the fairNumber to set
	 */
	public void setFairNumber(String fairNumber) {
		this.fairNumber = fairNumber;
	}
	/**
	 * @return the orderTarget
	 */
	public int getOrderTarget() {
		return orderTarget;
	}
	/**
	 * @param orderTarget the orderTarget to set
	 */
	public void setOrderTarget(int orderTarget) {
		this.orderTarget = orderTarget;
	}
	/**
	 * @return the signTarget
	 */
	public int getSignTarget() {
		return signTarget;
	}
	/**
	 * @param signTarget the signTarget to set
	 */
	public void setSignTarget(int signTarget) {
		this.signTarget = signTarget;
	}
	/**
	 * @return the fairId
	 */
	public long getFairId() {
		return fairId;
	}
	/**
	 * @param fairId the fairId to set
	 */
	public void setFairId(long fairId) {
		this.fairId = fairId;
	}
	/**
	 * @return the fairName
	 */
	public String getFairName() {
		return fairName;
	}
	/**
	 * @param fairName the fairName to set
	 */
	public void setFairName(String fairName) {
		this.fairName = fairName;
	}
}
