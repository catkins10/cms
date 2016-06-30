package com.yuanluesoft.jeaf.payment.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 支付:支付记录(payment_payment)
 * @author linchuan
 *
 */
public class Payment extends Record {
	private String applicationName; //应用名称
	private String applicationOrderId; //应用指定的订单ID
	private String payerId; //支付人ID
	private String payerName; //支付人姓名
	private String paymentReason; //支付原因
	private double money; //支付金额
	private String providerId; //服务提供者ID
	private String providerName; //服务提供者姓名
	private double providerMoney; //服务提供者报酬
	private String pageMode; //页面打开方式
	private String redirectOfSuccess; //支付完成跳转的URL
	private String redirectOfFailed; //支付失败跳转的URL
	private String paymentFrom; //打开支付页面的URL
	private Timestamp created; //创建时间
	private String payerIp; //支付人IP
	private long merchantId; //商户ID
	private String paymentMethod; //支付方式,如:光大银行
	private String accountType; //支付帐户类型,如:个人,企业
	private Timestamp lastQueryTime; //最后查询时间
	
	//以下信息从支付查询结果PaymentResult中获取
	private char transactSuccess = '0'; //支付是否成功
	private String transactSn; //支付系统流水号,由支付方式决定
	private String failedReason; //支付失败的原因
	private Timestamp transactTime; //支付完成时间
	private double transactMoney; //实际交易金额
	private double fee; //手续费
	private String bankOrderId; //银行端订单ID
	private String payerAccountName; //支付人账户名称
	private String payerBank; //支付人开户行
	private String payerAccount; //支付人账号
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
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
	 * @return the pageMode
	 */
	public String getPageMode() {
		return pageMode;
	}
	/**
	 * @param pageMode the pageMode to set
	 */
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	/**
	 * @return the payerIp
	 */
	public String getPayerIp() {
		return payerIp;
	}
	/**
	 * @param payerIp the payerIp to set
	 */
	public void setPayerIp(String payerIp) {
		this.payerIp = payerIp;
	}
	/**
	 * @return the paymentFrom
	 */
	public String getPaymentFrom() {
		return paymentFrom;
	}
	/**
	 * @param paymentFrom the paymentFrom to set
	 */
	public void setPaymentFrom(String paymentFrom) {
		this.paymentFrom = paymentFrom;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}
	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	/**
	 * @return the providerMoney
	 */
	public double getProviderMoney() {
		return providerMoney;
	}
	/**
	 * @param providerMoney the providerMoney to set
	 */
	public void setProviderMoney(double providerMoney) {
		this.providerMoney = providerMoney;
	}
	/**
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}
	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	/**
	 * @return the redirectOfFailed
	 */
	public String getRedirectOfFailed() {
		return redirectOfFailed;
	}
	/**
	 * @param redirectOfFailed the redirectOfFailed to set
	 */
	public void setRedirectOfFailed(String redirectOfFailed) {
		this.redirectOfFailed = redirectOfFailed;
	}
	/**
	 * @return the redirectOfSuccess
	 */
	public String getRedirectOfSuccess() {
		return redirectOfSuccess;
	}
	/**
	 * @param redirectOfSuccess the redirectOfSuccess to set
	 */
	public void setRedirectOfSuccess(String redirectOfSuccess) {
		this.redirectOfSuccess = redirectOfSuccess;
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
	 * @return the payerId
	 */
	public String getPayerId() {
		return payerId;
	}
	/**
	 * @param payerId the payerId to set
	 */
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	/**
	 * @return the payerName
	 */
	public String getPayerName() {
		return payerName;
	}
	/**
	 * @param payerName the payerName to set
	 */
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
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
	 * @return the merchantId
	 */
	public long getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
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
	/**
	 * @return the lastQueryTime
	 */
	public Timestamp getLastQueryTime() {
		return lastQueryTime;
	}
	/**
	 * @param lastQueryTime the lastQueryTime to set
	 */
	public void setLastQueryTime(Timestamp lastQueryTime) {
		this.lastQueryTime = lastQueryTime;
	}
}
