package com.yuanluesoft.bidding.enterprise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业:投标企业(bidding_bid_enterprise)
 * @author linchuan
 *
 */
public class BiddingBidEnterprise extends Record {
	private String name; //企业名称
	private String bank; //开户行
	private String account; //帐号
	
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