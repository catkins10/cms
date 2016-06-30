package com.yuanluesoft.job.company.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.job.company.pojo.JobCompany;

/**
 * 
 * @author linchuan
 *
 */
public class Job extends ActionForm {
	private long companyId; //企业ID
	private String name; //职位名称
	private String department; //招聘部门
	private int recruitNumber; //招聘人数
	private String description; //职位描述
	private String requirement; //职位要求
	private long postId; //职能类别ID
	private String post; //职能类别名称
	private String language; //语言要求,如:英语熟练
	private int minMonthlyPay; //最低月薪
	private int maxMonthlyPay; //最高月薪
	private String target; //招聘对象,社会人士、应届毕业生、实习生、劳务工
	private int qualification = -1; //学历要求,初中,高中,职业高中,职业中专,中专,大专,本科,MBA,硕士,博士,不限/-1
	private int workYear = -2; //工作年限,在读学生/-1,应届毕业生/0,1年,2年,3年,4年,5年,6年,7年,8年,9年,10年以上,不限/-2
	private int minAge; //年龄下限
	private int maxAge; //年龄上限
	private char sex = 'A'; //性别要求,不限制/A,男/M,女/F
	private String rank; //职称要求,不限,初级职称,中级职称,副高级职称,高级职称
	private Date endDate; //到期时间
	private int isPublic = 1; //是否公开
	private int urgent; //是否紧急职位
	private int queryConnt; //查询次数,允许清零
	private int applicantCount; //投递次数
	private double priority; //优先级
	private Timestamp created; //创建时间
	private Timestamp publicTime; //发布时间
	private Timestamp refreshTime; //刷新时间
	private int todayRefreshTimes; //当日刷新次数
	private Set areas; //工作地点
	private Set types; //工作性质
	private Set pushes; //推送
	private JobCompany company; //企业
	//扩展属性
	private int[] typeArray; //工作性质数组
	private String areaIds; //工作地点ID
	private String areaNames; //工作地点
	
	/**
	 * @return the applicantCount
	 */
	public int getApplicantCount() {
		return applicantCount;
	}
	/**
	 * @param applicantCount the applicantCount to set
	 */
	public void setApplicantCount(int applicantCount) {
		this.applicantCount = applicantCount;
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
	 * @return the isPublic
	 */
	public int getIsPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
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
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return maxAge;
	}
	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	/**
	 * @return the maxMonthlyPay
	 */
	public int getMaxMonthlyPay() {
		return maxMonthlyPay;
	}
	/**
	 * @param maxMonthlyPay the maxMonthlyPay to set
	 */
	public void setMaxMonthlyPay(int maxMonthlyPay) {
		this.maxMonthlyPay = maxMonthlyPay;
	}
	/**
	 * @return the minAge
	 */
	public int getMinAge() {
		return minAge;
	}
	/**
	 * @param minAge the minAge to set
	 */
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	/**
	 * @return the minMonthlyPay
	 */
	public int getMinMonthlyPay() {
		return minMonthlyPay;
	}
	/**
	 * @param minMonthlyPay the minMonthlyPay to set
	 */
	public void setMinMonthlyPay(int minMonthlyPay) {
		this.minMonthlyPay = minMonthlyPay;
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
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the publicTime
	 */
	public Timestamp getPublicTime() {
		return publicTime;
	}
	/**
	 * @param publicTime the publicTime to set
	 */
	public void setPublicTime(Timestamp publicTime) {
		this.publicTime = publicTime;
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
	 * @return the queryConnt
	 */
	public int getQueryConnt() {
		return queryConnt;
	}
	/**
	 * @param queryConnt the queryConnt to set
	 */
	public void setQueryConnt(int queryConnt) {
		this.queryConnt = queryConnt;
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
	 * @return the refreshTime
	 */
	public Timestamp getRefreshTime() {
		return refreshTime;
	}
	/**
	 * @param refreshTime the refreshTime to set
	 */
	public void setRefreshTime(Timestamp refreshTime) {
		this.refreshTime = refreshTime;
	}
	/**
	 * @return the requirement
	 */
	public String getRequirement() {
		return requirement;
	}
	/**
	 * @param requirement the requirement to set
	 */
	public void setRequirement(String requirement) {
		this.requirement = requirement;
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
	 * @return the todayRefreshTimes
	 */
	public int getTodayRefreshTimes() {
		return todayRefreshTimes;
	}
	/**
	 * @param todayRefreshTimes the todayRefreshTimes to set
	 */
	public void setTodayRefreshTimes(int todayRefreshTimes) {
		this.todayRefreshTimes = todayRefreshTimes;
	}
	/**
	 * @return the urgent
	 */
	public int getUrgent() {
		return urgent;
	}
	/**
	 * @param urgent the urgent to set
	 */
	public void setUrgent(int urgent) {
		this.urgent = urgent;
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
	 * @return the areas
	 */
	public Set getAreas() {
		return areas;
	}
	/**
	 * @param areas the areas to set
	 */
	public void setAreas(Set areas) {
		this.areas = areas;
	}
	/**
	 * @return the areaIds
	 */
	public String getAreaIds() {
		return areaIds;
	}
	/**
	 * @param areaIds the areaIds to set
	 */
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	/**
	 * @return the areaNames
	 */
	public String getAreaNames() {
		return areaNames;
	}
	/**
	 * @param areaNames the areaNames to set
	 */
	public void setAreaNames(String areaNames) {
		this.areaNames = areaNames;
	}
	/**
	 * @return the typeArray
	 */
	public int[] getTypeArray() {
		return typeArray;
	}
	/**
	 * @param typeArray the typeArray to set
	 */
	public void setTypeArray(int[] typeArray) {
		this.typeArray = typeArray;
	}
	/**
	 * @return the types
	 */
	public Set getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(Set types) {
		this.types = types;
	}
	/**
	 * @return the company
	 */
	public JobCompany getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(JobCompany company) {
		this.company = company;
	}
	/**
	 * @return the pushes
	 */
	public Set getPushes() {
		return pushes;
	}
	/**
	 * @param pushes the pushes to set
	 */
	public void setPushes(Set pushes) {
		this.pushes = pushes;
	}
}