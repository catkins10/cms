package com.yuanluesoft.j2oa.personnel.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Employee extends ActionForm {
	private String serialNumber; //编号
	private String name; //姓名
	private char sex = 'M'; //性别,M/F
	private Date birthday; //出生日期
	private String nativePlace; //籍贯
	private String nation; //民族
	private String bloodType; //血型
	private String identityCard; //身份证号
	private String maritalStatus; //婚姻状况,已婚、未婚
	private String health; //健康状况
	private String politicalStatus; //政治面貌
	private Date joinedDate; //入司时间
	private Date employedDate; //参加工作时间
	private String address; //家庭住址
	private String tel; //联系电话
	private String email; //E-mail地址
	private String level; //技术职称或等级
	private String department; //所在部门
	private String duty; //职务
	private String school; //毕业院校
	private Date graduationDate; //毕业时间
	private String education; //学历
	private String specialty; //学习专业
	private String dutyStatus; //工作状态,试用、在岗、退休、离职
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间

	private Set dutyChanges; //岗位变动情况
	private Set rewardsPunishments; //奖惩情况
	private Set educations; //学习经历
	private Set employments; //工作经历
	private Set trainings; //培训经历
	private Set certificates; //持有证书和资质情况
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
	 * @return the bloodType
	 */
	public String getBloodType() {
		return bloodType;
	}
	/**
	 * @param bloodType the bloodType to set
	 */
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	/**
	 * @return the certificates
	 */
	public Set getCertificates() {
		return certificates;
	}
	/**
	 * @param certificates the certificates to set
	 */
	public void setCertificates(Set certificates) {
		this.certificates = certificates;
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
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
	 * @return the dutyChanges
	 */
	public Set getDutyChanges() {
		return dutyChanges;
	}
	/**
	 * @param dutyChanges the dutyChanges to set
	 */
	public void setDutyChanges(Set dutyChanges) {
		this.dutyChanges = dutyChanges;
	}
	/**
	 * @return the dutyStatus
	 */
	public String getDutyStatus() {
		return dutyStatus;
	}
	/**
	 * @param dutyStatus the dutyStatus to set
	 */
	public void setDutyStatus(String dutyStatus) {
		this.dutyStatus = dutyStatus;
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
	 * @return the educations
	 */
	public Set getEducations() {
		return educations;
	}
	/**
	 * @param educations the educations to set
	 */
	public void setEducations(Set educations) {
		this.educations = educations;
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
	 * @return the employedDate
	 */
	public Date getEmployedDate() {
		return employedDate;
	}
	/**
	 * @param employedDate the employedDate to set
	 */
	public void setEmployedDate(Date employedDate) {
		this.employedDate = employedDate;
	}
	/**
	 * @return the employments
	 */
	public Set getEmployments() {
		return employments;
	}
	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(Set employments) {
		this.employments = employments;
	}
	/**
	 * @return the graduationDate
	 */
	public Date getGraduationDate() {
		return graduationDate;
	}
	/**
	 * @param graduationDate the graduationDate to set
	 */
	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}
	/**
	 * @return the health
	 */
	public String getHealth() {
		return health;
	}
	/**
	 * @param health the health to set
	 */
	public void setHealth(String health) {
		this.health = health;
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
	 * @return the joinedDate
	 */
	public Date getJoinedDate() {
		return joinedDate;
	}
	/**
	 * @param joinedDate the joinedDate to set
	 */
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
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
	 * @return the nativePlace
	 */
	public String getNativePlace() {
		return nativePlace;
	}
	/**
	 * @param nativePlace the nativePlace to set
	 */
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
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
	 * @return the rewardsPunishments
	 */
	public Set getRewardsPunishments() {
		return rewardsPunishments;
	}
	/**
	 * @param rewardsPunishments the rewardsPunishments to set
	 */
	public void setRewardsPunishments(Set rewardsPunishments) {
		this.rewardsPunishments = rewardsPunishments;
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
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	 * @return the trainings
	 */
	public Set getTrainings() {
		return trainings;
	}
	/**
	 * @param trainings the trainings to set
	 */
	public void setTrainings(Set trainings) {
		this.trainings = trainings;
	}
}