package com.yuanluesoft.j2oa.info.model;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class IssueTotal extends Record {
	private String unitName; //单位名称
	private double point; //积分
	private double remuneration; //稿酬
	private int issue; //采用数
	private int briefIssue; //简讯采用数
	private int sendCounty; //上报县办数量
	private int sendCity; //上报市办数量
	private int sendProvincial; //上报省办数量
	private int sendCountry; //上报国办数量
	private int countyIssue; //县办采用数量
	private int cityIssue; //市办采用数量
	private int provincialIssue; //省办采用数量
	private int countryIssue; //国办采用数量
	private int countyInstruct; //县领导批示数量
	private int cityInstruct; //市领导批示数量
	private int provincialInstruct; //省领导批示数量
	private int countryInstruct; //国家领导批示数量
	
	/**
	 * @return the briefIssue
	 */
	public int getBriefIssue() {
		return briefIssue;
	}
	/**
	 * @param briefIssue the briefIssue to set
	 */
	public void setBriefIssue(int briefIssue) {
		this.briefIssue = briefIssue;
	}
	/**
	 * @return the cityInstruct
	 */
	public int getCityInstruct() {
		return cityInstruct;
	}
	/**
	 * @param cityInstruct the cityInstruct to set
	 */
	public void setCityInstruct(int cityInstruct) {
		this.cityInstruct = cityInstruct;
	}
	/**
	 * @return the cityIssue
	 */
	public int getCityIssue() {
		return cityIssue;
	}
	/**
	 * @param cityIssue the cityIssue to set
	 */
	public void setCityIssue(int cityIssue) {
		this.cityIssue = cityIssue;
	}
	/**
	 * @return the countryInstruct
	 */
	public int getCountryInstruct() {
		return countryInstruct;
	}
	/**
	 * @param countryInstruct the countryInstruct to set
	 */
	public void setCountryInstruct(int countryInstruct) {
		this.countryInstruct = countryInstruct;
	}
	/**
	 * @return the countryIssue
	 */
	public int getCountryIssue() {
		return countryIssue;
	}
	/**
	 * @param countryIssue the countryIssue to set
	 */
	public void setCountryIssue(int countryIssue) {
		this.countryIssue = countryIssue;
	}
	/**
	 * @return the countyInstruct
	 */
	public int getCountyInstruct() {
		return countyInstruct;
	}
	/**
	 * @param countyInstruct the countyInstruct to set
	 */
	public void setCountyInstruct(int countyInstruct) {
		this.countyInstruct = countyInstruct;
	}
	/**
	 * @return the countyIssue
	 */
	public int getCountyIssue() {
		return countyIssue;
	}
	/**
	 * @param countyIssue the countyIssue to set
	 */
	public void setCountyIssue(int countyIssue) {
		this.countyIssue = countyIssue;
	}
	/**
	 * @return the issue
	 */
	public int getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(int issue) {
		this.issue = issue;
	}
	/**
	 * @return the point
	 */
	public double getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(double point) {
		this.point = point;
	}
	/**
	 * @return the provincialInstruct
	 */
	public int getProvincialInstruct() {
		return provincialInstruct;
	}
	/**
	 * @param provincialInstruct the provincialInstruct to set
	 */
	public void setProvincialInstruct(int provincialInstruct) {
		this.provincialInstruct = provincialInstruct;
	}
	/**
	 * @return the provincialIssue
	 */
	public int getProvincialIssue() {
		return provincialIssue;
	}
	/**
	 * @param provincialIssue the provincialIssue to set
	 */
	public void setProvincialIssue(int provincialIssue) {
		this.provincialIssue = provincialIssue;
	}
	/**
	 * @return the remuneration
	 */
	public double getRemuneration() {
		return remuneration;
	}
	/**
	 * @param remuneration the remuneration to set
	 */
	public void setRemuneration(double remuneration) {
		this.remuneration = remuneration;
	}
	/**
	 * @return the sendCity
	 */
	public int getSendCity() {
		return sendCity;
	}
	/**
	 * @param sendCity the sendCity to set
	 */
	public void setSendCity(int sendCity) {
		this.sendCity = sendCity;
	}
	/**
	 * @return the sendCountry
	 */
	public int getSendCountry() {
		return sendCountry;
	}
	/**
	 * @param sendCountry the sendCountry to set
	 */
	public void setSendCountry(int sendCountry) {
		this.sendCountry = sendCountry;
	}
	/**
	 * @return the sendCounty
	 */
	public int getSendCounty() {
		return sendCounty;
	}
	/**
	 * @param sendCounty the sendCounty to set
	 */
	public void setSendCounty(int sendCounty) {
		this.sendCounty = sendCounty;
	}
	/**
	 * @return the sendProvincial
	 */
	public int getSendProvincial() {
		return sendProvincial;
	}
	/**
	 * @param sendProvincial the sendProvincial to set
	 */
	public void setSendProvincial(int sendProvincial) {
		this.sendProvincial = sendProvincial;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}