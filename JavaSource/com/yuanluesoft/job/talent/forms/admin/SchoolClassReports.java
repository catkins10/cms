package com.yuanluesoft.job.talent.forms.admin;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author chuan
 *
 */
public class SchoolClassReports extends ActionForm {
	private long schoolClassId; //班级ID
	private String schoolClass; //班级名称
	private Date graduateDate; //毕业时间
	private String schoolingLength; //学制
	private String qualification; //学历层次
	private String specialty; //专业
	private String trainingMode; //培养方式
	private Date reportBegin; //报到开始时间
	private Date reportEnd; //报到截止时间
	private long noticeNumber; //就业通知书起始编号
	private long reportNumber; //就业报到证起始编号
	private List reports; //报到记录列表
	
	/**
	 * @return the graduateDate
	 */
	public Date getGraduateDate() {
		return graduateDate;
	}
	/**
	 * @param graduateDate the graduateDate to set
	 */
	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}
	/**
	 * @return the noticeNumber
	 */
	public long getNoticeNumber() {
		return noticeNumber;
	}
	/**
	 * @param noticeNumber the noticeNumber to set
	 */
	public void setNoticeNumber(long noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	/**
	 * @return the qualification
	 */
	public String getQualification() {
		return qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	/**
	 * @return the reportBegin
	 */
	public Date getReportBegin() {
		return reportBegin;
	}
	/**
	 * @param reportBegin the reportBegin to set
	 */
	public void setReportBegin(Date reportBegin) {
		this.reportBegin = reportBegin;
	}
	/**
	 * @return the reportEnd
	 */
	public Date getReportEnd() {
		return reportEnd;
	}
	/**
	 * @param reportEnd the reportEnd to set
	 */
	public void setReportEnd(Date reportEnd) {
		this.reportEnd = reportEnd;
	}
	/**
	 * @return the reportNumber
	 */
	public long getReportNumber() {
		return reportNumber;
	}
	/**
	 * @param reportNumber the reportNumber to set
	 */
	public void setReportNumber(long reportNumber) {
		this.reportNumber = reportNumber;
	}
	/**
	 * @return the reports
	 */
	public List getReports() {
		return reports;
	}
	/**
	 * @param reports the reports to set
	 */
	public void setReports(List reports) {
		this.reports = reports;
	}
	/**
	 * @return the schoolClassId
	 */
	public long getSchoolClassId() {
		return schoolClassId;
	}
	/**
	 * @param schoolClassId the schoolClassId to set
	 */
	public void setSchoolClassId(long schoolClassId) {
		this.schoolClassId = schoolClassId;
	}
	/**
	 * @return the schoolingLength
	 */
	public String getSchoolingLength() {
		return schoolingLength;
	}
	/**
	 * @param schoolingLength the schoolingLength to set
	 */
	public void setSchoolingLength(String schoolingLength) {
		this.schoolingLength = schoolingLength;
	}
	/**
	 * @return the specialty
	 */
	public String getSpecialty() {
		return specialty;
	}
	/**
	 * @param specialty the specialty to set
	 */
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	/**
	 * @return the trainingMode
	 */
	public String getTrainingMode() {
		return trainingMode;
	}
	/**
	 * @param trainingMode the trainingMode to set
	 */
	public void setTrainingMode(String trainingMode) {
		this.trainingMode = trainingMode;
	}
	/**
	 * @return the schoolClass
	 */
	public String getSchoolClass() {
		return schoolClass;
	}
	/**
	 * @param schoolClass the schoolClass to set
	 */
	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}
}