package com.yuanluesoft.job.apply.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 应聘:职位收藏(job_favorite)
 * @author linchuan
 *
 */
public class JobFavorite extends Record {
	private long talentId; //人才ID
	private long jobId; //职位ID
	private String jobName; //职位名称
	private long companyId; //公司ID
	private String company; //公司名称
	private String department; //招聘部门
	private int recruitNumber; //招聘人数
	private String post; //职能类别名称
	private String language; //语言要求,如:英语熟练
	private String monthlyPayRange; //月薪
	private String target; //招聘对象,社会人士、应届毕业生、实习生、劳务工
	private int qualification; //学历要求,初中,高中,职业高中,职业中专,中专,大专,本科,MBA,硕士,博士
	private int workYear; //工作年限,在读学生/-1,应届毕业生/0,1年,2年,3年,4年,5年,6年,7年,8年,9年,10年以上,不限
	private String ageRange; //年龄范围
	private char sex; //性别要求,不限制/A,男/M,女/F
	private String rank; //职称要求,不限,初级职称,中级职称,副高级职称,高级职称
	private Date endDate; //到期时间
	private Timestamp created; //收藏时间

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
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
	 * @return the jobId
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
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
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
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
	 * @return the qualification
	 */
	public int getQualification() {
		return qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(int qualification) {
		this.qualification = qualification;
	}
	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}
	/**
	 * @return the recruitNumber
	 */
	public int getRecruitNumber() {
		return recruitNumber;
	}
	/**
	 * @param recruitNumber the recruitNumber to set
	 */
	public void setRecruitNumber(int recruitNumber) {
		this.recruitNumber = recruitNumber;
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
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
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
	 * @return the ageRange
	 */
	public String getAgeRange() {
		return ageRange;
	}
	/**
	 * @param ageRange the ageRange to set
	 */
	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}
	/**
	 * @return the monthlyPayRange
	 */
	public String getMonthlyPayRange() {
		return monthlyPayRange;
	}
	/**
	 * @param monthlyPayRange the monthlyPayRange to set
	 */
	public void setMonthlyPayRange(String monthlyPayRange) {
		this.monthlyPayRange = monthlyPayRange;
	}
}