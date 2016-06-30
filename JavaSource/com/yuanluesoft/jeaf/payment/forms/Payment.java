package com.yuanluesoft.jeaf.payment.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Payment extends ActionForm {
	private String merchantIds; //URL参数:指定的商户ID,允许为空
	private boolean b2cOnly; //URL参数:是否只能使用B2C方式
	private boolean b2bOnly; //URL参数:是否只能使用B2B方式
	private String applicationName; //URL参数:应用名称
	private String orderId; //URL参数:订单ID
	private String payerId; //URL参数: 支付人ID
	private String payerName; //URL参数: 支付人姓名
	private String paymentReason; //URL参数:支付目的
	private double money; //URL参数:金额
	private String providerId; //URL参数:服务提供者ID,0表示系统,如出售在线备课软件时提供者ID为0
	private String providerName; //URL参数:服务提供者姓名
	private double providerMoney; //URL参数:支付给服务提供者的金额
	private String redirectOfSuccess; //URL参数:支付完成跳转的URL,接口为重定向方式时有效
	private String redirectOfFailed; //URL参数:支付失败跳转的URL,接口为重定向方式时有效
	private String from; //URL参数:打开支付页面的URL
	private String pageMode; //URL参数:页面打开方式
	
	private long selectedMerchantId; //选中的商户ID
	private String selectedAccountType; //选中的支付方式
	
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
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	 * @return the selectedAccountType
	 */
	public String getSelectedAccountType() {
		return selectedAccountType;
	}
	/**
	 * @param selectedAccountType the selectedAccountType to set
	 */
	public void setSelectedAccountType(String selectedAccountType) {
		this.selectedAccountType = selectedAccountType;
	}
	/**
	 * @return the b2bOnly
	 */
	public boolean isB2bOnly() {
		return b2bOnly;
	}
	/**
	 * @param only the b2bOnly to set
	 */
	public void setB2bOnly(boolean only) {
		b2bOnly = only;
	}
	/**
	 * @return the b2cOnly
	 */
	public boolean isB2cOnly() {
		return b2cOnly;
	}
	/**
	 * @param only the b2cOnly to set
	 */
	public void setB2cOnly(boolean only) {
		b2cOnly = only;
	}
	/**
	 * @return the merchantIds
	 */
	public String getMerchantIds() {
		return merchantIds;
	}
	/**
	 * @param merchantIds the merchantIds to set
	 */
	public void setMerchantIds(String merchantIds) {
		this.merchantIds = merchantIds;
	}
	/**
	 * @return the selectedMerchantId
	 */
	public long getSelectedMerchantId() {
		return selectedMerchantId;
	}
	/**
	 * @param selectedMerchantId the selectedMerchantId to set
	 */
	public void setSelectedMerchantId(long selectedMerchantId) {
		this.selectedMerchantId = selectedMerchantId;
	}
}