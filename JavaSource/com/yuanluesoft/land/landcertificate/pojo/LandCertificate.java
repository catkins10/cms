package com.yuanluesoft.land.landcertificate.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 土地证(land_certificate)
 * @author linchuan
 *
 */
public class LandCertificate extends Record {
	private String caseNumber; //受理编号
	private String caseYear; //年度
	private String caseBatch; //批号
	private int category; //收件类别
	private Timestamp created; //收件时间
	private String applicant; //申请人
	private String idCard; //身份证号码
	private String tel; //联系电话
	private String address; //土地座落
	private String certificateNumber; //证号
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the applicant
	 */
	public String getApplicant() {
		return applicant;
	}
	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	/**
	 * @return the caseBatch
	 */
	public String getCaseBatch() {
		return caseBatch;
	}
	/**
	 * @param caseBatch the caseBatch to set
	 */
	public void setCaseBatch(String caseBatch) {
		this.caseBatch = caseBatch;
	}
	/**
	 * @return the caseNumber
	 */
	public String getCaseNumber() {
		return caseNumber;
	}
	/**
	 * @param caseNumber the caseNumber to set
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	/**
	 * @return the caseYear
	 */
	public String getCaseYear() {
		return caseYear;
	}
	/**
	 * @param caseYear the caseYear to set
	 */
	public void setCaseYear(String caseYear) {
		this.caseYear = caseYear;
	}
	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}
	/**
	 * @param certificateNumber the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
}