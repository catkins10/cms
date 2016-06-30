package com.yuanluesoft.jeaf.payment.paymentmethod.icbcbank.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工商银行:支付记录(payment_icbcbank_order)
 * @author linchuan
 *
 */
public class IcbcbankOrder extends Record {
	private long paymentId; //支付单ID
	private String orderNum; //订单号
	private Timestamp tranDate; //交易日期
	private String shopCode; //商家号码
	private String shopAccount; //商城账号
	private String tranSerialNum; //指令序号
	private String tranStat; //订单处理状态
	private String bankRem; //指令错误信息
	private double amount; //订单总金额
	private String currType; //支付币种
	private String tranTime; //返回通知日期时间
	private String payeeAcct; //收款人账号
	private String payeeName; //收款人户名
	private String joinFlag; //校验联名标志
	private String merJoinFlag; //商城联名标志
	private String custJoinFlag; //客户联名标志
	private String custJoinNum; //联名会员号
	private String certId; //商户签名证书ID
	
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the bankRem
	 */
	public String getBankRem() {
		return bankRem;
	}
	/**
	 * @param bankRem the bankRem to set
	 */
	public void setBankRem(String bankRem) {
		this.bankRem = bankRem;
	}
	/**
	 * @return the certId
	 */
	public String getCertId() {
		return certId;
	}
	/**
	 * @param certId the certId to set
	 */
	public void setCertId(String certId) {
		this.certId = certId;
	}
	/**
	 * @return the currType
	 */
	public String getCurrType() {
		return currType;
	}
	/**
	 * @param currType the currType to set
	 */
	public void setCurrType(String currType) {
		this.currType = currType;
	}
	/**
	 * @return the custJoinFlag
	 */
	public String getCustJoinFlag() {
		return custJoinFlag;
	}
	/**
	 * @param custJoinFlag the custJoinFlag to set
	 */
	public void setCustJoinFlag(String custJoinFlag) {
		this.custJoinFlag = custJoinFlag;
	}
	/**
	 * @return the custJoinNum
	 */
	public String getCustJoinNum() {
		return custJoinNum;
	}
	/**
	 * @param custJoinNum the custJoinNum to set
	 */
	public void setCustJoinNum(String custJoinNum) {
		this.custJoinNum = custJoinNum;
	}
	/**
	 * @return the joinFlag
	 */
	public String getJoinFlag() {
		return joinFlag;
	}
	/**
	 * @param joinFlag the joinFlag to set
	 */
	public void setJoinFlag(String joinFlag) {
		this.joinFlag = joinFlag;
	}
	/**
	 * @return the merJoinFlag
	 */
	public String getMerJoinFlag() {
		return merJoinFlag;
	}
	/**
	 * @param merJoinFlag the merJoinFlag to set
	 */
	public void setMerJoinFlag(String merJoinFlag) {
		this.merJoinFlag = merJoinFlag;
	}
	/**
	 * @return the orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * @return the payeeAcct
	 */
	public String getPayeeAcct() {
		return payeeAcct;
	}
	/**
	 * @param payeeAcct the payeeAcct to set
	 */
	public void setPayeeAcct(String payeeAcct) {
		this.payeeAcct = payeeAcct;
	}
	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}
	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the shopAccount
	 */
	public String getShopAccount() {
		return shopAccount;
	}
	/**
	 * @param shopAccount the shopAccount to set
	 */
	public void setShopAccount(String shopAccount) {
		this.shopAccount = shopAccount;
	}
	/**
	 * @return the shopCode
	 */
	public String getShopCode() {
		return shopCode;
	}
	/**
	 * @param shopCode the shopCode to set
	 */
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	/**
	 * @return the tranDate
	 */
	public Timestamp getTranDate() {
		return tranDate;
	}
	/**
	 * @param tranDate the tranDate to set
	 */
	public void setTranDate(Timestamp tranDate) {
		this.tranDate = tranDate;
	}
	/**
	 * @return the tranSerialNum
	 */
	public String getTranSerialNum() {
		return tranSerialNum;
	}
	/**
	 * @param tranSerialNum the tranSerialNum to set
	 */
	public void setTranSerialNum(String tranSerialNum) {
		this.tranSerialNum = tranSerialNum;
	}
	/**
	 * @return the tranStat
	 */
	public String getTranStat() {
		return tranStat;
	}
	/**
	 * @param tranStat the tranStat to set
	 */
	public void setTranStat(String tranStat) {
		this.tranStat = tranStat;
	}
	/**
	 * @return the tranTime
	 */
	public String getTranTime() {
		return tranTime;
	}
	/**
	 * @param tranTime the tranTime to set
	 */
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
}