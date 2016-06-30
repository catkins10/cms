package com.yuanluesoft.jeaf.payment.pojo;

import java.util.LinkedHashSet;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 商户(payment_merchant)
 * @author linchuan
 *
 */
public class PaymentMerchant extends Record {
	private String name; //户名
	private String account; //帐号
	private String bank; //开户银行
	private String transferPassword; //转账密码
	private String paymentMethod; //支付方式,如:建设银行,光大银行
	private String accountTypes; //启用的帐户类型,如:个人,企业
	private int halt; //是否停用
	private Set parameters; //参数列表
	
	//扩展输性
	private String iconUrl; //图标URL
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public String getParameterValue(String parameterName) {
		PaymentMerchantParameter parameter = (PaymentMerchantParameter)ListUtils.findObjectByProperty(parameters, "parameterName", parameterName);
		return parameter==null ? null : parameter.getParameterValue();
	}
	
	/**
	 * 设置参数值
	 * @param parameterName
	 * @param parameterValue
	 */
	public void setParameterValue(String parameterName, String parameterValue) {
		if(parameters==null) {
			parameters = new LinkedHashSet();
		}
		parameters.add(new PaymentMerchantParameter(parameterName, parameterValue));
	}
	
	/**
	 * 获取全称
	 * @return
	 */
	public String getFullName() {
		return name + "(" + bank + ")";
	}
	
	/**
	 * @return the accountTypes
	 */
	public String getAccountTypes() {
		return accountTypes;
	}
	/**
	 * @param accountTypes the accountTypes to set
	 */
	public void setAccountTypes(String accountTypes) {
		this.accountTypes = accountTypes;
	}
	/**
	 * @return the halt
	 */
	public int getHalt() {
		return halt;
	}
	/**
	 * @param halt the halt to set
	 */
	public void setHalt(int halt) {
		this.halt = halt;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the parameters
	 */
	public Set getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Set parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * @return the transferPassword
	 */
	public String getTransferPassword() {
		return transferPassword;
	}

	/**
	 * @param transferPassword the transferPassword to set
	 */
	public void setTransferPassword(String transferPassword) {
		this.transferPassword = transferPassword;
	}
}