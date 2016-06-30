package com.yuanluesoft.job.talent.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 人才:就业跟踪调查表(job_talent_employment)
 * @author linchuan
 *
 */
public class Employment extends ActionForm {
	private long talentId; //人才ID
	private int graduationYear; //毕业年份
	private String name; //姓名
	private String schoolClass; //专业（班级）
	private String studentNumber; //学号
	private String studentSource; //生源所在地
	private String tel; //联系方式
	private String email; //电子邮箱
	private int employmentType; //就业类型,已签约、有接收函、定向委培、灵活就业、升学、出国出境、国家地方项目、暂不就业、待就业
	private String company; //单位名称
	private String companyAddress; //单位地址
	private String post; //岗位
	private int monthlyPay; //月薪
	private int counterpart; //专业是否对口,A对口、B较对口、C不对口、D很不对口
	private int satisfaction; //就业满意度,A满意、B基本满意、C不满意、D很不满意
	private String changeCompany; //第几次换单位
	private String leaveReason; //离职原因
	private int needHelp; //是否需要推荐
	private String waitReason; //暂不就业原因
	private int isNewest; //是否最新记录
	private Timestamp created; //登记时间
	private String remark; //备注
	
	/**
	 * @return the changeCompany
	 */
	public String getChangeCompany() {
		return changeCompany;
	}
	/**
	 * @param changeCompany the changeCompany to set
	 */
	public void setChangeCompany(String changeCompany) {
		this.changeCompany = changeCompany;
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
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}
	/**
	 * @param companyAddress the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	/**
	 * @return the counterpart
	 */
	public int getCounterpart() {
		return counterpart;
	}
	/**
	 * @param counterpart the counterpart to set
	 */
	public void setCounterpart(int counterpart) {
		this.counterpart = counterpart;
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
	 * @return the employmentType
	 */
	public int getEmploymentType() {
		return employmentType;
	}
	/**
	 * @param employmentType the employmentType to set
	 */
	public void setEmploymentType(int employmentType) {
		this.employmentType = employmentType;
	}
	/**
	 * @return the graduationYear
	 */
	public int getGraduationYear() {
		return graduationYear;
	}
	/**
	 * @param graduationYear the graduationYear to set
	 */
	public void setGraduationYear(int graduationYear) {
		this.graduationYear = graduationYear;
	}
	/**
	 * @return the isNewest
	 */
	public int getIsNewest() {
		return isNewest;
	}
	/**
	 * @param isNewest the isNewest to set
	 */
	public void setIsNewest(int isNewest) {
		this.isNewest = isNewest;
	}
	/**
	 * @return the leaveReason
	 */
	public String getLeaveReason() {
		return leaveReason;
	}
	/**
	 * @param leaveReason the leaveReason to set
	 */
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	/**
	 * @return the monthlyPay
	 */
	public int getMonthlyPay() {
		return monthlyPay;
	}
	/**
	 * @param monthlyPay the monthlyPay to set
	 */
	public void setMonthlyPay(int monthlyPay) {
		this.monthlyPay = monthlyPay;
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
	 * @return the needHelp
	 */
	public int getNeedHelp() {
		return needHelp;
	}
	/**
	 * @param needHelp the needHelp to set
	 */
	public void setNeedHelp(int needHelp) {
		this.needHelp = needHelp;
	}
	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
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
	 * @return the satisfaction
	 */
	public int getSatisfaction() {
		return satisfaction;
	}
	/**
	 * @param satisfaction the satisfaction to set
	 */
	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
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
	 * @return the waitReason
	 */
	public String getWaitReason() {
		return waitReason;
	}
	/**
	 * @param waitReason the waitReason to set
	 */
	public void setWaitReason(String waitReason) {
		this.waitReason = waitReason;
	}
}