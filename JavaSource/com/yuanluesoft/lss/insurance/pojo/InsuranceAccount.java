package com.yuanluesoft.lss.insurance.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 养老保险:个人账户查询(insurance_account)
 * @author linchuan
 *
 */
public class InsuranceAccount extends Record {
	private String identityCardNumber; //身份证号码
	private String name; //姓名
	private int year; //年度
	private double accountCapital; //本年度个人账户本金
	private double payCapital; //其中个人缴纳本金
	private int monthNumber; //本年底累计缴费月数
	private double yearTotal; //年底账户本息累计
	private double yearPayTotal; //其中个人缴纳本息
	private double cardinalNumber; //年缴费基数（含补缴）
	
	/**
	 * @return the accountCapital
	 */
	public double getAccountCapital() {
		return accountCapital;
	}
	/**
	 * @param accountCapital the accountCapital to set
	 */
	public void setAccountCapital(double accountCapital) {
		this.accountCapital = accountCapital;
	}
	/**
	 * @return the cardinalNumber
	 */
	public double getCardinalNumber() {
		return cardinalNumber;
	}
	/**
	 * @param cardinalNumber the cardinalNumber to set
	 */
	public void setCardinalNumber(double cardinalNumber) {
		this.cardinalNumber = cardinalNumber;
	}
	/**
	 * @return the identityCardNumber
	 */
	public String getIdentityCardNumber() {
		return identityCardNumber;
	}
	/**
	 * @param identityCardNumber the identityCardNumber to set
	 */
	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}
	/**
	 * @return the monthNumber
	 */
	public int getMonthNumber() {
		return monthNumber;
	}
	/**
	 * @param monthNumber the monthNumber to set
	 */
	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
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
	 * @return the payCapital
	 */
	public double getPayCapital() {
		return payCapital;
	}
	/**
	 * @param payCapital the payCapital to set
	 */
	public void setPayCapital(double payCapital) {
		this.payCapital = payCapital;
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
	/**
	 * @return the yearPayTotal
	 */
	public double getYearPayTotal() {
		return yearPayTotal;
	}
	/**
	 * @param yearPayTotal the yearPayTotal to set
	 */
	public void setYearPayTotal(double yearPayTotal) {
		this.yearPayTotal = yearPayTotal;
	}
	/**
	 * @return the yearTotal
	 */
	public double getYearTotal() {
		return yearTotal;
	}
	/**
	 * @param yearTotal the yearTotal to set
	 */
	public void setYearTotal(double yearTotal) {
		this.yearTotal = yearTotal;
	}
}