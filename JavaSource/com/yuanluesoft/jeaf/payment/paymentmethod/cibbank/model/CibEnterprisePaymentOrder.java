package com.yuanluesoft.jeaf.payment.paymentmethod.cibbank.model;

/**
 * 兴业银行企业支付订单,显示个用户,用户在企业银行界面创建B2B订单时需要引用到
 * @author linchuan
 *
 */
public class CibEnterprisePaymentOrder {
	private String paymentVerifyName; //企业创建B2B订单时提醒用户输入的密码的描述
	private String paymentVerifyCode; //企业创建B2B订单时用户要输入的密码
	private String sellAccountId; //卖方帐号
	private String sellAccountName; //卖方帐户名
	private String sellBank; //卖方开户行
	private String applicationOrderId; //订单号
	private String paymentReason; //支付原因
	private double money; //支付金额
	private String enterpriseBankURL; //企业银行
	private String paymentCompleteCallbackURL; //支付完成后打开的URL
	
	/**
	 * @return the applicationOrderId
	 */
	public String getApplicationOrderId() {
		return applicationOrderId;
	}
	/**
	 * @param applicationOrderId the applicationOrderId to set
	 */
	public void setApplicationOrderId(String applicationOrderId) {
		this.applicationOrderId = applicationOrderId;
	}
	/**
	 * @return the paymentReason
	 */
	public String getPaymentReason() {
		return paymentReason;
	}
	/**
	 * @param paymentReason the paymentReason to set
	 */
	public void setPaymentReason(String paymentReason) {
		this.paymentReason = paymentReason;
	}
	/**
	 * @return the paymentVerifyCode
	 */
	public String getPaymentVerifyCode() {
		return paymentVerifyCode;
	}
	/**
	 * @param paymentVerifyCode the paymentVerifyCode to set
	 */
	public void setPaymentVerifyCode(String paymentVerifyCode) {
		this.paymentVerifyCode = paymentVerifyCode;
	}
	/**
	 * @return the paymentVerifyName
	 */
	public String getPaymentVerifyName() {
		return paymentVerifyName;
	}
	/**
	 * @param paymentVerifyName the paymentVerifyName to set
	 */
	public void setPaymentVerifyName(String paymentVerifyName) {
		this.paymentVerifyName = paymentVerifyName;
	}
	/**
	 * @return the sellAccountId
	 */
	public String getSellAccountId() {
		return sellAccountId;
	}
	/**
	 * @param sellAccountId the sellAccountId to set
	 */
	public void setSellAccountId(String sellAccountId) {
		this.sellAccountId = sellAccountId;
	}
	/**
	 * @return the sellAccountName
	 */
	public String getSellAccountName() {
		return sellAccountName;
	}
	/**
	 * @param sellAccountName the sellAccountName to set
	 */
	public void setSellAccountName(String sellAccountName) {
		this.sellAccountName = sellAccountName;
	}
	/**
	 * @return the sellBank
	 */
	public String getSellBank() {
		return sellBank;
	}
	/**
	 * @param sellBank the sellBank to set
	 */
	public void setSellBank(String sellBank) {
		this.sellBank = sellBank;
	}
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
	 * @return the enterpriseBankURL
	 */
	public String getEnterpriseBankURL() {
		return enterpriseBankURL;
	}
	/**
	 * @param enterpriseBankURL the enterpriseBankURL to set
	 */
	public void setEnterpriseBankURL(String enterpriseBankURL) {
		this.enterpriseBankURL = enterpriseBankURL;
	}
	/**
	 * @return the paymentCompleteCallbackURL
	 */
	public String getPaymentCompleteCallbackURL() {
		return paymentCompleteCallbackURL;
	}
	/**
	 * @param paymentCompleteCallbackURL the paymentCompleteCallbackURL to set
	 */
	public void setPaymentCompleteCallbackURL(String paymentCompleteCallbackURL) {
		this.paymentCompleteCallbackURL = paymentCompleteCallbackURL;
	}
}