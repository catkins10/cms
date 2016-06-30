package com.yuanluesoft.jeaf.payment.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class PaymentMerchant extends ActionForm {
	private String name; //户名
	private String account; //帐号
	private String bank; //开户银行
	private String transferPassword; //转账密码
	private String paymentMethod; //支付方式,如:建设银行,光大银行
	private String accountTypes; //启用的帐户类型,如:个人,企业
	private int halt; //是否停用
	private Set parameters; //参数列表
	
	//扩展属性
	private String[] accountTypeArray; //启用的帐户类型,如:个人,企业
	
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
	 * @return the accountTypeArray
	 */
	public String[] getAccountTypeArray() {
		return accountTypeArray;
	}
	/**
	 * @param accountTypeArray the accountTypeArray to set
	 */
	public void setAccountTypeArray(String[] accountTypeArray) {
		this.accountTypeArray = accountTypeArray;
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