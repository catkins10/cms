package com.yuanluesoft.jeaf.payment.model;

import java.sql.Timestamp;

/**
 * 
 * @author linchuan
 *
 */
public class PaymentResult {
	private char transactSuccess = '0'; //是否交易成功
	private Timestamp transactTime; //交易时间
	private String failedReason; //交易失败原因
	private double transactMoney; //实际交易金额
	private double fee; //手续费
	private String transactSn; //支付系统流水号
	private String bankOrderId; //银行端订单ID
	//支付人账户信息
	private String payerAccountName; //账户名称
	private String payerBank; //开户行
	private String payerAccount; //账号
	
	/**
	 * @return the failedReason
	 */
	public String getFailedReason() {
		return failedReason;
	}
	/**
	 * @param failedReason the failedReason to set
	 */
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	/**
	 * @return the transactTime
	 */
	public Timestamp getTransactTime() {
		return transactTime;
	}
	/**
	 * @param transactTime the transactTime to set
	 */
	public void setTransactTime(Timestamp transactTime) {
		this.transactTime = transactTime;
	}
	/**
	 * @return the fee
	 */
	public double getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(double fee) {
		this.fee = fee;
	}
	/**
	 * @return the transactMoney
	 */
	public double getTransactMoney() {
		return transactMoney;
	}
	/**
	 * @param transactMoney the transactMoney to set
	 */
	public void setTransactMoney(double transactMoney) {
		this.transactMoney = transactMoney;
	}
	/**
	 * @return the transactSn
	 */
	public String getTransactSn() {
		return transactSn;
	}
	/**
	 * @param transactSn the transactSn to set
	 */
	public void setTransactSn(String transactSn) {
		this.transactSn = transactSn;
	}
	/**
	 * @return the transactSuccess
	 */
	public char getTransactSuccess() {
		return transactSuccess;
	}
	/**
	 * @param transactSuccess the transactSuccess to set
	 */
	public void setTransactSuccess(char transactSuccess) {
		this.transactSuccess = transactSuccess;
	}
	/**
	 * @return the bankOrderId
	 */
	public String getBankOrderId() {
		return bankOrderId;
	}
	/**
	 * @param bankOrderId the bankOrderId to set
	 */
	public void setBankOrderId(String bankOrderId) {
		this.bankOrderId = bankOrderId;
	}
	/**
	 * @return the payerAccountName
	 */
	public String getPayerAccountName() {
		return payerAccountName;
	}
	/**
	 * @param payerAccountName the payerAccountName to set
	 */
	public void setPayerAccountName(String payerAccountName) {
		this.payerAccountName = payerAccountName;
	}
	/**
	 * @return the payerAccount
	 */
	public String getPayerAccount() {
		return payerAccount;
	}
	/**
	 * @param payerAccount the payerAccount to set
	 */
	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}
	/**
	 * @return the payerBank
	 */
	public String getPayerBank() {
		return payerBank;
	}
	/**
	 * @param payerBank the payerBank to set
	 */
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
}
