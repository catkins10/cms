package com.yuanluesoft.job.talent.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Career extends ActionForm {
	private long talentId; //人才ID
	private Date startDate; //开始时间
	private Date endDate; //结束时间
	private String company; //公司
	private String industry; //行业
	private int scale; //公司规模
	private String type; //公司性质
	private String area; //所在地
	private String department; //部门
	private String job; //职位
	private long postId; //职位类别ID
	private String post; //工作职位类别
	private String description; //工作描述
	private int monthlyPay; //月薪
	private String reterence; //证明人
	private String reterenceJob; //证明人职务
	private String reterenceTel; //证明人电话
	private int counterpart = 1; //专业是否对口
	private String satisfaction; //就业满意度
	private String changePost; //第几次换岗位
	private String leaveReason; //离职原因
	private int needHelp; //是否需要推荐
	private String waitReason; //暂不就业原因
	
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
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
	 * @return the reterence
	 */
	public String getReterence() {
		return reterence;
	}
	/**
	 * @param reterence the reterence to set
	 */
	public void setReterence(String reterence) {
		this.reterence = reterence;
	}
	/**
	 * @return the reterenceJob
	 */
	public String getReterenceJob() {
		return reterenceJob;
	}
	/**
	 * @param reterenceJob the reterenceJob to set
	 */
	public void setReterenceJob(String reterenceJob) {
		this.reterenceJob = reterenceJob;
	}
	/**
	 * @return the reterenceTel
	 */
	public String getReterenceTel() {
		return reterenceTel;
	}
	/**
	 * @param reterenceTel the reterenceTel to set
	 */
	public void setReterenceTel(String reterenceTel) {
		this.reterenceTel = reterenceTel;
	}
	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the changePost
	 */
	public String getChangePost() {
		return changePost;
	}
	/**
	 * @param changePost the changePost to set
	 */
	public void setChangePost(String changePost) {
		this.changePost = changePost;
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
	 * @return the postId
	 */
	public long getPostId() {
		return postId;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(long postId) {
		this.postId = postId;
	}
	/**
	 * @return the satisfaction
	 */
	public String getSatisfaction() {
		return satisfaction;
	}
	/**
	 * @param satisfaction the satisfaction to set
	 */
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
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