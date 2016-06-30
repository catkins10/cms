package com.yuanluesoft.msa.lawmen.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Lawmen extends ActionForm {
	private String name; //姓名
	private char sex = 'M'; //性别
	private Date certificateDate; //执法证发证日期
	private String certificateNumber; //执法证编号
	private double priority; //优先级
	
	/**
	 * @return the certificateDate
	 */
	public Date getCertificateDate() {
		return certificateDate;
	}
	/**
	 * @param certificateDate the certificateDate to set
	 */
	public void setCertificateDate(Date certificateDate) {
		this.certificateDate = certificateDate;
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
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}
}