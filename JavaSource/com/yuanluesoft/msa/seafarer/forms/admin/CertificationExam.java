package com.yuanluesoft.msa.seafarer.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class CertificationExam extends ActionForm {
	private String speciality; //专业
	private String period; //期数
	private String category; //类别
	private String examAddress; //考试地点
	private Timestamp importTime; //导入时间
	private long importId; //导入记录ID
	private Set examinees; //合格的考生列表
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the examAddress
	 */
	public String getExamAddress() {
		return examAddress;
	}
	/**
	 * @param examAddress the examAddress to set
	 */
	public void setExamAddress(String examAddress) {
		this.examAddress = examAddress;
	}
	/**
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
	}
	/**
	 * @return the importTime
	 */
	public Timestamp getImportTime() {
		return importTime;
	}
	/**
	 * @param importTime the importTime to set
	 */
	public void setImportTime(Timestamp importTime) {
		this.importTime = importTime;
	}
	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}
	/**
	 * @return the speciality
	 */
	public String getSpeciality() {
		return speciality;
	}
	/**
	 * @param speciality the speciality to set
	 */
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	/**
	 * @return the examinees
	 */
	public Set getExaminees() {
		return examinees;
	}
	/**
	 * @param examinees the examinees to set
	 */
	public void setExaminees(Set examinees) {
		this.examinees = examinees;
	}
}