package com.yuanluesoft.bidding.enterprise.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 从业人员(bidding_jobholder)
 * @author linchuan
 *
 */
public class BiddingJobholder extends Record {
	private long enterpriseId; //工作单位ID
	private String enterpriseName; //工作单位
	private String name; //姓名
	private String category; //类别,项目经理/注册结构师/注册建筑师/注册监理师/代理监理师
	private char sex = 'M'; //性别,M/F
	private String identityCard; //身份证号码
	private String school; //毕业（培训）院校
	private String education; //学历
	private String schoolProfessional; //毕业专业
	private String professional; //专业
	private String secondProfessional; //第二专业
	private String otherProfessional; //其它专业
	private String duty; //职务
	private String job; //职称
	private String qualification; //资质等级/人员类别
	private String certificateNumber; //证书号码/培训证号
	private Date certificateCreated; //发证时间
	private Date certificateLimit; //有效期
	private String tel; //电话
	private String mobile; //手机
	private String address; //通讯地址
	private String postalCode; //邮编
	private String remark; //备注
	private long alterId; //变更记录ID
	
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
	 * @return the certificateCreated
	 */
	public Date getCertificateCreated() {
		return certificateCreated;
	}
	/**
	 * @param certificateCreated the certificateCreated to set
	 */
	public void setCertificateCreated(Date certificateCreated) {
		this.certificateCreated = certificateCreated;
	}
	/**
	 * @return the certificateLimit
	 */
	public Date getCertificateLimit() {
		return certificateLimit;
	}
	/**
	 * @param certificateLimit the certificateLimit to set
	 */
	public void setCertificateLimit(Date certificateLimit) {
		this.certificateLimit = certificateLimit;
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
	 * @return the duty
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty the duty to set
	 */
	public void setDuty(String duty) {
		this.duty = duty;
	}
	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}
	/**
	 * @param education the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}
	/**
	 * @return the enterpriseId
	 */
	public long getEnterpriseId() {
		return enterpriseId;
	}
	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	/**
	 * @return the identityCard
	 */
	public String getIdentityCard() {
		return identityCard;
	}
	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * @return the otherProfessional
	 */
	public String getOtherProfessional() {
		return otherProfessional;
	}
	/**
	 * @param otherProfessional the otherProfessional to set
	 */
	public void setOtherProfessional(String otherProfessional) {
		this.otherProfessional = otherProfessional;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the professional
	 */
	public String getProfessional() {
		return professional;
	}
	/**
	 * @param professional the professional to set
	 */
	public void setProfessional(String professional) {
		this.professional = professional;
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
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}
	/**
	 * @param school the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}
	/**
	 * @return the schoolProfessional
	 */
	public String getSchoolProfessional() {
		return schoolProfessional;
	}
	/**
	 * @param schoolProfessional the schoolProfessional to set
	 */
	public void setSchoolProfessional(String schoolProfessional) {
		this.schoolProfessional = schoolProfessional;
	}
	/**
	 * @return the secondProfessional
	 */
	public String getSecondProfessional() {
		return secondProfessional;
	}
	/**
	 * @param secondProfessional the secondProfessional to set
	 */
	public void setSecondProfessional(String secondProfessional) {
		this.secondProfessional = secondProfessional;
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
	 * @return the alterId
	 */
	public long getAlterId() {
		return alterId;
	}
	/**
	 * @param alterId the alterId to set
	 */
	public void setAlterId(long alterId) {
		this.alterId = alterId;
	}
}
