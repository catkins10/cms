package com.yuanluesoft.fet.tradestat.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业进口数据(tradestat_import)
 * @author linchuan
 *
 */
public class TradeImport extends Record {
	private String companyCode; //企业编号
	private String companyName; //单位名称
	private double monthlyTotal; //本月数
	private double yearTotal; //累计数
	private double lastYearMonthlyTotal; //同期数
	private double monthlyGrowthRate; //比增
	private double lastYearTotal; //同期累计数
	private double growthRate; //累计比增
	private String county; //区县
	private String countyCode; //县码
	private char isState = '0'; //国有
	private char isMachine = '0'; //机电
	private char isImportant = '0'; //重点企业
	private String countryCode; //投资国别
	private String developmentAreaCode; //开发区
	private int dataYear; //年份
	private int dataMonth; //月份
	
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the dataMonth
	 */
	public int getDataMonth() {
		return dataMonth;
	}
	/**
	 * @param dataMonth the dataMonth to set
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}
	/**
	 * @return the dataYear
	 */
	public int getDataYear() {
		return dataYear;
	}
	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}
	/**
	 * @return the growthRate
	 */
	public double getGrowthRate() {
		return growthRate;
	}
	/**
	 * @param growthRate the growthRate to set
	 */
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}
	/**
	 * @return the isImportant
	 */
	public char getIsImportant() {
		return isImportant;
	}
	/**
	 * @param isImportant the isImportant to set
	 */
	public void setIsImportant(char isImportant) {
		this.isImportant = isImportant;
	}
	/**
	 * @return the isMachine
	 */
	public char getIsMachine() {
		return isMachine;
	}
	/**
	 * @param isMachine the isMachine to set
	 */
	public void setIsMachine(char isMachine) {
		this.isMachine = isMachine;
	}
	/**
	 * @return the isState
	 */
	public char getIsState() {
		return isState;
	}
	/**
	 * @param isState the isState to set
	 */
	public void setIsState(char isState) {
		this.isState = isState;
	}
	/**
	 * @return the lastYearMonthlyTotal
	 */
	public double getLastYearMonthlyTotal() {
		return lastYearMonthlyTotal;
	}
	/**
	 * @param lastYearMonthlyTotal the lastYearMonthlyTotal to set
	 */
	public void setLastYearMonthlyTotal(double lastYearMonthlyTotal) {
		this.lastYearMonthlyTotal = lastYearMonthlyTotal;
	}
	/**
	 * @return the lastYearTotal
	 */
	public double getLastYearTotal() {
		return lastYearTotal;
	}
	/**
	 * @param lastYearTotal the lastYearTotal to set
	 */
	public void setLastYearTotal(double lastYearTotal) {
		this.lastYearTotal = lastYearTotal;
	}
	/**
	 * @return the monthlyTotal
	 */
	public double getMonthlyTotal() {
		return monthlyTotal;
	}
	/**
	 * @param monthlyTotal the monthlyTotal to set
	 */
	public void setMonthlyTotal(double monthlyTotal) {
		this.monthlyTotal = monthlyTotal;
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
	/**
	 * @return the countyCode
	 */
	public String getCountyCode() {
		return countyCode;
	}
	/**
	 * @param countyCode the countyCode to set
	 */
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	/**
	 * @return the developmentAreaCode
	 */
	public String getDevelopmentAreaCode() {
		return developmentAreaCode;
	}
	/**
	 * @param developmentAreaCode the developmentAreaCode to set
	 */
	public void setDevelopmentAreaCode(String developmentAreaCode) {
		this.developmentAreaCode = developmentAreaCode;
	}
	/**
	 * @return the monthlyGrowthRate
	 */
	public double getMonthlyGrowthRate() {
		return monthlyGrowthRate;
	}
	/**
	 * @param monthlyGrowthRate the monthlyGrowthRate to set
	 */
	public void setMonthlyGrowthRate(double monthlyGrowthRate) {
		this.monthlyGrowthRate = monthlyGrowthRate;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
}
