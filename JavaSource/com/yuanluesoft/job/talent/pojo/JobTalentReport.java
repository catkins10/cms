package com.yuanluesoft.job.talent.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:就业报到(job_talent_report)
 * @author linchuan
 *
 */
public class JobTalentReport extends Record {
	private long talentId; //人才ID
	private long schoolClassId; //班级ID
	private String schoolClass; //班级名称
	private String company; //单位名称
	private String companyCode; //单位组织机构代码
	private String companyType; //单位性质
	private String companySector; //单位产业
	private String companyIndustry; //单位行业
	private String personnelFileCompany; //档案接收单位
	private String personnelFileAddress; //档案接收地址
	private String name; //姓名
	private char sex; //性别
	private Date birthday; //出生年月
	private Date graduateDate; //毕业时间
	private String residence; //入学前户口所在地
	private String nation; //民族
	private String politicalStatus; //政治面貌
	private String studentNumber; //学号
	private String schoolingLength; //学制
	private String qualification; //学历层次
	private String specialty; //专业
	private String trainingMode; //培养方式
	private String tel; //联系电话
	private String address; //家庭地址
	private String email; //电子邮箱
	private String jobType; //工作职位类别
	private Date reportBegin; //报到开始时间
	private Date reportEnd; //报到截止时间
	private String noticeNumber; //就业通知书编号
	private String reportNumber; //就业报到证编号
	private String remark; //备注
	
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
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
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
	 * @return the companyIndustry
	 */
	public String getCompanyIndustry() {
		return companyIndustry;
	}
	/**
	 * @param companyIndustry the companyIndustry to set
	 */
	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}
	/**
	 * @return the companyType
	 */
	public String getCompanyType() {
		return companyType;
	}
	/**
	 * @param companyType the companyType to set
	 */
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
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
	 * @return the jobType
	 */
	public String getJobType() {
		return jobType;
	}
	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
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
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}
	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	/**
	 * @return the noticeNumber
	 */
	public String getNoticeNumber() {
		return noticeNumber;
	}
	/**
	 * @param noticeNumber the noticeNumber to set
	 */
	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	/**
	 * @return the personnelFileAddress
	 */
	public String getPersonnelFileAddress() {
		return personnelFileAddress;
	}
	/**
	 * @param personnelFileAddress the personnelFileAddress to set
	 */
	public void setPersonnelFileAddress(String personnelFileAddress) {
		this.personnelFileAddress = personnelFileAddress;
	}
	/**
	 * @return the personnelFileCompany
	 */
	public String getPersonnelFileCompany() {
		return personnelFileCompany;
	}
	/**
	 * @param personnelFileCompany the personnelFileCompany to set
	 */
	public void setPersonnelFileCompany(String personnelFileCompany) {
		this.personnelFileCompany = personnelFileCompany;
	}
	/**
	 * @return the politicalStatus
	 */
	public String getPoliticalStatus() {
		return politicalStatus;
	}
	/**
	 * @param politicalStatus the politicalStatus to set
	 */
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getReportNumber() {
		return reportNumber;
	}
	/**
	 * @param reportNumber the reportNumber to set
	 */
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	/**
	 * @return the residence
	 */
	public String getResidence() {
		return residence;
	}
	/**
	 * @param residence the residence to set
	 */
	public void setResidence(String residence) {
		this.residence = residence;
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
	 * @return the studentNumber
	 */
	public String getStudentNumber() {
		return studentNumber;
	}
	/**
	 * @param studentNumber the studentNumber to set
	 */
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	/**
	 * @return the talentId
	 */
	public long getTalentId() {
		return talentId;
	}
	/**
	 * @param talentId the talentId to set
	 */
	public void setTalentId(long talentId) {
		this.talentId = talentId;
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
	 * @return the companySector
	 */
	public String getCompanySector() {
		return companySector;
	}
	/**
	 * @param companySector the companySector to set
	 */
	public void setCompanySector(String companySector) {
		this.companySector = companySector;
	}
}