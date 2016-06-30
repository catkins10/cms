package com.yuanluesoft.lss.insurance.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 养老保险:记账利率、缴纳比例(insurance_parameter)
 * @author linchuan
 *
 */
public class InsuranceParameter extends Record {
	private int year; //年度
	private double rate; //年记账利率
	private double accountRatio; //个人账户比例
	private double payRatio; //个人缴纳比例
	
	/**
	 * @return the accountRatio
	 */
	public double getAccountRatio() {
		return accountRatio;
	}
	/**
	 * @param accountRatio the accountRatio to set
	 */
	public void setAccountRatio(double accountRatio) {
		this.accountRatio = accountRatio;
	}
	/**
	 * @return the payRatio
	 */
	public double getPayRatio() {
		return payRatio;
	}
	/**
	 * @param payRatio the payRatio to set
	 */
	public void setPayRatio(double payRatio) {
		this.payRatio = payRatio;
	}
	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}