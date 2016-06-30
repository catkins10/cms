package com.yuanluesoft.qualification.beauty.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

public class Beauty extends Record {
	private long number;	//证书编号
	private long testAdmissionTicketNumber;	//准考证号
	private String personId;	//身份证号
	private String name;	//姓名
	private String sex;	//性别
	private Date birthday;	//出生日期
	private String educationLevel;	//文化程度
	private String phone;	//联系电话
	private String profession;	//鉴定职业
	private String level;	//鉴定级别
	private String subject;	//鉴定科目
	private String type;	//鉴定分类
	private String company;	//工作单位
	private int serviceYears;	//工作年限
	private String mark;	//评定成绩
	private String authority;	//上报机构
	private float theoryMark;	//理论成绩
	private String theorySituation;	//理论情况
	private float practiceMark;	//实操成绩
	private String practiceSituation;	//实操情况
	private float foreignMark;	//外语成绩
	private String foreignSituation;	//外语情况
	private float generalMark;	//综合成绩
	private String generalSituation;	//综合情况
	private Date markDecidedDate;	//成绩认定日期
	private Date sendDate;	//发证日期
	
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public float getForeignMark() {
		return foreignMark;
	}
	public void setForeignMark(float foreignMark) {
		this.foreignMark = foreignMark;
	}
	public String getForeignSituation() {
		return foreignSituation;
	}
	public void setForeignSituation(String foreignSituation) {
		this.foreignSituation = foreignSituation;
	}
	public float getGeneralMark() {
		return generalMark;
	}
	public void setGeneralMark(float generalMark) {
		this.generalMark = generalMark;
	}
	public String getGeneralSituation() {
		return generalSituation;
	}
	public void setGeneralSituation(String generalSituation) {
		this.generalSituation = generalSituation;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Date getMarkDecidedDate() {
		return markDecidedDate;
	}
	public void setMarkDecidedDate(Date markDecidedDate) {
		this.markDecidedDate = markDecidedDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getPracticeMark() {
		return practiceMark;
	}
	public void setPracticeMark(float practiceMark) {
		this.practiceMark = practiceMark;
	}
	public String getPracticeSituation() {
		return practiceSituation;
	}
	public void setPracticeSituation(String practiceSituation) {
		this.practiceSituation = practiceSituation;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public int getServiceYears() {
		return serviceYears;
	}
	public void setServiceYears(int serviceYears) {
		this.serviceYears = serviceYears;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public long getTestAdmissionTicketNumber() {
		return testAdmissionTicketNumber;
	}
	public void setTestAdmissionTicketNumber(long testAdmissionTicketNumber) {
		this.testAdmissionTicketNumber = testAdmissionTicketNumber;
	}
	public float getTheoryMark() {
		return theoryMark;
	}
	public void setTheoryMark(float theoryMark) {
		this.theoryMark = theoryMark;
	}
	public String getTheorySituation() {
		return theorySituation;
	}
	public void setTheorySituation(String theorySituation) {
		this.theorySituation = theorySituation;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
