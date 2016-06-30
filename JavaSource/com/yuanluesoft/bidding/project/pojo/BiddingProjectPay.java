package com.yuanluesoft.bidding.project.pojo;

import java.sql.Timestamp;

/**
 * 缴费(bidding_project_pay)
 * @author linchuan
 *
 */
public class BiddingProjectPay extends BiddingProjectComponent {
	private double tendereeMoney; //建设单位缴费金额
	private String tendereePay; //建设单位缴费情况,已缴/减免
	private String tendereeReason; //建设单位减免理由
	private double pitchonMoney; //中标单位缴费金额
	private String pitchonPay; //中标单位缴费情况,已缴/减免
	private String pitchonReason; //中标单位减免理由
	private Timestamp payTime; //缴费时间
	
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
	 * @return the pitchonMoney
	 */
	public double getPitchonMoney() {
		return pitchonMoney;
	}
	/**
	 * @param pitchonMoney the pitchonMoney to set
	 */
	public void setPitchonMoney(double pitchonMoney) {
		this.pitchonMoney = pitchonMoney;
	}
	/**
	 * @return the pitchonPay
	 */
	public String getPitchonPay() {
		return pitchonPay;
	}
	/**
	 * @param pitchonPay the pitchonPay to set
	 */
	public void setPitchonPay(String pitchonPay) {
		this.pitchonPay = pitchonPay;
	}
	/**
	 * @return the pitchonReason
	 */
	public String getPitchonReason() {
		return pitchonReason;
	}
	/**
	 * @param pitchonReason the pitchonReason to set
	 */
	public void setPitchonReason(String pitchonReason) {
		this.pitchonReason = pitchonReason;
	}
	/**
	 * @return the tendereeMoney
	 */
	public double getTendereeMoney() {
		return tendereeMoney;
	}
	/**
	 * @param tendereeMoney the tendereeMoney to set
	 */
	public void setTendereeMoney(double tendereeMoney) {
		this.tendereeMoney = tendereeMoney;
	}
	/**
	 * @return the tendereePay
	 */
	public String getTendereePay() {
		return tendereePay;
	}
	/**
	 * @param tendereePay the tendereePay to set
	 */
	public void setTendereePay(String tendereePay) {
		this.tendereePay = tendereePay;
	}
	/**
	 * @return the tendereeReason
	 */
	public String getTendereeReason() {
		return tendereeReason;
	}
	/**
	 * @param tendereeReason the tendereeReason to set
	 */
	public void setTendereeReason(String tendereeReason) {
		this.tendereeReason = tendereeReason;
	}
}
