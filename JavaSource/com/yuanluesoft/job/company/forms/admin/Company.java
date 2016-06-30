package com.yuanluesoft.job.company.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Company extends ActionForm {
	private String name; //企业名称
	private String loginName; //登录用户名
	private String password; //登录密码
	private String type; //公司性质,外资（欧美）,外资（非欧美）,合资,国企,民营公司,国内上市公司,外企代表处,政府机关,事业单位,非营利机构,其它性质
	private long areaId; //所在地区ID
	private String area; //所在地区名称
	private String introduction; //单位简介
	private String representative; //法人代表
	private String licenseNo; //营业执照号
	private int scale; //企业规模,1~49人,50~99人,100~499人,500~999人,1000人以上
	private String address; //单位地址
	private String postalcode; //邮政编码
	private String linkman; //联系人
	private String linkmanJob; //联系人职务
	private int linkmanPublic; //联系人是否公开
	private String linkmanTel; //联系电话
	private int linkmanTelPublic; //联系电话是否公开
	private String fax; //传真号码
	private int faxPublic; //传真号码是否公开
	private String email; //电子邮箱
	private int emailPublic; //电子邮箱是否公开
	private String webSite; //网址
	private int collegeBuddy; //是否校友企业
	private Timestamp created; //注册时间
	private String ip; //注册IP
	private int status; //状态,0/注册,1/审核通过,2/审核未通过
	private long approverId; //审核人ID
	private String approver; //审核人
	private Timestamp approvalTime; //审核时间
	private String failedReason; //审核未通过原因
	private Set industries; //所在行业
	private Set mailTemplates; //邮件模板
	private Set jobs; //职位
	
	//扩展属性
	private String industryIds; //所在行业ID
	private String industryNames; //所在行业
	private boolean approvalPass; //是否审核通过
	
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
	 * @return the emailPublic
	 */
	public int getEmailPublic() {
		return emailPublic;
	}
	/**
	 * @param emailPublic the emailPublic to set
	 */
	public void setEmailPublic(int emailPublic) {
		this.emailPublic = emailPublic;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the faxPublic
	 */
	public int getFaxPublic() {
		return faxPublic;
	}
	/**
	 * @param faxPublic the faxPublic to set
	 */
	public void setFaxPublic(int faxPublic) {
		this.faxPublic = faxPublic;
	}
	/**
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}
	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the licenseNo
	 */
	public String getLicenseNo() {
		return licenseNo;
	}
	/**
	 * @param licenseNo the licenseNo to set
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the linkmanJob
	 */
	public String getLinkmanJob() {
		return linkmanJob;
	}
	/**
	 * @param linkmanJob the linkmanJob to set
	 */
	public void setLinkmanJob(String linkmanJob) {
		this.linkmanJob = linkmanJob;
	}
	/**
	 * @return the linkmanPublic
	 */
	public int getLinkmanPublic() {
		return linkmanPublic;
	}
	/**
	 * @param linkmanPublic the linkmanPublic to set
	 */
	public void setLinkmanPublic(int linkmanPublic) {
		this.linkmanPublic = linkmanPublic;
	}
	/**
	 * @return the linkmanTel
	 */
	public String getLinkmanTel() {
		return linkmanTel;
	}
	/**
	 * @param linkmanTel the linkmanTel to set
	 */
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}
	/**
	 * @return the linkmanTelPublic
	 */
	public int getLinkmanTelPublic() {
		return linkmanTelPublic;
	}
	/**
	 * @param linkmanTelPublic the linkmanTelPublic to set
	 */
	public void setLinkmanTelPublic(int linkmanTelPublic) {
		this.linkmanTelPublic = linkmanTelPublic;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	 * @return the postalcode
	 */
	public String getPostalcode() {
		return postalcode;
	}
	/**
	 * @param postalcode the postalcode to set
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	/**
	 * @return the representative
	 */
	public String getRepresentative() {
		return representative;
	}
	/**
	 * @param representative the representative to set
	 */
	public void setRepresentative(String representative) {
		this.representative = representative;
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
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}
	/**
	 * @param webSite the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	/**
	 * @return the industries
	 */
	public Set getIndustries() {
		return industries;
	}
	/**
	 * @param industries the industries to set
	 */
	public void setIndustries(Set industries) {
		this.industries = industries;
	}
	/**
	 * @return the jobs
	 */
	public Set getJobs() {
		return jobs;
	}
	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(Set jobs) {
		this.jobs = jobs;
	}
	/**
	 * @return the mailTemplates
	 */
	public Set getMailTemplates() {
		return mailTemplates;
	}
	/**
	 * @param mailTemplates the mailTemplates to set
	 */
	public void setMailTemplates(Set mailTemplates) {
		this.mailTemplates = mailTemplates;
	}
	/**
	 * @return the industryIds
	 */
	public String getIndustryIds() {
		return industryIds;
	}
	/**
	 * @param industryIds the industryIds to set
	 */
	public void setIndustryIds(String industryIds) {
		this.industryIds = industryIds;
	}
	/**
	 * @return the industryNames
	 */
	public String getIndustryNames() {
		return industryNames;
	}
	/**
	 * @param industryNames the industryNames to set
	 */
	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}
	/**
	 * @return the approvalPass
	 */
	public boolean isApprovalPass() {
		return approvalPass;
	}
	/**
	 * @param approvalPass the approvalPass to set
	 */
	public void setApprovalPass(boolean approvalPass) {
		this.approvalPass = approvalPass;
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
	public int getCollegeBuddy() {
		return collegeBuddy;
	}
	public void setCollegeBuddy(int collegeBuddy) {
		this.collegeBuddy = collegeBuddy;
	}
}