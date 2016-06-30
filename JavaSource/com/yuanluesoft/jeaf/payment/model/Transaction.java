package com.yuanluesoft.jeaf.payment.model;

import java.sql.Timestamp;

/**
 * 交易记录
 * @author linchuan
 *
 */
public class Transaction {
	private Timestamp transactionTime; //交易时间
	private String voucherType; //凭证种类
	private String voucherNumber; //凭证号
	private double money; //发生额/元	
	private double remaining; //余额/元
	private String peerAccountName; //对方户名
	private String peerAccount; //对方帐号
	private String summary; //摘要
	private String remark; //备注
	private String transactionNumber; //账户明细编号-交易流水号
	private String myAccount; //本方账号
	private String myAccountName; //本方账户名称
	private String myBank; //本方账户开户机构
	
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
	 * @return the myAccount
	 */
	public String getMyAccount() {
		return myAccount;
	}
	/**
	 * @param myAccount the myAccount to set
	 */
	public void setMyAccount(String myAccount) {
		this.myAccount = myAccount;
	}
	/**
	 * @return the myAccountName
	 */
	public String getMyAccountName() {
		return myAccountName;
	}
	/**
	 * @param myAccountName the myAccountName to set
	 */
	public void setMyAccountName(String myAccountName) {
		this.myAccountName = myAccountName;
	}
	/**
	 * @return the myBank
	 */
	public String getMyBank() {
		return myBank;
	}
	/**
	 * @param myBank the myBank to set
	 */
	public void setMyBank(String myBank) {
		this.myBank = myBank;
	}
	/**
	 * @return the peerAccount
	 */
	public String getPeerAccount() {
		return peerAccount;
	}
	/**
	 * @param peerAccount the peerAccount to set
	 */
	public void setPeerAccount(String peerAccount) {
		this.peerAccount = peerAccount;
	}
	/**
	 * @return the peerAccountName
	 */
	public String getPeerAccountName() {
		return peerAccountName;
	}
	/**
	 * @param peerAccountName the peerAccountName to set
	 */
	public void setPeerAccountName(String peerAccountName) {
		this.peerAccountName = peerAccountName;
	}
	/**
	 * @return the remaining
	 */
	public double getRemaining() {
		return remaining;
	}
	/**
	 * @param remaining the remaining to set
	 */
	public void setRemaining(double remaining) {
		this.remaining = remaining;
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
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}
	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	/**
	 * @return the transactionTime
	 */
	public Timestamp getTransactionTime() {
		return transactionTime;
	}
	/**
	 * @param transactionTime the transactionTime to set
	 */
	public void setTransactionTime(Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}
	/**
	 * @return the voucherNumber
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}
	/**
	 * @param voucherNumber the voucherNumber to set
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	/**
	 * @return the voucherType
	 */
	public String getVoucherType() {
		return voucherType;
	}
	/**
	 * @param voucherType the voucherType to set
	 */
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
}