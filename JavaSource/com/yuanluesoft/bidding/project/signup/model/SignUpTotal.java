package com.yuanluesoft.bidding.project.signup.model;

/**
 * 
 * @author linchuan
 *
 */
public class SignUpTotal {
	private int total; //报名数
	private int paymentTotal; //完成缴费的数量
	private int pledgePaymentTotal; //完成保证金缴费的数量
	
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the paymentTotal
	 */
	public int getPaymentTotal() {
		return paymentTotal;
	}
	/**
	 * @param paymentTotal the paymentTotal to set
	 */
	public void setPaymentTotal(int paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	/**
	 * @return the pledgePaymentTotal
	 */
	public int getPledgePaymentTotal() {
		return pledgePaymentTotal;
	}
	/**
	 * @param pledgePaymentTotal the pledgePaymentTotal to set
	 */
	public void setPledgePaymentTotal(int pledgePaymentTotal) {
		this.pledgePaymentTotal = pledgePaymentTotal;
	}
}
