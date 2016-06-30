package com.yuanluesoft.jeaf.payment.paymentmethod.abcbank.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 农业银行:B2B支付记录(payment_abcbank_b2b_order)
 * @author linchuan
 *
 */
public class AbcbankB2BOrder extends Record {
	private long paymentId; //支付单ID
	private String merchantID; //商户代码
	private String corporationCustomerNo; //买方企业客户代码
	private String merchantTrnxNo; //商户交易编号
	private String trnxSN; //交易流水号
	private String trnxType; //交易类型
	private double trnxAMT; //交易金额
	private String accountNo; //付款方账号
	private String accountName; //付款方名称
	private String accountBank; //付款方账户行联行号
	private String accountDBNo; //收款方账号
	private String accountDBName; //收款方名称
	private String accountDBBank; //收款方账户行联行号
	private String trnxTime; //交易时间
	private String trnxStatus; //交易状态,2为成功
	private String errorMessage; //交易执行失败原因
	
	/**
	 * @return the accountBank
	 */
	public String getAccountBank() {
		return accountBank;
	}
	/**
	 * @param accountBank the accountBank to set
	 */
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	/**
	 * @return the accountDBBank
	 */
	public String getAccountDBBank() {
		return accountDBBank;
	}
	/**
	 * @param accountDBBank the accountDBBank to set
	 */
	public void setAccountDBBank(String accountDBBank) {
		this.accountDBBank = accountDBBank;
	}
	/**
	 * @return the accountDBName
	 */
	public String getAccountDBName() {
		return accountDBName;
	}
	/**
	 * @param accountDBName the accountDBName to set
	 */
	public void setAccountDBName(String accountDBName) {
		this.accountDBName = accountDBName;
	}
	/**
	 * @return the accountDBNo
	 */
	public String getAccountDBNo() {
		return accountDBNo;
	}
	/**
	 * @param accountDBNo the accountDBNo to set
	 */
	public void setAccountDBNo(String accountDBNo) {
		this.accountDBNo = accountDBNo;
	}
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	/**
	 * @return the corporationCustomerNo
	 */
	public String getCorporationCustomerNo() {
		return corporationCustomerNo;
	}
	/**
	 * @param corporationCustomerNo the corporationCustomerNo to set
	 */
	public void setCorporationCustomerNo(String corporationCustomerNo) {
		this.corporationCustomerNo = corporationCustomerNo;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the merchantID
	 */
	public String getMerchantID() {
		return merchantID;
	}
	/**
	 * @param merchantID the merchantID to set
	 */
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	/**
	 * @return the merchantTrnxNo
	 */
	public String getMerchantTrnxNo() {
		return merchantTrnxNo;
	}
	/**
	 * @param merchantTrnxNo the merchantTrnxNo to set
	 */
	public void setMerchantTrnxNo(String merchantTrnxNo) {
		this.merchantTrnxNo = merchantTrnxNo;
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
	 * @return the trnxAMT
	 */
	public double getTrnxAMT() {
		return trnxAMT;
	}
	/**
	 * @param trnxAMT the trnxAMT to set
	 */
	public void setTrnxAMT(double trnxAMT) {
		this.trnxAMT = trnxAMT;
	}
	/**
	 * @return the trnxSN
	 */
	public String getTrnxSN() {
		return trnxSN;
	}
	/**
	 * @param trnxSN the trnxSN to set
	 */
	public void setTrnxSN(String trnxSN) {
		this.trnxSN = trnxSN;
	}
	/**
	 * @return the trnxStatus
	 */
	public String getTrnxStatus() {
		return trnxStatus;
	}
	/**
	 * @param trnxStatus the trnxStatus to set
	 */
	public void setTrnxStatus(String trnxStatus) {
		this.trnxStatus = trnxStatus;
	}
	/**
	 * @return the trnxTime
	 */
	public String getTrnxTime() {
		return trnxTime;
	}
	/**
	 * @param trnxTime the trnxTime to set
	 */
	public void setTrnxTime(String trnxTime) {
		this.trnxTime = trnxTime;
	}
	/**
	 * @return the trnxType
	 */
	public String getTrnxType() {
		return trnxType;
	}
	/**
	 * @param trnxType the trnxType to set
	 */
	public void setTrnxType(String trnxType) {
		this.trnxType = trnxType;
	}
}