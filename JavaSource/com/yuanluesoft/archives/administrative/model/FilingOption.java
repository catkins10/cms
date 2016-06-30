/*
 * Created on 2006-9-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.model;


/**
 *
 * @author linchuan
 *
 */
public class FilingOption {
	private String fondsName; //全宗名称
	private String unit; //机构或问题
	private String rotentionPeriod; //保管期限
	private String secureLevel; //密级
	private String docCategory; //公文种类
	private int filingYear; //归档年度
	private String responsibilityPerson; //责任者
	
	/**
	 * @return the docCategory
	 */
	public String getDocCategory() {
		return docCategory;
	}
	/**
	 * @param docCategory the docCategory to set
	 */
	public void setDocCategory(String docCategory) {
		this.docCategory = docCategory;
	}
	/**
	 * @return the filingYear
	 */
	public int getFilingYear() {
		return filingYear;
	}
	/**
	 * @param filingYear the filingYear to set
	 */
	public void setFilingYear(int filingYear) {
		this.filingYear = filingYear;
	}
	/**
	 * @return the fondsName
	 */
	public String getFondsName() {
		return fondsName;
	}
	/**
	 * @param fondsName the fondsName to set
	 */
	public void setFondsName(String fondsName) {
		this.fondsName = fondsName;
	}
	/**
	 * @return the responsibilityPerson
	 */
	public String getResponsibilityPerson() {
		return responsibilityPerson;
	}
	/**
	 * @param responsibilityPerson the responsibilityPerson to set
	 */
	public void setResponsibilityPerson(String responsibilityPerson) {
		this.responsibilityPerson = responsibilityPerson;
	}
	/**
	 * @return the rotentionPeriod
	 */
	public String getRotentionPeriod() {
		return rotentionPeriod;
	}
	/**
	 * @param rotentionPeriod the rotentionPeriod to set
	 */
	public void setRotentionPeriod(String rotentionPeriod) {
		this.rotentionPeriod = rotentionPeriod;
	}
	/**
	 * @return the secureLevel
	 */
	public String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel the secureLevel to set
	 */
	public void setSecureLevel(String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}