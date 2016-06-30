package com.yuanluesoft.jeaf.payment.model;

/**
 * 账户类型
 * @author linchuan
 *
 */
public class AccountType {
	private String name; //帐户类型

	public AccountType(String name) {
		super();
		this.name = name;
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
}