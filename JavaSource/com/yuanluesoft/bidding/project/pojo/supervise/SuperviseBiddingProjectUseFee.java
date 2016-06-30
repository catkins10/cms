package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;

/**
 * 缴费(bidding_project_use_fee)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectUseFee extends SuperviseBiddingProjectComponent {
	private double money; //缴费金额
	private Timestamp billingTime; //开票时间
	private Timestamp payTime; //收款时间
	
	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}
	/**
	 * @return the payTime
	 */
	public Timestamp getPayTime() {
		return payTime;
	}
	/**
	 * @param payTime the payTime to set
	 */
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	/**
	 * @return the billingTime
	 */
	public Timestamp getBillingTime() {
		return billingTime;
	}

	/**
	 * @param billingTime the billingTime to set
	 */
	public void setBillingTime(Timestamp billingTime) {
		this.billingTime = billingTime;
	}
}
