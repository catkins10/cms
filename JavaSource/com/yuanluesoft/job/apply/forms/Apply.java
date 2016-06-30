package com.yuanluesoft.job.apply.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.talent.pojo.JobTalent;

/**
 * 
 * @author linchuan
 *
 */
public class Apply extends ActionForm {
	private long jobId; //职位ID
	private String jobName; //职位名称
	private long companyId; //公司ID
	private String company; //公司名称
	private long talentId; //求职人ID
	private String talentName; //求职人姓名
	private char sex = 'M'; //性别
	private String school; //毕业院校
	private String specialty; //专业
	private int qualification; //学历
	private int workYear; //工作年限
	private Timestamp created; //求职时间
	private int status; //状态,0/申请,1/删除,2/拟面试,3/面试通过,4/录用
	private Set invitations; //邀请函
	private Set interviews; //面试情况
	private Set offers; //录用通知书
	
	//扩展属性
	private Job job; //职位
	private JobTalent talent; //人才
	
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
	 * @return the talentName
	 */
	public String getTalentName() {
		return talentName;
	}
	/**
	 * @param talentName the talentName to set
	 */
	public void setTalentName(String talentName) {
		this.talentName = talentName;
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
	 * @return the interviews
	 */
	public Set getInterviews() {
		return interviews;
	}
	/**
	 * @param interviews the interviews to set
	 */
	public void setInterviews(Set interviews) {
		this.interviews = interviews;
	}
	/**
	 * @return the invitations
	 */
	public Set getInvitations() {
		return invitations;
	}
	/**
	 * @param invitations the invitations to set
	 */
	public void setInvitations(Set invitations) {
		this.invitations = invitations;
	}
	/**
	 * @return the offers
	 */
	public Set getOffers() {
		return offers;
	}
	/**
	 * @param offers the offers to set
	 */
	public void setOffers(Set offers) {
		this.offers = offers;
	}
	/**
	 * @return the job
	 */
	public Job getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(Job job) {
		this.job = job;
	}
	/**
	 * @return the talent
	 */
	public JobTalent getTalent() {
		return talent;
	}
	/**
	 * @param talent the talent to set
	 */
	public void setTalent(JobTalent talent) {
		this.talent = talent;
	}
}