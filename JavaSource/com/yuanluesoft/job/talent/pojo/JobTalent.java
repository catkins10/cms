package com.yuanluesoft.job.talent.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才(job_talent)
 * @author linchuan
 *
 */
public class JobTalent extends Record {
	private String name; //姓名
	private String email; //邮箱
	private String password; //密码
	private char sex = 'M'; //性别,男/M,女/F
	private String nation; //民族
	private Date birthday; //生日
	private String identificationType; //证件类型
	private String identificationNumber; //证件号码
	private int maritalStatus; //婚姻状况
	private int stature; //身高
	private String politicalStatus; //政治面貌
	private int workYear; //工作年限,在读学生,应届毕业生,1年,2年,3-4年,5-7年,8-9年,10年以上,不限
	private String residence; //户口所在地
	private String studentSource; //生源所在地
	private int isPoor; //是否困难生
	private long areaId; //居住地ID
	private String area; //居住地名称
	private String cell; //手机
	private String im; //IM
	private String englishLevel; //英语等级,未参加,未通过,英语四级,英语六级,专业四级,专业八级
	private String japaneseLevel; //日语等级,无,一级,二级,三级,四级
	private String computerLevel; //计算机水平
	private String drivingLicense; //驾照
	private String selfAppraisal; //自我评价
	private int receivePushMail; //接收推送邮件,NUMERIC(1),1/接收,0/不接收
	private Timestamp created; //注册时间
	private Timestamp lastModified; //最后更新时间
	private int status; //状态,0/注册,1/审核通过,2/审核未通过,3/停用,4/审核未通过,且已经重填求职意向
	private long approverId; //审核人ID
	private String approver; //审核人
	private Timestamp approvalTime; //审核时间
	private String failedReason; //审核未通过原因
	private Set intentions; //求职意向
	private Set schoolings; //教育经历
	private Set trainings; //培训经历
	private Set speechs; //语言能力
	private Set careers; //工作经历
	private Set employments; //就业跟踪调查表
	private Set reports; //就业报到
	private Set projects; //项目经验
	private Set certificates; //证书
	private Set abilities; //其它技能
	
	/**
	 * @return the approver
	 */
	public String getApprover() {
		return approver;
	}
	/**
	 * @param approver the approver to set
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}
	/**
	 * @return the approverId
	 */
	public long getApproverId() {
		return approverId;
	}
	/**
	 * @param approverId the approverId to set
	 */
	public void setApproverId(long approverId) {
		this.approverId = approverId;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
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
	 * @return the cell
	 */
	public String getCell() {
		return cell;
	}
	/**
	 * @param cell the cell to set
	 */
	public void setCell(String cell) {
		this.cell = cell;
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
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}
	/**
	 * @param drivingLicense the drivingLicense to set
	 */
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
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
	 * @return the englishLevel
	 */
	public String getEnglishLevel() {
		return englishLevel;
	}
	/**
	 * @param englishLevel the englishLevel to set
	 */
	public void setEnglishLevel(String englishLevel) {
		this.englishLevel = englishLevel;
	}
	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	/**
	 * @return the identificationType
	 */
	public String getIdentificationType() {
		return identificationType;
	}
	/**
	 * @param identificationType the identificationType to set
	 */
	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}
	/**
	 * @return the im
	 */
	public String getIm() {
		return im;
	}
	/**
	 * @param im the im to set
	 */
	public void setIm(String im) {
		this.im = im;
	}
	/**
	 * @return the japaneseLevel
	 */
	public String getJapaneseLevel() {
		return japaneseLevel;
	}
	/**
	 * @param japaneseLevel the japaneseLevel to set
	 */
	public void setJapaneseLevel(String japaneseLevel) {
		this.japaneseLevel = japaneseLevel;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the maritalStatus
	 */
	public int getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(int maritalStatus) {
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the stature
	 */
	public int getStature() {
		return stature;
	}
	/**
	 * @param stature the stature to set
	 */
	public void setStature(int stature) {
		this.stature = stature;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the abilities
	 */
	public Set getAbilities() {
		return abilities;
	}
	/**
	 * @param abilities the abilities to set
	 */
	public void setAbilities(Set abilities) {
		this.abilities = abilities;
	}
	/**
	 * @return the careers
	 */
	public Set getCareers() {
		return careers;
	}
	/**
	 * @param careers the careers to set
	 */
	public void setCareers(Set careers) {
		this.careers = careers;
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
	 * @return the intentions
	 */
	public Set getIntentions() {
		return intentions;
	}
	/**
	 * @param intentions the intentions to set
	 */
	public void setIntentions(Set intentions) {
		this.intentions = intentions;
	}
	/**
	 * @return the projects
	 */
	public Set getProjects() {
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(Set projects) {
		this.projects = projects;
	}
	/**
	 * @return the schoolings
	 */
	public Set getSchoolings() {
		return schoolings;
	}
	/**
	 * @param schoolings the schoolings to set
	 */
	public void setSchoolings(Set schoolings) {
		this.schoolings = schoolings;
	}
	/**
	 * @return the speechs
	 */
	public Set getSpeechs() {
		return speechs;
	}
	/**
	 * @param speechs the speechs to set
	 */
	public void setSpeechs(Set speechs) {
		this.speechs = speechs;
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
	/**
	 * @return the approvalTime
	 */
	public Timestamp getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the workYear
	 */
	public int getWorkYear() {
		return workYear;
	}
	/**
	 * @param workYear the workYear to set
	 */
	public void setWorkYear(int workYear) {
		this.workYear = workYear;
	}
	/**
	 * @return the selfAppraisal
	 */
	public String getSelfAppraisal() {
		return selfAppraisal;
	}
	/**
	 * @param selfAppraisal the selfAppraisal to set
	 */
	public void setSelfAppraisal(String selfAppraisal) {
		this.selfAppraisal = selfAppraisal;
	}
	/**
	 * @return the computerLevel
	 */
	public String getComputerLevel() {
		return computerLevel;
	}
	/**
	 * @param computerLevel the computerLevel to set
	 */
	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}
	/**
	 * @return the failedReason
	 */
	public String getFailedReason() {
		return failedReason;
	}
	/**
	 * @param failedReason the failedReason to set
	 */
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	/**
	 * @return the isPoor
	 */
	public int getIsPoor() {
		return isPoor;
	}
	/**
	 * @param isPoor the isPoor to set
	 */
	public void setIsPoor(int isPoor) {
		this.isPoor = isPoor;
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
	 * @return the studentSource
	 */
	public String getStudentSource() {
		return studentSource;
	}
	/**
	 * @param studentSource the studentSource to set
	 */
	public void setStudentSource(String studentSource) {
		this.studentSource = studentSource;
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
	 * @return the receivePushMail
	 */
	public int getReceivePushMail() {
		return receivePushMail;
	}
	/**
	 * @param receivePushMail the receivePushMail to set
	 */
	public void setReceivePushMail(int receivePushMail) {
		this.receivePushMail = receivePushMail;
	}
	/**
	 * @return the reports
	 */
	public Set getReports() {
		return reports;
	}
	/**
	 * @param reports the reports to set
	 */
	public void setReports(Set reports) {
		this.reports = reports;
	}
}